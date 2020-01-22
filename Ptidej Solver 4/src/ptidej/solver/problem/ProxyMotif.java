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
 * @since  2007/02/24 
 */
public final class ProxyMotif {
	public static Problem getProblem(final List allEntities) {
		final Problem pb =
			new Problem(
				90,
				"Proxy Design Motif",
				allEntities);


		final Variable client = new Variable(pb, "client", false);
		final Variable subject =
			new Variable(pb, "subject", true);
		final Variable proxy =
			new Variable(pb, "proxy", true);
		final Variable realsubject =
			new Variable(pb, "realsubject", false);

		pb.addVar(client);
		pb.addVar(subject);
		pb.addVar(proxy);
		pb.addVar(realsubject);

		// Constraints
		pb.post(
			new AssociationConstraint(
				"Client ----> Subject",
				"",
				client,
				subject,
				100,
				DefaultNoApproximations.getDefaultApproximations()));

		pb.post(
			new AssociationConstraint(
				"Proxy ----> RealSubject",
				"",
				proxy,
				realsubject,
				100,
				DefaultNoApproximations.getDefaultApproximations()));

		pb.post(
			new StrictInheritanceConstraint(
				"Proxy -|>- Subject",
				"",
				proxy,
				subject,
				100,
				DefaultNoApproximations.getDefaultApproximations()));

		pb.post(
			new StrictInheritanceConstraint(
				"RealSubject -|>- Subject",
				"",
				realsubject,
				subject,
				100,
				DefaultNoApproximations.getDefaultApproximations()));

		pb.post(
			new AbstractEntityConstraint(
				"Subject is abstract",
				"",
				subject,
				100,
				DefaultNoApproximations.getDefaultApproximations()));

		return pb;
	}
}
