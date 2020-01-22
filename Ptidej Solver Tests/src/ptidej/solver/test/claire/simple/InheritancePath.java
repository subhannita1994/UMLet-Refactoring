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
import ptidej.solver.test.data.pattern.InheritancePathTest;
import ptidej.solver.test.data.source.InheritanceTest;

public final class InheritancePath extends Primitive {
	private static Occurrence[] BuiltSolutions = null;

	public InheritancePath(final String name) {
		super(name);
	}
	protected void setUp() throws IllegalAccessException,
			InstantiationException {

		if (InheritancePath.BuiltSolutions == null) {
			InheritancePath.BuiltSolutions =
				this.testDesignPattern(
					InheritancePath.class,
					Primitive.ALL_SOLUTIONS,
					((IDesignMotifModel) InheritancePathTest.class
						.newInstance()).getName(),
					InheritanceTest.class,
					OccurrenceGenerator.SOLVER_AUTOMATIC,
					OccurrenceGenerator.PROBLEM_AC4);
		}
	}
	public void testNumberOfSolutions() {
		Assert.assertEquals(
			"Number of solutions",
			10,
			InheritancePath.BuiltSolutions.length);
	}
	public void testSolution1() {
		Assert.assertEquals(
			"A -|>- java.lang.Object",
			"java.lang.Object",
			InheritancePath.BuiltSolutions[0].getComponent(
				InheritancePathTest.SUPER_ENTITY).getDisplayValue());
		Assert.assertEquals(
			"A -|>- java.lang.Object",
			"A",
			InheritancePath.BuiltSolutions[0].getComponent(
				InheritancePathTest.SUB_ENTITY).getDisplayValue());
	}
	public void testSolution10() {
		Assert.assertEquals(
			"F -|>- java.lang.Object",
			"java.lang.Object",
			InheritancePath.BuiltSolutions[9].getComponent(
				InheritancePathTest.SUPER_ENTITY).getDisplayValue());
		Assert.assertEquals(
			"F -|>- java.lang.Object",
			"F",
			InheritancePath.BuiltSolutions[9].getComponent(
				InheritancePathTest.SUB_ENTITY).getDisplayValue());
	}
	public void testSolution2() {
		Assert.assertEquals("B -|>- A", "A", InheritancePath.BuiltSolutions[1]
			.getComponent(InheritancePathTest.SUPER_ENTITY)
			.getDisplayValue());
		Assert.assertEquals("B -|>- A", "B", InheritancePath.BuiltSolutions[1]
			.getComponent(InheritancePathTest.SUB_ENTITY)
			.getDisplayValue());
	}
	public void testSolution3() {
		Assert.assertEquals(
			"B -|>- java.lang.Object",
			"java.lang.Object",
			InheritancePath.BuiltSolutions[2].getComponent(
				InheritancePathTest.SUPER_ENTITY).getDisplayValue());
		Assert.assertEquals(
			"B -|>- java.lang.Object",
			"B",
			InheritancePath.BuiltSolutions[2].getComponent(
				InheritancePathTest.SUB_ENTITY).getDisplayValue());
	}
	public void testSolution4() {
		Assert.assertEquals("C -|>- A", "A", InheritancePath.BuiltSolutions[3]
			.getComponent(InheritancePathTest.SUPER_ENTITY)
			.getDisplayValue());
		Assert.assertEquals("C -|>- A", "C", InheritancePath.BuiltSolutions[3]
			.getComponent(InheritancePathTest.SUB_ENTITY)
			.getDisplayValue());
	}
	public void testSolution5() {
		Assert.assertEquals("C -|>- B", "B", InheritancePath.BuiltSolutions[4]
			.getComponent(InheritancePathTest.SUPER_ENTITY)
			.getDisplayValue());
		Assert.assertEquals("C -|>- B", "C", InheritancePath.BuiltSolutions[4]
			.getComponent(InheritancePathTest.SUB_ENTITY)
			.getDisplayValue());
	}
	public void testSolution6() {
		Assert.assertEquals(
			"C -|>- java.lang.Object",
			"java.lang.Object",
			InheritancePath.BuiltSolutions[5].getComponent(
				InheritancePathTest.SUPER_ENTITY).getDisplayValue());
		Assert.assertEquals(
			"C -|>- java.lang.Object",
			"C",
			InheritancePath.BuiltSolutions[5].getComponent(
				InheritancePathTest.SUB_ENTITY).getDisplayValue());
	}
	public void testSolution7() {
		Assert.assertEquals(
			"D -|>- java.lang.Object",
			"java.lang.Object",
			InheritancePath.BuiltSolutions[6].getComponent(
				InheritancePathTest.SUPER_ENTITY).getDisplayValue());
		Assert.assertEquals(
			"D -|>- java.lang.Object",
			"D",
			InheritancePath.BuiltSolutions[6].getComponent(
				InheritancePathTest.SUB_ENTITY).getDisplayValue());
	}
	public void testSolution8() {
		Assert.assertEquals("E -|>- D", "D", InheritancePath.BuiltSolutions[7]
			.getComponent(InheritancePathTest.SUPER_ENTITY)
			.getDisplayValue());
		Assert.assertEquals("E -|>- D", "E", InheritancePath.BuiltSolutions[7]
			.getComponent(InheritancePathTest.SUB_ENTITY)
			.getDisplayValue());
	}
	public void testSolution9() {
		Assert.assertEquals(
			"E -|>- java.lang.Object",
			"java.lang.Object",
			InheritancePath.BuiltSolutions[8].getComponent(
				InheritancePathTest.SUPER_ENTITY).getDisplayValue());
		Assert.assertEquals(
			"E -|>- java.lang.Object",
			"E",
			InheritancePath.BuiltSolutions[8].getComponent(
				InheritancePathTest.SUB_ENTITY).getDisplayValue());
	}
	public void testSolutionPercentage() {
		for (int i = 0; i < InheritancePath.BuiltSolutions.length; i++) {
			Assert.assertEquals(
				"Solution with all constraints",
				100,
				InheritancePath.BuiltSolutions[i].getConfidence());
		}
	}
}
