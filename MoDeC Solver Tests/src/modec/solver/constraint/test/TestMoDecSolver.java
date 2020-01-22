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
package modec.solver.constraint.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Yann-Ga�l Gu�h�neuc
 * @since 2013/04/28
 */
public final class TestMoDecSolver extends junit.framework.TestSuite {

	public TestMoDecSolver() {
	}
	public TestMoDecSolver(final Class theClass) {
		super(theClass);
	}
	public TestMoDecSolver(final String name) {
		super(name);
	}
	public static Test suite() {
		final TestMoDecSolver suite = new TestMoDecSolver();
		suite.addTest(new TestSuite(TestCaller.class));
		suite.addTest(new TestSuite(TestMessageFollowsImmediately.class));
		return suite;
	}
}
