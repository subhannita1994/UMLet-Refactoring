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

public final class Composition extends Association {
	public Composition(
		final IPrimitiveFactory primitiveFactory,
		final String name,
		final int cardinality,
		final Entity origin,
		final Entity target) {

		super(primitiveFactory, name, cardinality, origin, target);
	}
	protected IPrimitive getLine() {
		return this.getPrimitiveFactory().createDottedLine(
			this.getPosition(),
			this.getDimension(),
			this.getColor());
	}
	protected ISymbol getOriginSymbol() {
		return this.getPrimitiveFactory().createAssociationSymbol(
			this.findIntersectionPointWithOrigin(),
			this.getDimension(),
			this.getColor());
	}
	protected String getSymbol() {
		return "[#]->";
	}
	public int getVisibilityDisplay() {
		return IVisibility.COMPOSITION_DISPLAY_ELEMENTS;
	}
	public int getVisibilityName() {
		return IVisibility.COMPOSITION_NAMES;
	}
}
