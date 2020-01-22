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
package padl.creator.aolfile.misc;

import java.io.File;
import padl.creator.aolfile.jlex.JLex;

/**
 * @author Yann-Ga�l Gu�h�neuc
 */
public class GenerateAOLLexer {
	public static void main(final String[] args) throws Exception {
		JLex.main(new String[] { "rsc/AOL.lex" });

		final File previousLexer =
			new File("src/padl/creator/parser/AOLLexer.java");
		previousLexer.delete();
		final File generatedFile = new File("rsc/AOL.lex.java");
		generatedFile.renameTo(previousLexer);
	}
}
