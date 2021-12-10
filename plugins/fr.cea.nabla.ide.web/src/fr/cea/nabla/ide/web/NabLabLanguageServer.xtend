/*******************************************************************************
 * Copyright (c) 2021 CEA
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 * Contributors: see AUTHORS file
 *******************************************************************************/
package fr.cea.nabla.ide.web

import org.eclipse.xtext.ide.server.LanguageServerImpl
import org.eclipse.xtext.ide.server.WorkspaceManager

class NabLabLanguageServer extends LanguageServerImpl
{
	override WorkspaceManager getWorkspaceManager()
	{
		super.workspaceManager
	}
	
	override protected getLanguageClient()
	{
		super.getLanguageClient()
	}
}
