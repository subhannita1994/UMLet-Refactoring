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
import padl.creator.cppfile.eclipse.misc.EclipseCPPParserCaller;
import padl.kernel.ICodeLevelModel;

public class Simple2Test extends TestCase {
	public Simple2Test(String name) {
		super(name);
	}
	public void test1() {
		final ICodeLevelModel codeLevelModel =
			EclipseCPPParserCaller.getInstance().getCodeLevelModelUsingOSGiEmbedded(
				"../PADL Creator C++ (Eclipse) Tests/data/Simple2/");
		Assert.assertNotNull("The code-level model is null!", codeLevelModel);
		Assert.assertEquals(9, codeLevelModel.getNumberOfTopLevelEntities());
	}
}
