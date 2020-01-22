/*******************************************************************************
 * Copyright (c) 2002-2014 Yann-Ga�l Gu�h�neuc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Yann-Ga�l Gu�h�neuc and others, see in file; API and its implementation
 ******************************************************************************/
package caffeine.example.gcd.ui.extension;

import caffeine.Caffeine;
import caffeine.Constants;

/**
 * @version 	0.1
 * @author		Yann-Ga�l Gu�h�neuc
 */
public final class CaffeineLauncher {
	public static void main(final String[] args) {
		Caffeine.getUniqueInstance().start(
			"../Caffeine/Rules/OnlyStaticEvents.pl",
			"../Caffeine/cfparse.jar;../Caffeine/javassist.jar;../Caffeine/bin;../Caffeine Examples/bin",
			"caffeine.example.gcd.ui.extension.ExtendedGCDRobot",
			new String[] { "caffeine.example.gcd.ui.extension.*" },
			Constants.GENERATE_FINALIZER_ENTRY_EVENT
				| Constants.GENERATE_FINALIZER_EXIT_EVENT
				| Constants.GENERATE_METHOD_ENTRY_EVENT
				| Constants.GENERATE_METHOD_EXIT_EVENT
				| Constants.GENERATE_PROGRAM_END_EVENT);
	}
}