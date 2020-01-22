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
package ptidej.solver.test.claire.defect;

import junit.framework.Assert;
import ptidej.solver.Occurrence;
import ptidej.solver.OccurrenceGenerator;
import ptidej.solver.test.claire.Primitive;
import ptidej.solver.test.claire.example.Composite2;
import ptidej.solver.test.data.source.AssociationTest;

public final class AssociationDistance extends Primitive {
	private static final char[] ASSOCIATED_ENTITY = "AssociatedEntity"
		.toCharArray();
	private static final char[] ASSOCIATION_DISTANCE = "AssociationDistance"
		.toCharArray();
	private static final char[] ENTITY = "Entity".toCharArray();
	private static Occurrence[] Solutions;

	public AssociationDistance(final String name) {
		super(name);
	}
	protected void setUp() throws IllegalAccessException,
			InstantiationException {

		if (AssociationDistance.Solutions == null) {
			//	final IIdiomLevelModel idiomLevelModel =
			//		Factory.getUniqueInstance().createIdiomLevelModel(
			//			"ptidej.example.composite2");
			//	idiomLevelModel.create(
			//		new LightClassFileCreator(
			//			DefaultFileRepository.getDefaultFileRepository(),
			//			new String[] { PropertyManager.getExamplesDirectory()+"ptidej/example/composite2/" }));

			AssociationDistance.Solutions =
				this.testDesignPattern(
					Composite2.class,
					Primitive.ALL_SOLUTIONS,
					"AssociationDistanceTest".toCharArray(),
					AssociationTest.class,
					OccurrenceGenerator.SOLVER_AUTOMATIC,
					OccurrenceGenerator.PROBLEM_CUSTOM);
		}
	}
	public void testNumberOfSolutions() {
		Assert.assertEquals(
			"Number of solutions",
			4,
			AssociationDistance.Solutions.length);
	}
	public void testSolution1() {
		//	1.100.Name = AssociationDistance Pattern Problem
		//	1.100.XCommand = System.out.println("No transformation required.");
		//	1.100.Entity = AggregateClass1
		//	1.100.AssociatedEntity = AggregatedClass1
		//	1.100.AssociationDistance = 1
		try {
			Assert.assertEquals(
				"Solution 1",
				"AggregateClass1",
				AssociationDistance.Solutions[0].getComponent(
					AssociationDistance.ENTITY).getDisplayValue());
			Assert.assertEquals(
				"Solution 1",
				"AggregatedClass1",
				AssociationDistance.Solutions[0].getComponent(
					AssociationDistance.ASSOCIATED_ENTITY).getDisplayValue());
			Assert
				.assertEquals(
					"Solution 1",
					"1",
					AssociationDistance.Solutions[0]
						.getComponent(AssociationDistance.ASSOCIATION_DISTANCE)
						.getDisplayValue());
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
	}
	public void testSolution2() {
		//	2.100.Name = AssociationDistance Pattern Problem
		//	2.100.XCommand = System.out.println("No transformation required.");
		//	2.100.Entity = AggregateClass2
		//	2.100.AssociatedEntity = AggregatedClass2
		//	2.100.AssociationDistance = 1
		Assert.assertEquals(
			"Solution 2",
			"AggregateClass2",
			AssociationDistance.Solutions[1].getComponent(
				AssociationDistance.ENTITY).getDisplayValue());
		Assert.assertEquals(
			"Solution 2",
			"AggregatedClass2",
			AssociationDistance.Solutions[1].getComponent(
				AssociationDistance.ASSOCIATED_ENTITY).getDisplayValue());
		Assert.assertEquals("Solution 2", "1", AssociationDistance.Solutions[1]
			.getComponent(AssociationDistance.ASSOCIATION_DISTANCE)
			.getDisplayValue());
	}
	public void testSolution3() {
		//	3.100.Name = AssociationDistance Pattern Problem
		//	3.100.XCommand = System.out.println("No transformation required.");
		//	3.100.Entity = AggregatedClass1
		//	3.100.AssociatedEntity = AssociatedClass1
		//	3.100.AssociationDistance = 1
		Assert.assertEquals(
			"Solution 3",
			"AggregatedClass1",
			AssociationDistance.Solutions[2].getComponent(
				AssociationDistance.ENTITY).getDisplayValue());
		Assert.assertEquals(
			"Solution 3",
			"AssociatedClass1",
			AssociationDistance.Solutions[2].getComponent(
				AssociationDistance.ASSOCIATED_ENTITY).getDisplayValue());
		Assert.assertEquals("Solution 3", "1", AssociationDistance.Solutions[2]
			.getComponent(AssociationDistance.ASSOCIATION_DISTANCE)
			.getDisplayValue());
	}
	public void testSolution4() {
		//	4.100.Name = AssociationDistance Pattern Problem
		//	4.100.XCommand = System.out.println("No transformation required.");
		//	4.100.Entity = AggregateClass1
		//	4.100.AssociatedEntity = AssociatedClass1
		//	4.100.AssociationDistance = 2
		Assert.assertEquals(
			"Solution 4",
			"AggregateClass1",
			AssociationDistance.Solutions[3].getComponent(
				AssociationDistance.ENTITY).getDisplayValue());
		Assert.assertEquals(
			"Solution 4",
			"AssociatedClass1",
			AssociationDistance.Solutions[3].getComponent(
				AssociationDistance.ASSOCIATED_ENTITY).getDisplayValue());
		Assert.assertEquals("Solution 4", "2", AssociationDistance.Solutions[3]
			.getComponent(AssociationDistance.ASSOCIATION_DISTANCE)
			.getDisplayValue());
	}
}
