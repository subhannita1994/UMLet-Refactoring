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

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Token;

import padl.creator.csharpfile.v3.parser.CSharpParser;
import padl.creator.csharpfile.v3.parser.builder.BuilderContext;
import padl.creator.csharpfile.v3.parser.builder.CodeBuilder;
import padl.kernel.Constants;
import padl.kernel.IConstituent;
import padl.kernel.IConstituentOfOperation;
import padl.kernel.IFirstClassEntity;
import padl.kernel.IMethod;
import padl.kernel.IParameter;
import padl.kernel.exception.CreationException;

public class InterfaceMethodBuilderImpl extends AbstractPADLCodeBuilder
		implements CodeBuilder {

	private IMethod method;

	public InterfaceMethodBuilderImpl(final CodeBuilder parent) {
		super(parent);
	}

	public void add(final IConstituent childElement) {
		this.method.addConstituent((IConstituentOfOperation) childElement);
	}

	public IConstituent close() {
		return this.method;
	}

	public void create(final ParseTree node, final BuilderContext context)
			throws CreationException {
		
		final List<Token> tokens = Util.getFlatTokenList(node);
		
		final String methodName = Util.getFirstTokenWithType(tokens, CSharpParser.IDENTIFIER).getText();
		
		// sanity check
		if (methodName == null) {
			throw new CreationException(
				"Found an interface method without name ??");
		}
		
		// create the method
		this.method =
			this.factory.createMethod(methodName
				.toLowerCase()
				.toCharArray(), methodName.toLowerCase().toCharArray()); // Lower case to help pattern detection by PADL ;)
		
		this.method.setAbstract(true);
		
		final String returnType = Util.getTokenBeforeType(tokens, CSharpParser.IDENTIFIER).getText().toLowerCase();

		if (returnType != null) {
			switch (returnType) {
				case "void" :
					break;
				default :
					{
						this.method.setReturnType(returnType
								.toCharArray());
					}
			}
		}

		// find out the modifiers of this method
		if (Util.tokenExists(tokens, CSharpParser.PUBLIC)) {
			this.method.setPublic(true);
		} else if (Util.tokenExists(tokens, CSharpParser.PRIVATE)) {
			this.method.setPrivate(true);
		} else if (Util.tokenExists(tokens, CSharpParser.PROTECTED)) {
			this.method.setProtected(true);
		}		
		
		// First, restrict tokens to those between ( and )
		final List<Token> tokensMethod = Util.getTokensBetween(tokens, CSharpParser.OPEN_PARENS, CSharpParser.CLOSE_PARENS);
		
		// Then, extract the sublists
		final ArrayList<ArrayList<Token>> subLists = Util.getTokensSublist(tokensMethod, CSharpParser.COMMA);
		
		// Finally, loop though the sublists to add the methods
		for (final ArrayList<Token> subList: subLists) {
			if (subList.size() > 1) {
				// Member Type: before last element
				String memberType = subList.get(subList.size() - 2).getText();
			
				// Member Name: last element
				String memberName = subList.get(subList.size() - 1).getText();
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
	}
}
