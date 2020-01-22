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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import junit.framework.Assert;
import junit.framework.TestCase;
import padl.analysis.UnsupportedSourceModelException;
import padl.analysis.repository.AACRelationshipsAnalysis;
import padl.creator.classfile.CompleteClassFileCreator;
import padl.kernel.IAbstractLevelModel;
import padl.kernel.ICodeLevelModel;
import padl.kernel.IFirstClassEntity;
import padl.kernel.IIdiomLevelModel;
import padl.kernel.IMethod;
import padl.kernel.IMethodInvocation;
import padl.kernel.exception.CreationException;
import padl.kernel.impl.Factory;

/**
 * @author Farouk Zaidi
 * @since  2004-03-23
 */
public class TestDup extends TestCase {
	private String className = "padl.example.relationship.MethodDump";
	private IAbstractLevelModel currentModel;
	private String fileName =
		"../PADL Creator ClassFile Tests/bin/padl/example/relationship/MethodDump.class";

	public TestDup(final String aName) {
		super(aName);
	}
	private boolean compareArrays(
		final String[] expectedResult,
		final String[] reference) {

		if (expectedResult.length != reference.length) {
			return false;
		}
		for (int i = 0; i < expectedResult.length; i++) {
			if (!expectedResult[i].equals(reference[i])) {
				System.err
					.println("Problem with method invocation number " + i);
				return false;
			}
		}
		return true;
	}
	private Object[] listMethodInvocations(final String methodName) {
		final IFirstClassEntity firstClassEntity =
			(IFirstClassEntity) this.currentModel
				.getTopLevelEntityFromID(this.className);
		final IMethod method =
			(IMethod) firstClassEntity.getConstituentFromID(methodName);
		final List listMI = new ArrayList();
		final Iterator iteratorOnConstituents =
			method.getIteratorOnConstituents(IMethodInvocation.class);
		while (iteratorOnConstituents.hasNext()) {
			final IMethodInvocation methodInvocation =
				(IMethodInvocation) iteratorOnConstituents.next();
			listMI.add(methodInvocation.getDisplayName());
		}
		final String[] result = new String[listMI.size()];
		listMI.toArray(result);
		return result;
	}
	protected void setUp() throws CreationException,
			UnsupportedSourceModelException {

		final ICodeLevelModel codeLevelModel =
			Factory.getInstance().createCodeLevelModel("MethodDump");
		codeLevelModel.create(new CompleteClassFileCreator(
			new String[] { this.fileName }));

		// this.currentModel = codeLevelModel.convert(codeLevelModel.getDisplayName());
		this.currentModel =
			(IIdiomLevelModel) new AACRelationshipsAnalysis()
				.invoke(codeLevelModel);
	}
	/**
	 * TODO: This test fails because the DUPxxx are not analyzed correctly
	 * during the parsing (MethodInvocationAnalyzer). The test is here to show it clearly. 
	 */
	public void testAnalyzeDup() {
		final String[] expectedResult =
			{ "counter:padl.example.relationship.MethodDump:NoMethod:NoTarget",
					"counter:padl.example.relationship.MethodDump:=:NoTarget" };

		final String methodName = "analyzeDup()";
		final String[] result =
			(String[]) this.listMethodInvocations(methodName);
		Assert.assertTrue(this.compareArrays(expectedResult, result));
	}
	public void testControlIfWhileForSwitch() {
		final String[] expectedResult =
			{
					"NoField:NoFieldEntity:NoMethod:java.lang.Integer",
					"NoField:NoFieldEntity:NoMethod:java.lang.System",
					"NoField:NoFieldEntity:NoMethod:java.lang.StringBuffer",
					"NoField:NoFieldEntity:NoMethod:java.lang.Integer",
					"NoField:NoFieldEntity:NoMethod:java.lang.String",
					"NoField:NoFieldEntity:NoMethod:java.io.PrintStream",
					"NoField:NoFieldEntity:NoMethod:java.lang.Integer",
					"NoField:NoFieldEntity:NoMethod:java.lang.StringBuffer",
					"NoField:NoFieldEntity:NoMethod:java.io.PrintStream",
					"NoField:NoFieldEntity:NoMethod:java.lang.Integer",
					"NoField:NoFieldEntity:NoMethod:java.lang.Integer",
					"NoField:NoFieldEntity:NoMethod:java.lang.Integer",
					"NoField:NoFieldEntity:NoMethod:java.lang.Integer",
					"NoField:NoFieldEntity:NoMethod:java.lang.System",
					"NoField:NoFieldEntity:NoMethod:java.io.PrintStream",
					"NoField:NoFieldEntity:NoMethod:java.lang.Integer",
					"NoField:NoFieldEntity:NoMethod:java.lang.Integer",
					"NoField:NoFieldEntity:Integer:java.lang.Integer",
					"p:padl.example.relationship.MethodDump:floatValue:java.lang.Integer",
					"NoField:NoFieldEntity:valueOf:java.lang.String",
					"NoField:NoFieldEntity:StringBuffer:java.lang.StringBuffer",
					"NoField:NoFieldEntity:append:java.lang.StringBuffer",
					"p:padl.example.relationship.MethodDump:NoMethod:NoTarget",
					"NoField:NoFieldEntity:append:java.lang.StringBuffer",
					"NoField:NoFieldEntity:toString:java.lang.StringBuffer",
					"out:java.lang.System:println:java.io.PrintStream",
					"p:padl.example.relationship.MethodDump:NoMethod:NoTarget",
					"p:padl.example.relationship.MethodDump:byteValue:java.lang.Integer",
					"p:padl.example.relationship.MethodDump:intValue:java.lang.Integer",
					"p:padl.example.relationship.MethodDump:intValue:java.lang.Integer",
					"p:padl.example.relationship.MethodDump:floatValue:java.lang.Integer",
					"f:padl.example.relationship.MethodDump:NoMethod:NoTarget",
					"out:java.lang.System:println:java.io.PrintStream",
					"p:padl.example.relationship.MethodDump:=:NoTarget",
					"str:padl.example.relationship.MethodDump:NoMethod:NoTarget",
					"NoField:NoFieldEntity:getInteger:java.lang.Integer",
					"p:padl.example.relationship.MethodDump:=:NoTarget",
					"f:padl.example.relationship.MethodDump:NoMethod:NoTarget",
					"f:padl.example.relationship.MethodDump:=:NoTarget",
					"f:padl.example.relationship.MethodDump:NoMethod:NoTarget",
					"p:padl.example.relationship.MethodDump:NoMethod:NoTarget",
					"p:padl.example.relationship.MethodDump:intValue:java.lang.Integer" };

		final String methodName = "controlIfWhileForSwitch()";
		final String[] result =
			(String[]) this.listMethodInvocations(methodName);
		Assert.assertTrue(this.compareArrays(expectedResult, result));
	}
	public void testDetectOperators() {
		final String[] expectedResult =
			{
					"NoField:NoFieldEntity:NoMethod:java.lang.Integer",
					"NoField:NoFieldEntity:NoMethod:java.lang.Integer",
					"NoField:NoFieldEntity:NoMethod:java.lang.Integer",
					"p:padl.example.relationship.MethodDump:floatValue:java.lang.Integer",
					"f:padl.example.relationship.MethodDump:=:NoTarget",
					"p:padl.example.relationship.MethodDump:intValue:java.lang.Integer",
					"p:padl.example.relationship.MethodDump:intValue:java.lang.Integer",
					"f:padl.example.relationship.MethodDump:NoMethod:NoTarget" };

		final String methodName = "detectOperators()";
		final String[] result =
			(String[]) this.listMethodInvocations(methodName);
		Assert.assertTrue(this.compareArrays(expectedResult, result));
	}
	public void testDynamicMethod() {
		final String[] expectedResult =
			{
					"NoField:NoFieldEntity:NoMethod:java.lang.Integer",
					"NoField:NoFieldEntity:NoMethod:java.lang.String",
					"NoField:NoFieldEntity:NoMethod:java.lang.String",
					"NoField:NoFieldEntity:NoMethod:java.lang.String",
					"NoField:NoFieldEntity:NoMethod:java.lang.String",
					"NoField:NoFieldEntity:NoMethod:java.lang.String",
					"NoField:NoFieldEntity:NoMethod:java.lang.Integer",
					"NoField:NoFieldEntity:NoMethod:java.lang.Integer",
					"NoField:NoFieldEntity:NoMethod:java.lang.String",
					"NoField:NoFieldEntity:NoMethod:java.lang.String",
					"p:padl.example.relationship.MethodDump:intValue:java.lang.Integer",
					"str:padl.example.relationship.MethodDump:length:java.lang.String",
					"str:padl.example.relationship.MethodDump:substring:java.lang.String",
					"str:padl.example.relationship.MethodDump:charAt:java.lang.String",
					"str:padl.example.relationship.MethodDump:NoMethod:NoTarget",
					"NoField:NoFieldEntity:compareTo:java.lang.String",
					"NoField:NoFieldEntity:length:java.lang.String",
					"p:padl.example.relationship.MethodDump:NoMethod:NoTarget",
					"str:padl.example.relationship.MethodDump:NoMethod:NoTarget",
					"NoField:NoFieldEntity:dynamicMethod:padl.example.relationship.MethodDump" };

		final String methodName =
			"dynamicMethod(java.lang.Integer, java.lang.String, java.lang.String, int)";
		final String[] result =
			(String[]) this.listMethodInvocations(methodName);
		Assert.assertTrue(this.compareArrays(expectedResult, result));
	}
	public void testInstanceOf() {
		final String[] expectedResult =
			{
					"NoField:NoFieldEntity:NoMethod:java.lang.Object",
					"NoField:NoFieldEntity:NoMethod:java.lang.String",
					"NoField:NoFieldEntity:NoMethod:java.lang.Number",
					"NoField:NoFieldEntity:NoMethod:java.lang.System",
					"NoField:NoFieldEntity:NoMethod:java.lang.StringBuffer",
					"NoField:NoFieldEntity:NoMethod:java.io.PrintStream",
					"NoField:NoFieldEntity:NoMethod:java.lang.String",
					"NoField:NoFieldEntity:NoMethod:java.lang.Integer",
					"NoField:NoFieldEntity:NoMethod:java.lang.StringBuffer",
					"p:padl.example.relationship.MethodDump:NoMethod:NoTarget",
					"str:padl.example.relationship.MethodDump:NoMethod:NoTarget",
					"NoField:NoFieldEntity:StringBuffer:java.lang.StringBuffer",
					"NoField:NoFieldEntity:append:java.lang.StringBuffer",
					"NoField:NoFieldEntity:append:java.lang.StringBuffer",
					"NoField:NoFieldEntity:toString:java.lang.StringBuffer",
					"out:java.lang.System:println:java.io.PrintStream" };

		final String methodName = "instanceOf()";
		final String[] result =
			(String[]) this.listMethodInvocations(methodName);
		Assert.assertTrue(this.compareArrays(expectedResult, result));
	}
	public void testLoadAndStoreInArray() {
		final String[] expectedResult =
			{
					"NoField:NoFieldEntity:NoMethod:java.lang.Integer",
					"NoField:NoFieldEntity:NoMethod:java.lang.System",
					"NoField:NoFieldEntity:NoMethod:java.io.PrintStream",
					"myTab:padl.example.relationship.MethodDump:=:NoTarget",
					"myTab:padl.example.relationship.MethodDump:NoMethod:NoTarget",
					"f:padl.example.relationship.MethodDump:NoMethod:NoTarget",
					"p:padl.example.relationship.MethodDump:intValue:java.lang.Integer",
					"myTab:padl.example.relationship.MethodDump:NoMethod:NoTarget",
					"f:padl.example.relationship.MethodDump:NoMethod:NoTarget",
					"out:java.lang.System:println:java.io.PrintStream" };

		final String methodName = "loadAndStoreInArray()";
		final String[] result =
			(String[]) this.listMethodInvocations(methodName);
		Assert.assertTrue(this.compareArrays(expectedResult, result));
	}
	public void testMethod3aryOperator() {
		final String[] expectedResult =
			{ "NoField:NoFieldEntity:NoMethod:java.lang.Integer",
					"NoField:NoFieldEntity:NoMethod:java.lang.Math",
					"NoField:NoFieldEntity:NoMethod:java.lang.System",
					"NoField:NoFieldEntity:NoMethod:java.io.PrintStream",
					"NoField:NoFieldEntity:NoMethod:java.lang.Integer",
					"p:padl.example.relationship.MethodDump:NoMethod:NoTarget",
					"NoField:NoFieldEntity:floatValue:java.lang.Integer",
					"NoField:NoFieldEntity:random:java.lang.Math",
					"out:java.lang.System:println:java.io.PrintStream" };

		final String methodName = "method3aryOperator(java.lang.Integer)";
		final String[] result =
			(String[]) this.listMethodInvocations(methodName);
		Assert.assertTrue(this.compareArrays(expectedResult, result));
	}
	public void testMethodPrintln() {
		final String[] expectedResult =
			{
					"NoField:NoFieldEntity:NoMethod:java.lang.System",
					"NoField:NoFieldEntity:NoMethod:java.lang.StringBuffer",
					"NoField:NoFieldEntity:NoMethod:java.lang.Integer",
					"NoField:NoFieldEntity:NoMethod:java.lang.String",
					"NoField:NoFieldEntity:NoMethod:java.io.PrintStream",
					"NoField:NoFieldEntity:NoMethod:java.lang.StringBuffer",
					"NoField:NoFieldEntity:NoMethod:java.lang.Object",
					"NoField:NoFieldEntity:NoMethod:java.lang.Integer",
					"NoField:NoFieldEntity:NoMethod:java.lang.StringBuffer",
					"NoField:NoFieldEntity:NoMethod:java.lang.StringBuffer",
					"NoField:NoFieldEntity:NoMethod:java.io.PrintStream",
					"NoField:NoFieldEntity:NoMethod:java.lang.Integer",
					"p:padl.example.relationship.MethodDump:floatValue:java.lang.Integer",
					"NoField:NoFieldEntity:valueOf:java.lang.String",
					"NoField:NoFieldEntity:StringBuffer:java.lang.StringBuffer",
					"NoField:NoFieldEntity:append:java.lang.StringBuffer",
					"NoField:NoFieldEntity:append:java.lang.StringBuffer",
					"NoField:NoFieldEntity:Object:java.lang.Object",
					"NoField:NoFieldEntity:compareTo:java.lang.Integer",
					"NoField:NoFieldEntity:append:java.lang.StringBuffer",
					"NoField:NoFieldEntity:toString:java.lang.StringBuffer",
					"out:java.lang.System:println:java.io.PrintStream" };

		final String methodName = "methodPrintln(short, java.lang.Integer)";
		final String[] result =
			(String[]) this.listMethodInvocations(methodName);
		Assert.assertTrue(this.compareArrays(expectedResult, result));
	}
	public void testStaticInvocations() {
		final String[] expectedResult =
			{
					"NoField:NoFieldEntity:random:java.lang.Math",
					"NoField:NoFieldEntity:NoMethod:java.lang.Integer",
					"NoField:NoFieldEntity:NoMethod:java.lang.Integer",
					"NoField:NoFieldEntity:NoMethod:java.lang.System",
					"NoField:NoFieldEntity:random:java.lang.Math",
					"NoField:NoFieldEntity:parseInt:java.lang.Integer",
					"p:padl.example.relationship.MethodDump:NoMethod:NoTarget",
					"NoField:NoFieldEntity:getInteger:java.lang.Integer",
					"str:padl.example.relationship.MethodDump:NoMethod:NoTarget",
					"NoField:NoFieldEntity:load:java.lang.System" };

		final String methodName = "staticInvocations()";
		final String[] result =
			(String[]) this.listMethodInvocations(methodName);
		Assert.assertTrue(this.compareArrays(expectedResult, result));
	}
	public void testStoreToField() {
		final String[] expectedResult =
			{
					"NoField:NoFieldEntity:NoMethod:java.lang.Integer",
					"NoField:NoFieldEntity:NoMethod:java.lang.Integer",
					"NoField:NoFieldEntity:NoMethod:java.lang.Integer",
					"NoField:NoFieldEntity:NoMethod:java.lang.System",
					"NoField:NoFieldEntity:NoMethod:java.io.PrintStream",
					"str:padl.example.relationship.MethodDump:NoMethod:NoTarget",
					"NoField:NoFieldEntity:Integer:java.lang.Integer",
					"p:padl.example.relationship.MethodDump:=:NoTarget",
					"p:padl.example.relationship.MethodDump:toString:java.lang.Integer",
					"str:padl.example.relationship.MethodDump:=:NoTarget",
					"str:padl.example.relationship.MethodDump:NoMethod:NoTarget",
					"NoField:NoFieldEntity:getInteger:java.lang.Integer",
					"p:padl.example.relationship.MethodDump:=:NoTarget",
					"out:java.lang.System:println:java.io.PrintStream" };

		final String methodName = "storeToField()";
		final String[] result =
			(String[]) this.listMethodInvocations(methodName);
		Assert.assertTrue(this.compareArrays(expectedResult, result));
	}
}
