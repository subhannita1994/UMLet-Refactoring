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
package padl.generator.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import padl.generator.test.cases.SanityTest;

/**
 * @author yann
 */
public class TestPADLGenerator extends TestSuite {
	public TestPADLGenerator() {
	}
	public TestPADLGenerator(final Class theClass) {
		super(theClass);
	}
	public TestPADLGenerator(final String name) {
		super(name);
	}
	public static Test suite() {
		final TestPADLGenerator suite = new TestPADLGenerator();
		suite.addTestSuite(SanityTest.class);
		return suite;
	}
}
