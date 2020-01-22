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
package ptidej.test.all;

import jct.test.TestJCT;
import jct.test.TestJCTLong;
import junit.framework.Test;
import junit.framework.TestSuite;
import modec.solver.constraint.test.TestMoDecSolver;
import padl.analysis.test.TestPADLAnalyses;
import padl.generator.test.TestPADLGenerator;
import padl.micropatterns.test.TestMicroPatterns;
import padl.pagerank.test.TestPADLGeneratorPageRank;
import padl.refactoring.test.TestRefactorings;
import padl.serialiser.TestDB4OSerialiser;
import padl.serialiser.TestJOSSerialiser;
import padl.test.TestPADL;
import parser.test.suite.JavaParserTestSuite;
import pom.test.TestPOM;
import ptidej.solver.test.TestPtidejSolver;
import ptidej.solver.test.java.fingerprint.TestMetricalPtidejSolver;
import ptidej.ui.kernel.builder.test.TestAspectJBuilder;
import sad.detection.test.TestSAD;
import caffeine.test.TestCaffeine;
import epi.test.TestEPI;

/**
 * @author Yann-Ga�l Gu�h�neuc
 * @since  2008/12/04
 */
public final class TestAllPtidejButCreators extends TestSuite {
	public static Test suite() {
		final TestAllPtidejButCreators suite = new TestAllPtidejButCreators();

		suite.addTest(TestJCT.suite());
		suite.addTest(TestJCTLong.suite());
		suite.addTest(JavaParserTestSuite.suite());

		suite.addTest(TestCaffeine.suite());

		suite.addTest(TestEPI.suite());

		suite.addTest(TestPADL.suite());
		suite.addTest(TestPADLAnalyses.suite());
		suite.addTest(TestPOM.suite());

		suite.addTest(TestMetricalPtidejSolver.suite());

		suite.addTest(TestPADLGenerator.suite());
		suite.addTest(TestPADLGeneratorPageRank.suite());

		suite.addTest(TestMicroPatterns.suite());

		suite.addTest(TestMoDecSolver.suite());
		suite.addTest(TestPtidejSolver.suite());

		suite.addTestSuite(TestAspectJBuilder.class);

		suite.addTest(TestRefactorings.suite());

		suite.addTest(TestSAD.suite());

		suite.addTest(TestDB4OSerialiser.suite());
		suite.addTest(TestJOSSerialiser.suite());
		return suite;
	}
}
