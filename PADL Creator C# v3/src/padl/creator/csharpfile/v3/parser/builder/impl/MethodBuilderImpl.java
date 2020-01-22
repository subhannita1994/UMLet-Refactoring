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
import java.util.Iterator;
import java.util.List;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import padl.creator.csharpfile.v3.parser.CSharpParser;
import padl.creator.csharpfile.v3.parser.builder.BuilderContext;
import padl.creator.csharpfile.v3.parser.builder.CodeBuilder;
import padl.kernel.Constants;
import padl.kernel.IClass;
import padl.kernel.IConstituent;
import padl.kernel.IConstituentOfOperation;
import padl.kernel.IEntity;
import padl.kernel.IField;
import padl.kernel.IFirstClassEntity;
import padl.kernel.IMethod;
import padl.kernel.IMethodInvocation;
import padl.kernel.IOperation;
import padl.kernel.IParameter;
import padl.kernel.exception.CreationException;

/**
 * 
 */
public class MethodBuilderImpl extends AbstractPADLCodeBuilder implements
		CodeBuilder {

	protected IOperation method;

	public MethodBuilderImpl(final CodeBuilder parent) {
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
			throw new CreationException("Found a method without name ??");
		}

		// create the method
		this.method =
			this.factory.createMethod(methodName
				.toLowerCase()
				.toCharArray(), methodName.toLowerCase().toCharArray()); // Lower case to help pattern detection by PADL ;)
		
		final String returnType = Util.getTokenBeforeType(tokens, CSharpParser.IDENTIFIER).getText().toLowerCase();

		if (returnType != null) {
			switch (returnType) {
				case "void" :
					break;
				default :
					{
						((IMethod) this.method).setReturnType(returnType
							.toCharArray());
						final IConstituent constituent =
							context.getModel().getConstituentFromName(
								returnType);
						if (constituent != null
								&& constituent instanceof IEntity) {
							this.method.addConstituent(this.factory
								.createMethodInvocation(
									IMethodInvocation.CLASS_INSTANCE,
									1,
									0,
									(IFirstClassEntity) constituent));
						}
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

		// find out if it is a virtual method
		if (Util.tokenExists(tokens, CSharpParser.VIRTUAL)) {
			this.method.setAbstract(true);
		}
		
		// find out if it is a static method
		if (Util.tokenExists(tokens, CSharpParser.STATIC)) {
			this.method.setStatic(true);
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

		final CodeBuilder builder = this.getParent();
		if (builder != null) {
			final IConstituent c = builder.close();
			// only check if parent builder was for a class
			if (c instanceof IClass) {
				IClass clazz = (IClass) c;
				// check if this method uses class member fields				
				this.findMethodParameters(tokens, clazz, context);
				
				this.findMemberInvocations(tokens, clazz, context);
				
				this.findCreationInvocations(tokens, clazz, context);
			}
		}

	}
	protected IMethodInvocation createInvocation(
		final IClass clazz,
		final BuilderContext context,
		final Token memberType,
		IMethodInvocation invocation,
		final IField field) {

		if (field.getName().equals(memberType.getText())) {
			IEntity entity = null;
			if (field.getComment() != null) {
				final IConstituent constituent =
					context.getModel().getConstituentFromName(
						field.getComment());
				if (constituent != null && constituent instanceof IEntity) {
					entity = (IEntity) constituent;
				}
			}
			else {
				final IConstituent constituent =
					context.getModel().getConstituentFromName(field.getType());
				if (constituent != null && constituent instanceof IEntity) {
					entity = (IEntity) constituent;
				}
			}

			if (entity != null) {
				invocation =
					this.factory.createMethodInvocation(
						IMethodInvocation.INSTANCE_INSTANCE,
						1,
						0,
						(IFirstClassEntity) entity,
						clazz);
			}
		}
		return invocation;
	}

	protected void findCreationInvocations(
			final List<Token> tokens,
			final IClass clazz,
			final BuilderContext context) {
		if (!Util.tokenExists(tokens, CSharpParser.NEW)) {
			return;
		}
		
		final List<Token> identifiers = Util.getTokensWithType(tokens, CSharpParser.IDENTIFIER);
		
		for (final Token identifier: identifiers) {
			// check if the new object is of a known class
			final IConstituent constituent =
				context.getModel().getConstituentFromName(
						identifier.getText());
			if (constituent != null) {
				final IMethodInvocation invocation =
					this.factory.createMethodInvocation(
						IMethodInvocation.INSTANCE_CREATION,
						1,
						0,
						(IFirstClassEntity) constituent);
				this.method.addConstituent(invocation);
			}
		}
	}

	protected void findMemberInvocations(
			final List<Token> tokens,
			final IClass clazz,
			final BuilderContext context) {
		
		final List<Token> identifiers = Util.getTokensWithType(tokens, CSharpParser.IDENTIFIER);
		
		for (final Token identifier: identifiers) {
			IMethodInvocation invocation = null;

			// check this class
			Iterator<?> iter = clazz.getIteratorOnConstituents(IField.class);
			while (iter.hasNext() && invocation == null) {
				IField field = (IField) iter.next();
				invocation =
					this.createInvocation(
						clazz,
						context,
						identifier,
						invocation,
						field);
				
				// check all inherited classes
				if (invocation == null) {
					final Iterator<?> iter2 =
						clazz.getIteratorOnInheritedEntities();
					while (iter2.hasNext()) {
						final Object classObj = iter2.next();
						if (classObj instanceof IClass) {
							iter = clazz.getIteratorOnConstituents(IField.class);
							while (iter.hasNext() && invocation == null) {
								field = (IField) iter.next();
								if (field.isPublic() || field.isProtected()) {
									invocation =
										this.createInvocation(
											clazz,
											context,
											identifier,
											invocation,
											field);
								}
							}
						}
					}
				}

				if (invocation != null) {
					this.method.addConstituent(invocation);
				}
			}
		}
	}
	
	protected void findMethodParameters(
			final List<Token> tokens,
			final IClass clazz,
			final BuilderContext context) {
		List<Token> identifiers = Util.getTokensWithType(tokens, CSharpParser.IDENTIFIER);
		
		for (final Token identifier: identifiers) {
			IMethodInvocation invocation = null;
			
			// check this class
			Iterator<?> iter = clazz.getIteratorOnConstituents(IField.class);
			while (iter.hasNext() && invocation == null) {
				final IField field = (IField) iter.next();
				invocation =
					this.createInvocation(
						clazz,
						context,
						identifier,
						invocation,
						field);
			}

			// check all inherited classes
			if (invocation == null) {
				final Iterator<?> iter2 =
					clazz.getIteratorOnInheritedEntities();
				while (iter2.hasNext()) {
					final Object classObj = iter2.next();
					if (classObj instanceof IClass) {
						iter = clazz.getIteratorOnConstituents(IField.class);
						while (iter.hasNext() && invocation == null) {
							final IField field = (IField) iter.next();
							if (field.isPublic() || field.isProtected()) {
								invocation =
									this.createInvocation(
										clazz,
										context,
										identifier,
										invocation,
										field);
							}
						}
					}
				}
			}

			if (invocation != null) {
				this.method.addConstituent(invocation);
			}
		}
	}
}
