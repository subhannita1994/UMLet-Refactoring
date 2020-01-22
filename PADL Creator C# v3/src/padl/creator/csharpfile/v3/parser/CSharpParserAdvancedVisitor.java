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
package padl.creator.csharpfile.v3.parser;

import java.util.Stack;

// Generated from CSharpParser.g4 by ANTLR 4.7.1

import padl.creator.csharpfile.v3.parser.builder.BuilderContext;
import padl.creator.csharpfile.v3.parser.builder.CodeBuilder;
import padl.creator.csharpfile.v3.parser.builder.impl.ClassBuilderImpl;
import padl.creator.csharpfile.v3.parser.builder.impl.ClassConstructorBuilderImpl;
import padl.creator.csharpfile.v3.parser.builder.impl.ClassMemberBuilderImpl;
import padl.creator.csharpfile.v3.parser.builder.impl.InterfaceBuilderImpl;
import padl.creator.csharpfile.v3.parser.builder.impl.InterfaceMethodBuilderImpl;
import padl.creator.csharpfile.v3.parser.builder.impl.MethodBuilderImpl;
import padl.kernel.ICodeLevelModel;
import padl.kernel.exception.CreationException;
import padl.kernel.impl.Factory;

/**
 * This class provides an empty implementation of {@link CSharpParserVisitor},
 * which can be extended to create a visitor which only needs to handle a subset
 * of the available methods.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public class CSharpParserAdvancedVisitor<T> extends CSharpParserBaseVisitor<T> {
	
	private ICodeLevelModel model = null;
	private final Stack<CodeBuilder> codeElements = new Stack<CodeBuilder>();

	/**
	 * Use this constructor to complement an existing model
	 * @param aModel the ICodeLevelModel to complement.
	 */
	public CSharpParserAdvancedVisitor(final ICodeLevelModel aModel) {
		this.model = aModel;
	}

	/**
	 * Use this constructor to start from scratch
	 * @param aName the name given to the internally created ICodeLevelModel.
	 */
	public CSharpParserAdvancedVisitor(final String aName) {
		this.model = Factory.getInstance().createCodeLevelModel(aName);
	}

	/**
	 * Returns the ICodeLevelModel created/complemented during this visit.
	 * @return the ICodeLevelModel created/complemented during this visit.
	 */
	public ICodeLevelModel getModel() {
		return this.model;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitClass_definition(CSharpParser.Class_definitionContext ctx) { 
		try {
			// Second pass reader
			final CodeBuilder builder =
				new ClassBuilderImpl(
					this.codeElements.isEmpty() ? null
							: this.codeElements.peek());
			final BuilderContext builderContext = new BuilderContext(model);
			builder.create(ctx, builderContext);
			this.codeElements.push(builder);
		} catch (CreationException e) {
			e.printStackTrace();
		}
		return visitChildren(ctx);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitInterface_definition(CSharpParser.Interface_definitionContext ctx) { 
		try {
			// Second pass reader
			final CodeBuilder builder =
					new InterfaceBuilderImpl(
						this.codeElements.isEmpty() ? null
								: this.codeElements.peek());
			final BuilderContext builderContext = new BuilderContext(model);
			builder.create(ctx, builderContext);
			this.codeElements.push(builder);
		} catch (CreationException e) {
			e.printStackTrace();
		}
		return visitChildren(ctx);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitConstructor_declaration(CSharpParser.Constructor_declarationContext ctx) { 
		try {
			final CodeBuilder builder =
					new ClassConstructorBuilderImpl(
						this.codeElements.isEmpty() ? null
								: this.codeElements.peek());
			final BuilderContext builderContext = new BuilderContext(model);
			builder.create(ctx, builderContext);
			this.codeElements.peek().add(builder.close());
		} catch (CreationException e) {
			e.printStackTrace();
		}
		
		return visitChildren(ctx); 
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitMethod_declaration(CSharpParser.Method_declarationContext ctx) { 
		try {
			final CodeBuilder builder =
						new MethodBuilderImpl(
							this.codeElements.isEmpty() ? null
									: this.codeElements.peek());
			final BuilderContext builderContext = new BuilderContext(model);
			// Let's take the parent of the parent so that we have the modifier
			builder.create(ctx.parent.parent, builderContext);
			this.codeElements.peek().add(builder.close());
		} catch (CreationException e) {
			e.printStackTrace();
		}
		
		return visitChildren(ctx);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitField_declaration(CSharpParser.Field_declarationContext ctx) { 
		try {
			final CodeBuilder builder =
						new ClassMemberBuilderImpl(
							this.codeElements.isEmpty() ? null
									: this.codeElements.peek());
			final BuilderContext builderContext = new BuilderContext(model);
			// We get the 3rd parent of the context to have the modifier (public, private etc) AND the type
			builder.create(ctx.parent.parent.parent, builderContext);
			this.codeElements.peek().add(builder.close());
		} catch (CreationException e) {
			e.printStackTrace();
		}
		
		return visitChildren(ctx); 
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitInterface_member_declaration(CSharpParser.Interface_member_declarationContext ctx) { 
		try {
			final CodeBuilder builder =
					new InterfaceMethodBuilderImpl(
						this.codeElements.isEmpty() ? null
								: this.codeElements.peek());
			final BuilderContext builderContext = new BuilderContext(model);
			builder.create(ctx, builderContext);
			this.codeElements.peek().add(builder.close());
		} catch (CreationException e) {
			e.printStackTrace();
		}	
		return visitChildren(ctx); 
	}
}