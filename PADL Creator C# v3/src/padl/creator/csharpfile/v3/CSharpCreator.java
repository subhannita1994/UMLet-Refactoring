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
package padl.creator.csharpfile.v3;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTree;

import padl.creator.csharpfile.v3.parser.CSharpLexer;
import padl.creator.csharpfile.v3.parser.CSharpParser;
import padl.creator.csharpfile.v3.parser.CSharpParserInitialVisitor;
import padl.creator.csharpfile.v3.parser.CSharpParserAdvancedVisitor;
import padl.creator.csharpfile.v3.parser.CSharpParserVisitor;
import padl.generator.helper.ModelGenerator;
import padl.kernel.ICodeLevelModel;
import padl.kernel.ICodeLevelModelCreator;
import padl.kernel.exception.CreationException;
import util.io.ProxyConsole;

/**
 * Facade class that manages the Parsing and Converting of CSharp source code
 * to the PADL model.
 */
public class CSharpCreator implements ICodeLevelModelCreator {
	private final File source;
	
	/**
	 * Parses the given File(s) (should be a C# source file) and return it's modelized version.
	 * Uses the ModelGenerator.
	 * @param source either the File object representing the C# source file or a File object
	 * representing a directory of C# source files.
	 * @return the PADL model of the given C# source(s) file(s).
	 * @throws CreationException 
	 * @throws java.io.IOException
	 */
	public static ICodeLevelModel parse(final String aSourceFileOrDirectory)
			throws CreationException {
		return ModelGenerator.generateModelFromCSharpFiles("C# Model", aSourceFileOrDirectory);
	}
	
	/**
	 * Create the CSharpCreator
	 * @param aSourceFileOrDirectory source file or directory to be created as a String
	 * Will be converted to File
	 */
	public CSharpCreator(final String aSourceFileOrDirectory) {
		this.source = new File(aSourceFileOrDirectory);
	}
	
	/**
	 * Perform 2 passes to create the CSharp model
	 */
	public void create(final ICodeLevelModel aCodeLevelModel)
			throws CreationException {
		try {
			// 1st pass that identifies the Classes and Interfaces
			if (this.source.isDirectory()) {
				for (final File input : this.source.listFiles()) {
					if (!input.isHidden()) {
						this.readFileFirstPass(input, aCodeLevelModel);
					}
				}
			}
			else {
				this.readFileFirstPass(this.source, aCodeLevelModel);
			}
			
			// 2nd pass that detects Interaction between Classes and Interfaces
			if (this.source.isDirectory()) {
				for (final File input : this.source.listFiles()) {
					if (!input.isHidden()) {
						this.readFileSecondPass(input, aCodeLevelModel);
					}
				}
			}
			else {
				this.readFileSecondPass(this.source, aCodeLevelModel);
			}
		}
		catch (final IOException | RecognitionException e) {
			e.printStackTrace(ProxyConsole.getInstance().errorOutput());
			throw new CreationException(e.getMessage());
		}
	}

	/**
	 * Perform the first pass
	 * @param source
	 * @param aCodeLevelModel
	 * @throws IOException
	 * @throws RecognitionException
	 */
	private void readFileFirstPass(final File source, final ICodeLevelModel aCodeLevelModel)
			throws IOException, RecognitionException {
		final CSharpParser parser = this.setUpPass(source, aCodeLevelModel);
		
		final CSharpParserVisitor<?> visitor =
				new CSharpParserInitialVisitor<Object>(aCodeLevelModel);
		
		this.visitParseTree(parser, visitor);
	}
	
	/**
	 * Perform the second pass
	 * @param source
	 * @param aCodeLevelModel
	 * @throws IOException
	 * @throws RecognitionException
	 */
	private void readFileSecondPass(final File source, final ICodeLevelModel aCodeLevelModel)
			throws IOException, RecognitionException {
		final CSharpParser parser = this.setUpPass(source, aCodeLevelModel);
		
		final CSharpParserVisitor<?> visitor =
				new CSharpParserAdvancedVisitor<Object>(aCodeLevelModel);
		
		this.visitParseTree(parser, visitor);
	}
	
	/**
	 * Setup a pass: create the lexer and the parser
	 * @param source
	 * @param aCodeLevelModel
	 * @return
	 * @throws IOException
	 * @throws RecognitionException
	 */
	private CSharpParser setUpPass(final File source, final ICodeLevelModel aCodeLevelModel)
			throws IOException, RecognitionException {
		// sanity check
		if (source == null || !source.exists() || source.isDirectory()) {
			// Yann 2013/05/10: Must let client know that something is wrong!
			throw new IOException("Cannot find C# source files in " + source);
		}

		final InputStream in =
				new FileInputStream(source);
		final CSharpLexer lexer = new CSharpLexer(CharStreams.fromStream(in, StandardCharsets.UTF_8));

		return new CSharpParser(new CommonTokenStream(lexer));
	}
	
	/**
	 * Visit the parse tree generated by the parser
	 * @param parser
	 * @param visitor
	 */
	private void visitParseTree(final CSharpParser parser, final CSharpParserVisitor<?> visitor) {
		final ParseTree tree = parser.compilation_unit();
		
		visitor.visit(tree);
	}
}
