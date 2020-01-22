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
package ptidej.solver.test.data.pattern;

import padl.kernel.IClass;
import padl.kernel.IComposition;
import padl.motif.models.TestMotifModel;

public final class BadCompositionTest extends TestMotifModel {
	public static final char[] AGGREGATE = "Aggregate".toCharArray();
	public static final char[] AGGREGATED = "Aggregated".toCharArray();
	private static final char[] BAD_COMPOSITION_TEST = "BadCompositionTest"
		.toCharArray();
	private static final char[] COMPOSITION = "composition".toCharArray();
	private static final long serialVersionUID = -8188921942712025544L;

	public BadCompositionTest() {
		final IClass anAggregateClass =
			this.getFactory().createClass(
				BadCompositionTest.AGGREGATE,
				BadCompositionTest.AGGREGATE);
		final IClass anAggregatedClass =
			this.getFactory().createClass(
				BadCompositionTest.AGGREGATED,
				BadCompositionTest.AGGREGATED);
		final IComposition aComposition =
			this.getFactory().createCompositionRelationship(
				BadCompositionTest.COMPOSITION,
				anAggregatedClass,
				2);

		anAggregateClass.addConstituent(aComposition);

		this.addConstituent(anAggregateClass);
		this.addConstituent(anAggregatedClass);
	}
	public char[] getName() {
		return BadCompositionTest.BAD_COMPOSITION_TEST;
	}
}
