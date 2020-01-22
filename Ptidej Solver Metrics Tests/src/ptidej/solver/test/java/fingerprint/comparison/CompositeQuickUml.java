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
package ptidej.solver.test.java.fingerprint.comparison;

import junit.framework.Assert;
import ptidej.solver.Occurrence;
import ptidej.solver.fingerprint.Rule;

public final class CompositeQuickUml extends Primitive {
	private static Occurrence[] BuiltSolutions;
	private static Occurrence[] BuiltSolutionsNoRule;

	public CompositeQuickUml(final String name) {
		super(name);
	}
	protected void setUp() {
		Primitive.initialize("QuickUML");

		if (CompositeQuickUml.BuiltSolutions == null) {
			CompositeQuickUml.BuiltSolutions =
				Primitive.automaticSolve(
					ptidej.solver.fingerprint.problem.CompositeMotif.class,
					"../QuickUML 2001/bin/",
					"QuickUML",
					Rule.C_LEAF_ROLE_1);
		}
		if (CompositeQuickUml.BuiltSolutionsNoRule == null) {
			CompositeQuickUml.BuiltSolutionsNoRule =
				Primitive.automaticSolve(
					ptidej.solver.problem.CompositeMotif.class,
					"../QuickUML 2001/bin/",
					"QuickUML");
		}
	}
	public void testNumberSolution() {
		Assert
			.assertTrue(
				"Less solution with rules.",
				CompositeQuickUml.BuiltSolutions.length < CompositeQuickUml.BuiltSolutionsNoRule.length);
	}
}
