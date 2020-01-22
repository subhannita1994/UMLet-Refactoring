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
package ptidej.solver.problem.aol;

import java.util.List;

import ptidej.solver.Problem;
import ptidej.solver.Variable;
import ptidej.solver.approximation.DefaultNoApproximations;
import ptidej.solver.approximation.TSE07ExtensibleStrictInheritanceApproximations;
import ptidej.solver.constraint.repository.AssociationConstraint;
import ptidej.solver.constraint.repository.IgnoranceConstraint;
import ptidej.solver.constraint.repository.NoGhostEntityConstraint;
import ptidej.solver.constraint.repository.NoInheritancePathConstraint;
import ptidej.solver.constraint.repository.NotEqualConstraint;
import ptidej.solver.constraint.repository.StrictInheritanceConstraint;

/**
 * @author Yann-Ga�l Gu�h�neuc
 * @since  2004/09/05 
 */
public final class AdapterMotif {
	public static Problem getProblem(final List allEntities) {
		final Problem pb =
			new Problem(90, "Adapter Design Motif", allEntities);

		final Variable client = new Variable(pb, "client", false);
		final Variable target = new Variable(pb, "target", true);
		final Variable adapter = new Variable(pb, "adapter", false);
		final Variable adaptee = new Variable(pb, "adpatee", false);

		pb.addVar(target);
		pb.addVar(adapter);
		pb.addVar(adaptee);
		pb.addVar(client);

		// Constraints
		pb.post(
			new AssociationConstraint(
				"Client ---�> Target",
				"",
				client,
				target,
				100,
				DefaultNoApproximations.getDefaultApproximations()));

		//	pb.post(
		//		new AbstractEntityConstraint(
		//			"Target is abstract",
		//			"",
		//			target,
		//			100,
		//			DefaultNoApproximations.getDefaultApproximations()));
		pb.post(
			new NoGhostEntityConstraint(
				"Target <> ?",
				"",
				target,
				100,
				DefaultNoApproximations.getDefaultApproximations()));
		pb.post(
			new IgnoranceConstraint(
				"Target -/--> Adapter",
				"",
				target,
				adapter,
				100,
				DefaultNoApproximations.getDefaultApproximations()));

		pb.post(
			new StrictInheritanceConstraint(
				"Adapter -|>- Target",
				"",
				adapter,
				target,
				100,
				TSE07ExtensibleStrictInheritanceApproximations
					.getDefaultApproximations()));
		pb.post(
			new AssociationConstraint(
				"Adapter ----> Adaptee",
				"",
				adapter,
				adaptee,
				100,
				DefaultNoApproximations.getDefaultApproximations()));

		pb.post(
			new NoGhostEntityConstraint(
				"Adaptee <> ?",
				"",
				adaptee,
				100,
				DefaultNoApproximations.getDefaultApproximations()));
		pb.post(
			new NotEqualConstraint(
				"Adaptee <> Adapter",
				"",
				adaptee,
				adapter,
				100,
				DefaultNoApproximations.getDefaultApproximations()));
		pb.post(
			new NoInheritancePathConstraint(
				"Adaptee -|>-.../...-|>- Adapter",
				"",
				adaptee,
				adapter,
				100,
				DefaultNoApproximations.getDefaultApproximations()));
		pb.post(
			new NoInheritancePathConstraint(
				"Adapter -|>-.../...-|>- Adaptee",
				"",
				adapter,
				adaptee,
				100,
				DefaultNoApproximations.getDefaultApproximations()));

		return pb;
	}
}
