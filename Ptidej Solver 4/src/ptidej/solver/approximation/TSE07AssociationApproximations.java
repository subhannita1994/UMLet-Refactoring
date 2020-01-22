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
public class TSE07AssociationApproximations implements IApproximations {
	private static final String[] APPROXIMATIONS = {
			"ptidej.solver.constraint.repository.AssociationConstraint",
			"ptidej.solver.constraint.repository.UseConstraint" };

	private static TSE07AssociationApproximations UniqueInstance;
	public static TSE07AssociationApproximations getDefaultApproximations() {
		if (TSE07AssociationApproximations.UniqueInstance == null) {
			TSE07AssociationApproximations.UniqueInstance =
				new TSE07AssociationApproximations();
		}
		return TSE07AssociationApproximations.UniqueInstance;
	}

	private TSE07AssociationApproximations() {
	}
	public String[] getApproximations() {
		return TSE07AssociationApproximations.APPROXIMATIONS;
	}
}
