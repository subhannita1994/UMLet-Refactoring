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
public class TSE07ExtensibleInheritanceApproximations implements
		IApproximations {

	private static final String[] APPROXIMATIONS = {
			"ptidej.solver.constraint.repository.InheritanceConstraint",
			"ptidej.solver.constraint.repository.InheritancePathConstraint" };

	private static TSE07ExtensibleInheritanceApproximations UniqueInstance;
	public static TSE07ExtensibleInheritanceApproximations getDefaultApproximations() {
		if (TSE07ExtensibleInheritanceApproximations.UniqueInstance == null) {
			TSE07ExtensibleInheritanceApproximations.UniqueInstance =
				new TSE07ExtensibleInheritanceApproximations();
		}
		return TSE07ExtensibleInheritanceApproximations.UniqueInstance;
	}

	private TSE07ExtensibleInheritanceApproximations() {
	}
	public String[] getApproximations() {
		return TSE07ExtensibleInheritanceApproximations.APPROXIMATIONS;
	}
}
