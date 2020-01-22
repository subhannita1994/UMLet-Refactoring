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

import java.util.List;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import padl.creator.csharpfile.v3.parser.CSharpParser;
import padl.creator.csharpfile.v3.parser.builder.BuilderContext;
import padl.creator.csharpfile.v3.parser.builder.CodeBuilder;
import padl.kernel.Constants;
import padl.kernel.IClass;
import padl.kernel.IParameter;
import padl.kernel.exception.CreationException;

/**
 *
 */
public class ClassBuilderImpl extends InitialClassBuilderImpl implements
		CodeBuilder {

	public ClassBuilderImpl(final CodeBuilder parent) {
		super(parent);
	}

	public void create(final ParseTree node, final BuilderContext context)
			throws CreationException {
		
		final List<Token> tokens = Util.getFlatTokenList(node);
		
		final String className = Util.getFirstTokenWithType(tokens, CSharpParser.IDENTIFIER).getText();
		
		if (className == null) {
			throw new CreationException(
				"Could not find the name of the class ?");
		}

		// find out if we know about this interface
		this.clazz =
			(IClass) context.getModel().getConstituentFromName(
					className);
		if (this.clazz == null) {
			this.clazz =
				this._create(className, node, context);
		}

		// find out if this class inherits from another class
		if (Util.tokenExists(tokens, CSharpParser.COLON)) {
			this.findNextInherits(tokens, this.clazz, context);
		}
	}

}
