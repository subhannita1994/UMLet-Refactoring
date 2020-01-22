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
package ptidej.solver.test.java.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;
import junit.framework.Assert;
import junit.framework.TestCase;
import padl.generator.helper.ModelGenerator;
import padl.kernel.IIdiomLevelModel;
import ptidej.solver.Occurrence;
import ptidej.solver.OccurrenceBuilder;
import ptidej.solver.Problem;
import ptidej.solver.domain.Manager;
import ptidej.solver.problem.CompositeMotif;
import util.io.ProxyConsole;
import util.io.ReaderInputStream;

public class Composite1FromClassFiles extends TestCase {
	private static int NumberOfExpectedSolutions;
	private static Occurrence[] FoundSolutions;
	private static Occurrence[] ExpectedSolutions;

	public Composite1FromClassFiles(final String name) {
		super(name);
	}
	protected void setUp()
			throws IllegalAccessException, InstantiationException {

		if (Composite1FromClassFiles.FoundSolutions == null) {
			try {
				final IIdiomLevelModel idiomLevelModel =
					ModelGenerator.generateModelFromClassFilesDirectories(
						new String[] {
								"../Ptidej Tests/bin/ptidej/example/composite1/" });

				// Expected solutions.
				Composite1FromClassFiles.ExpectedSolutions = SolutionReader
					.getExpectedSolutions("Composite1", idiomLevelModel);
				Composite1FromClassFiles.NumberOfExpectedSolutions =
					SolutionReader.getExpectedNumberOfSolutions(
						"Composite1",
						idiomLevelModel);

				// Solutions found.
				final Problem problem =
					CompositeMotif.getProblem(Manager.build(idiomLevelModel));

				final StringWriter writer = new StringWriter();
				problem.setWriter(new PrintWriter(writer));
				problem.automaticSolve(true);

				// To write the solutions as expected solutions
				//	final Writer w = new FileWriter(
				//		"../Ptidej Solver Tests/bin/ptidej/solver/test/java/example/ConstraintResultsForComposite1.ini");
				//	w.write(writer.getBuffer().toString());
				//	w.close();

				final Properties properties = new Properties();
				properties.load(
					new ReaderInputStream(
						new StringReader(writer.getBuffer().toString())));
				final OccurrenceBuilder solutionBuilder =
					OccurrenceBuilder.getInstance();
				Composite1FromClassFiles.FoundSolutions =
					solutionBuilder.getCanonicalOccurrences(properties);
			}
			catch (final IOException e) {
				e.printStackTrace(ProxyConsole.getInstance().errorOutput());
			}
		}
	}
	public void testNumberOfSolutions() {
		Assert.assertEquals(
			"Number of solutions",
			Composite1FromClassFiles.NumberOfExpectedSolutions,
			Composite1FromClassFiles.FoundSolutions.length);
	}
	public void testSolutions() {
		for (int i =
			0; i < Composite1FromClassFiles.NumberOfExpectedSolutions; i++) {
			Assert.assertEquals(
				"",
				Composite1FromClassFiles.ExpectedSolutions[i],
				Composite1FromClassFiles.FoundSolutions[i]);
		}
	}
}
