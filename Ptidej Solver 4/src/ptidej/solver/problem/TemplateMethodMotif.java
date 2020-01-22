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
import ptidej.solver.constraint.repository.AbstractEntityConstraint;
import ptidej.solver.constraint.repository.AssociationConstraint;
import ptidej.solver.constraint.repository.StrictInheritanceConstraint;

/**
 * @author Yann-Ga�l Gu�h�neuc
 * @since  2007/03/02 
 */
public final class TemplateMethodMotif {
	public static Problem getProblem(final List allEntities) {
		final Problem pb =
			new Problem(90, "Template Method Design Motif", allEntities);

		final Variable client = new Variable(pb, "client", false);
		final Variable abstractclass =
			new Variable(pb, "abstractclass", true);
		final Variable concreteclass =
			new Variable(pb, "concreteclass", false);

		pb.addVar(client);
		pb.addVar(abstractclass);
		pb.addVar(concreteclass);

		// Constraints
		pb.post(
			new AssociationConstraint(
				"Client <>--> AbstractClass",
				"",
				client,
				abstractclass,
				100,
				DefaultNoApproximations.getDefaultApproximations()));

		pb.post(
			new StrictInheritanceConstraint(
				"ConcreteClass -|>- AbstractClass",
				"",
				concreteclass,
				abstractclass,
				100,
				DefaultNoApproximations.getDefaultApproximations()));

		pb.post(
			new AbstractEntityConstraint(
				"AbstractClass is abstract",
				"",
				abstractclass,
				100,
				DefaultNoApproximations.getDefaultApproximations()));

		return pb;
	}
}
