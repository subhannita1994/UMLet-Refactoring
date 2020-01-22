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
package parser.test.suite;

import junit.framework.Test;
import junit.framework.TestSuite;
import parser.client.test.ConciseParseTest;
import parser.client.test.VerboseParseTest;
import parser.parser.test.FileListJavaProjectTest;
import parser.parser.test.JavaParserLineAndBlockCommentTest;
import parser.parser.test.JavaParserTest;

public class JavaParserTestSuite extends TestSuite {
	public JavaParserTestSuite() {
	}
	public JavaParserTestSuite(final Class<?> theClass) {
		super(theClass);
	}
	public JavaParserTestSuite(final String name) {
		super(name);
	}
	public static Test suite() {
		JavaParserTestSuite suite = new JavaParserTestSuite();

		suite.addTestSuite(VerboseParseTest.class);
		suite.addTestSuite(ConciseParseTest.class);
		suite.addTestSuite(JavaParserTest.class);
		suite.addTestSuite(FileListJavaProjectTest.class);
		suite.addTestSuite(JavaParserLineAndBlockCommentTest.class);

		return suite;
	}
}
