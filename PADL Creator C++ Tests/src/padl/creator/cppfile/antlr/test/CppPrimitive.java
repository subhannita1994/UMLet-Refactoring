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
package padl.creator.cppfile.antlr.test;

import junit.framework.Assert;
import junit.framework.TestCase;
import padl.kernel.IFactory;
import padl.kernel.cpp.antlr.ICPPFactoryANTLR;
import padl.kernel.cpp.antlr.impl.CPPFactoryANTLR;

/**
 * @author Yann-Ga�l Gu�h�neuc
 * @since  2003/12/09
 */
public abstract class CppPrimitive extends TestCase {
	private static ICPPFactoryANTLR factory =
		(ICPPFactoryANTLR) CPPFactoryANTLR.getInstance();

	public static void assertAssigable(
		final String aMessage,
		final Class anInterface,
		final Class aClass) {

		if (!anInterface.isAssignableFrom(aClass)) {
			Assert.fail(aMessage);
		}
	}
	public static void setFactory(final ICPPFactoryANTLR aFactory) {
		CppPrimitive.factory = aFactory;
	}
	public static IFactory getFactory() {
		return CppPrimitive.factory;
	}

	public CppPrimitive(final String aName) {
		super(aName);
	}
}
