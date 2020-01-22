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
package sad.rule.creator.model.impl;

import java.util.ArrayList;
import java.util.List;
import sad.rule.creator.model.ISemantic;

/**
 * @author Pierre Leduc
 */
public class Semantic extends Attribute implements ISemantic {
	private final int type;
	private final List values;

	public Semantic(final String aName, final int aType, final List aValue) {
		super(aName);
		this.values = new ArrayList(aValue);
		this.type = aType;
	}

	public int getSemanticType() {
		return this.type;
	}

	public List getSemanticValues() {
		return this.values;
	}

	public String toString() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append(this.getClass().getName());
		buffer.append("\nName: ");
		buffer.append(this.getID());
		buffer.append("\nType: ");
		buffer.append(this.getSemanticType());
		buffer.append("\nValues: ");
		buffer.append(this.getSemanticValues());

		return buffer.toString();
	}
}
