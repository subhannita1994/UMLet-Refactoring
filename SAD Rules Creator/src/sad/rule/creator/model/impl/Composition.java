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
package sad.rule.creator.model.impl;

import sad.rule.creator.model.IComposition;
import sad.rule.creator.model.IConstituent;

/**
 * @author Pierre Leduc
 */
public class Composition extends Relationship implements IComposition {

	public Composition(
		final String anID,
		final IConstituent aSourceConstituent,
		final IConstituent aTargetConstituent,
		final int aSourceCardinality,
		final int aTargetCardinality) {
		super(
			anID,
			aSourceConstituent,
			aTargetConstituent,
			aSourceCardinality,
			aTargetCardinality);
	}
}
