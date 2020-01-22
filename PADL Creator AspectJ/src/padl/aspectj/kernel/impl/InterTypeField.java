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
package padl.aspectj.kernel.impl;

import padl.aspectj.kernel.IInterTypeField;
import padl.kernel.IFirstClassEntity;
import padl.kernel.impl.Field;

/**
 * @author Jean-Yves Guyomarc'h
 */
public class InterTypeField extends Field implements IInterTypeField {
	private static final long serialVersionUID = 3041336627789230817L;

	private IFirstClassEntity target;

	public InterTypeField(final char[] anID) {
		super(anID);
		this.target = null;
	}

	public InterTypeField(
		final char[] anID,
		final char[] aFieldType,
		final int aCardinality) {

		super(anID, anID, aFieldType, aCardinality);
		this.target = null;
	}

	public IFirstClassEntity getTargetEntity() {
		return this.target;
	}

	public void setTargetEntity(final IFirstClassEntity anEntity) {
		this.target = anEntity;

	}
}
