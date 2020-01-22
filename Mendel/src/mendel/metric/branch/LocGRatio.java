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
package mendel.metric.branch;

import mendel.metric.DispatchMetric;
import mendel.model.JClassEntity;
import mendel.model.JClassEntity.Group;

public class LocGRatio extends DispatchMetric {

	public String getName() {
		return "LocG Ratio";
	}

	@Override
	public String compute(JClassEntity entity) {
		int sup = entity.getSuperClass().count(Group.LOCAL);
		int loc = entity.count(Group.LOCAL);
		return (sup==0)? "0": new Float( loc / (float) sup ).toString();
	}

}
