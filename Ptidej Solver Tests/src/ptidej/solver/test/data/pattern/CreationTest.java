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
package ptidej.solver.test.data.pattern;

import padl.kernel.Constants;
import padl.kernel.IClass;
import padl.kernel.ICreation;
import padl.motif.models.TestMotifModel;

public final class CreationTest extends TestMotifModel {
	public static final char[] CREATED = "Created".toCharArray();
	private static final char[] CREATION_TEST = "CreationTest".toCharArray();
	public static final char[] CREATOR = "Creator".toCharArray();
	private static final char[] MESSAGE = "message".toCharArray();
	private static final long serialVersionUID = 143856099019687459L;

	public CreationTest() {
		final IClass creator =
			this.getFactory().createClass(
				CreationTest.CREATOR,
				CreationTest.CREATOR);
		final IClass created =
			this.getFactory().createClass(
				CreationTest.CREATED,
				CreationTest.CREATED);
		final ICreation creationLink =
			this.getFactory().createCreationRelationship(
				CreationTest.MESSAGE,
				created,
				Constants.CARDINALITY_ONE);
		creator.addConstituent(creationLink);
		this.addConstituent(creator);
		this.addConstituent(created);
	}
	public char[] getName() {
		return CreationTest.CREATION_TEST;
	}
}
