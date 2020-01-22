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
package padl.creator.classfile.test;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import junit.framework.Assert;
import junit.framework.TestCase;
import padl.kernel.IElement;
import padl.kernel.IFactory;
import padl.kernel.impl.Factory;

/**
 * @author Yann-Ga�l Gu�h�neuc
 * @since  2003/12/09
 */
public abstract class ClassFilePrimitive extends TestCase {
	public static void assertAssigable(
		final String aMessage,
		final Class anInterface,
		final Class aClass) {

		if (!anInterface.isAssignableFrom(aClass)) {
			Assert.fail(aMessage);
		}
	}
	public static IFactory getFactory() {
		return Factory.getInstance();
	}
	public ClassFilePrimitive(final String aName) {
		super(aName);
	}
	public static int[] getConstituentPosition(
		final Class aConstituentType,
		final IElement[] someElements) {

		final Set positionsSet = new TreeSet();
		for (int i = 0; i < someElements.length; i++) {
			final IElement element = someElements[i];
			final Class[] interfaces = element.getClass().getInterfaces();
			for (int j = 0; j < interfaces.length; j++) {
				final Class interfece = interfaces[j];
				if (interfece.equals(aConstituentType)) {
					positionsSet.add(new Integer(i));
				}
			}
		}

		final int[] positionsArray = new int[positionsSet.size()];
		final Iterator iterator = positionsSet.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			final Integer position = (Integer) iterator.next();
			positionsArray[i] = position.intValue();
			i++;
		}
		return positionsArray;
	}
}
