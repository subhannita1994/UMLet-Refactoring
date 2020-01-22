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
package epi.test.data;

/**
 * @author Yann-Ga�l Gu�h�neuc
 * @since  08/01/15
 */
public final class Command implements BitVectorPattern {
	private static Command UniqueInstance;
	public static Command getInstance() {
		if (Command.UniqueInstance == null) {
			Command.UniqueInstance = new Command();
		}
		return Command.UniqueInstance;
	}

	private static final String COMMAND_NAME = "Command";
	private static final String COMMAND_STRING_NONE =
		"Invoker aggregation EPI_Abstract_Command inheritance ConcreteCommand association Receiver ignorance Invoker ignorance ConcreteCommand dummyRelationship Receiver ignorance EPI_Abstract_Command dummyRelationship Receiver ignorance ConcreteCommand";
	private static final String COMMAND_STRING_1 =
		"Invoker aggregation EPI_Abstract_Command inheritance ConcreteCommand association Receiver ignorance Invoker ignorance ConcreteCommand dummyRelationship Receiver ignorance EPI_Abstract_Command dummyRelationship Receiver ignorance ConcreteCommand";
	private static final String COMMAND_STRING_2 =
		"Invoker aggregation EPI_Abstract_Command inheritance ConcreteCommand association Receiver ignorance Invoker ignorance ConcreteCommand dummyRelationship Receiver ignorance EPI_Abstract_Command dummyRelationship Receiver ignorance ConcreteCommand";
	private static final String COMMAND_STRING_3 =
		"Invoker aggregation Command inheritance ConcreteCommand association Receiver ignorance Invoker ignorance ConcreteCommand dummyRelationship Receiver ignorance Command dummyRelationship Receiver ignorance ConcreteCommand";
	private static final String COMMAND_STRING_4 = "";
	// Same as NONE because all roles must be played.
	private static final String COMMAND_STRING_1_AND_2 =
		"Invoker aggregation EPI_Abstract_Command inheritance ConcreteCommand association Receiver ignorance Invoker ignorance ConcreteCommand dummyRelationship Receiver ignorance EPI_Abstract_Command dummyRelationship Receiver ignorance ConcreteCommand";
	private static final String COMMAND_STRING_1_AND_3 =
		"Invoker aggregation Command inheritance ConcreteCommand association Receiver ignorance Invoker ignorance ConcreteCommand dummyRelationship Receiver ignorance Command dummyRelationship Receiver ignorance ConcreteCommand";
	private static final String COMMAND_STRING_1_AND_4 = ""; // Same as 1
	private static final String COMMAND_STRING_2_AND_3 =
		"Invoker aggregation Command inheritance ConcreteCommand association Receiver ignorance Invoker ignorance ConcreteCommand dummyRelationship Receiver ignorance Command dummyRelationship Receiver ignorance ConcreteCommand";
	private static final String COMMAND_STRING_2_AND_4 = "";
	// Same as 2 and 4
	private static final String COMMAND_STRING_3_AND_4 = "";
	// Same as 3 and 4
	private static final String COMMAND_STRING_1_AND_2_AND_3 =
		"Invoker aggregation Command inheritance ConcreteCommand association Receiver ignorance Invoker ignorance ConcreteCommand dummyRelationship Receiver ignorance Command dummyRelationship Receiver ignorance ConcreteCommand";
	private static final String COMMAND_STRING_ALL = "";
	// Same as 1 and 2 and 3
	private static final String[] COMMAND_STRINGS =
		new String[] {
			COMMAND_STRING_NONE,
			COMMAND_STRING_1,
			COMMAND_STRING_2,
			COMMAND_STRING_3,
			COMMAND_STRING_4,
			COMMAND_STRING_1_AND_2,
			COMMAND_STRING_1_AND_3,
			COMMAND_STRING_1_AND_4,
			COMMAND_STRING_2_AND_3,
			COMMAND_STRING_2_AND_4,
			COMMAND_STRING_3_AND_4,
			COMMAND_STRING_1_AND_2_AND_3,
			COMMAND_STRING_ALL };

	public String getName(){
		return COMMAND_NAME;
	}
	public String[] getStrings(){
		return COMMAND_STRINGS;
	}
}
