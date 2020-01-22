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
package padl.creator.cppfile.eclipse.test.simple;

import junit.framework.Assert;
import junit.framework.TestCase;
import padl.generator.helper.ModelGenerator;
import padl.kernel.IIdiomLevelModel;

public class ClassesTest extends TestCase {
	public ClassesTest(String name) {
		super(name);
	}
	public void test1() {
		final IIdiomLevelModel idiomLevelModel =
			ModelGenerator
				.generateModelFromCppFilesUsingEclipse(
					"Funny",
					new String[] { "../PADL Creator C++ (Eclipse) Tests/data/Classes/" });
		Assert.assertNotNull("The idiom-level model is null!", idiomLevelModel);
		Assert.assertEquals(81, idiomLevelModel.getNumberOfTopLevelEntities());
	}
}
