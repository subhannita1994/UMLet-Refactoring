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
package ptidej.solver.solver;

import java.util.ArrayList;
import ptidej.solver.AssignVariable;
import ptidej.solver.Problem;
import ptidej.solver.Solver;
import ptidej.solver.branching.AutomaticBranching;
import ptidej.solver.repair.AutomaticRepair;

/**
 * Writen in CLAIRE by
 * @author Yann-Ga�l Gu�h�neuc
 * Translated and adapted from CLAIRE version to JAVA by
 * @author Iyadh Sidhom
 * @author Salim Bensemmane
 * @author Fay�al Skhiri
 */
public class AutomaticSolver extends Solver {
	public AutomaticSolver(final Problem pb) {
		super(pb);

		final ArrayList list = new ArrayList();
		list.add(new AssignVariable());
		list.add(new AutomaticBranching());

		// Attach branching to solver.
		this.attachPalmBranchings(list);

		// Attach the repair to solver.
		this.attachPalmRepair(new AutomaticRepair(pb));

	}
}
