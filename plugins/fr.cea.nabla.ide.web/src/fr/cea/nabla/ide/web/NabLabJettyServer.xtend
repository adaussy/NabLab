/** 
 * Copyright (c) 2021 CEA
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * SPDX-License-Identifier: EPL-2.0
 * Contributors: see AUTHORS file
 */
package fr.cea.nabla.ide.web

import java.util.EnumSet
import javax.servlet.DispatcherType
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.DefaultHandler
import org.eclipse.jetty.server.handler.HandlerList
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.eclipse.xtext.resource.EObjectAtOffsetHelper
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class NabLabJettyServer
{
	final Logger logger = LoggerFactory.getLogger(NabLabJettyServer)

	def void start(NabLabLanguageServer languageServer, EObjectAtOffsetHelper eObjectAtOffsetHelper) throws Exception
	{
		var server = new Server(8082)
		server.setStopAtShutdown(true)
		var context = new ServletContextHandler(null, "/", false, false)
		var latexServlet = new LatexServlet(eObjectAtOffsetHelper, languageServer)
		context.addServlet(new ServletHolder(latexServlet), "/latex")
		var cors = context.addFilter(CrossOriginFilter, "/*", EnumSet.of(DispatcherType.REQUEST))
		cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*")
		cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*")
		cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,HEAD")
		cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin")
		var handlers = new HandlerList()
		handlers.setHandlers(#[context, new DefaultHandler()])
		server.setHandler(handlers)
		try
		{
			server.start()
			logger.info("NabLab JettyServer started")
		}
		catch (Exception exception)
		{
			logger.error('''NabLab JettyServer error on start : «exception.getMessage()»''')
		}

		try
		{
			server.join()
		}
		catch (Exception exception)
		{
			logger.error('''NabLab JettyServer error on join : «exception.getMessage()»''')
		}

	}
}
