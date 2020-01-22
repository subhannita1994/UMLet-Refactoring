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
import mendel.model.SetOps;
import mendel.part.tool.AbstractPrototypeRecTool;
import mendel.part.tool.MajorityPrototypeRecTool;

public class JaccardDistance extends DispatchMetric {

	private AbstractPrototypeRecTool tool;
	
	public JaccardDistance() {
		this.tool = new MajorityPrototypeRecTool();
	}
	
	public String compute(JClassEntity entity) {
		Prototype prototype = this.tool.buildPrototype(entity.getSuperClass());
		float jaccardDistance = SetOps.jaccardDistance(entity.getLocalMethods(), prototype.getInterface());		
		return new Float( jaccardDistance ).toString();
	}

	public String getName() {
		return "Jaccard";
	}

}
