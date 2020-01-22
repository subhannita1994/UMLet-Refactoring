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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import junit.framework.Assert;
import junit.framework.TestCase;
import padl.creator.javafile.eclipse.test.util.Utils;
import padl.kernel.IClass;
import padl.kernel.ICodeLevelModel;
import padl.kernel.IConstructor;
import padl.kernel.IMethodInvocation;
import padl.kernel.IOperation;
import util.io.ProxyConsole;

public class MethodInvocationExplorer extends TestCase {
	public MethodInvocationExplorer(final String name) {
		super(name);
	}
	public void testMethodInvocationInPadl() {
		final String path =
			"../PADL Creator JavaFile (Eclipse) Tests/rsc/padl/example/methodInvocation/bin/";
		final ICodeLevelModel model =
			Utils.createCompleteJavaClassesPadlModel("", new String[] { path });

		final List listOfClasses = new ArrayList();
		listOfClasses.add(model
			.getTopLevelEntityFromID("padl.example.methodInvocation.A"));
		/*listOfClasses
				.add((IClass) model
						.getTopLevelEntityFromID("padl.example.methodinvocation.C"));*/

		final Iterator iterator = listOfClasses.iterator();
		while (iterator.hasNext()) {
			final IClass clazz = (IClass) iterator.next();
			ProxyConsole
				.getInstance()
				.debugOutput()
				.println(clazz.getDisplayID());
			try {
				final Iterator methodsIter =
					clazz.getConcurrentIteratorOnConstituents(IConstructor.class);

				while (methodsIter.hasNext()) {
					// for each m method, list all its method invocations
					final IOperation method = (IOperation) methodsIter.next();
					final Iterator methodInvocationsIter =
						method.getConcurrentIteratorOnConstituents(Class
							.forName("padl.kernel.impl.MethodInvocation"));
					ProxyConsole
						.getInstance()
						.debugOutput()
						.println("Operation " + method.getDisplayID());
					while (methodInvocationsIter.hasNext()) {
						final IMethodInvocation methodInvocation =
							(IMethodInvocation) methodInvocationsIter.next();
						ProxyConsole
							.getInstance()
							.debugOutput()
							.println(methodInvocation.toString());
						ProxyConsole
							.getInstance()
							.debugOutput()
							.println("type:" + methodInvocation.getType());
						ProxyConsole.getInstance().debugOutput().println();
					}
				}
			}
			catch (final ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		Assert.assertTrue(true);
	}
}
