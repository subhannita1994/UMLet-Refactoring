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
package ptidej.solver.test.claire.approximate;

import junit.framework.Assert;
import padl.motif.IDesignMotifModel;
import ptidej.solver.Occurrence;
import ptidej.solver.OccurrenceGenerator;
import ptidej.solver.test.claire.Primitive;

public final class Composite extends Primitive {
	public Composite(final String name) {
		super(name);
	}

	public void testCompositeDesignPattern1() throws IllegalAccessException,
			InstantiationException {

		final Occurrence[] builtSolutions =
			this.testDesignPattern(
				Composite.class,
				Primitive.CANONICAL_SOLUTIONS,
				((IDesignMotifModel) padl.motif.repository.Composite.class
					.newInstance()).getName(),
				padl.motif.repository.Composite.class,
				OccurrenceGenerator.SOLVER_AUTOMATIC,
				OccurrenceGenerator.PROBLEM_CUSTOM);

		Assert.assertEquals("Number of solutions", 2, builtSolutions.length);

		Assert.assertEquals(
			"Percentage of the solution with all constraints",
			100,
			builtSolutions[0].getConfidence());
		Assert.assertEquals(
			"Component is the component",
			"Component",
			builtSolutions[0].getComponent(
				padl.motif.repository.Composite.COMPONENT).getDisplayValue());
		Assert.assertEquals(
			"Composite is the composite",
			"Composite",
			builtSolutions[0].getComponent(
				padl.motif.repository.Composite.COMPOSITE).getDisplayValue());
		Assert.assertEquals("Leaf is the leaf", "Leaf", builtSolutions[0]
			.getComponent(padl.motif.repository.Composite.LEAF)
			.getDisplayValue());

		Assert.assertEquals(
			"Solution with all constraints",
			50,
			builtSolutions[1].getConfidence());
		Assert.assertEquals(
			"Component is the component",
			String.valueOf(padl.motif.repository.Composite.COMPONENT),
			builtSolutions[1].getComponent(
				padl.motif.repository.Composite.COMPONENT).getDisplayValue());
		Assert.assertEquals(
			"Leaf is the composite",
			String.valueOf(padl.motif.repository.Composite.LEAF),
			builtSolutions[1].getComponent(
				padl.motif.repository.Composite.COMPOSITE).getDisplayValue());
		Assert.assertEquals(
			"Composite is the leaf",
			String.valueOf(padl.motif.repository.Composite.COMPOSITE),
			builtSolutions[1]
				.getComponent(padl.motif.repository.Composite.LEAF)
				.getDisplayValue());
	}

	public void testCompositeDesignPattern2() throws IllegalAccessException,
			InstantiationException {

		final Occurrence[] builtSolutions =
			this.testDesignPattern(
				Composite.class,
				Primitive.ALL_SOLUTIONS,
				((IDesignMotifModel) padl.motif.repository.Composite.class
					.newInstance()).getName(),
				padl.motif.repository.Composite.class,
				OccurrenceGenerator.SOLVER_AUTOMATIC,
				OccurrenceGenerator.PROBLEM_AC4);

		Assert.assertEquals("Number of solutions", 1, builtSolutions.length);

		Assert.assertEquals(
			"Solution with all constraints",
			100,
			builtSolutions[0].getConfidence());

		Assert.assertEquals(
			"Component is the component",
			String.valueOf(padl.motif.repository.Composite.COMPONENT),
			builtSolutions[0].getComponent(
				padl.motif.repository.Composite.COMPONENT).getDisplayValue());
		Assert.assertEquals(
			"Composite is the composite",
			String.valueOf(padl.motif.repository.Composite.COMPOSITE),
			builtSolutions[0].getComponent(
				padl.motif.repository.Composite.COMPOSITE).getDisplayValue());
		Assert.assertEquals("Leaf is the leaf", String
			.valueOf(padl.motif.repository.Composite.LEAF), builtSolutions[0]
			.getComponent(padl.motif.repository.Composite.LEAF)
			.getDisplayValue());
	}
}
