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
package ptidej.solver.test.claire.simple;

import junit.framework.Assert;
import padl.motif.IDesignMotifModel;
import ptidej.solver.Occurrence;
import ptidej.solver.OccurrenceGenerator;
import ptidej.solver.test.claire.Primitive;
import ptidej.solver.test.data.pattern.UseTest;

public final class Use extends Primitive {
	private static Occurrence[] BuiltSolutions;

	public Use(final String name) {
		super(name);
	}
	protected void setUp() throws IllegalAccessException,
			InstantiationException {

		if (Use.BuiltSolutions == null) {
			Use.BuiltSolutions =
				this
					.testDesignPattern(
						Use.class,
						Primitive.ALL_SOLUTIONS,
						((IDesignMotifModel) ptidej.solver.test.data.pattern.UseTest.class
							.newInstance()).getName(),
						ptidej.solver.test.data.source.UseTest.class,
						OccurrenceGenerator.SOLVER_AUTOMATIC,
						OccurrenceGenerator.PROBLEM_AC4);
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
