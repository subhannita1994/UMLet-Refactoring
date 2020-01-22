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

/**
 * This version of Novelty metric computes the BranchMeanSet including self.
 * 
 * @author deniersi
 *
 */
public class NoveltyIndexWithSelf extends DispatchMetric {

	private BranchMeanSet meanSet = new BranchMeanSet();
	
//		public String compute(IEntity entity) {
//		boolean flag = entity.recursiveLookup(true);
//		String res = super.compute(entity);
//		entity.recursiveLookup(flag);
//		return res;
//	}

	public String getName() {
		return "Branch Novelty with Self";
	}

	
	@Override
	public String compute(JClassEntity entity) {
		Float mean = new Float(this.meanSet.compute(entity));
		return new Float( entity.count(Group.LOCAL) / mean ).toString();
	}

}
