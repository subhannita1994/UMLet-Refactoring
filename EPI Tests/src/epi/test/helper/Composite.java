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
package epi.test.helper;

import java.util.Hashtable;
import padl.analysis.UnsupportedSourceModelException;
import padl.analysis.repository.AACRelationshipsAnalysis;
import padl.creator.classfile.CompleteClassFileCreator;
import padl.kernel.ICodeLevelModel;
import padl.kernel.IIdiomLevelModel;
import padl.kernel.exception.CreationException;
import padl.kernel.impl.Factory;
import util.io.ProxyConsole;
import util.io.ProxyDisk;
import epi.solver.Approximation;
import epi.solver.EPISolver;
import epi.solver.OptimisedBitVectorSolver;
import epi.solver.Solution;
import epi.solver.StringBuilder;

public class Composite {
	public static void main(final String[] args) {
		final String root = "../Ptidej Tests/bin/ptidej/example/composite2/";

		try {
			final ICodeLevelModel codeLevelModel =
				Factory.getInstance().createCodeLevelModel("Test.TestMetrics");
			codeLevelModel.create(new CompleteClassFileCreator(
				new String[] { root }));

			final IIdiomLevelModel model =
				(IIdiomLevelModel) new AACRelationshipsAnalysis()
					.invoke(codeLevelModel);

			ProxyConsole.getInstance().debugOutput().println("Start...");

			final Approximation approx = new Approximation();
			approx.setDefaultApproximation();
			final String progString = StringBuilder.buildModelString(model);
			final String patternString =
				StringBuilder.buildPatternString("Composite");
			final String patternStringa =
				StringBuilder.buildPatternString("Composite");
			final Hashtable domains = null;

			EPISolver solver =
				new OptimisedBitVectorSolver(
					progString,
					patternString,
					"Composite",
					approx,
					domains);

			ProxyConsole.getInstance().debugOutput().println(patternString);
			Solution.print(solver.computeSolutions(), ProxyDisk
				.getInstance()
				.fileTempOutput("rsc/Composite2.solutions.txt"));

			solver =
				new OptimisedBitVectorSolver(
					progString,
					patternStringa,
					"Composite",
					approx,
					domains);

			Solution.print(solver.computeSolutions(), ProxyDisk
				.getInstance()
				.fileTempOutput("rsc/Composite2.solutions.txt"));

			ProxyConsole.getInstance().debugOutput().println("Done.");
		}
		catch (final CreationException e) {
			e.printStackTrace(ProxyConsole.getInstance().errorOutput());
		}
		catch (final UnsupportedSourceModelException e) {
			e.printStackTrace(ProxyConsole.getInstance().errorOutput());
		}
	}
}
