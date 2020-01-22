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
package ptidej.solver.test.claire.example;

import junit.framework.Assert;
import padl.creator.classfile.CompleteClassFileCreator;
import padl.kernel.ICodeLevelModel;
import padl.kernel.exception.CreationException;
import padl.kernel.impl.Factory;
import padl.motif.IDesignMotifModel;
import ptidej.solver.Occurrence;
import ptidej.solver.OccurrenceGenerator;
import ptidej.solver.test.claire.Primitive;

public class Composite2 extends Primitive {
	private static int NumberOfExpectedSolutions;
	private static Occurrence[] FoundSolutions;
	private static Occurrence[] ExpectedSolutions;

	public Composite2(final String name) {
		super(name);
	}
	protected void setUp() throws IllegalAccessException,
			InstantiationException {

		if (Composite2.FoundSolutions == null) {
			final IDesignMotifModel pattern =
				(IDesignMotifModel) padl.motif.repository.Composite.class
					.newInstance();

			final ICodeLevelModel codeLevelModel =
				Factory.getInstance().createCodeLevelModel(
					"ptidej.example.composite2");
			try {
				codeLevelModel
					.create(new CompleteClassFileCreator(
						new String[] { "../Ptidej Tests/bin/ptidej/example/composite2/" },
						false));
			}
			catch (final CreationException e) {
				e.printStackTrace();
			}

			// Expected solutions.
			Composite2.ExpectedSolutions =
				SolutionReader.getExpectedSolutions(
					"Composite2",
					codeLevelModel);
			Composite2.NumberOfExpectedSolutions =
				SolutionReader.getExpectedNumberOfSolutions(
					"Composite2",
					codeLevelModel);

			// Solutions found.
			Composite2.FoundSolutions =
				this.testDesignPattern(
					Composite2.class,
					Primitive.ALL_SOLUTIONS,
					pattern.getName(),
					codeLevelModel,
					OccurrenceGenerator.SOLVER_COMBINATORIAL_AUTOMATIC,
					OccurrenceGenerator.PROBLEM_AC4);
		}
	}
	public void testNumberOfSolutions() {
		Assert.assertEquals(
			"Number of solutions",
			Composite2.NumberOfExpectedSolutions,
			Composite2.FoundSolutions.length);
	}
	public void testSolutions() {
		for (int i = 0; i < Composite2.NumberOfExpectedSolutions; i++) {
			Assert.assertEquals(
				"",
				Composite2.ExpectedSolutions[i],
				Composite2.FoundSolutions[i]);
		}
	}
	//	public void testComponentSolutionComponent() {
	//		for (int i = 0; i < Composite2.NUMBER_OF_EXPECTED_SOLUTIONS; i++) {
	//			this.testSolutionComponent(
	//				Composite2.Solutions[i],
	//				"Component",
	//				"ptidej.example.composite2.Element");
	//		}
	//	}
	//	public void testCompositeSolutionComponent() {
	//		for (int i = 0; i < Composite2.NUMBER_OF_EXPECTED_SOLUTIONS; i++) {
	//			this.testSolutionComponent(
	//				Composite2.Solutions[i],
	//				"Composite",
	//				"ptidej.example.composite2.Document");
	//		}
	//	}
	//	public void testLeaf1SolutionComponent() {
	//		for (int i = 0; i < Composite2.NUMBER_OF_EXPECTED_SOLUTIONS; i++) {
	//			this.testSolutionComponent(
	//				Composite2.Solutions[i],
	//				"Leaf-1",
	//				"ptidej.example.composite2.IndentedParagraph");
	//		}
	//	}
	//	public void testLeaf2SolutionComponent() {
	//		for (int i = 0; i < Composite2.NUMBER_OF_EXPECTED_SOLUTIONS; i++) {
	//			this.testSolutionComponent(
	//				Composite2.Solutions[i],
	//				"Leaf-2",
	//				"ptidej.example.composite2.Paragraph");
	//		}
	//	}
	//	public void testLeaf3SolutionComponent() {
	//		for (int i = 0; i < Composite2.NUMBER_OF_EXPECTED_SOLUTIONS; i++) {
	//			this.testSolutionComponent(
	//				Composite2.Solutions[i],
	//				"Leaf-3",
	//				"ptidej.example.composite2.Title");
	//		}
	//	}
	//	public void testSolutionPercentage() {
	//		Composite2.assertEquals(
	//			"Distorted solution percentage",
	//			12,
	//			Composite2.Solutions[0].getPercentage());
	//		Composite2.assertEquals(
	//			"Distorted solution percentage",
	//			6,
	//			Composite2.Solutions[1].getPercentage());
	//		Composite2.assertEquals(
	//			"Distorted solution percentage",
	//			3,
	//			Composite2.Solutions[2].getPercentage());
	//		for (int i = 3; i < Composite2.NUMBER_OF_EXPECTED_SOLUTIONS; i++) {
	//			Composite2.assertEquals(
	//				"Distorted solution percentage",
	//				1,
	//				Composite2.Solutions[i].getPercentage());
	//		}
	//	}
}
