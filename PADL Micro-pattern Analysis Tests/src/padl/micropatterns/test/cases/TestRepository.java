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
package padl.micropatterns.test.cases;

import junit.framework.Assert;
import junit.framework.TestCase;
import padl.micropattern.MicroPatternDetectionsRepository;

public class TestRepository extends TestCase {
	public TestRepository(final String name) {
		super(name);
	}
	public final void testGetInstance() {
		Assert.assertNotNull(MicroPatternDetectionsRepository.getInstance());
	}
	public final void testListOfMicroPatternDetections() {
		Assert.assertEquals(
			"Number of micro-pattern detectors",
			27,
			MicroPatternDetectionsRepository
				.getInstance()
				.getMicroPatternDetections().length);
	}
}
