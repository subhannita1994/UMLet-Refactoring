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
package parser.parser.test;

import java.util.Arrays;
import junit.framework.Assert;
import junit.framework.TestCase;
import parser.input.SourceInputsHolder;
import parser.input.impl.FileSystemJavaProject;
import parser.wrapper.JavaParser;
import parser.wrapper.NamedCompilationUnit;

public class JavaParserTest extends TestCase {
	public void testParse() throws Exception {
		final String[] classpathEntries = new String[] { "" };
		final String[] sourcepathEntries =
			new String[] { "../Java Parser Tests/resources/NestingClasses/src" };

		final SourceInputsHolder javaProject =
			new FileSystemJavaProject(
				Arrays.asList(classpathEntries),
				Arrays.asList(sourcepathEntries));

		final NamedCompilationUnit[] namedCompilationUnits =
			new JavaParser(javaProject).parse();

		Assert.assertEquals("JavaParserTest", namedCompilationUnits.length, 3);
	}
}
