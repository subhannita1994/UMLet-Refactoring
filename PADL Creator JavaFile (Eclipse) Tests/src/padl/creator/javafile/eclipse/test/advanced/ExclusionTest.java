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
package padl.creator.javafile.eclipse.test.advanced;

import junit.framework.Assert;
import junit.framework.TestCase;

public class ExclusionTest extends TestCase {

	public ExclusionTest(final String name) {
		super(name);

	}

	// une classe dans une m�thode ou dans une inner classe
	// une classe dans enum
	//rechercher des exemples
	//continuer sur padlutil et faire les diff�rents tests � ce niveau
	//j'esp�re trouver facilement les exemples

	public void test1() {
		Assert.assertTrue(true);
	}
}
