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

import java.lang.reflect.Modifier;
import java.util.Iterator;
import junit.framework.Assert;
import junit.framework.TestCase;
import padl.creator.javafile.eclipse.test.util.Utils;
import padl.kernel.Constants;
import padl.kernel.ICodeLevelModel;
import padl.kernel.IFirstClassEntity;
import padl.kernel.IMethod;
import padl.kernel.IMethodInvocation;
import util.io.ProxyConsole;

public class MI_InstanceClassTest2 extends TestCase {

	public MI_InstanceClassTest2(final String name) {
		super(name);
	}

	//02022012- Aminata S
	//In the byte code, an INVOKE Static ignore if the invocation is from field or not,so 
	//we will now consider only the type INSTANCE_CLASS and not INSTANCE_CLASS_FROM_FIELD

	public void testINSTANCE_CLASS1() {
		final String sourcePath =
			"../PADL Creator JavaFile (Eclipse) Tests/src/";
		final String[] javaFiles =
			new String[] {
					"../PADL Creator JavaFile (Eclipse) Tests/src/padl/example/relationship/A.java",
					"../PADL Creator JavaFile (Eclipse) Tests/src/padl/example/relationship/Aggregation_INSTANCE_CLASS_FROM_FIELD_1.java" };
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
					"../PADL Creator JavaFile (Eclipse) Tests/bin/padl/example/relationship/Aggregation_INSTANCE_CLASS_FROM_FIELD_1.class" };
		final ICodeLevelModel classModel =
			Utils.createCompleteJavaClassesPadlModel("", classFiles);

		final IFirstClassEntity javaClass =
			javaModel
				.getTopLevelEntityFromID("padl.example.relationship.Aggregation_INSTANCE_CLASS_FROM_FIELD_1");
		final IMethod javaMethod =
			(IMethod) javaClass.getConstituentFromName("foo");
		final IMethodInvocation javaMethodInvocation =
			(IMethodInvocation) javaMethod
				.getConstituentFromID("Method Invocation_>PADL<_1");
		//ProxyConsole.getInstance().debugOutput().println(javaMethod.toString());
		final Iterator iterJava =
			javaMethod.getIteratorOnConstituents(IMethodInvocation.class);
		while (iterJava.hasNext()) {
			ProxyConsole
				.getInstance()
				.debugOutput()
				.println("javaMethod " + iterJava.next().toString());
		}

		final IFirstClassEntity classClass =
			classModel
				.getTopLevelEntityFromID("padl.example.relationship.Aggregation_INSTANCE_CLASS_FROM_FIELD_1");
		final IMethod classMethod =
			(IMethod) classClass.getConstituentFromName("foo");
		ProxyConsole
			.getInstance()
			.debugOutput()
			.println("***********************");
		final Iterator iterClass =
			classMethod.getIteratorOnConstituents(IMethodInvocation.class);
		while (iterClass.hasNext()) {
			ProxyConsole
				.getInstance()
				.debugOutput()
				.println("classMethod " + iterClass.next().toString());
		}

		Assert.assertEquals(
			"Method invocation type",
			IMethodInvocation.INSTANCE_CLASS,
			javaMethodInvocation.getType());
		Assert.assertNull(
			"Declaring entity is null",
			javaMethodInvocation.getFieldDeclaringEntity());
		Assert.assertNull(
			"Field name is null",
			javaMethodInvocation.getFirstCallingField());
		Assert.assertEquals(
			"Target entity",
			"padl.example.relationship.A",
			javaMethodInvocation.getTargetEntity().getDisplayID());
		Assert.assertEquals(
			"called method name",
			"staticMethod",
			javaMethodInvocation.getCalledMethod().getDisplayName());
		Assert.assertEquals(
			"method invocation cardinality",
			Constants.CARDINALITY_ONE,
			javaMethodInvocation.getCardinality());
		Assert.assertEquals(
			"method invocation visibility",
			Modifier.PUBLIC,
			javaMethodInvocation.getVisibility());

		Assert.assertTrue(MethodInvocationComparator
			.isMIofJavaModelMethodIncludedInMIofClassModelMethod(
				javaMethod,
				classMethod));

	}

	public void testINSTANCE_CLASS2() {
		final String sourcePath =
			"../PADL Creator JavaFile (Eclipse) Tests/src/";
		final String[] javaFiles =
			new String[] {
					"../PADL Creator JavaFile (Eclipse) Tests/src/padl/example/relationship/A.java",
					"../PADL Creator JavaFile (Eclipse) Tests/src/padl/example/relationship/Aggregation_INSTANCE_CLASS_FROM_FIELD_2.java" };
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
					"../PADL Creator JavaFile (Eclipse) Tests/bin/padl/example/relationship/Aggregation_INSTANCE_CLASS_FROM_FIELD_2.class" };
		final ICodeLevelModel classModel =
			Utils.createCompleteJavaClassesPadlModel("", classFiles);

		final IFirstClassEntity javaClass =
			javaModel
				.getTopLevelEntityFromID("padl.example.relationship.Aggregation_INSTANCE_CLASS_FROM_FIELD_2");
		final IMethod javaMethod =
			(IMethod) javaClass.getConstituentFromName("foo");
		final IMethodInvocation javaMethodInvocation =
			(IMethodInvocation) javaMethod
				.getConstituentFromID("Method Invocation_>PADL<_1");
		//ProxyConsole.getInstance().debugOutput().println(javaMethod.toString());
		final Iterator iterJava =
			javaMethod.getIteratorOnConstituents(IMethodInvocation.class);
		while (iterJava.hasNext()) {
			ProxyConsole
				.getInstance()
				.debugOutput()
				.println("javaMethod " + iterJava.next().toString());
		}

		final IFirstClassEntity classClass =
			classModel
				.getTopLevelEntityFromID("padl.example.relationship.Aggregation_INSTANCE_CLASS_FROM_FIELD_2");
		final IMethod classMethod =
			(IMethod) classClass.getConstituentFromName("foo");

		final Iterator iterClass =
			classMethod.getIteratorOnConstituents(IMethodInvocation.class);
		ProxyConsole
			.getInstance()
			.debugOutput()
			.println("***********************");
		while (iterClass.hasNext()) {
			ProxyConsole
				.getInstance()
				.debugOutput()
				.println("classMethod " + iterClass.next().toString());
		}

		Assert.assertEquals(
			"Method invocation type",
			IMethodInvocation.INSTANCE_CLASS,
			javaMethodInvocation.getType());
		Assert.assertNull(
			"Declaring entity is null",
			javaMethodInvocation.getFieldDeclaringEntity());
		Assert.assertNull(
			"Field name is null",
			javaMethodInvocation.getFirstCallingField());
		Assert.assertEquals(
			"Target entity",
			"padl.example.relationship.A",
			javaMethodInvocation.getTargetEntity().getDisplayID());
		Assert.assertEquals(
			"called method name",
			"staticMethod",
			javaMethodInvocation.getCalledMethod().getDisplayName());
		//02022012 - Aminata S
		//We changed Many by one because for INVOKE STATIC, the cardinality is always ONE
		Assert.assertEquals(
			"method invocation cardinality",
			Constants.CARDINALITY_ONE,
			javaMethodInvocation.getCardinality());
		Assert.assertEquals(
			"method invocation visibility",
			Modifier.PUBLIC,
			javaMethodInvocation.getVisibility());

		Assert.assertTrue(MethodInvocationComparator
			.isMIofJavaModelMethodIncludedInMIofClassModelMethod(
				javaMethod,
				classMethod));

	}

}
