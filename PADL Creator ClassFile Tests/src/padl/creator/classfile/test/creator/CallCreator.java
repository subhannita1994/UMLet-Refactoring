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
package padl.creator.classfile.test.creator;

import java.io.PrintWriter;
import padl.analysis.UnsupportedSourceModelException;
import padl.analysis.repository.AACRelationshipsAnalysis;
import padl.creator.classfile.CompleteClassFileCreator;
import padl.creator.classfile.test.ClassFilePrimitive;
import padl.kernel.ICodeLevelModel;
import padl.kernel.IIdiomLevelModel;
import padl.kernel.exception.CreationException;
import padl.util.ModelStatistics;
import padl.visitor.IWalker;
import util.io.NullWriter;
import util.io.ProxyConsole;

/**
 * @author Yann-Ga�l Gu�h�neuc
 * @since  2007/02/21
 */
public class CallCreator {
	public static void main(final String[] args) {
		final String[] paths =
			new String[] { "../DPL - JHotDraw v5.1/bin/",
					"../DPL - JRefactory v2.6.24/bin/",
					"../DPL - JUnit v3.7/bin/",
					"../DPL - MapperXML v1.9.7/bin/",
					"../DPL - QuickUML 2001/bin/" };

		ProxyConsole.getInstance().setDebugOutput(
			new PrintWriter(new NullWriter()));
		ProxyConsole.getInstance().setNormalOutput(new NullWriter());
		ProxyConsole.getInstance().setErrorOutput(new NullWriter());

		final IWalker walker = new InheritanceImplementationCounter();

		for (int i = 0; i < paths.length; i++) {
			final String path = paths[i];

			try {
				final ModelStatistics modelStatistics = new ModelStatistics();
				final ICodeLevelModel codeLevelModel =
					ClassFilePrimitive.getFactory().createCodeLevelModel("");
				codeLevelModel.addModelListener(modelStatistics);
				codeLevelModel.create(new CompleteClassFileCreator(
					new String[] { path },
					true));

				final IIdiomLevelModel idiomLevelModel =
					(IIdiomLevelModel) new AACRelationshipsAnalysis()
						.invoke(codeLevelModel);

				walker.reset();
				idiomLevelModel.walk(walker);

				System.out.println(path);
				System.out.println(modelStatistics);
				System.out.print("Number of inheritances/implementations: ");
				System.out.println(walker.getResult());
				System.out.println();
			}
			catch (final CreationException e) {
				e.printStackTrace(ProxyConsole.getInstance().errorOutput());
			}
			catch (final UnsupportedSourceModelException e) {
				e.printStackTrace(ProxyConsole.getInstance().errorOutput());
			}
		}
	}
}
