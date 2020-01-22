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
package ptidej.ui.primitive.awt;

import java.awt.Dimension;
import java.awt.Point;
import ptidej.ui.Constants;
import ptidej.ui.RGB;
import ptidej.ui.primitive.IArrowSymbol;

public final class ArrowSymbol extends Symbol implements IArrowSymbol {
	ArrowSymbol(
		final PrimitiveFactory primitiveFactory,
		final Point origin,
		final Dimension dimension,
		final RGB color) {

		super(primitiveFactory, origin, dimension, color);
	}
	public void paint(final int xOffset, final int yOffset) {
		final int[][] coordinates =
			this.computeCoordinates(
				Constants.ARROW_SYMBOL_DIMENSION.width,
				Constants.ARROW_SYMBOL_DIMENSION.height);

		int[] x = coordinates[0];
		int[] y = coordinates[1];
		x = new int[] { x[1], x[0], x[3] };
		y = new int[] { y[1], y[0], y[3] };

		this.getGraphics().translate(xOffset, yOffset);
		this.getGraphics().setColor(this.getAWTColor());
		this.getGraphics().drawPolyline(x, y, 3);
		this.getGraphics().translate(-xOffset, -yOffset);
	}
}
