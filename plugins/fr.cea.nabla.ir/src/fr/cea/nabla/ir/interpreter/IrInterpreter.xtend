/*******************************************************************************
 * Copyright (c) 2020 CEA
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 * Contributors: see AUTHORS file
 *******************************************************************************/
package fr.cea.nabla.ir.interpreter

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import fr.cea.nabla.ir.Utils
import fr.cea.nabla.ir.ir.IrModule
import fr.cea.nabla.ir.ir.IrRoot
import fr.cea.nabla.javalib.LevelDBUtils
import fr.cea.nabla.javalib.mesh.PvdFileWriter2D
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.util.ArrayList
import java.util.logging.Logger
import java.util.logging.StreamHandler
import org.eclipse.xtend.lib.annotations.Accessors
import org.iq80.leveldb.Options

import static fr.cea.nabla.ir.interpreter.ExpressionInterpreter.*
import static fr.cea.nabla.ir.interpreter.VariableValueFactory.*
import static org.iq80.leveldb.impl.Iq80DBFactory.bytes
import static org.iq80.leveldb.impl.Iq80DBFactory.factory

import static extension fr.cea.nabla.ir.ExtensionProviderExtensions.*
import static extension fr.cea.nabla.ir.IrModuleExtensions.*
import static extension fr.cea.nabla.ir.IrRootExtensions.*
import static extension fr.cea.nabla.ir.interpreter.NablaValueExtensions.*

class IrInterpreter
{
	public static val ITERATION_VARIABLE_NAME = "InterpreterIteration"

	@Accessors val Context context
	val IrRoot ir
	val PvdFileWriter2D writer
	val JobInterpreter jobInterpreter
	val Logger logger
	val String levelDatabasePath
	var boolean levelDBcompareResult

	new(IrRoot ir, StreamHandler handler)
	{
		this(ir, handler, "")
	}

	new(IrRoot ir, StreamHandler handler, String outputDirName)
	{
		// create a Logger and a Handler
		logger = Logger.getLogger(IrInterpreter.name)
		logger.setLevel(handler.level)  //Create only logs if needed by handler
		logger.setUseParentHandlers(false) // Suppress default console
		logger.handlers.forEach(h | logger.removeHandler(h))
		logger.addHandler(handler)

		this.ir = ir
		this.context = new Context(ir, logger)
		this.writer = new PvdFileWriter2D(ir.name, outputDirName)
		this.jobInterpreter = new JobInterpreter(writer)
		this.levelDatabasePath = "results/interpreter/" + ir.name.toLowerCase + "/"
		this.levelDBcompareResult = true
	}

	/**
	 * Interpret variable with default values.
	 * This method is public to be used by default Json file generation.
	 */
	def interpreteOptionsDefaultValues()
	{
		for (v : ir.options)
			context.addVariableValue(v, createValue(v.type, v.name, v.defaultValue, context))
	}

	def interprete(String jsonContent, String wsPath)
	{
		context.logInfo("  Start interpreting " + ir.name + " module")
//		context.logInfo("     " + classloaderUrls.size + " URL(s) provided to class loader")
//		classloaderUrls.forEach[x | context.logInfo("       " + x.toString)]

		// Start initialising the variables with default values
		interpreteOptionsDefaultValues

		val gson = new Gson
		val jsonObject = gson.fromJson(jsonContent, JsonObject)

		// Create mesh and mesh variables
		if (!jsonObject.has("mesh")) throw new RuntimeException("Mesh block missing in Json")
		context.initMesh(gson, jsonObject.get("mesh").toString, ir.connectivities)

		// Read options in Json
		for (m : ir.modules)
			init(m, jsonObject.get(m.name), wsPath)

		// Interprete variables that are not options
		for (v : ir.variables.filter[!option])
			context.addVariableValue(v, createValue(v.type, v.name, v.defaultValue, context))

		// Copy Node Cooords
		context.addVariableValue(ir.initNodeCoordVariable, new NV2Real(context.meshWrapper.nodes))

		// Interprete Top level jobs
		for (j : ir.main.calls)
			jobInterpreter.interprete(j, context)

		// Non regression testing
		val mainModuleName = ir.mainModule.name
		if (jsonObject.has(mainModuleName))
		{
			val jsonMainModuleOptions = jsonObject.get(mainModuleName).asJsonObject
			val nrName = Utils.NonRegressionNameAndValue.key
			if (jsonMainModuleOptions.has(nrName))
			{
				val jsonNrName = jsonMainModuleOptions.get(nrName).asString
				if (jsonNrName.equals(Utils.NonRegressionValues.CreateReference.toString))
					createDB(refDBName)
				if (jsonNrName.equals(Utils.NonRegressionValues.CompareToReference.toString))
				{
					createDB(curDBName)
					levelDBcompareResult = LevelDBUtils.compareDB(curDBName, refDBName)
					LevelDBUtils.destroyDB(curDBName)
				}
			}
		}

		context.logVariables("At the end")
		context.logInfo("  End interpreting")

		return context
	}

	def info(String message)
	{
		logger.info(message)
	}

	def getLevelDBCompareResult()
	{
		levelDBcompareResult
	}

	private def init(IrModule m, JsonElement jsonElt, String wsPath)
	{
		val jsonOptions = (jsonElt === null ? null : jsonElt.asJsonObject)
		for (v : m.options)
		{
			if (jsonOptions !== null && jsonOptions.has(v.name))
			{
				val vValue = context.getVariableValue(v)
				val jsonOpt = jsonOptions.get(v.name)
				NablaValueJsonSetter::setValue(vValue, jsonOpt)
			}
			else
			{
				if (v.defaultValue === null)
					// v is not present in json file and is mandatory
					throw new IllegalStateException("Mandatory option missing in Json file: " + v.name)
				else
					context.setVariableValue(v, interprete(v.defaultValue, context))
			}
		}

		try(val classLoader = buildClassLoader(ir, wsPath))
		{
			for (provider : m.providers)
			{
				val providerHelper = context.extensionProviderCache.get(provider, classLoader, wsPath)
				if (jsonOptions !== null && jsonOptions.has(provider.instanceName))
					providerHelper.jsonInit(jsonOptions.get(provider.instanceName).toString)
				providerHelper.initFunctions(provider.functions)
			}
		}
	}

	private def createDB(String db_name)
	{
		val levelDBOptions = new Options()

		// Destroy if exists
		factory.destroy(new File(db_name), levelDBOptions)

		// Create data base
		levelDBOptions.createIfMissing(true)
		val db = factory.open(new File(db_name), levelDBOptions);

		val batch = db.createWriteBatch();
		try
		{
			for (v : ir.variables.filter[!option])
				batch.put(bytes(v.name), context.getVariableValue(v).serialize);
			db.write(batch);
		}
		finally
		{
			// Make sure you close the batch to avoid resource leaks.
			batch.close();
		}
		db.close();
		System.out.println("Reference database " + db_name + " created.");
	}

	private def getRefDBName() { levelDatabasePath + ir.name + "DB.ref" }
	private def getCurDBName() { levelDatabasePath + ir.name + "DB.current" }

	private def buildClassLoader(IrRoot ir, String wsPath)
	{
		val urls = new ArrayList<URL>
		for (p : ir.providers.filter[x | x.extensionName != "Math"])
		{
			val urlText = "file://" + wsPath + p.installPath + "/" + p.libName + ".jar"
			urls += new URL(urlText)
		}
		return new URLClassLoader(urls)
	}
}
