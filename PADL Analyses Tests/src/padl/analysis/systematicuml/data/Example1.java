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
package padl.analysis.systematicuml.data;

/**
 * @author Yann-Ga�l Gu�h�neuc
 * @since  2004/07/24
 */
public class Example1 {
	private A[] listOfAs = new A[10];
	private int numberOfAs = 0;

	public void addA(final A a) {
		this.listOfAs[this.numberOfAs++] = a;
	}
	public A getA(final int index) {
		return this.listOfAs[index];
	}
	public void removeA(final A a) {
		// ...
	}
	public static void main(final String[] args) {
		final Example1 example1 = new Example1();
		example1.addA(new A());
		// ...
	}
}
