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
import ptidej.solver.approximation.TSE07CollapsableInheritanceApproximations;
import ptidej.solver.approximation.TSE07ExtensibleStrictInheritanceApproximations;
import ptidej.solver.constraint.repository.AssociationConstraint;
import ptidej.solver.constraint.repository.CreationConstraint;
import ptidej.solver.constraint.repository.NoGhostEntityConstraint;
import ptidej.solver.constraint.repository.NoInheritancePathConstraint;
import ptidej.solver.constraint.repository.NotEqualConstraint;
import ptidej.solver.constraint.repository.StrictInheritanceConstraint;

/**
 * @author Jean-Yves Guyomarc'h
 * @author Yann-Ga�l Gu�h�neuc
 */
public final class AbstractFactoryMotif {
	public static Problem getProblem(final List allEntities) {
		final Problem pb =
			new Problem(90, "Abstract Factory Design Motif", allEntities);

		final Variable abstractfactory =
			new Variable(pb, "abstractfactory", true);
		final Variable concretefactory =
			new Variable(pb, "concretefactory", true);
		final Variable abstractproduct =
			new Variable(pb, "abstractproduct", true);
		final Variable concreteproduct =
			new Variable(pb, "concreteproduct", false, 3);

		pb.addVar(abstractfactory);
		pb.addVar(concretefactory);
		pb.addVar(abstractproduct);
		pb.addVar(concreteproduct);

		// Constraints
		pb.post(
			new StrictInheritanceConstraint(
				"ConcreteFactory -|>- AbstractFactory",
				"",
				concretefactory,
				abstractfactory,
				50,
				TSE07CollapsableInheritanceApproximations
					.getDefaultApproximations()));

		pb.post(
			new StrictInheritanceConstraint(
				"ConcreteProduct -|>- AbstractProduct",
				"",
				concreteproduct,
				abstractproduct,
				50,
				TSE07ExtensibleStrictInheritanceApproximations
					.getDefaultApproximations()));

		pb.post(
			new NoInheritancePathConstraint(
				"ConcreteProduct -|>- .../... -|>- AbstractFactory",
				"",
				concreteproduct,
				abstractfactory,
				100,
				DefaultNoApproximations.getDefaultApproximations()));

		pb.post(
			new NoInheritancePathConstraint(
				"ConcreteFactory -|>- .../... -|>- AbstractProduct",
				"",
				concretefactory,
				abstractproduct,
				100,
				DefaultNoApproximations.getDefaultApproximations()));

		pb.post(
			new NoGhostEntityConstraint(
				"AbstractProduct <> ?",
				"",
				abstractproduct,
				100,
				DefaultNoApproximations.getDefaultApproximations()));

		pb.post(
			new AssociationConstraint(
				"ConcreteFactory ----> AbstractProduct",
				"",
				concretefactory,
				abstractproduct,
				90,
				DefaultNoApproximations.getDefaultApproximations()));

		pb.post(
			new CreationConstraint(
				"ConcreteFactory -*--> ConcreteProduct",
				"",
				concretefactory,
				concreteproduct,
				90,
				DefaultNoApproximations.getDefaultApproximations()));

		pb.post(
			new NotEqualConstraint(
				"AbstractFactory <> AbstractProduct",
				"",
				abstractfactory,
				abstractproduct,
				100,
				DefaultNoApproximations.getDefaultApproximations()));
		pb.post(
			new NotEqualConstraint(
				"ConcreteFactory <> ConcreteProduct",
				"",
				concretefactory,
				concreteproduct,
				100,
				DefaultNoApproximations.getDefaultApproximations()));
		pb.post(
			new NotEqualConstraint(
				"AbstractFactory <> ConcreteProduct",
				"",
				abstractfactory,
				concreteproduct,
				100,
				DefaultNoApproximations.getDefaultApproximations()));
		pb.post(
			new NotEqualConstraint(
				"ConcreteFactory <> AbstractProduct",
				"",
				concretefactory,
				abstractproduct,
				100,
				DefaultNoApproximations.getDefaultApproximations()));

		return pb;
	}
}
