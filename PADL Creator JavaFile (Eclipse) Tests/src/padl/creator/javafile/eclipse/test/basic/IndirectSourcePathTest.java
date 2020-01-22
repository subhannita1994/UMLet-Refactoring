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
package padl.creator.javafile.eclipse.test.basic;

import junit.framework.Assert;
import junit.framework.TestCase;
import padl.creator.javafile.eclipse.test.util.Utils;
import padl.creator.javafile.eclipse.util.PadlParserUtil;
import padl.creator.javafile.eclipse.visitor.PADLPrinterVisitor;
import padl.kernel.Constants;
import padl.kernel.ICodeLevelModel;
import padl.kernel.IPackage;
import padl.kernel.impl.Factory;

public class IndirectSourcePathTest extends TestCase {

	public IndirectSourcePathTest(final String aName) {
		super(aName);
	}

	public void testSourcePathIndirect() {
		final String javaFilesFolderPath = "../Java Parser/";
		final String javaFilesFolderPath1 = "../Java Parser/src/";
		final String classPathEntry = "";

		//Model from source code

		final ICodeLevelModel padlModelFromJavaFiles =
			Utils.createLightJavaFilesPadlModel(
				"",
				javaFilesFolderPath,
				classPathEntry);

		final ICodeLevelModel padlModelFromJavaFiles1 =
			Utils.createLightJavaFilesPadlModel(
				"",
				javaFilesFolderPath1,
				classPathEntry);

		//	padlModelFromJavaFiles.walk(new PadlPrinterVisitor());

		//	padlModelFromJavaFiles.walk(new ModelComparator(padlModelFromJavaFiles));

		Assert.assertNotNull(padlModelFromJavaFiles);
		Assert.assertNotNull(padlModelFromJavaFiles1);
	}

	public void testSourcePathIndirect1() {
		final String sourcePath = "WHATEVER";
		final String[] javaFiles =
			new String[] {
					"../PADL Creator JavaFile (Eclipse) Tests/rsc/PADL testdata/padl/example/packaje/",
					"../PADL Creator JavaFile (Eclipse) Tests/rsc/Aminata testdata/MyDefaultClass.java" };
		final String classPathEntry = "";

		ICodeLevelModel model = Factory.getInstance().createCodeLevelModel("");
		model =
			Utils.createLightJavaFilesPadlModel(
				"",
				sourcePath,
				classPathEntry,
				javaFiles);

		final IPackage packaje1 = PadlParserUtil.getPackage("padl", model);
		final IPackage packaje2 =
			PadlParserUtil.getPackage("padl.example", model);
		final IPackage packaje3 =
			PadlParserUtil.getPackage("padl.example.packaje", model);
		final IPackage packaje4 =
			PadlParserUtil.getPackage("padl.example.packaje.toto", model);
		final IPackage packajeDefault = PadlParserUtil.getPackage("", model);

		model.walk(new PADLPrinterVisitor());

		Assert.assertNotNull(packaje1);
		Assert.assertNotNull(packaje2);
		Assert.assertNotNull(packaje3);
		Assert.assertNull(packaje4);
		Assert.assertNotNull(packajeDefault);
		Assert.assertEquals(
			packajeDefault.getID(),
			Constants.DEFAULT_PACKAGE_ID);
	}

}
