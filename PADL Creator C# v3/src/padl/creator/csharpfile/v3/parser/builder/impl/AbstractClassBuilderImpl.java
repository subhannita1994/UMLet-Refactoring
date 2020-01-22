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

import padl.creator.csharpfile.v3.parser.CSharpParser;
import padl.creator.csharpfile.v3.parser.builder.BuilderContext;
import padl.creator.csharpfile.v3.parser.builder.CodeBuilder;
import padl.kernel.IClass;
import padl.kernel.IConstituent;
import padl.kernel.IEntity;
import padl.kernel.IFirstClassEntity;
import padl.kernel.IGhost;
import padl.kernel.IInterface;
import padl.kernel.IInterfaceActor;

/**
 *
 */
public abstract class AbstractClassBuilderImpl extends AbstractPADLCodeBuilder {

	protected AbstractClassBuilderImpl(final CodeBuilder parent) {
		super(parent);
	}
	
	protected void findNextInherits(final List<Token> tokens, final IEntity entity, final BuilderContext context) {
		// First, get the line where the "public class XX" is declared
		final int declarationLine = tokens.get(0).getLine();
		// Then, get the tokens from this line
		final List<Token> declarationLineTokens = Util.getTokensFromLine(tokens, declarationLine);
		// Now, get the identifiers after the colon. There can only be one parent class,
		// but possibly one or more interface implementations
		final List<Token> identifiers = Util.getTokensAfterType(declarationLineTokens, CSharpParser.COLON);
		// Loop though them
		for (Token extend_: identifiers) {
			// figure out if this class is known to the current ICodeLevelModel
			IConstituent constituent =
				context.getModel().getConstituentFromName(extend_.getText());
			if (constituent == null) {
				// we need to create a GhostClass
				constituent =
					this.factory.createGhost(
						extend_.getText().toCharArray(),
						extend_.getText().toCharArray());
				((IGhost) entity)
					.addInheritedEntity((IFirstClassEntity) constituent);
			}
			else {
				if (entity instanceof IClass
						&& constituent instanceof IInterface) {
					((IClass) entity)
						.addImplementedInterface((IInterfaceActor) constituent);
				}
				else {
					((IClass) entity)
						.addInheritedEntity((IFirstClassEntity) constituent);
				}
			}
		}
	}
}
