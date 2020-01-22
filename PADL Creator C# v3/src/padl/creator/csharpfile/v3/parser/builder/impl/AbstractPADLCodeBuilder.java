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
package padl.creator.csharpfile.v3.parser.builder.impl;

import padl.creator.csharpfile.v3.parser.CSharpParser;
import padl.creator.csharpfile.v3.parser.builder.BuilderContext;
import padl.creator.csharpfile.v3.parser.builder.CodeBuilder;
import padl.kernel.IFactory;
import padl.kernel.impl.Factory;

/**
 *
 */
public abstract class AbstractPADLCodeBuilder implements CodeBuilder {

	protected final static int[] PREDEFINED_TYPES =
		new int[] { CSharpParser.BOOL, CSharpParser.BYTE,
				CSharpParser.CHAR, CSharpParser.DECIMAL, CSharpParser.DOUBLE,
				CSharpParser.FLOAT, CSharpParser.INT, CSharpParser.LONG,
				CSharpParser.OBJECT, CSharpParser.SBYTE, CSharpParser.SHORT,
				CSharpParser.STRING, CSharpParser.UINT, CSharpParser.ULONG,
				CSharpParser.USHORT };
	protected final static int[] MODIFIERS = new int[] {
			CSharpParser.PUBLIC, CSharpParser.PRIVATE, CSharpParser.PROTECTED };
	protected final static int[] RETURN_TYPE = new int[] {
			CSharpParser.IDENTIFIER, CSharpParser.VOID, CSharpParser.BOOL,
			CSharpParser.BYTE, CSharpParser.CHAR, CSharpParser.DECIMAL,
			CSharpParser.DOUBLE, CSharpParser.FLOAT, CSharpParser.INT,
			CSharpParser.LONG, CSharpParser.OBJECT, CSharpParser.SBYTE,
			CSharpParser.SHORT, CSharpParser.STRING, CSharpParser.UINT,
			CSharpParser.ULONG, CSharpParser.USHORT };

	protected final IFactory factory = Factory.getInstance();

	private final CodeBuilder parent;
	protected boolean isOpened = false;
	protected int blockCountOnCreate = -1;
	protected final static int[] IDENTIFIER_OR_CLOSING_BLOCK =
		new int[] { CSharpParser.IDENTIFIER,
				CSharpParser.CLOSE_BRACKET };

	protected AbstractPADLCodeBuilder(final CodeBuilder parent) {
		this.parent = parent;
	}
	
	public CodeBuilder getParent() {
		return this.parent;
	}

	public boolean isReadyToClose(final BuilderContext context) {
		return this.blockCountOnCreate == context.getBlockCount();
	}

	public void open(final BuilderContext context) {
		if (!this.isOpened) {
			this.blockCountOnCreate = context.getBlockCount();
			this.isOpened = true;
		}
	}

}
