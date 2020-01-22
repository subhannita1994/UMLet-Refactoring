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
package caffeine.analysis.junit.money;

import caffeine.Caffeine;
import caffeine.Constants;

/**
 * @version 0.2..
 * @author	Yann-Ga�l Gu�h�neuc
 */
public final class CL_MoneyTest_MoneyBag {
	public static void main(final String[] args) {
		Caffeine
			.getUniqueInstance()
			.start(
				"../Caffeine Analyses/src/caffeine/analysis/junit/money/MoneyTest-MoneyBag2.trace",
				"Rules/AllStaticEvents.pl",
				"../Caffeine/cfparse.jar;../Caffeine/javassist.jar;../Caffeine;../JUnit",
				"junit.samples.money.MoneyTest",
				new String[] {
					"junit.samples.money.MoneyTest",
					"junit.samples.money.MoneyBag" },
				Constants.GENERATE_CONSTRUCTOR_ENTRY_EVENT
					| Constants.GENERATE_FIELD_MODIFICATION_EVENT
					| Constants.GENERATE_FINALIZER_EXIT_EVENT
					| Constants.GENERATE_PROGRAM_END_EVENT,
				new String[][] {
					new String[] {
						"junit.samples.money.MoneyTest",
						"junit.samples.money.MoneyBag",
						"fMB1" },
					new String[] {
						"junit.samples.money.MoneyTest",
						"junit.samples.money.MoneyBag",
						"fMB2" }
		});
	}
}
