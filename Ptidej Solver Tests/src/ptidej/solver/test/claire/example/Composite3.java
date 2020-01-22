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

public class Composite3 extends Primitive {
	private static int NumberOfExpectedSolutions;
	private static Occurrence[] FoundSolutions;
	private static Occurrence[] ExpectedSolutions;

	public Composite3(final String name) {
		super(name);
	}
	protected void setUp() throws IllegalAccessException,
			InstantiationException {

		if (Composite3.FoundSolutions == null) {
			final IDesignMotifModel pattern =
				(IDesignMotifModel) padl.motif.repository.Composite.class
					.newInstance();

			final ICodeLevelModel codeLevelModel =
				Factory.getInstance().createCodeLevelModel(
					"ptidej.example.composite3");
			try {
				codeLevelModel
					.create(new CompleteClassFileCreator(
						new String[] { "../Ptidej Tests/bin/ptidej/example/composite3/" }));
			}
			catch (final CreationException e) {
				e.printStackTrace();
			}

			// Expected solutions.
			Composite3.ExpectedSolutions =
				SolutionReader.getExpectedSolutions(
					"Composite3",
					codeLevelModel);
			Composite3.NumberOfExpectedSolutions =
				SolutionReader.getExpectedNumberOfSolutions(
					"Composite3",
					codeLevelModel);

			// Solutions found.
			Composite3.FoundSolutions =
				this.testDesignPattern(
					Composite3.class,
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
			Composite3.NumberOfExpectedSolutions,
			Composite3.FoundSolutions.length);
	}
	public void testSolutions() {
		for (int i = 0; i < Composite3.NumberOfExpectedSolutions; i++) {
			Assert.assertEquals(
				"",
				Composite3.ExpectedSolutions[i],
				Composite3.FoundSolutions[i]);
		}
	}
	//	public void testComponentSolutionComponent() {
	//		for (int i = 0; i < Composite3.NUMBER_OF_EXPECTED_SOLUTIONS; i++) {
	//			this.testSolutionComponent(
	//				Composite3.Solutions[i],
	//				"Component",
	//				"ptidej.example.composite3.Element");
	//		}
	//	}
	//	public void testCompositeSolutionComponent() {
	//		for (int i = 0; i < Composite3.NUMBER_OF_EXPECTED_SOLUTIONS; i++) {
	//			this.testSolutionComponent(
	//				Composite3.Solutions[i],
	//				"Composite",
	//				"ptidej.example.composite3.Document");
	//		}
	//	}
	//	public void testLeaf1SolutionComponent() {
	//		for (int i = 0; i < Composite3.NUMBER_OF_EXPECTED_SOLUTIONS; i++) {
	//			this.testSolutionComponent(
	//				Composite3.Solutions[i],
	//				"Leaf-1",
	//				"ptidej.example.composite3.IndentedParagraph");
	//		}
	//	}
	//	public void testLeaf2SolutionComponent() {
	//		for (int i = 0; i < Composite3.NUMBER_OF_EXPECTED_SOLUTIONS; i++) {
	//			this.testSolutionComponent(
	//				Composite3.Solutions[i],
	//				"Leaf-2",
	//				"ptidej.example.composite3.Paragraph");
	//		}
	//	}
	//	public void testLeaf3SolutionComponent() {
	//		for (int i = 0; i < Composite3.NUMBER_OF_EXPECTED_SOLUTIONS; i++) {
	//			this.testSolutionComponent(
	//				Composite3.Solutions[i],
	//				"Leaf-3",
	//				"ptidej.example.composite3.Title");
	//		}
	//	}
	//	public void testSolutionPercentage() {
	//		Composite3.assertEquals(
	//			"Distorted solution percentage",
	//			25,
	//			Composite3.Solutions[0].getPercentage());
	//		Composite3.assertEquals(
	//			"Distorted solution percentage",
	//			12,
	//			Composite3.Solutions[1].getPercentage());
	//		Composite3.assertEquals(
	//			"Distorted solution percentage",
	//			6,
	//			Composite3.Solutions[2].getPercentage());
	//		Composite3.assertEquals(
	//			"Distorted solution percentage",
	//			7,
	//			Composite3.Solutions[3].getPercentage());
	//		Composite3.assertEquals(
	//			"Distorted solution percentage",
	//			3,
	//			Composite3.Solutions[4].getPercentage());
	//		Composite3.assertEquals(
	//			"Distorted solution percentage",
	//			1,
	//			Composite3.Solutions[5].getPercentage());
	//		Composite3.assertEquals(
	//			"Distorted solution percentage",
	//			2,
	//			Composite3.Solutions[6].getPercentage());
	//		for (int i = 7; i < Composite3.NUMBER_OF_EXPECTED_SOLUTIONS; i++) {
	//			Composite3.assertEquals(
	//				"Distorted solution percentage",
	//				1,
	//				Composite3.Solutions[i].getPercentage());
	//		}
	//	}
}
