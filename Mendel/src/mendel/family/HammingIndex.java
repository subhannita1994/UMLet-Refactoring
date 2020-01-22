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
package mendel.family;

import mendel.metric.DispatchMetric;
import mendel.model.JClassEntity;
import mendel.model.JClassEntity.Group;

public class HammingIndex extends DispatchMetric {

	private HammingDistance tool;
	
	public HammingIndex() {
		this.tool = new HammingDistance();
	}
	
	public String compute(JClassEntity entity) {
		Integer distance = new Integer(this.tool.compute(entity));
		float nom = entity.count(Group.LOCAL);
//		float index = Math.max(0, nom - distance) / (nom==0? 1 : nom);
		float index = (nom - distance) / (nom==0? 1 : nom);
		return new Float( index ).toString();
	}

	public String getName() {
		return "Hamming Index";
	}

}
