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
package caffeine.analysis.toplevelfields;

import caffeine.Caffeine;
import caffeine.Constants;

public final class NoTopLevelFieldsCaffeineLauncher {
	public static void main(final String[] args) {
		Caffeine
			.getUniqueInstance()
			.start(
				"../Caffeine/Rules/AACTrace.pl",
				"../Caffeine/javassist.jar;../Caffeine/cfparse.jar;../Caffeine",
				"caffeine.analysis.toplevelfields.TopLevelFields",
				new String[] { "caffeine.analysis.toplevelfields.*", },
				Constants.GENERATE_FIELD_MODIFICATION_EVENT
					| Constants.GENERATE_FINALIZER_EXIT_EVENT,
				new String[][] {
					new String[] {
						"caffeine.analysis.toplevelfields.TopLevelFields",
						"caffeine.analysis.toplevelfields.ContainedObject",
						"object1" },
					new String[] {
						"caffeine.analysis.toplevelfields.TopLevelFields",
						"caffeine.analysis.toplevelfields.ContainedObject",
						"object2" }
		});
	}
}
