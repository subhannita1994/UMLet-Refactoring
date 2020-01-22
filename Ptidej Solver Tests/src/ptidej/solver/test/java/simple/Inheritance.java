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
import ptidej.solver.test.data.problem.InheritanceTest;
import ptidej.solver.test.java.Primitive;

public final class Inheritance extends Primitive {
	private static Occurrence[] BuiltSolutions;

	public Inheritance(final String name) {
		super(name);
	}
	protected void setUp() {
		if (Inheritance.BuiltSolutions == null) {
			Inheritance.BuiltSolutions =
				Primitive.automaticSolve(
					InheritanceTest.class,
					ptidej.solver.test.data.source.InheritanceTest.class);
		}
	}
	public void testNumberOfSolutions() {
		Assert.assertEquals(
			"Number of solutions",
			13,
			Inheritance.BuiltSolutions.length);
	}
	public void testSolution1() {
		Assert.assertEquals("A == A", "A", Inheritance.BuiltSolutions[0]
			.getComponent(InheritanceTest.SUPER_ENTITY)
			.getDisplayValue());
		Assert.assertEquals("A == A", "A", Inheritance.BuiltSolutions[0]
			.getComponent(InheritanceTest.SUB_ENTITY)
			.getDisplayValue());
	}
	public void testSolution10() {
		Assert.assertEquals("E == E", "E", Inheritance.BuiltSolutions[9]
			.getComponent(InheritanceTest.SUPER_ENTITY)
			.getDisplayValue());
		Assert.assertEquals("E == E", "E", Inheritance.BuiltSolutions[9]
			.getComponent(InheritanceTest.SUB_ENTITY)
			.getDisplayValue());
	}
	public void testSolution11() {
		Assert.assertEquals("F == F", "F", Inheritance.BuiltSolutions[10]
			.getComponent(InheritanceTest.SUPER_ENTITY)
			.getDisplayValue());
		Assert.assertEquals("F == F", "F", Inheritance.BuiltSolutions[10]
			.getComponent(InheritanceTest.SUB_ENTITY)
			.getDisplayValue());
	}
	public void testSolution12() {
		Assert.assertEquals(
			"F -|>- java.lang.Object",
			"java.lang.Object",
			Inheritance.BuiltSolutions[11].getComponent(
				InheritanceTest.SUPER_ENTITY).getDisplayValue());
		Assert.assertEquals(
			"F -|>- java.lang.Object",
			"F",
			Inheritance.BuiltSolutions[11].getComponent(
				InheritanceTest.SUB_ENTITY).getDisplayValue());
	}
	public void testSolution13() {
		Assert.assertEquals(
			"java.lang.Object == java.lang.Object",
			"java.lang.Object",
			Inheritance.BuiltSolutions[12].getComponent(
				InheritanceTest.SUPER_ENTITY).getDisplayValue());
		Assert.assertEquals(
			"java.lang.Object == java.lang.Object",
			"java.lang.Object",
			Inheritance.BuiltSolutions[12].getComponent(
				InheritanceTest.SUB_ENTITY).getDisplayValue());
	}
	public void testSolution2() {
		Assert.assertEquals(
			"A -|>- java.lang.Object",
			"java.lang.Object",
			Inheritance.BuiltSolutions[1].getComponent(
				InheritanceTest.SUPER_ENTITY).getDisplayValue());
		Assert.assertEquals(
			"A -|>- java.lang.Object",
			"A",
			Inheritance.BuiltSolutions[1].getComponent(
				InheritanceTest.SUB_ENTITY).getDisplayValue());
	}
	public void testSolution3() {
		Assert.assertEquals("B -|>- A", "A", Inheritance.BuiltSolutions[2]
			.getComponent(InheritanceTest.SUPER_ENTITY)
			.getDisplayValue());
		Assert.assertEquals("B -|>- A", "B", Inheritance.BuiltSolutions[2]
			.getComponent(InheritanceTest.SUB_ENTITY)
			.getDisplayValue());
	}
	public void testSolution4() {
		Assert.assertEquals("B == B", "B", Inheritance.BuiltSolutions[3]
			.getComponent(InheritanceTest.SUPER_ENTITY)
			.getDisplayValue());
		Assert.assertEquals("B == B", "B", Inheritance.BuiltSolutions[3]
			.getComponent(InheritanceTest.SUB_ENTITY)
			.getDisplayValue());
	}
	public void testSolution5() {
		Assert.assertEquals("C -|>- B", "B", Inheritance.BuiltSolutions[4]
			.getComponent(InheritanceTest.SUPER_ENTITY)
			.getDisplayValue());
		Assert.assertEquals("C -|>- B", "C", Inheritance.BuiltSolutions[4]
			.getComponent(InheritanceTest.SUB_ENTITY)
			.getDisplayValue());
	}
	public void testSolution6() {
		Assert.assertEquals("C == C", "C", Inheritance.BuiltSolutions[5]
			.getComponent(InheritanceTest.SUPER_ENTITY)
			.getDisplayValue());
		Assert.assertEquals("C == C", "C", Inheritance.BuiltSolutions[5]
			.getComponent(InheritanceTest.SUB_ENTITY)
			.getDisplayValue());
	}
	public void testSolution7() {
		Assert.assertEquals("D == D", "D", Inheritance.BuiltSolutions[6]
			.getComponent(InheritanceTest.SUPER_ENTITY)
			.getDisplayValue());
		Assert.assertEquals("D == D", "D", Inheritance.BuiltSolutions[6]
			.getComponent(InheritanceTest.SUB_ENTITY)
			.getDisplayValue());
	}
	public void testSolution8() {
		Assert.assertEquals(
			"D -|>- java.lang.Object",
			"java.lang.Object",
			Inheritance.BuiltSolutions[7].getComponent(
				InheritanceTest.SUPER_ENTITY).getDisplayValue());
		Assert.assertEquals(
			"D -|>- java.lang.Object",
			"D",
			Inheritance.BuiltSolutions[7].getComponent(
				InheritanceTest.SUB_ENTITY).getDisplayValue());
	}
	public void testSolution9() {
		Assert.assertEquals("E -|>- D", "D", Inheritance.BuiltSolutions[8]
			.getComponent(InheritanceTest.SUPER_ENTITY)
			.getDisplayValue());
		Assert.assertEquals("E -|>- D", "E", Inheritance.BuiltSolutions[8]
			.getComponent(InheritanceTest.SUB_ENTITY)
			.getDisplayValue());
	}
	public void testSolutionPercentage() {
		for (int i = 0; i < Inheritance.BuiltSolutions.length; i++) {
			Assert.assertEquals(
				"Solution with all constraints",
				100,
				Inheritance.BuiltSolutions[i].getConfidence());
		}
	}
}
