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
public class TSE07AssociationOrNoneApproximations implements IApproximations {
	private static final String[] APPROXIMATIONS = {
			"ptidej.solver.constraint.repository.AssociationConstraint",
			"ptidej.solver.constraint.repository.UseConstraint",
			"ptidej.solver.constraint.repository.NotEqualConstraint" };

	private static TSE07AssociationOrNoneApproximations UniqueInstance;
	public static TSE07AssociationOrNoneApproximations getDefaultApproximations() {
		if (TSE07AssociationOrNoneApproximations.UniqueInstance == null) {
			TSE07AssociationOrNoneApproximations.UniqueInstance =
				new TSE07AssociationOrNoneApproximations();
		}
		return TSE07AssociationOrNoneApproximations.UniqueInstance;
	}

	private TSE07AssociationOrNoneApproximations() {
	}
	public String[] getApproximations() {
		return TSE07AssociationOrNoneApproximations.APPROXIMATIONS;
	}
}
