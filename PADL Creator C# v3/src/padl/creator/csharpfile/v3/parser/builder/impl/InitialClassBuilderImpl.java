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
import padl.kernel.IClass;
import padl.kernel.IConstituent;
import padl.kernel.IConstituentOfEntity;
import padl.kernel.exception.CreationException;

/**
 * This builder creates an IClass model without any relationships, extensions, members, etc. 
 * It is meant to be used within a 1st simple scan of all the classes to analyze.
 */
public class InitialClassBuilderImpl extends AbstractClassBuilderImpl implements
		CodeBuilder {

	protected IClass clazz;

	public InitialClassBuilderImpl(final CodeBuilder parent) {
		super(parent);
	}

	protected IClass _create(
		final String className,
		final ParseTree node,
		final BuilderContext context) {

		final IClass out =
			this.factory.createClass(
				className.toCharArray(),
				className.toCharArray());

		final List<Token> tokens = Util.getFlatTokenList(node);

		// find out the modifiers of this interface
		if (Util.tokenExists(tokens, CSharpParser.PUBLIC)) {
			out.setPublic(true);
		} else if (Util.tokenExists(tokens, CSharpParser.PRIVATE)) {
			out.setPrivate(true);
		} else if (Util.tokenExists(tokens, CSharpParser.PROTECTED)) {
			out.setProtected(true);
		}

		// find out if this class is abstract
		if (Util.tokenExists(tokens, CSharpParser.ABSTRACT)) {
			out.setAbstract(true);
		}

		return out;
	}

	public void add(final IConstituent childElement) {
		this.clazz.addConstituent((IConstituentOfEntity) childElement);
	}

	public IConstituent close() {
		return this.clazz;
	}

	public void create(final ParseTree node, final BuilderContext context)
			throws CreationException {
		
		final String className = Util.getFirstTokenWithType(Util.getFlatTokenList(node), CSharpParser.IDENTIFIER).getText();
		
		if (className == null) {
			throw new CreationException(
				"Could not find the name of the class ?");
		}

		this.clazz = this._create(className, node, context);
	}
}
