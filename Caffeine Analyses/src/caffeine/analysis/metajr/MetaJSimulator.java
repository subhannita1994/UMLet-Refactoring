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
package caffeine.analysis.metajr;

import caffeine.simulator.Caffeine;

/**
 * @version 0.2
 * @author	Yann-Ga�l Gu�h�neuc
 */
public class MetaJSimulator {
	public static void main(final String[] args) {
		Caffeine.getUniqueInstance().run("Rules/Composition.pl",
		// "Caffeine/Analysis/MetaJ/BaseInstance-Instance.trace");
		"Caffeine/Analysis/MetaJ/Original.Multiple.trace");
	}
}
