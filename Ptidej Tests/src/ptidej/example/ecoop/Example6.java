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
package ptidej.example.ecoop;

import java.util.Hashtable;

/**
 * @author Yann-Ga�l Gu�h�neuc
 */
public class Example6 {
	public static void main(final String[] args) {
		final Example6 example5 = new Example6();
		example5.listOfAs.put(new Integer(0), new A());
		// ...
	}

	private final Hashtable listOfAs = new Hashtable();
}
