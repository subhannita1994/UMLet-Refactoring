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
package ptidej.viewer.extension.repository.callgraph.model;

/**
 * @author Yann-Ga�l Gu�h�neuc
 * @since  2007/10/06
 */
public interface IVisitor {
	void enter(final Call aCall);
	void exit(final Call aCall);
	void enter(final Method aMethod);
	void exit(final Method aMethod);
	Object getResult();
}
