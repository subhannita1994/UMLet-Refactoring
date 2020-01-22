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
package ptidej.ui.analysis.repository.comparator.ui;

import java.awt.Dimension;
import java.awt.Point;

import padl.kernel.IClass;
import padl.kernel.IConstituentOfModel;
import ptidej.ui.RGB;
import ptidej.ui.kernel.Class;
import ptidej.ui.kernel.builder.Builder;
import ptidej.ui.primitive.IPrimitiveFactory;

/**
 * @author Yann-Ga�l Gu�h�neuc
 * @since  2004/12/17
 */
public class RemovedClass extends Class {
	public RemovedClass(
		final IPrimitiveFactory aPrimitiveFactory,
		final Builder aBuilder,
		final IConstituentOfModel anEntity) {

		super(aPrimitiveFactory, aBuilder, (IClass) anEntity);
	}
	protected String getStereotype() {
		return "";
	}
	public void paint(final int xOffset, final int yOffset) {
		super.paint(xOffset, yOffset);

		// Yann 2005/12/06: Emphasisis!
		// I create a thiker rectangle for better
		// output on paper :-)
		final int thickness = 5;
		for (int i = 0; i < thickness; i++) {
			final Point position =
				new Point(
					(int) this.getPosition().getX() - i,
					(int) this.getPosition().getY() - i);
			final Dimension dimension =
				new Dimension(
					(int) this.getDimension().getWidth() + 2 * i,
					(int) this.getDimension().getHeight() + 2 * i);
			this
				.getPrimitiveFactory()
				.createRectangle(position, dimension, new RGB(210, 0, 0))
				.paint(xOffset, yOffset);
		}
	}
}
