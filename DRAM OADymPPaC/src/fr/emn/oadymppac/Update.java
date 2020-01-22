/*
 * $Id: Update.java,v 1.1 2004/03/01 15:37:16 guehene Exp $
 *
 * Copyright (c) 2001-2002 Jean-Daniel Fekete, Mohammad Ghoniem and
 * Ecole des Mines de Nantes.  All rights reserved.
 *
 * This software is proprietary information of Jean-Daniel Fekete and
 * Ecole des Mines de Nantes.  You shall use it only in accordance
 * with the terms of the license agreement you accepted when
 * downloading this software.  The license is available in the file
 * licence.txt and at the following URL:
 * http://www.emn.fr/fekete/oadymppac/licence.html
 */
package fr.emn.oadymppac;

/**
 * This interface exposes the contents of the <code>update</code>
 * element in the trace.
 *
 * @author Jean-Daniel Fekete
 * @author Mohammad Ghoniem
 * @version $Revision: 1.1 $
 */
public interface Update extends SolverConstants {
	/**
	 * Returns the name of the involved constraint.
	 */
	public String getCName();
	/**
	 * Returns the type of update (<code>ground</code>, <code>any</code>,
	 * <code>min</code>, <code>max</code>, <code>minmax</code>,
	 * <code>empty</code>, <code>val</code>, <code>nothing</code>).
	 */
	public short getType();
	/**
	 * Returns the list of values concerned by the current update.
	 */
	public ValueList getValueList();
	/**
	 * Returns the name of the involved variable.
	 */
	public String getVName();
}
