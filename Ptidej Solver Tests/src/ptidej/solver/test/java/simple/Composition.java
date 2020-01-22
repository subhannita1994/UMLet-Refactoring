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
import ptidej.solver.test.data.problem.CompositionTest;
import ptidej.solver.test.java.Primitive;

public final class Composition extends Primitive {
	private static Occurrence[] BuiltSolutions;

	public Composition(final String name) {
		super(name);
	}
	protected void setUp() {
		if (Composition.BuiltSolutions == null) {
			Composition.BuiltSolutions =
				Primitive.automaticSolve(
					ptidej.solver.test.data.problem.CompositionTest.class,
					ptidej.solver.test.data.source.CompositionTest.class);
		}
	}
	public void testNumberOfSolutions() {
		Assert.assertEquals(
			"Number of solutions",
			2,
			Composition.BuiltSolutions.length);
	}
	public void testSolution1() {
		Assert.assertEquals(
			"AggregateClass1 == AggregateClass1",
			"AggregateClass1",
			Composition.BuiltSolutions[0].getComponent(
				CompositionTest.AGGREGATE).getDisplayValue());
		Assert.assertEquals(
			"AggregatedClass1 == AggregatedClass1",
			"AggregatedClass1",
			Composition.BuiltSolutions[0].getComponent(
				CompositionTest.AGGREGATED).getDisplayValue());
	}
	public void testSolution2() {
		Assert.assertEquals(
			"AggregateClass2 == AggregateClass2",
			"AggregateClass2",
			Composition.BuiltSolutions[1].getComponent(
				CompositionTest.AGGREGATE).getDisplayValue());
		Assert.assertEquals(
			"AggregatedClass2 == AggregatedClass2",
			"AggregatedClass2",
			Composition.BuiltSolutions[1].getComponent(
				CompositionTest.AGGREGATED).getDisplayValue());
	}
	public void testSolutionPercentage() {
		for (int i = 0; i < Composition.BuiltSolutions.length; i++) {
			Assert.assertEquals(
				"Solution with all constraints",
				100,
				Composition.BuiltSolutions[i].getConfidence());
		}
	}
}
