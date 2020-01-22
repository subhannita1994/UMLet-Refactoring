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
package padl.test.remove;

import junit.framework.Assert;
import junit.framework.TestCase;
import padl.motif.IDesignMotifModel;
import padl.motif.repository.Composite;

public final class Remove extends TestCase {
	private IDesignMotifModel compositePattern;
	private IDesignMotifModel clonedCompositePattern2;
	public Remove(String name) {
		super(name);
	}
	protected void setUp() throws CloneNotSupportedException {
		this.compositePattern = new Composite();

		this.clonedCompositePattern2 =
			(IDesignMotifModel) this.compositePattern.clone();
		this.clonedCompositePattern2.removeConstituentFromID("Composite"
			.toCharArray());
	}
	public void test2() {
		Assert.assertEquals(
			"Number of entities",
			2,
			this.clonedCompositePattern2.getNumberOfConstituents());
	}
}
