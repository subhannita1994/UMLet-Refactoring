/*******************************************************************************
 * Copyright (c) 2001-2014 Yann-Ga�l Gu�h�neuc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Yann-Ga�l Gu�h�neuc and others, see in file; API and its implementation
 ******************************************************************************/
package padl.creator.csharpfile.v3.parser.builder;

import org.antlr.v4.runtime.tree.ParseTree;

import padl.kernel.IConstituent;
import padl.kernel.exception.CreationException;

/**
 * A CodeBuilder is responsible to create and customize a specific type of
 * PADL model entity. The builder are mainly used to do the conversion from
 * an AST to PADL. Check ANTLR2PADLReader for a typical usage.
 */
public interface CodeBuilder {
	/**
	 * Add the given constituent to the builded entity.
	 * @param childElement
	 * @
	 */
	void add(IConstituent childElement);

	/**
	 * Returns the builded entity.
	 * @return
	 */
	IConstituent close();

	/**
	 * Creates the PADL entity.
	 * @param node
	 * @param context
	 * @throws CreationException
	 */
	void create(final ParseTree node, final BuilderContext context)
			throws CreationException;

	/**
	 * Returns the enveloping CodeBuilder, if any, null otherwise.
	 * @return the enveloping CodeBuilder, if any, null otherwise.
	 */
	CodeBuilder getParent();
}
