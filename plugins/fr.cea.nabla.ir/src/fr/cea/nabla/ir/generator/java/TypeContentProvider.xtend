/*******************************************************************************
 * Copyright (c) 2020 CEA
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 * Contributors: see AUTHORS file
 *******************************************************************************/
package fr.cea.nabla.ir.generator.java

import fr.cea.nabla.ir.ir.BaseType
import fr.cea.nabla.ir.ir.ConnectivityType
import fr.cea.nabla.ir.ir.Expression
import fr.cea.nabla.ir.ir.IrType
import fr.cea.nabla.ir.ir.ItemIndex
import fr.cea.nabla.ir.ir.LinearAlgebraType
import fr.cea.nabla.ir.ir.PrimitiveType

import static fr.cea.nabla.ir.ExtensionProviderExtensions.*

import static extension fr.cea.nabla.ir.ContainerExtensions.*
import static extension fr.cea.nabla.ir.IrTypeExtensions.*
import static extension fr.cea.nabla.ir.generator.java.ExpressionContentProvider.*

class TypeContentProvider
{
	static def String getJavaType(IrType it)
	{
		switch it
		{
			case null: null
			BaseType: primitive.javaType + sizes.map['[]'].join
			ConnectivityType: base.javaType + connectivities.map['[]'].join
			LinearAlgebraType case sizes.size == 1: getNsPrefix(provider, '.') + 'Vector'
			LinearAlgebraType case sizes.size == 2: getNsPrefix(provider, '.') + 'Matrix'
			default: throw new RuntimeException("Unexpected type: " + class.name)
		}
	}

	static def getJavaAllocation(IrType it, String name)
	{
		switch it
		{
			BaseType case scalar: ''''''
			BaseType: ''' = new «primitive.javaType»«formatIteratorsAndIndices(it, sizes.map[content])»'''
			ConnectivityType: ''' = new «primitive.javaType»«formatIteratorsAndIndices(it, connectivities.map[nbElemsVar] + base.sizes.map[content])»'''
			LinearAlgebraType: ''' = new «javaType»("«name»", «formatIteratorsAndIndices(it, sizes.map[content])»)'''
		}
	}

	static def formatIteratorsAndIndices(IrType it, Iterable<ItemIndex> iterators, Iterable<Expression> indices)
	{
		formatIteratorsAndIndices(it, iterators.map[name] + indices.map[content])
	}

	static def formatIteratorsAndIndices(IrType it, Iterable<CharSequence> iteratorsAndIndices)
	{
		switch it
		{
			case (iteratorsAndIndices.empty): ''''''
			LinearAlgebraType: '''«FOR i : iteratorsAndIndices SEPARATOR ', '»«i»«ENDFOR»'''
			default: '''«FOR i : iteratorsAndIndices»[«i»]«ENDFOR»'''
		}
	}

	private static def String getJavaType(PrimitiveType t)
	{
		switch t
		{
			case null: 'void'
			case BOOL: 'boolean'
			case INT: 'int'
			case REAL: 'double'
		}
	}
}