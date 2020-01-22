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
package parser.parser.test.visitor;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import parser.wrapper.ExtendedASTVisitor;
import parser.wrapper.NamedCompilationUnit;

/**
 * 
 */
public class TestVisitor extends ExtendedASTVisitor {

	private void display(final String str) {
		System.out.println(str);
	}

	@Override
	public void endVisit(final CompilationUnit node) {
		this.display("Leave a Compilation Unit");
		super.endVisit(node);
	}

	@Override
	public void endVisit(final NamedCompilationUnit aNamedCompilationUnit) {
		this.display("Leave a Named Compilation Unit \n");
		super.endVisit(aNamedCompilationUnit);
	}

	@Override
	public void endVisit(final TypeDeclaration node) {
		this.display("Finish " + node.resolveBinding().getQualifiedName());
		super.endVisit(node);
	}

	@Override
	public void endVisitJavaFilePath(final String javaFilePath) {
		this.display("Finish " + javaFilePath);
		super.endVisitJavaFilePath(javaFilePath);
	}

	@Override
	public boolean visit(final CompilationUnit node) {
		this.display("Enter a Compilation Unit");
		return super.visit(node);
	}

	@Override
	public boolean visit(final NamedCompilationUnit aNamedCompilationUnit) {
		this.display("Enter a Named Compilation Unit");
		return super.visit(aNamedCompilationUnit);
	}

	@Override
	public boolean visit(final TypeDeclaration node) {
		this.display("Visit " + node.resolveBinding().getQualifiedName());
		return super.visit(node);
	}

	@Override
	public boolean visitJavaFilePath(final String javaFilePath) {
		this.display("Visit " + javaFilePath);
		return super.visitJavaFilePath(javaFilePath);
	}

}
