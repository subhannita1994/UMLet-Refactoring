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
package padl.example.fieldaccess;
 
public class Test {
	int a;
	void rien() {
		this.a = 3;
	}
	public static void main(String[] args) {
		Test t = new Test();
		t.a = 3;
		t.rien();
		System.out.println(t.a);
	}
}
