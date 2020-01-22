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
package padl.creator.classfile.test.example;

import java.util.Iterator;
import java.util.List;
import junit.framework.Assert;
import junit.framework.TestCase;
import padl.creator.classfile.relationship.DeepMethodInvocationAnalyzer;
import padl.creator.classfile.util.ExtendedMethodInfo;
import com.ibm.toad.cfparse.ClassFile;
import com.ibm.toad.cfparse.MethodInfo;
import com.ibm.toad.cfparse.MethodInfoList;
import com.ibm.toad.cfparse.utils.CFUtils;

/**
 * @author Farouk Zaidi
 * @since  2004/03/23
 */
public class TestNew extends TestCase {
	private ClassFile classfile;
	private String className = "padl.example.relationship.MethodDump";

	public TestNew(final String aName) {
		super(aName);
	}
	private ExtendedMethodInfo getMethodInfo(final String nameMethod) {
		final MethodInfoList ml = this.classfile.getMethods();

		// For every class method.
		for (int i = 0; i < ml.length(); i++) {
			final MethodInfo mi = ml.get(i);
			if (mi.getName().equals(nameMethod)) {
				return new ExtendedMethodInfo(this.classfile, mi);
			}
		}
		return null;
	}
	protected void setUp() throws Exception {
		super.setUp();

		// Procedure for loading a class file.
		this.classfile =
			(ClassFile) CFUtils
				.forName(this.className, CFUtils.CLASSFILE, true);
		if (this.classfile == null) {
			throw new RuntimeException("Error loading class files");
		}
	}
	public void testFooDup() {
		final String methodName = "fooNew";
		final ExtendedMethodInfo methodInfo = this.getMethodInfo(methodName);
		final DeepMethodInvocationAnalyzer analyzer =
			DeepMethodInvocationAnalyzer.getUniqueInstance();
		final List couples = analyzer.analyzeMethod(methodInfo);
		final StringBuffer buffer = new StringBuffer();

		for (final Iterator iter = couples.iterator(); iter.hasNext();) {
			final String[] element = (String[]) iter.next();
			buffer.append(element[0]);
			buffer.append(",");
			buffer.append(element[1]);
			buffer.append("|");
		}
		final String expectedResult =
			"null,java.lang.Integer <init>.void (int)|padl.example.relationship.MethodDump p.java.lang.Integer,=|padl.example.relationship.MethodDump p.java.lang.Integer,java.lang.Integer intValue.int ()|null,java.lang.Integer <init>.void (int)|padl.example.relationship.MethodDump p.java.lang.Integer,java.lang.Integer compareTo.int (java.lang.Integer)|padl.example.relationship.MethodDump p.java.lang.Integer,java.lang.Integer intValue.int ()|null,java.lang.Integer <init>.void (int)|padl.example.relationship.MethodDump p.java.lang.Integer,=|padl.example.relationship.MethodDump p.java.lang.Integer,java.lang.Integer compareTo.int (java.lang.Integer)|";
		Assert.assertTrue(expectedResult.equals(buffer.toString()));
	}
}
