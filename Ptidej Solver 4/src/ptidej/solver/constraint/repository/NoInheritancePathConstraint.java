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
package ptidej.solver.constraint.repository;

import ptidej.solver.Variable;
import ptidej.solver.approximation.IApproximations;
import ptidej.solver.constraint.AbstractInheritanceConstraint;

/**
 * Writen in CLAIRE by
 * @author Yann-Ga�l Gu�h�neuc
 * Translated and adapted from CLAIRE version to JAVA by
 * @author Iyadh Sidhom
 * @author Salim Bensemmane
 * @author Fay�al Skhiri
 */
public class NoInheritancePathConstraint
	extends AbstractInheritanceConstraint {

	public NoInheritancePathConstraint(
		final String name,
		final String commande,
		final Variable v0,
		final Variable v1,
		final int weight,
		final IApproximations approximations) {

		super(name, commande, v0, v1, weight, approximations);
		this.setSymbol("-|>-.../...-|>- or =");
		this.setFieldName("allReachableSuperEntities");
		this.setNegative(true);
	}
}
