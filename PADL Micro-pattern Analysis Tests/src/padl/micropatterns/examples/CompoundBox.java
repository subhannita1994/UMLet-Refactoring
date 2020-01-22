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


public class CompoundBox {
	private NonPrimitive instanceAttribute;
	private int fieldOne;
	private int fieldTwo;
	
	// private constructor
	private CompoundBox() {
	}
	
	private void methodOne () {	
		this.instanceAttribute = null;
		this.fieldOne = 0;
		this.fieldTwo = 0;
	}
	
	public void methodTwo () {		
	}
}
