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
package ptidej.solver.branching;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import ptidej.solver.Branching;
import ptidej.solver.Problem;
import ptidej.solver.Variable;
import util.io.ProxyConsole;
import util.multilingual.MultilingualManager;
import choco.ContradictionException;
import choco.integer.IntVar;
import choco.integer.var.IntDomain;
import choco.palm.explain.Explanation;
import choco.palm.integer.PalmIntDomain;
import choco.palm.prop.PalmEngine;
import choco.util.IntIterator;

/**
 * Writen in CLAIRE by
 * @author Yann-Ga�l Gu�h�neuc
 * Translated and adapted from CLAIRE version to JAVA by
 * @author Iyadh Sidhom
 * @author Salim Bensemmane
 * @author Fay�al Skhiri
 */
public class InteractiveBranching extends Branching {
	public Object selectBranchingObject() {
		Problem pb = (Problem) this.extender.getManager().getProblem();
		List allEntities = pb.getAllEntities();
		PalmEngine pe = (PalmEngine) pb.getPropagationEngine();

		String outputText = "";

		this.setSolutionNumber(this.getSolutionNumber() + 1);
		System.out.print(MultilingualManager.getString(
			"SOL",
			InteractiveBranching.class,
			new Object[] { new Integer(this.getSolutionNumber()) }));

		IntVar[] vars = pb.getVars();
		for (int i = 0; i < vars.length; ++i) {
			IntVar var = vars[i];
			if (var.isInstantiated()) {

				outputText =
					outputText + "\t" + var.toString() + " = "
							+ allEntities.get(var.getValue()).toString() + "  ";
			}
			else {
				int nbROTW = 0;
				IntDomain domain = var.getDomain();
				IntIterator itr = domain.getIterator();
				while (itr.hasNext()) {
					int value = itr.next();
					nbROTW = nbROTW++;
					outputText =
						outputText + "\t" + var.toString() + "-" + nbROTW
								+ " = " + allEntities.get(value).toString()
								+ "\n";
				}

			}

		}

		System.out.println(outputText + "\n");
		System.out.println(MultilingualManager.getString(
			"ANOTHER_SOL",
			InteractiveBranching.class));
		BufferedReader keyBoard =
			new BufferedReader(new InputStreamReader(System.in));
		String choix = null;
		try {
			choix = keyBoard.readLine();
		}
		catch (IOException e) {
			e.printStackTrace(ProxyConsole.getInstance().errorOutput());
		}

		if (choix.equals("n")) {
			try {
				pe.raiseSystemContradiction();
			}
			catch (ContradictionException e2) {

				System.exit(0);

			}
		}
		else {
			Explanation expl = pb.makeExplanation();
			IntVar lastVar = vars[vars.length - 1];
			//equivalent de la methode getExplanation(pb.vars,expl) In claire Version
			for (int i = 0; i < vars.length; ++i)
				((Variable) vars[i]).self_explain(PalmIntDomain.DOM, expl);

			//POSTING DECISION CONSTRAINT TO lastVAR
			IntDomain domain = lastVar.getDomain();
			if (domain.getSize() != 0) {
				IntIterator itr = domain.getIterator();
				while (itr.hasNext()) {
					int value = itr.next();
					pb.post(pb.neq(lastVar, value), expl);

				}
			}
			else
				pb.post(pb.neq(lastVar, lastVar.getValue()), expl);

			//THEN WE PROPAGATE THE PROBLEM AGAIN
			try {
				pb.propagate();
			}
			catch (final ContradictionException e1) {
				e1.printStackTrace(ProxyConsole.getInstance().errorOutput());
			}

		}
		return new Object();
	}
}
