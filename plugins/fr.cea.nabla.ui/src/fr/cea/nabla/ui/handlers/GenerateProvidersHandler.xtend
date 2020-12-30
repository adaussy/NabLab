package fr.cea.nabla.ui.handlers

import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.Singleton
import fr.cea.nabla.generator.NablaGeneratorMessageDispatcher.MessageType
import fr.cea.nabla.generator.ext.ExtensionProviderGenerator
import fr.cea.nabla.ir.Utils
import fr.cea.nabla.ir.generator.cpp.UnzipHelper
import fr.cea.nabla.nabla.NablaExtension
import java.io.File
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IResource
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Shell

@Singleton
class GenerateProvidersHandler extends AbstractGenerateHandler
{
	@Inject ExtensionProviderGenerator generator
	@Inject Provider<ResourceSet> resourceSetProvider

	override generate(IFile nablaFile, Shell shell)
	{
		val project = nablaFile.project

		consoleFactory.openConsole
		val traceFunction = [MessageType type, String msg | consoleFactory.printConsole(type, msg)]
		dispatcher.traceListeners += traceFunction

		new Thread
		([
			try
			{
				consoleFactory.clearAndActivateConsole
				consoleFactory.printConsole(MessageType.Start, "Starting generation process for: " + nablaFile.name)
				consoleFactory.printConsole(MessageType.Exec, "Loading nabla resources")
				val plaftormUri = URI::createPlatformResourceURI(project.name + '/' + nablaFile.projectRelativePath, true)
				val resourceSet = resourceSetProvider.get
				val uriMap = resourceSet.URIConverter.URIMap
				uriMap.put(URI::createURI('platform:/resource/fr.cea.nabla/'), URI::createURI('platform:/plugin/fr.cea.nabla/'))
				val emfResource = resourceSet.createResource(plaftormUri)
				EcoreUtil::resolveAll(resourceSet)
				emfResource.load(null)

				val startTime = System.currentTimeMillis
				val nablaExt = emfResource.contents.filter(NablaExtension).head
				shell.display.syncExec([shell.cursor = shell.display.getSystemCursor(SWT.CURSOR_WAIT)])
				// TODO share lib with examples
				val libcppnablaHome = new File(project.workspace.root.location.toString)
				UnzipHelper.unzipLibCppNabla(libcppnablaHome)
				generator.generate(nablaExt, project, libcppnablaHome.absolutePath + '/' + UnzipHelper.DirectoryName)
				shell.display.syncExec([shell.cursor = null])
				val endTime = System.currentTimeMillis
				consoleFactory.printConsole(MessageType.Exec, "Code generation ended in " + (endTime-startTime)/1000.0 + "s")

				project.refreshLocal(IResource::DEPTH_INFINITE, null)
				consoleFactory.printConsole(MessageType.End, "Generation ended successfully for: " + nablaFile.name)
			}
			catch (Exception e)
			{
				shell.display.syncExec([shell.cursor = null])
				consoleFactory.printConsole(MessageType.Error, "Generation failed for: " + nablaFile.name)
				consoleFactory.printConsole(MessageType.Error, e.message)
				consoleFactory.printConsole(MessageType.Error, Utils.getStackTrace(e))
			}
		]).start

		dispatcher.traceListeners -= traceFunction
	}
}