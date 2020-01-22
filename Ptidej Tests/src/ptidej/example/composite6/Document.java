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
package ptidej.example.composite6;

import java.util.Vector;

public class Document {
	private final Vector elements = new Vector();

	public void addElement(final Element e) {
		this.elements.add(e);
	}
	public Element getElement(final int pos) {
		return (Element) this.elements.get(pos);
	}
	public void printAll() {
		for (int i = 0; i < this.elements.size(); i++) {
			((Element) this.elements.get(i)).print();
		}
	}
	public Element removeElement(final Element e) {
		this.elements.remove(e);
		return e;
	}
}
