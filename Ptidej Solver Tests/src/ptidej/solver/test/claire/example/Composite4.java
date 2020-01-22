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

public class Composite4 extends Primitive {
	private static int NumberOfExpectedSolutions;
	private static Occurrence[] FoundSolutions;
	private static Occurrence[] ExpectedSolutions;

	public Composite4(final String name) {
		super(name);
	}
	protected void setUp() throws IllegalAccessException,
			InstantiationException {

		if (Composite4.FoundSolutions == null) {
			final IDesignMotifModel pattern =
				(IDesignMotifModel) padl.motif.repository.Composite.class
					.newInstance();

			final ICodeLevelModel codeLevelModel =
				Factory.getInstance().createCodeLevelModel(
					"ptidej.example.composite4");
			try {
				codeLevelModel
					.create(new CompleteClassFileCreator(
						new String[] { "../Ptidej Tests/bin/ptidej/example/composite4/" }));
			}
			catch (final CreationException e) {
				e.printStackTrace();
			}

			// Expected solutions.
			Composite4.ExpectedSolutions =
				SolutionReader.getExpectedSolutions(
					"Composite4",
					codeLevelModel);
			Composite4.NumberOfExpectedSolutions =
				SolutionReader.getExpectedNumberOfSolutions(
					"Composite4",
					codeLevelModel);

			// Solutions found.
			Composite4.FoundSolutions =
				this.testDesignPattern(
					Composite4.class,
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
			Composite4.NumberOfExpectedSolutions,
			Composite4.FoundSolutions.length);
	}
	public void testSolutions() {
		for (int i = 0; i < Composite4.NumberOfExpectedSolutions; i++) {
			Assert.assertEquals(
				"",
				Composite4.ExpectedSolutions[i],
				Composite4.FoundSolutions[i]);
		}
	}
	//	public void testSolution1() {
	//		this.testSolutionComponent(
	//			Composite4.Solutions[0],
	//			"Component",
	//			"ptidej.example.composite4.Element");
	//		this.testSolutionComponent(
	//			Composite4.Solutions[0],
	//			"Composite",
	//			"ptidej.example.composite4.Document");
	//		this.testSolutionComponent(
	//			Composite4.Solutions[0],
	//			"Leaf",
	//			"ptidej.example.composite4.IndentedParagraph");
	//	}
	//	public void testSolution2to4() {
	//		for (int i = 1; i < 4; i++) {
	//			this.testSolutionComponent(
	//				Composite4.Solutions[i],
	//				"Component",
	//				"ptidej.example.composite4.Element");
	//			this.testSolutionComponent(
	//				Composite4.Solutions[i],
	//				"Composite",
	//				"ptidej.example.composite4.Document");
	//			this.testSolutionComponent(
	//				Composite4.Solutions[i],
	//				"Leaf-1",
	//				"ptidej.example.composite4.IndentedParagraph");
	//			this.testSolutionComponent(
	//				Composite4.Solutions[i],
	//				"Leaf-2",
	//				"ptidej.example.composite4.Title");
	//		}
	//	}
	//	public void testSolution5() {
	//		this.testSolutionComponent(
	//			Composite4.Solutions[4],
	//			"Component",
	//			"ptidej.example.composite4.Element");
	//		this.testSolutionComponent(
	//			Composite4.Solutions[4],
	//			"Composite",
	//			"ptidej.example.composite4.Title");
	//		this.testSolutionComponent(
	//			Composite4.Solutions[4],
	//			"Leaf",
	//			"ptidej.example.composite4.IndentedParagraph");
	//	}
	//	public void testSolution6to8() {
	//		for (int i = 5; i < 8; i++) {
	//			this.testSolutionComponent(
	//				Composite4.Solutions[i],
	//				"Component",
	//				"ptidej.example.composite4.Element");
	//			this.testSolutionComponent(
	//				Composite4.Solutions[i],
	//				"Composite",
	//				"ptidej.example.composite4.Document");
	//			this.testSolutionComponent(
	//				Composite4.Solutions[i],
	//				"Leaf-1",
	//				"ptidej.example.composite4.IndentedParagraph");
	//			this.testSolutionComponent(
	//				Composite4.Solutions[i],
	//				"Leaf-2",
	//				"ptidej.example.composite4.Paragraph");
	//		}
	//	}
	//	public void testSolution9() {
	//		this.testSolutionComponent(
	//			Composite4.Solutions[8],
	//			"Component",
	//			"ptidej.example.composite4.Element");
	//		this.testSolutionComponent(
	//			Composite4.Solutions[8],
	//			"Composite",
	//			"ptidej.example.composite4.Title");
	//		this.testSolutionComponent(
	//			Composite4.Solutions[8],
	//			"Leaf-1",
	//			"ptidej.example.composite4.IndentedParagraph");
	//		this.testSolutionComponent(
	//			Composite4.Solutions[8],
	//			"Leaf-2",
	//			"ptidej.example.composite4.Paragraph");
	//	}
	//	public void testSolution10() {
	//		this.testSolutionComponent(
	//			Composite4.Solutions[9],
	//			"Component",
	//			"ptidej.example.composite4.Paragraph");
	//		this.testSolutionComponent(
	//			Composite4.Solutions[9],
	//			"Composite",
	//			"ptidej.example.composite4.Element");
	//		this.testSolutionComponent(
	//			Composite4.Solutions[9],
	//			"Leaf",
	//			"ptidej.example.composite4.IndentedParagraph");
	//	}
	//	public void testSolution11to13() {
	//		for (int i = 10; i < 13; i++) {
	//			this.testSolutionComponent(
	//				Composite4.Solutions[10],
	//				"Component",
	//				"ptidej.example.composite4.Element");
	//			this.testSolutionComponent(
	//				Composite4.Solutions[10],
	//				"Composite",
	//				"ptidej.example.composite4.Document");
	//			this.testSolutionComponent(
	//				Composite4.Solutions[10],
	//				"Leaf-1",
	//				"ptidej.example.composite4.IndentedParagraph");
	//			this.testSolutionComponent(
	//				Composite4.Solutions[10],
	//				"Leaf-2",
	//				"ptidej.example.composite4.Paragraph");
	//			this.testSolutionComponent(
	//				Composite4.Solutions[10],
	//				"Leaf-3",
	//				"ptidej.example.composite4.Title");
	//		}
	//	}
	//	public void testSolution14() {
	//		this.testSolutionComponent(
	//			Composite4.Solutions[13],
	//			"Component",
	//			"ptidej.example.composite4.Element");
	//		this.testSolutionComponent(
	//			Composite4.Solutions[13],
	//			"Composite",
	//			"ptidej.example.composite4.Title");
	//		this.testSolutionComponent(
	//			Composite4.Solutions[13],
	//			"Leaf-1",
	//			"ptidej.example.composite4.IndentedParagraph");
	//		this.testSolutionComponent(
	//			Composite4.Solutions[13],
	//			"Leaf-2",
	//			"ptidej.example.composite4.Paragraph");
	//	}
	//	public void testSolution15() {
	//		this.testSolutionComponent(
	//			Composite4.Solutions[14],
	//			"Component",
	//			"ptidej.example.composite4.Paragraph");
	//		this.testSolutionComponent(
	//			Composite4.Solutions[14],
	//			"Composite",
	//			"ptidej.example.composite4.Element");
	//		this.testSolutionComponent(
	//			Composite4.Solutions[14],
	//			"Leaf",
	//			"ptidej.example.composite4.IndentedParagraph");
	//	}
	//	public void testSolutionPercentage() {
	//		Composite4.assertEquals(
	//			"Distorted solution percentage",
	//			25,
	//			Composite4.Solutions[0].getPercentage());
	//		Composite4.assertEquals(
	//			"Distorted solution percentage",
	//			7,
	//			Composite4.Solutions[1].getPercentage());
	//		Composite4.assertEquals(
	//			"Distorted solution percentage",
	//			3,
	//			Composite4.Solutions[2].getPercentage());
	//		Composite4.assertEquals(
	//			"Distorted solution percentage",
	//			1,
	//			Composite4.Solutions[3].getPercentage());
	//		Composite4.assertEquals(
	//			"Distorted solution percentage",
	//			1,
	//			Composite4.Solutions[4].getPercentage());
	//		Composite4.assertEquals(
	//			"Distorted solution percentage",
	//			2,
	//			Composite4.Solutions[5].getPercentage());
	//		for (int i = 6; i < Composite4.NUMBER_OF_EXPECTED_SOLUTIONS; i++) {
	//			Composite2.assertEquals(
	//				"Distorted solution percentage",
	//				1,
	//				Composite4.Solutions[i].getPercentage());
	//		}
	//	}
}
