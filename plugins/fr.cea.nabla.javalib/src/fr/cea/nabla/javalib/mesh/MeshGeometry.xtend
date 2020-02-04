/*******************************************************************************
 * Copyright (c) 2020 CEA
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 * Contributors: see AUTHORS file
 *******************************************************************************/
package fr.cea.nabla.javalib.mesh

import org.eclipse.xtend.lib.annotations.Data

@Data
class MeshGeometry
{
	val double[][] nodes
	val Edge[] edges
	val Quad[] quads

	def dump()
	{
		println('Mesh Geometry')
		println('  nodes (' + nodes.size + ') : ' + nodes.map[toString].join(', '))
		println('  edges (' + edges.size + ') : ' + edges.map[toString].join(', '))	
		println('  quads (' + quads.size + ') : ' + quads.map[toString].join(', '))
	}
}