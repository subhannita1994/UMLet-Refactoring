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
package ptidej.solver.problem;

import java.util.List;

import ptidej.solver.Problem;
import ptidej.solver.Variable;
import ptidej.solver.approximation.DefaultNoApproximations;
import ptidej.solver.constraint.repository.AggregationConstraint;
import ptidej.solver.constraint.repository.AssociationConstraint;
import ptidej.solver.constraint.repository.CreationConstraint;
import ptidej.solver.constraint.repository.IgnoranceConstraint;
import ptidej.solver.constraint.repository.NoGhostEntityConstraint;

/**
 * @author Lucas Nelaupe, Ferrand Anthony, Tran Quang Dung, Verdier Frédéric 
 * @since  2014/06/01 
 */
public final class FacadeMotif {
	public static Problem getProblem(final List allEntities) {
		final Problem pb = new Problem(90, "Facade Design Motif", allEntities);

		final Variable client = new Variable(pb, "client", false);
		final Variable facadedCode = new Variable(pb, "facadedCode", false);
		final Variable facade = new Variable(pb, "facade", true);

		pb.addVar(client);
		pb.addVar(facadedCode);
		pb.addVar(facade);

		// Constraints
		/* --- Facade constraints --- */
		pb.post(
			new AggregationConstraint(
				"facade ---­> facadedCode",
				"",
				facade,
				facadedCode,
				100,
				DefaultNoApproximations.getDefaultApproximations()));
		
		pb.post(
				new CreationConstraint(
					"facade -1--> facadedCode",
					"",
					facade,
					facadedCode,
					100,
					DefaultNoApproximations.getDefaultApproximations()));
		
		pb.post(
				new NoGhostEntityConstraint(
					"facade <> ?",
					"",
					facade,
					100,
					DefaultNoApproximations.getDefaultApproximations()));
		
		/* -------------------------- */
		
		/* --- Client constraints --- */
		pb.post(
			new AssociationConstraint(
				"client ---­> facade",
				"",
				client,
				facade,
				100,
				DefaultNoApproximations.getDefaultApproximations()));
		
		pb.post(
				new IgnoranceConstraint(
					"client -/--> facadedCode",
					"",
					client,
					facadedCode,
					100,
					DefaultNoApproximations.getDefaultApproximations()));
		
		pb.post(
				new NoGhostEntityConstraint(
					"client <> ?",
					"",
					client,
					100,
					DefaultNoApproximations.getDefaultApproximations()));

		
		/* -------------------------- */
		
		return pb;
	}
}
