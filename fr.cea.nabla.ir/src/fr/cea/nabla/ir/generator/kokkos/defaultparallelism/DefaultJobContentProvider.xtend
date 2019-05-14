/*******************************************************************************
 * Copyright (c) 2018 CEA
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * 	Benoit Lelandais - initial implementation
 * 	Marie-Pierre Oudot - initial implementation
 * 	Jean-Sylvain Camier - Nabla generation support
 *******************************************************************************/
package fr.cea.nabla.ir.generator.kokkos.defaultparallelism

import fr.cea.nabla.ir.generator.kokkos.JobContentProvider
import fr.cea.nabla.ir.ir.Job

class DefaultJobContentProvider extends JobContentProvider 
{
	override getJobCallsContent(Iterable<Job> jobs) 
	{
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	override getContent(Job it) 
	{
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
}