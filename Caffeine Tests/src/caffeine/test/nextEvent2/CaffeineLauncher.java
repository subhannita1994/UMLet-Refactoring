/*
 * (c) Copyright 2001 Yann-Ga�l Gu�h�neuc,
 * Ecole des Mines de Nantes and Object Technology Internationl, Inc.
 * 
 * Use and copying of this software and preparation of derivative works
 * based upon this software are permitted. Any copy of this software or
 * of any derivative work must include the above copyright notice of
 * Yann-Ga�l Gu�h�neuc, this paragraph and the one after it.
 * 
 * This software is made available AS IS, and THE AUTHOR DISCLAIMS
 * ALL WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE, AND NOT WITHSTANDING ANY OTHER PROVISION CONTAINED HEREIN, ANY
 * LIABILITY FOR DAMAGES RESULTING FROM THE SOFTWARE OR ITS USE IS
 * EXPRESSLY DISCLAIMED, WHETHER ARISING IN CONTRACT, TORT (INCLUDING
 * NEGLIGENCE) OR STRICT LIABILITY, EVEN IF YANN-GAEL GUEHENEUC IS ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * All Rights Reserved.
 */
package caffeine.test.nextEvent2;

import caffeine.Caffeine;
import caffeine.Constants;
import caffeine.test.Primitive;

/**
 * @author	Yann-Ga�l Gu�h�neuc
 * @version 0.1
 */
public final class CaffeineLauncher extends Primitive {
	public CaffeineLauncher(final String name) {
		super(name);
	}
	public void test() {
		try {
			Caffeine
				.getUniqueInstance()
				.start(
				// "../Caffeine Tests/src/caffeine/test/nextEvent2/Test.trace",
					"../Caffeine Tests/src/caffeine/test/nextEvent2/SomeEvents.pl",
					"../Caffeine/cfparse.jar;../Caffeine/javassist.jar;../Caffeine/bin;../Caffeine Tests/bin",
					"caffeine.test.nextEvent2.SimpleExample",
					new String[] { "caffeine.test.nextEvent2.*" },
					Constants.ANALYSIS_CONTROLLED_EVENTS,
					new String[][] { new String[] {
							"caffeine.test.nextEvent2.HelloWorld",
							"java.lang.String", "greetings" } });
		}
		catch (final Exception e) {
			this.setProgramTerminated(true);
			this.setThrownException(e);
			throw new RuntimeException(e);
		}
	}
}