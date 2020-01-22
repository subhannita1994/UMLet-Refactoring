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
package padl.creator.cppfile.eclipse.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import padl.creator.cppfile.eclipse.test.big.ChromeTest;
import padl.creator.cppfile.eclipse.test.big.QMakeTest;
import padl.creator.cppfile.eclipse.test.simple.ClassesTest;
import padl.creator.cppfile.eclipse.test.simple.FieldAccessTest;
import padl.creator.cppfile.eclipse.test.simple.GetOrCreateTest;
import padl.creator.cppfile.eclipse.test.simple.SimpleTest;
import padl.creator.cppfile.eclipse.test.simple.Simple2Test;
import padl.creator.cppfile.eclipse.test.simple.Simple3Test;
import padl.creator.cppfile.eclipse.test.simple.StructuresTest;

/**
 * 
 * @author Yann
 * @since  2013/05/04
 *
 */
public final class TestCreatorCPPFileUsingEclipse extends TestSuite {
	public TestCreatorCPPFileUsingEclipse() {
	}
	public TestCreatorCPPFileUsingEclipse(final Class theClass) {
		super(theClass);
	}
	public TestCreatorCPPFileUsingEclipse(final String name) {
		super(name);
	}
	public static Test suite() {
		final TestCreatorCPPFileUsingEclipse suite =
			new TestCreatorCPPFileUsingEclipse();

		suite.addTestSuite(ClassesTest.class);
		suite.addTestSuite(FieldAccessTest.class);
		suite.addTestSuite(SimpleTest.class);
		suite.addTestSuite(GetOrCreateTest.class);
		suite.addTestSuite(Simple2Test.class);
		suite.addTestSuite(Simple3Test.class);
		suite.addTestSuite(StructuresTest.class);

		suite.addTestSuite(QMakeTest.class);
		suite.addTestSuite(ChromeTest.class);

		return suite;
	}
}
