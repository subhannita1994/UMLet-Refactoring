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
import padl.kernel.Constants;
import padl.kernel.IConstituent;
import padl.kernel.IEntity;
import padl.kernel.IField;

/**
 *
 */
public class ClassMemberBuilderImpl extends AbstractPADLCodeBuilder {

	private IField field;

	public ClassMemberBuilderImpl(final CodeBuilder parent) {
		super(parent);
	}

	public void create(final ParseTree node, final BuilderContext context) {
		final List<Token> tokens = Util.getFlatTokenList(node);
		
		final Token equalsSign = Util.getFirstTokenWithType(tokens, CSharpParser.EQUALS);

		Token name = null;
		if (equalsSign != null) {
			name = Util.getTokenBeforeType(tokens, CSharpParser.EQUALS);
		}
		else {
			name = Util.getFirstTokenWithType(tokens, CSharpParser.IDENTIFIER);
		}

		final Token type = Util.getTokenBeforeType(tokens, CSharpParser.IDENTIFIER);
		if (type != null && name != null) {
			// Add the member
			this.field =
				this.factory.createField(
					name.getText().toCharArray(),
					name.getText().toCharArray(),
					type.getText().toCharArray(),
					Constants.CARDINALITY_ONE);
			if (Util.tokenExists(tokens, CSharpParser.PUBLIC)) {
				this.field.setPublic(true);
			} else if (Util.tokenExists(tokens, CSharpParser.PRIVATE)) {
				this.field.setPrivate(true);
			} else if (Util.tokenExists(tokens, CSharpParser.PROTECTED)) {
				this.field.setProtected(true);
			}

			// hardcoded list detection for class member fields
			if (type.getText().contains("List")) {
				this.field.setCardinality(2); // a list has a potential cardinality of more than one... 
				if (Util.tokenExists(tokens, CSharpParser.OPEN_BRACE) && Util.tokenExists(tokens, CSharpParser.CLOSE_BRACE)) {
					final Token realType =
						Util.getFirstTokenWithType(tokens, new int[] { CSharpParser.IDENTIFIER,
									CSharpParser.GT });
					if (realType != null) {
						final IConstituent constituent =
							context.getModel().getConstituentFromName(
								realType.getText());
						if (constituent != null
								&& constituent instanceof IEntity) {
							this.field.setComment(realType.getText()); // place name in comments ;)
						}
					}
				}
			}
		}
	}
	
	public void add(final IConstituent childElement) {
		// ignore
	}

	public IConstituent close() {
		return this.field;
	}
}
