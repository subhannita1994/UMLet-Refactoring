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
package padl.creator.classfile.test.path;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Yann-Ga�l Gu�h�neuc
 * @since  2009/05/02
 */
public class TestPath extends TestSuite {
	public static Test suite() {
		final TestPath suite = new TestPath();
		suite.addTest(new TestPath(TestPathArgoUML.class));
		return suite;
	}
	public TestPath() {
	}
	public TestPath(final Class theClass) {
		super(theClass);
	}
	public TestPath(final String aName) {
		super(aName);
	}
}
