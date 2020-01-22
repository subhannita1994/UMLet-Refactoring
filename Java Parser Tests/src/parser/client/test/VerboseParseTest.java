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
package parser.client.test;

import java.util.Arrays;
import org.eclipse.jdt.core.dom.ASTVisitor;
import parser.client.WrapperClientWithLog;
import parser.client.test.visitor.MyVisitor;
import parser.input.SourceInputsHolder;
import parser.input.impl.FileSystemJavaProject;

/**
 * 
 */
public class VerboseParseTest extends ParseTest {
	public VerboseParseTest() throws Exception {
		this("VerboseParseTest");
	}
	public VerboseParseTest(final String aName) throws Exception {
		super(aName);

		final String classPathEntry =
			"../Java Parser Tests/resources/CodeAnalyser/libs/tools.jar";
		final String[] classpathEntries = new String[] { classPathEntry };

		final String sourcePathEntry =
			"../Java Parser Tests/resources/CodeAnalyser/src";
		final String[] sourcePathEntries = new String[] { sourcePathEntry };

		final String resultFilePath = "log_verbose.txt";

		final SourceInputsHolder javaProject =
			new FileSystemJavaProject(
				Arrays.asList(classpathEntries),
				Arrays.asList(sourcePathEntries));

		final WrapperClientWithLog parserClient =
			new WrapperClientWithLog(javaProject, resultFilePath);

		final String oracleFilePath =
			"../Java Parser Tests/resources/CodeAnalyser/log/log_oracle_verbose.txt";

		final String testCaseName = "CodeAnalyser Verbose Test Case";

		final ASTVisitor visitor = new MyVisitor(parserClient);

		this.init(
			resultFilePath,
			oracleFilePath,
			testCaseName,
			parserClient,
			visitor);
	}

	public void testParse() {
		super.testParse();
	}
}
