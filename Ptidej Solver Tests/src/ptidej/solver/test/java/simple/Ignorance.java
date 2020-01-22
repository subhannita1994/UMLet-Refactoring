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
import ptidej.solver.test.data.problem.IgnoranceTest;
import ptidej.solver.test.data.source.UseTest;
import ptidej.solver.test.java.Primitive;

public final class Ignorance extends Primitive {
	private static Occurrence[] BuiltSolutions;

	public Ignorance(final String name) {
		super(name);
	}
	protected void setUp() {
		if (Ignorance.BuiltSolutions == null) {
			Ignorance.BuiltSolutions =
				Primitive.automaticSolve(IgnoranceTest.class, UseTest.class);
		}
	}
	public void testNumberOfSolutions() {
		Assert.assertEquals(
			"Number of solutions",
			4,
			Ignorance.BuiltSolutions.length);
	}
	public void testSolution1() {
		Assert.assertEquals(
			"Compiler -/--> ProgramNode",
			"Compiler",
			Ignorance.BuiltSolutions[0]
				.getComponent(IgnoranceTest.A)
				.getDisplayValue());
		Assert.assertEquals(
			"Compiler -/--> ProgramNode",
			"ProgramNode",
			Ignorance.BuiltSolutions[0]
				.getComponent(IgnoranceTest.B)
				.getDisplayValue());
	}
	public void testSolution2() {
		Assert.assertEquals(
			"ProgramNodeBuilder -/--> Compiler",
			"ProgramNodeBuilder",
			Ignorance.BuiltSolutions[1]
				.getComponent(IgnoranceTest.A)
				.getDisplayValue());
		Assert.assertEquals(
			"ProgramNodeBuilder -/--> Compiler",
			"Compiler",
			Ignorance.BuiltSolutions[1]
				.getComponent(IgnoranceTest.B)
				.getDisplayValue());
	}
	public void testSolution3() {
		Assert.assertEquals(
			"ProgramNode -/--> Compiler",
			"ProgramNode",
			Ignorance.BuiltSolutions[2]
				.getComponent(IgnoranceTest.A)
				.getDisplayValue());
		Assert.assertEquals(
			"ProgramNode -/--> Compiler",
			"Compiler",
			Ignorance.BuiltSolutions[2]
				.getComponent(IgnoranceTest.B)
				.getDisplayValue());
	}
	public void testSolution4() {
		Assert.assertEquals(
			"ProgramNode -/--> ProgramNodeBuilder",
			"ProgramNode",
			Ignorance.BuiltSolutions[3]
				.getComponent(IgnoranceTest.A)
				.getDisplayValue());
		Assert.assertEquals(
			"ProgramNode -/--> ProgramNodeBuilder",
			"ProgramNodeBuilder",
			Ignorance.BuiltSolutions[3]
				.getComponent(IgnoranceTest.B)
				.getDisplayValue());
	}
	public void testSolutionPercentage() {
		for (int i = 0; i < Ignorance.BuiltSolutions.length; i++) {
			Assert.assertEquals(
				"Solution with all constraints",
				100,
				Ignorance.BuiltSolutions[i].getConfidence());
		}
	}
}
