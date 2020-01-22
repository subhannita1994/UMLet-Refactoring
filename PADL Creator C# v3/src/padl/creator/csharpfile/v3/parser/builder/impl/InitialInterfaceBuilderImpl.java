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
package padl.creator.csharpfile.v3.parser.builder.impl;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

import org.antlr.v4.runtime.Token;

import padl.creator.csharpfile.v3.parser.CSharpParser;
import padl.creator.csharpfile.v3.parser.builder.BuilderContext;
import padl.creator.csharpfile.v3.parser.builder.CodeBuilder;
import padl.kernel.IConstituent;
import padl.kernel.IConstituentOfEntity;
import padl.kernel.IInterface;
import padl.kernel.exception.CreationException;

/**
 * This builder creates an IInterface model without any relationships, extensions, members, etc.
 * It is meant to be used within a 1st simple scan of all the classes to analyze.
 */
public class InitialInterfaceBuilderImpl extends AbstractClassBuilderImpl {

	protected IInterface interfaze;

	public InitialInterfaceBuilderImpl(final CodeBuilder parent) {
		super(parent);
	}

	protected IInterface _create(
		final String interfaceName,
		final ParseTree node,
		final BuilderContext context) {

		final IInterface out =
			this.factory.createInterface(
				interfaceName.toCharArray(),
				interfaceName.toCharArray());
		
		final List<Token> tokens = Util.getFlatTokenList(node);

		// find out the modifiers of this interface
		if (Util.tokenExists(tokens, CSharpParser.PUBLIC)) {
			out.setPublic(true);
		} else if (Util.tokenExists(tokens, CSharpParser.PRIVATE)) {
			out.setPrivate(true);
		} else if (Util.tokenExists(tokens, CSharpParser.PROTECTED)) {
			out.setProtected(true);
		}
		
		return out;
	}

	public void add(final IConstituent childElement) {
		this.interfaze.addConstituent((IConstituentOfEntity) childElement);
	}

	public IConstituent close() {
		return this.interfaze;
	}

	public void create(final ParseTree node, final BuilderContext context)
			throws CreationException {
		
		final List<Token> tokens = Util.getFlatTokenList(node);
		
		final Token interfaceNameElement = Util.getFirstTokenWithType(tokens, CSharpParser.IDENTIFIER);
		
		if (interfaceNameElement == null) {
			throw new CreationException(
				"Could not find the name of the interface ?");
		}

		this.interfaze =
			this._create(interfaceNameElement.getText(), node, context);
	}
}
