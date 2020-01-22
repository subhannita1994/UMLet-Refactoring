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
package caffeine.analysis.jhotdraw;

import caffeine.Caffeine;

/**
 * @version 0.1
 * @author	Yann-Ga�l Gu�h�neuc
 */
public class CaffeineLauncherForSingleton {
	public static void main(final String[] args) {
		Caffeine.getUniqueInstance().start(
			"../Caffeine/Rules/Singleton.pl",
			"../Caffeine/cfparse.jar;../Caffeine/javassist.jar;../Caffeine/bin;../JHotDraw v5.1/bin/",
			"CH.ifa.draw.samples.javadraw.JavaDrawApp",
			new String[] { "CH.ifa.draw.util.*" },
			null);
	}
}
