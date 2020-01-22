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
package padl.creator.msefile.misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Writer;
import util.io.ProxyDisk;

/**
 * @author Yann-Ga�l Gu�h�neuc
 */
public class GenerateMSEParser {
	public static void main(final String[] args) throws Exception {
		padl.creator.msefile.javacup.Main.main(new String[] { "-parser",
				"MSEParser", "-symbols", "MSESymbols", "-interface",
				"-nonterms", "-progress", "rsc/MSE.cup" });

		final File previousParser = new File("src/padl/creator/MSEParser.java");
		previousParser.delete();
		final File generatedParser = new File("MSEParser.java");
		generatedParser.renameTo(previousParser);

		final File previousSymbols =
			new File("src/padl/creator/MSESymbols.java");
		previousSymbols.delete();
		final File generatedSymbols = new File("MSESymbols.java");
		generatedSymbols.renameTo(previousSymbols);

		/*
		 * Some code to replace reference to "java_cup.runtime"
		 * with "padl.creator.javacup.runtime".
		 */
		final LineNumberReader reader =
			new LineNumberReader(new InputStreamReader(new FileInputStream(
				"src/padl/creator/MSEParser.java")));
		final StringBuffer buffer = new StringBuffer();
		String readLine;
		while ((readLine = reader.readLine()) != null) {
			buffer.append(readLine);
			buffer.append('\n');
		}
		reader.close();

		final String toBeRemovedString = "java_cup.runtime";
		int pos;
		while ((pos = buffer.indexOf(toBeRemovedString)) > 0) {
			buffer.replace(
				pos,
				pos + toBeRemovedString.length(),
				"padl.creator.javacup.runtime");
		}

		final Writer writer =
			ProxyDisk.getInstance().fileAbsoluteOutput(
				"src/padl/creator/MSEParser.java");
		writer.write(buffer.toString());
		writer.close();
	}
}
