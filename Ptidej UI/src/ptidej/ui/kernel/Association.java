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
package ptidej.ui.kernel;

import ptidej.ui.IVisibility;
import ptidej.ui.primitive.IPrimitive;
import ptidej.ui.primitive.IPrimitiveFactory;
import ptidej.ui.primitive.ISymbol;

public class Association extends Relationship {
	private final String associationFieldName;
	private final String associationTypeName;

	public Association(
		final IPrimitiveFactory primitiveFactory,
		final String name,
		final int cardinality,
		final Entity origin,
		final Entity target) {

		super(primitiveFactory, cardinality, origin, target);

		this.associationFieldName = name;
		this.associationTypeName = target.getName();
	}
	protected IPrimitive getLine() {
		return this.getPrimitiveFactory().createLine(
			this.getPosition(),
			this.getDimension(),
			this.getColor());
	}
	protected ISymbol getOriginSymbol() {
		return null;
	}
	protected String getSymbol() {
		return "----> " + this.associationTypeName + this.associationFieldName;
	}
	protected final ISymbol getTargetSymbol() {
		return this.getPrimitiveFactory().createArrowSymbol(
			this.findIntersectionPointWithTarget(),
			this.getDimension(),
			this.getColor());
	}
	public int getVisibilityDisplay() {
		return IVisibility.ASSOCIATION_DISPLAY_ELEMENTS;
	}
	int getVisibilityName() {
		return IVisibility.ASSOCIATION_NAMES;
	}
}
