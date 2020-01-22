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
public class TSE07CompositionApproximations implements IApproximations {
	private static final String[] APPROXIMATIONS = {
			"ptidej.solver.constraint.repository.CompositionConstraint",
			"ptidej.solver.constraint.repository.AggregationConstraint" };

	private static TSE07CompositionApproximations UniqueInstance;
	public static TSE07CompositionApproximations getDefaultApproximations() {
		if (TSE07CompositionApproximations.UniqueInstance == null) {
			TSE07CompositionApproximations.UniqueInstance =
				new TSE07CompositionApproximations();
		}
		return TSE07CompositionApproximations.UniqueInstance;
	}

	private TSE07CompositionApproximations() {
	}
	public String[] getApproximations() {
		return TSE07CompositionApproximations.APPROXIMATIONS;
	}
}
