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
package ptidej.example.apsec;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yann-Ga�l Gu�h�neuc
 * @since  2004/07/24
 */
public class Example2 {
	public static void main(final String[] args) {
		final Example2 example2 = new Example2();
		example2.addA(new A());
		// ...
	}

	private final List listOfAs = new ArrayList();
	public void addA(final A a) {
		this.listOfAs.add(a);
	}
	public A getA(final int index) {
		return (A) this.listOfAs.remove(index);
	}
	public void removeA(final A a) {
		this.listOfAs.remove(a);
	}
}
