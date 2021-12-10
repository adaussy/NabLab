/*******************************************************************************
 * Copyright (c) 2022 CEA
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 * Contributors: see AUTHORS file
 *******************************************************************************/
package fr.cea.nabla.ide

import fr.cea.nabla.ide.commands.LSPCommandsHandler
import org.eclipse.xtext.ide.server.commands.IExecutableCommandService

/**
 * Use this class to register ide components.
 */
class NablaIdeModule extends AbstractNablaIdeModule
{
	def Class<? extends IExecutableCommandService> bindIExecutableCommandService()
	{
		typeof(LSPCommandsHandler)
	}
}
