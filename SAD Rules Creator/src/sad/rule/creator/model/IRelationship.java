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
package sad.rule.creator.model;

/**
 * @author Pierre Leduc
 */
public interface IRelationship extends IConstituent {

	public abstract int getSourceCardinality();
	public IConstituent getSourceConstituent();
	public abstract int getTargetCardinality();
	public IConstituent getTargetConstituent();
	public void setSourceConstituent(final IConstituent aConstituent);
	public void setTargetConstituent(final IConstituent aConstituent);
}
