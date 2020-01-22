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
package padl.micropatterns.examples;

public abstract class Outline {
	
	Outline () {
	}

	public int TestMethod() {
		read("as", 0, 2);
		return 0;
	}
	
	
	public int TestMethod2() {
		read("as", 0, 2);
		return 1;
	}
	
	abstract public int read(String str, int off, int len);
}
