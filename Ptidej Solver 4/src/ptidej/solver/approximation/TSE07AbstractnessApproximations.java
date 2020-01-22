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
package ptidej.solver.approximation;

/**
 * @author	Yann-Ga�l Gu�h�neuc
 * @since	2006/08/16
 */
public class TSE07AbstractnessApproximations implements IApproximations {
	private static final String[] APPROXIMATIONS = {
			"ptidej.solver.constraint.repository.AbstractEntityConstraint",
			"ptidej.solver.constraint.repository.NoAbstractEntityConstraint" };

	private static TSE07AbstractnessApproximations UniqueInstance;
	public static TSE07AbstractnessApproximations getDefaultApproximations() {
		if (TSE07AbstractnessApproximations.UniqueInstance == null) {
			TSE07AbstractnessApproximations.UniqueInstance =
				new TSE07AbstractnessApproximations();
		}
		return TSE07AbstractnessApproximations.UniqueInstance;
	}

	private TSE07AbstractnessApproximations() {
	}
	public String[] getApproximations() {
		return TSE07AbstractnessApproximations.APPROXIMATIONS;
	}
}
