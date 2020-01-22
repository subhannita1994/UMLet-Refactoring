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
/**
 * 
 */
package mendel.filter;

import mendel.model.IEntity;
import mendel.model.JClassEntity;

public class LowLeafFilter extends EntityFilter {
	public boolean accept(IEntity entity) {
		JClassEntity classEntity = (JClassEntity) entity;
		return !(classEntity.inheritanceDepth() < 2 && classEntity.isLeaf());
	}
	public String toString() {
		return "Reject Low Leaves";
	}
}
