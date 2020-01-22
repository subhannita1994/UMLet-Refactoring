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
package padl.kernel.cpp.antlr;

import padl.kernel.IFirstClassEntity;

/**
 * @author floresvw
 * @since 2004/08/10
 */

//Ward 2004/08/19: Hierarchy level
//For now IStructure is created at the same level as IClass.
//So, it has all the inheritance properties.
//Implementation to be checked ...

public interface IStructure extends IFirstClassEntity {
	String LOGO = "\"S\"";

	boolean isForceAbstract();
	void setAbstract(final boolean aBoolean);
	void setVisibility(final int aVisibility);
}
