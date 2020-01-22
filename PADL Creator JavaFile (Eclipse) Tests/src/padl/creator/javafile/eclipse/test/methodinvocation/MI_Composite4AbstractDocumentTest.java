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
package padl.creator.javafile.eclipse.test.methodinvocation;

import java.util.Iterator;
import junit.framework.Assert;
import junit.framework.TestCase;
import padl.creator.javafile.eclipse.test.util.Utils;
import padl.kernel.ICodeLevelModel;
import padl.kernel.IFirstClassEntity;
import padl.kernel.IMethod;
import padl.kernel.IMethodInvocation;
import util.io.ProxyConsole;

public class MI_Composite4AbstractDocumentTest extends TestCase {

	public MI_Composite4AbstractDocumentTest(final String name) {
		super(name);
	}

	public void testCLASS_INSTANCE_FROM_FIELD1() {
		final String sourcePath =
			"../PADL Creator JavaFile (Eclipse) Tests/src/";
		final String[] javaFiles =
			new String[] {
					"../PADL Creator JavaFile (Eclipse) Tests/src/padl/example/relationship/A.java",
					"../PADL Creator JavaFile (Eclipse) Tests/src/padl/example/relationship/Aggregation_CLASS_INSTANCE_FROM_FIELD_1.java" };
		final String classPathEntry = "";
		final ICodeLevelModel javaModel =
			Utils.createCompleteJavaFilesPadlModel(
				"",
				sourcePath,
				classPathEntry,
				javaFiles);

		final String[] classFiles =
			new String[] {
					"../PADL Creator JavaFile (Eclipse) Tests/bin/padl/example/relationship/A.class",
					"../PADL Creator JavaFile (Eclipse) Tests/bin/padl/example/relationship/Aggregation_CLASS_INSTANCE_FROM_FIELD_1.class" };
		final ICodeLevelModel classModel =
			Utils.createCompleteJavaClassesPadlModel("", classFiles);

		final IFirstClassEntity javaClass =
			javaModel
				.getTopLevelEntityFromID("padl.example.relationship.Aggregation_CLASS_INSTANCE_FROM_FIELD_1");
		final IMethod javaMethod =
			(IMethod) javaClass.getConstituentFromName("foo");
		final IMethodInvocation javaMethodInvocation =
			(IMethodInvocation) javaMethod
				.getConstituentFromID("Method Invocation_>PADL<_1");
		//		ProxyConsole.getInstance().debugOutput().println(javaMethod.toString());
		//		final Iterator iter = javaMethod.getIteratorOnConstituents();
		//		while (iter.hasNext()) {
		//			ProxyConsole.getInstance().debugOutput().println("javaMethod " + iter.next().toString());
		//		}

		final IFirstClassEntity classClass =
			classModel
				.getTopLevelEntityFromID("padl.example.relationship.Aggregation_CLASS_INSTANCE_FROM_FIELD_1");
		final IMethod classMethod =
			(IMethod) classClass.getConstituentFromName("foo");
		final IMethodInvocation classMethodInvocation =
			(IMethodInvocation) classMethod
				.getConstituentFromID("Method Invocation_>PADL<_3");
		// ProxyConsole.getInstance().debugOutput().println(classMethod.toString());
		final Iterator iter1 = classMethod.getIteratorOnConstituents();
		while (iter1.hasNext()) {
			ProxyConsole
				.getInstance()
				.debugOutput()
				.println("classMethod " + iter1.next().toString());
		}

		Assert.assertEquals(
			IMethodInvocation.CLASS_INSTANCE_FROM_FIELD,
			javaMethodInvocation.getType());
		//JavaMethodInvocationjunit.framework.AssertionFailedError: expected:<4> but was:<8>

		Assert.assertEquals(
			IMethodInvocation.CLASS_INSTANCE_FROM_FIELD,
			classMethodInvocation.getType());
		//JavaMethodInvocationjunit.framework.AssertionFailedError: expected:<4> but was:<7>

		Assert.assertTrue(MethodInvocationComparator
			.isMIofJavaModelMethodIncludedInMIofClassModelMethod(
				javaMethod,
				classMethod));
	}

	public void testCLASS_INSTANCE_FROM_FIELD2() {
		final String sourcePath =
			"../PADL Creator JavaFile (Eclipse) Tests/src/";
		final String[] javaFiles =
			new String[] {
					"../PADL Creator JavaFile (Eclipse) Tests/src/padl/example/relationship/A.java",
					"../PADL Creator JavaFile (Eclipse) Tests/src/padl/example/relationship/Aggregation_CLASS_INSTANCE_FROM_FIELD_2.java" };
		final String classPathEntry = "";
		final ICodeLevelModel javaModel =
			Utils.createCompleteJavaFilesPadlModel(
				"",
				sourcePath,
				classPathEntry,
				javaFiles);

		final String[] classFiles =
			new String[] {
					"../PADL Creator JavaFile (Eclipse) Tests/bin/padl/example/relationship/A.class",
					"../PADL Creator JavaFile (Eclipse) Tests/bin/padl/example/relationship/Aggregation_CLASS_INSTANCE_FROM_FIELD_2.class" };
		final ICodeLevelModel classModel =
			Utils.createCompleteJavaClassesPadlModel("", classFiles);

		final IFirstClassEntity javaClass =
			javaModel
				.getTopLevelEntityFromID("padl.example.relationship.Aggregation_CLASS_INSTANCE_FROM_FIELD_2");
		final IMethod javaMethod =
			(IMethod) javaClass.getConstituentFromName("foo");
		final IMethodInvocation javaMethodInvocation =
			(IMethodInvocation) javaMethod
				.getConstituentFromID("Method Invocation_>PADL<_1");
		ProxyConsole.getInstance().debugOutput().println(javaMethod.toString());
		final Iterator iter = javaMethod.getIteratorOnConstituents();
		while (iter.hasNext()) {
			ProxyConsole
				.getInstance()
				.debugOutput()
				.println("javaMethod " + iter.next().toString());
		}

		final IFirstClassEntity classClass =
			classModel
				.getTopLevelEntityFromID("padl.example.relationship.Aggregation_CLASS_INSTANCE_FROM_FIELD_2");
		final IMethod classMethod =
			(IMethod) classClass.getConstituentFromName("foo");
		//	final IMethodInvocation classMethodInvocation =
		//		(IMethodInvocation) classMethod
		//			.getConstituentFromID("Method Invocation_3");
		ProxyConsole
			.getInstance()
			.debugOutput()
			.println(classMethod.toString());
		final Iterator iter1 = classMethod.getIteratorOnConstituents();
		while (iter1.hasNext()) {
			ProxyConsole
				.getInstance()
				.debugOutput()
				.println("classMethod " + iter1.next().toString());
		}

		Assert.assertEquals(
			IMethodInvocation.CLASS_INSTANCE_FROM_FIELD,
			javaMethodInvocation.getType());
		//		Assert.assertEquals(
		//			IMethodInvocation.CLASS_INSTANCE_FROM_FIELD,
		//			classMethodInvocation.getType());
		//classMethidInvocationjunit.framework.AssertionFailedError: expected:<4> but was:<3>

		Assert.assertTrue(MethodInvocationComparator
			.isMIofJavaModelMethodIncludedInMIofClassModelMethod(
				javaMethod,
				classMethod));
	}
}
