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

public final class Facade extends Primitive {
	public Facade(final String name) {
		super(name);
	}

	/*
	 * Facade.
	 */
	private void testFacadeDesignPattern(final Occurrence[] builtSolutions) {
		Assert.assertEquals("Number of solutions", 1, builtSolutions.length);

		Assert.assertEquals(
			"One solution with all constraints",
			100,
			builtSolutions[0].getConfidence());

		Assert.assertEquals("Facade is the facade", "Facade", builtSolutions[0]
			.getComponent(padl.motif.repository.Facade.FACADE)
			.getDisplayValue());
		Assert.assertEquals(
			"SubsystemEntity is the subsystem entity",
			"SubsystemEntity",
			builtSolutions[0]
				.getComponent(padl.motif.repository.Facade.SUBSYSTEM_ENTITY)
				.getDisplayValue());
		Assert.assertEquals("Client is the Client", "Client", builtSolutions[0]
			.getComponent(padl.motif.repository.Facade.CLIENT)
			.getDisplayValue());
	}
	public void testFacadeDesignPattern1() throws IllegalAccessException,
			InstantiationException {

		final Occurrence[] builtSolutions =
			this.testDesignPattern(
				Facade.class,
				Primitive.ALL_SOLUTIONS,
				((IDesignMotifModel) padl.motif.repository.Facade.class
					.newInstance()).getName(),
				padl.motif.repository.Facade.class,
				OccurrenceGenerator.SOLVER_AUTOMATIC,
				OccurrenceGenerator.PROBLEM_AC4);

		this.testFacadeDesignPattern(builtSolutions);
	}
	public void testFacadeDesignPattern2() throws IllegalAccessException,
			InstantiationException {

		final Occurrence[] builtSolutions =
			this.testDesignPattern(
				Facade.class,
				Primitive.ALL_SOLUTIONS,
				((IDesignMotifModel) padl.motif.repository.Facade.class
					.newInstance()).getName(),
				padl.motif.repository.Facade.class,
				OccurrenceGenerator.SOLVER_AUTOMATIC,
				OccurrenceGenerator.PROBLEM_CUSTOM);

		this.testFacadeDesignPattern(builtSolutions);
	}

}
