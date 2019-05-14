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
package fr.cea.nabla.ir.generator.kokkos.hierarchicalparallelism

import fr.cea.nabla.ir.generator.kokkos.JobContentProvider
import fr.cea.nabla.ir.ir.Job

import static extension fr.cea.nabla.ir.JobExtensions.*
import static extension fr.cea.nabla.ir.generator.Utils.*

class HierarchicalJobContentProvider extends JobContentProvider 
{
	override getJobCallsContent(Iterable<Job> jobs) 
	'''
		«val jobsByAt = jobs.groupBy[at]»
		«FOR at : jobsByAt.keySet.sort»
			«val atJobs = jobsByAt.get(at)»
			// @«at»
			«IF (atJobs.forall[!hasIterable])»
				«FOR j : atJobs»
				«j.name.toFirstLower»();
				«ENDFOR»
			«ELSE»			
			Kokkos::parallel_for(team_policy, KOKKOS_LAMBDA(member_type thread) {
				«FOR j : atJobs»
					«IF !j.hasLoop»
					if (thread.league_rank() == 0)
						«IF !j.hasIterable /* Only simple instruction jobs */»
							Kokkos::single(Kokkos::PerTeam(thread), KOKKOS_LAMBDA(){«j.name.toFirstLower»();});
						«ELSE /* Only for jobs containing ReductionInstruction */»
							«j.name.toFirstLower»(thread);
						«ENDIF»
					«ELSE»
						«j.name.toFirstLower»(thread);
					«ENDIF»
				«ENDFOR»
			});
			«ENDIF»
			
		«ENDFOR»
	'''
	
	override getContent(Job it)
	{
		if (hasIterable)
			parallelHeader
		else
			sequentialHeader
	}
	
	private def getParallelHeader(Job it)
	'''
		«comment»
		KOKKOS_INLINE_FUNCTION
		void «name.toFirstLower»(const member_type& team_member) noexcept
		{
			«innerContent»
		}
	'''

	private def getSequentialHeader(Job it)
	'''
		«comment»
		KOKKOS_INLINE_FUNCTION
		void «name.toFirstLower»() noexcept
		{
			«innerContent»
		}
	'''
}