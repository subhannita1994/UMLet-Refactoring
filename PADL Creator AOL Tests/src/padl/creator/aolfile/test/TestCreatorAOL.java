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
package padl.creator.aolfile.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TestCreatorAOL extends TestSuite {
	public TestCreatorAOL() {
	}
	public TestCreatorAOL(final Class theClass) {
		super(theClass);
	}
	public TestCreatorAOL(final String name) {
		super(name);
	}
	public static Test suite() {
		final TestCreatorAOL suite = new TestCreatorAOL();
		suite.addTestSuite(Test1.class);
		suite.addTestSuite(Test2.class);
		return suite;
	}

}
