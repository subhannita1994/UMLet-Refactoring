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
package ptidej.solver.test.java.simple;

import junit.framework.Assert;
import ptidej.solver.Occurrence;
import ptidej.solver.test.data.problem.UseTest;
import ptidej.solver.test.java.Primitive;

public final class Use extends Primitive {
	private static Occurrence[] BuiltSolutions;

	public Use(final String name) {
		super(name);
	}
	protected void setUp() {
		if (Use.BuiltSolutions == null) {
			Use.BuiltSolutions =
				Primitive.automaticSolve(
					UseTest.class,
					ptidej.solver.test.data.source.UseTest.class);
		}
	}
	public void testNumberOfSolutions() {
		Assert
			.assertEquals("Number of solutions", 2, Use.BuiltSolutions.length);
	}
	public void testSolution1() {
		Assert.assertEquals(
			"Compiler ----> ProgramNodeBuilder",
			"Compiler",
			Use.BuiltSolutions[0]
				.getComponent(UseTest.CALLER)
				.getDisplayValue());
		Assert.assertEquals(
			"Compiler ----> ProgramNodeBuilder",
			"ProgramNodeBuilder",
			Use.BuiltSolutions[0]
				.getComponent(UseTest.CALLEE)
				.getDisplayValue());
	}
	public void testSolution2() {
		Assert.assertEquals(
			"ProgramNodeBuilder ----> ProgramNode",
			"ProgramNodeBuilder",
			Use.BuiltSolutions[1]
				.getComponent(UseTest.CALLER)
				.getDisplayValue());
		Assert.assertEquals(
			"ProgramNodeBuilder ----> ProgramNode",
			"ProgramNode",
			Use.BuiltSolutions[1]
				.getComponent(UseTest.CALLEE)
				.getDisplayValue());
	}
	public void testSolutionPercentage() {
		for (int i = 0; i < Use.BuiltSolutions.length; i++) {
			Assert.assertEquals(
				"Solution with all constraints",
				100,
				Use.BuiltSolutions[i].getConfidence());
		}
	}
}
