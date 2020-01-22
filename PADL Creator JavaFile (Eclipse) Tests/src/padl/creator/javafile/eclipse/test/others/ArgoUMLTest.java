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
package padl.creator.javafile.eclipse.test.others;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import junit.framework.Assert;
import junit.framework.TestCase;
import padl.creator.javafile.eclipse.test.util.Utils;
import padl.kernel.ICodeLevelModel;
import padl.kernel.IFirstClassEntity;
import util.io.ProxyDisk;

public class ArgoUMLTest extends TestCase {
	public ArgoUMLTest(final String aName) {
		super(aName);
	}
	public void testArgouml() {
		final String sourcePath =
			"../PADL Creator JavaFile (Eclipse) Tests/data/argouml/";
		final String classPathEntry = "";

		final ICodeLevelModel model =
			Utils.createCompleteJavaFilesPadlModel(
				"",
				sourcePath,
				classPathEntry);

		try {
			final Writer writer =
				ProxyDisk.getInstance().fileTempOutput("Result.txt");
			writer.write("Summary for: ");
			writer.write(model.getDisplayName());

			//Print the model by the generator

			writer.write("\nNumber of top-level entities: ");
			writer.write(model.getNumberOfTopLevelEntities());
			final Iterator iter = model.getIteratorOnTopLevelEntities();
			while (iter.hasNext()) {
				final IFirstClassEntity entity =
					(IFirstClassEntity) iter.next();
				writer.write('\n');
				writer.write(entity.getDisplayID());
				writer.write(" ");
				writer.write(entity.getNumberOfConstituents());
				writer.write(" ");
				writer.write(entity.getClass().getName());
			}
			writer.close();
		}
		catch (final IOException e) {
			e.printStackTrace();
		}
		Assert.assertTrue(true);
	}
}
