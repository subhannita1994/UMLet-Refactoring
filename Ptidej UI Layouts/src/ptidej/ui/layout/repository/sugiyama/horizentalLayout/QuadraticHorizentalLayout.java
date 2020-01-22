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
/**
 * 
 */
package ptidej.ui.layout.repository.sugiyama.horizentalLayout;

import ptidej.ui.layout.repository.sugiyama.graph.Node;

/**
 * @author mohamedkahla
 * date 14-06-2006
 *
 */
public final class QuadraticHorizentalLayout extends HorizentalLayout {

	/**
	 * The quadratic programming layout method
	 */
	public QuadraticHorizentalLayout(Node[][] aMatrix) {
		super(aMatrix); // HorizentalLayout constructor

	}

	// quadratic programming
	public void doHorizentalLayout() {
		if (super.nbLevels > 1) {
			buildBinaryGraphModel();
		}

		final QuadraticHorizentalSolver solver =
			new QuadraticHorizentalSolver(
				super.binaryGraphModel,
				super.matrix,
				super.nbLevels);

		solver.solveQuadratic();

	}

	/**
	 * 
	 */
	public double[][] getVertexGlobalOrdring() {
		return super.solver.getVertexGlobalOrdring();
	}

}
