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
package padl.analysis.aac.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Yann-Ga�l Gu�h�neuc
 * @since 2004/11/11
 */
public final class TestAACAnalyses
	extends junit.framework.TestSuite {

	public TestAACAnalyses() {
	}
	public TestAACAnalyses(final Class theClass) {
		super(theClass);
	}
	public TestAACAnalyses(final String name) {
		super(name);
	}
	public static Test suite() {
		final TestAACAnalyses suite =
			new TestAACAnalyses();
		suite.addTest(new TestSuite(Aggregation_CLASS_INSTANCE_FROM_FIELD_1.class));
		return suite;
	}
}
