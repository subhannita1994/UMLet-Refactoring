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

import padl.aspectj.kernel.IPointcut;
import ptidej.ui.primitive.IPrimitiveFactory;

/**
 * @author Jean-Yves Guyomarc'h
 * @since 2005-08-16
 */
public final class Pointcut extends Element {
	private IPointcut pointcut;

	public Pointcut(
		final IPrimitiveFactory primitiveFactory,
		final IPointcut aPointcut) {
		super(primitiveFactory);
		this.pointcut = aPointcut;
	}
	public void build() {
		// Nothing to do.
	}
	public String getName() {
		final StringBuffer name = new StringBuffer("Pointcut: ");
		name.append(this.pointcut.getDisplayName());
		return name.toString();
	}
	public void paint(final int xOffset, final int yOffset) {
		// Nothing to do.
	}
	public String toString() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("Pointcut: ");
		switch (this.pointcut.getVisibility()) {
			case 1 :
				buffer.append("+ ");
				break;
			case 2 :
				buffer.append("- ");
				break;
			case 4 :
				buffer.append("# ");
				break;
			default :
				break;
		}
		buffer.append(this.pointcut.getDisplayName() + "(...)");
		return buffer.toString();
	}
}
