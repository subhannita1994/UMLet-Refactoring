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
package ptidej.solver.repair;

import java.util.ArrayList;
import ptidej.solver.Problem;
import ptidej.solver.Repair;
import choco.AbstractConstraint;

/**
 * Writen in CLAIRE by
 * @author Yann-Ga�l Gu�h�neuc
 * Translated and adapted from CLAIRE version to JAVA by
 * @author Iyadh Sidhom
 * @author Salim Bensemmane
 * @author Fay�al Skhiri
 */
public abstract class MemoryRepair extends Repair {
	private ArrayList userRelaxedConstraints;

	public MemoryRepair(final Problem problem) {
		super(problem);
		this.userRelaxedConstraints = new ArrayList();
	}
	public final void remove(final AbstractConstraint constraint) {
		this.userRelaxedConstraints.add(constraint);
	}
	public ArrayList getRemovedConstraints() {
		return this.userRelaxedConstraints;
	}
	public void setRemoveConstraint(final ArrayList removedList) {
		this.userRelaxedConstraints = removedList;
	}
}
