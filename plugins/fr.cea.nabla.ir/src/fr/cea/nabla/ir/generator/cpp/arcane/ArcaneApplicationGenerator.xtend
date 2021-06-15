/*******************************************************************************
 * Copyright (c) 2021 CEA
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 * Contributors: see AUTHORS file
 *******************************************************************************/
package fr.cea.nabla.ir.generator.cpp.arcane

import fr.cea.nabla.ir.JobCallerExtensions
import fr.cea.nabla.ir.generator.ApplicationGenerator
import fr.cea.nabla.ir.generator.GenerationContent
import fr.cea.nabla.ir.generator.Utils
import fr.cea.nabla.ir.generator.cpp.CppGeneratorUtils
import fr.cea.nabla.ir.ir.ExecuteTimeLoopJob
import fr.cea.nabla.ir.ir.IrModule
import fr.cea.nabla.ir.ir.IrRoot
import fr.cea.nabla.ir.ir.Job
import fr.cea.nabla.ir.transformers.ReplaceReductions
import java.util.ArrayList

import static extension fr.cea.nabla.ir.IrModuleExtensions.*

class ArcaneApplicationGenerator implements ApplicationGenerator
{
	val String arcaneDir

	override getName() { 'Arcane' }
	override getIrTransformationStep() { new ReplaceReductions(true) }

	new(String arcaneDir)
	{
		this.arcaneDir = arcaneDir
	}

	override getGenerationContents(IrRoot ir)
	{
		val fileContents = new ArrayList<GenerationContent>
		for (module : ir.modules)
		{
			fileContents += new GenerationContent(module.name.toFirstUpper + '.axl', AxlContentProvider.getContent(module), false)
			val className = ArcaneUtils.getModuleName(module)
			fileContents += new GenerationContent(className + '.h', getHeaderFileContent(module, className), false)
			fileContents += new GenerationContent(className + '.cc', getSourceFileContent(module, className), false)
		}
		fileContents += new GenerationContent('CMakeLists.txt', CMakeContentProvider.getContent(ir, arcaneDir), false)
		fileContents += new GenerationContent(ir.name + '.config', TimeLoopContentProvider.getContent(ir), false)
		fileContents += new GenerationContent('main.cc', MainContentProvider.getContent(ir), false)
		return fileContents
	}

	private def getHeaderFileContent(IrModule it, String className)
	'''
	/* «Utils::doNotEditWarning» */

	#ifndef «CppGeneratorUtils.getHDefineName(className)»
	#define «CppGeneratorUtils.getHDefineName(className)»

	#include <arcane/utils/Array.h>
	#include "«name.toFirstUpper»_axl.h"

	using namespace Arcane;
	«IF !functions.empty»

	/******************** Free functions declarations ********************/

	namespace «CppGeneratorUtils.getFreeFunctionNs(it)»
	{
	«FOR f : functions»
		«FunctionContentProvider.getDeclarationContent(f)»;
	«ENDFOR»
	}
	«ENDIF»

	/******************** Module declaration ********************/

	class «className»
	: public Arcane«name.toFirstUpper»Object
	{
	public:
		«className»(const ModuleBuildInfo& mbi)
		: Arcane«name.toFirstUpper»Object(mbi) {}
		~«className»() {}

		virtual void init() override;
		virtual void compute() override;

		VersionInfo versionInfo() const override { return VersionInfo(1, 0, 0); }

	private:
		«FOR j : jobs.filter[!mainTimeLoop]»
		«JobContentProvider.getDeclarationContent(j)»
		«ENDFOR»
	};

	#endif
	'''

	private def getSourceFileContent(IrModule it, String className)
	'''
	/* «Utils::doNotEditWarning» */

	#include "«className».h"
	#include <arcane/Concurrency.h>

	using namespace Arcane;
	«IF !functions.empty»

	/******************** Free functions definitions ********************/

	namespace «CppGeneratorUtils.getFreeFunctionNs(it)»
	{
	«FOR f : functions SEPARATOR '\n'»
		«FunctionContentProvider.getDefinitionContent(f)»
	«ENDFOR»
	}
	«ENDIF»


	/******************** Module entry points ********************/

	void «className»::init()
	{
		«FOR c : irRoot.main.calls»
		«Utils::getCallName(c).replace('.', '->')»(); // @«c.at»
		«ENDFOR»
	}

	void «className»::compute()
	{
		«FOR c : (jobs.findFirst[mainTimeLoop] as ExecuteTimeLoopJob).calls»
		«Utils::getCallName(c).replace('.', '->')»(); // @«c.at»
		«ENDFOR»
	}


	/******************** Module methods ********************/

	«FOR j : jobs.filter[!mainTimeLoop] SEPARATOR '\n'»
		«JobContentProvider.getDefinitionContent(j)»
	«ENDFOR»
	'''

	/** The main time loop job is represented by the compute entry point */
	private def boolean isMainTimeLoop(Job j)
	{
		j instanceof ExecuteTimeLoopJob && JobCallerExtensions.isMainTimeLoop(j as ExecuteTimeLoopJob)
	}
}