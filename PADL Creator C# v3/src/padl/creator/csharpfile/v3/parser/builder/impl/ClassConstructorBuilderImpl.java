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

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import padl.creator.csharpfile.v3.parser.CSharpParser;
import padl.creator.csharpfile.v3.parser.builder.BuilderContext;
import padl.creator.csharpfile.v3.parser.builder.CodeBuilder;
import padl.kernel.Constants;
import padl.kernel.IClass;
import padl.kernel.IConstituent;
import padl.kernel.IFirstClassEntity;
import padl.kernel.IParameter;
import padl.kernel.exception.CreationException;

/**
 *
 */
public class ClassConstructorBuilderImpl extends MethodBuilderImpl {

	public ClassConstructorBuilderImpl(final CodeBuilder parent) {
		super(parent);
	}

	public void create(final ParseTree node, final BuilderContext context)
			throws CreationException {

		// create the constructor
		this.method =
			this.factory.createConstructor(
				this.getParent().close().getName(),
				this.getParent().close().getName());
		
		final List<Token> tokens = Util.getFlatTokenList(node);

		// find out the modifiers of this method
		if (Util.tokenExists(tokens, CSharpParser.PUBLIC)) {
			this.method.setPublic(true);
		} else if (Util.tokenExists(tokens, CSharpParser.PRIVATE)) {
			this.method.setPrivate(true);
		} else if (Util.tokenExists(tokens, CSharpParser.PROTECTED)) {
			this.method.setProtected(true);
		}

		// find out the parameters
		
		// First, restrict tokens to those between ( and )
		final List<Token> tokensMethod = Util.getTokensBetween(tokens, CSharpParser.OPEN_PARENS, CSharpParser.CLOSE_PARENS);
		
		// Then, extract the sublists
		final ArrayList<ArrayList<Token>> subLists = Util.getTokensSublist(tokensMethod, CSharpParser.COMMA);
		
		// Finally, loop though the sublists to add the methods
		for (final ArrayList<Token> subList: subLists) {
			if (subList.size() > 1) {
				// Member Type: before last element
				final String memberType = subList.get(subList.size() - 2).getText();
			
				// Member Name: last element
				final String memberName = subList.get(subList.size() - 1).getText();
				final char[] targetID = memberType.toCharArray();
				IFirstClassEntity target =
					context.getModel().getTopLevelEntityFromID(targetID);
				if (target == null) {
					target = this.factory.createGhost(targetID, targetID);
					context.getModel().addConstituent(target);
				}
				// Add the member
				final IParameter parameter =
					this.factory.createParameter(target, memberName
						.toCharArray(), Constants.CARDINALITY_ONE);
				this.method.addConstituent(parameter);
			}
		}

		final CodeBuilder builder = this.getParent();
		if (builder != null) {
			final IConstituent c = builder.close();
			// only check if parent builder was for a class
			if (c instanceof IClass) {
				IClass clazz = (IClass) c;
				
				// check if this method uses class member fields
				this.findMemberInvocations(tokens, clazz, context);
			}
		}
	}

}
