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
package ptidej.ui.occurrence.swt;

import java.awt.Dimension;
import java.awt.Point;
import java.util.StringTokenizer;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import ptidej.ui.Constants;
import ptidej.ui.RGB;
import ptidej.ui.primitive.swt.Primitive;

public class GroupOccurrenceTip extends ptidej.ui.primitive.swt.Primitive
		implements ptidej.ui.occurrence.IGroupOccurrenceTip {

	private final String tip;
	private final int lineHeight;

	GroupOccurrenceTip(
		final Device device,
		final GC graphics,
		final Point position,
		final String tip,
		final RGB color) {

		super(device, graphics, position, Constants.NULL_DIMENSION, color);

		this.setPosition(new Point(
			position.x + Constants.TIP_GAP.width,
			position.y + Constants.TIP_GAP.height));

		final StringBuffer buffer = new StringBuffer();
		char c;
		for (int i = 0; i < tip.length(); i++) {
			c = tip.charAt(i);
			if (c != '\t') {
				buffer.append(c);
			}
			else {
				for (int j = 0; j < Constants.TAB_LENGTH; j++) {
					buffer.append(' ');
				}
			}
		}

		this.tip = buffer.toString();
		this.lineHeight = this.getGraphics().getFontMetrics().getHeight();

		final StringTokenizer tokenizer = new StringTokenizer(this.tip, "\n");
		final int numberOfLines = tokenizer.countTokens();
		int longestLineWidth = 0;
		while (tokenizer.hasMoreTokens()) {
			longestLineWidth =
				Math.max(
					longestLineWidth,
					this.getGraphics().textExtent(tokenizer.nextToken()).x);
		}
		longestLineWidth += Constants.TIP_GAP.width;

		this.setDimension(new Dimension(longestLineWidth, numberOfLines
				* this.lineHeight + Constants.TIP_GAP.height));
	}
	public int getVisibleElements() {
		return 0;
	}
	public void paint(final int xOffset, final int yOffset) {
		this.getGraphics().setBackground(
			Primitive.convertColor(
				this.getDevice(),
				Constants.TIP_BACKGROUND_COLOR));
		this.getGraphics().fillRectangle(
			this.getPosition().x + xOffset,
			this.getPosition().y + yOffset,
			this.getDimension().width,
			this.getDimension().height);
		this.getGraphics().setForeground(this.getSWTColor());
		this.getGraphics().drawRectangle(
			this.getPosition().x + xOffset,
			this.getPosition().y + yOffset,
			this.getDimension().width,
			this.getDimension().height);

		final StringTokenizer tokenizer = new StringTokenizer(this.tip, "\n");
		int yPos = this.getGraphics().getFontMetrics().getLeading();
		while (tokenizer.hasMoreTokens()) {
			this.getGraphics().drawString(
				tokenizer.nextToken(),
				this.getPosition().x + xOffset + Constants.TIP_GAP.width / 2,
				this.getPosition().y + yOffset + yPos);
			yPos += this.lineHeight;
		}
	}
	public void setVisibleElements(final int visibility) {
	}
	public String toString() {
		return this.tip;
	}
}
