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
package modec.solver.constraint.motif;

import java.io.BufferedWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import metamodel.scenarioDiagram.ScenarioDiagram;
import modec.solver.constraint.Callee;
import modec.solver.constraint.Caller;
import modec.solver.constraint.Created;
import modec.solver.constraint.Creator;
import modec.solver.constraint.IsContainedInMessage;
import modec.solver.constraint.MessageFollows;
import modec.solver.constraint.ParameterCalleeSameType;
import modec.util.ExecutionTraceParser;
import util.io.ProxyDisk;
import choco.integer.IntVar;
import choco.palm.PalmProblem;
import choco.palm.integer.constraints.PalmNotEqualXYC;
import choco.palm.search.PalmSolution;

/**
 * @author Janice Ng
 */
public class Command_QuickUML_v2001_ResizeADiagram {

	public static void main(final String[] args) {

		final PalmProblem problem = new PalmProblem();
		final ExecutionTraceParser etp =
			new ExecutionTraceParser(
				"C:\\Documents and Settings\\ngjanice\\Eclipse\\workspace\\MoDeC Bytecode Instrumentation Tests\\trace\\Evaluation_QuickUML_Command_ResizeADiagram.trace");

		//new ExecutionTraceParser("../MoDeC Bytecode Instrumentation Tests/Evaluation_QuickUML_Command_ResizeADiagram.trace");

		final ScenarioDiagram sd = etp.getScenarioDiagram();
		final List componentsMessages = sd.visitComponentMessages();
		final List allClassifiers = sd.getAllClassifiers(componentsMessages);
		final int nbMessages = sd.countNbMessages(componentsMessages);
		final int nbClassifiers = sd.countNbClassifiers(allClassifiers);

		sd.determineSourceCalledMessages(componentsMessages, allClassifiers);
		sd.determineDestinationCalledMessages(
			componentsMessages,
			allClassifiers);
		sd.determineClassifierIdx(allClassifiers);
		sd.determineIdxClassifier(allClassifiers);
		sd.determineMessageContainer(componentsMessages);

		final List diagramType = new ArrayList();
		diagramType.add("diagram.Diagram");

		final Map subclasses = new HashMap();
		subclasses.put("diagram.Diagram", diagramType);

		final IntVar v_aReceiver =
			problem.makeBoundIntVar("aReceiver", 0, nbClassifiers);
		final IntVar v_aClient =
			problem.makeBoundIntVar("aClient", 0, nbClassifiers);
		final IntVar v_aCommand =
			problem.makeBoundIntVar("aCommand", 0, nbClassifiers);
		final IntVar v_anInvoker =
			problem.makeBoundIntVar("anInvoker", 0, nbClassifiers);
		//				final IntVar v_executeCallee =
		//				problem.makeBoundIntVar("executeCallee", 0, nbClassifiers);
		final IntVar v_newCommand =
			problem.makeBoundIntVar("newCommand", 0, nbMessages);
		//		final IntVar v_storeCommand =
		//			problem.makeBoundIntVar("storeCommand", 0, nbMessages);
		final IntVar v_execute =
			problem.makeBoundIntVar("execute", 0, nbMessages);
		final IntVar v_action_1 =
			problem.makeBoundIntVar("action_1", 0, nbMessages);
		final IntVar v_action_2 =
			problem.makeBoundIntVar("action_2", 0, nbMessages);

		//		problem.post(new PalmNotEqualXYC(v_executeCallee, v_aCommand, 0));
		problem.post(new PalmNotEqualXYC(v_aReceiver, v_aClient, 0));
		problem.post(new PalmNotEqualXYC(v_aClient, v_aCommand, 0));
		problem.post(new PalmNotEqualXYC(v_aCommand, v_anInvoker, 0));
		problem.post(new PalmNotEqualXYC(v_aReceiver, v_aCommand, 0));
		//		problem.post(new PalmNotEqualXYC(v_aReceiver, v_anInvoker, 0));

		//		problem.post(
		//			new MessageFollows(
		//				v_newCommand,
		//				v_storeCommand,
		//				sd,
		//				componentsMessages));
		//		problem.post(
		//			new MessageFollows(
		//				v_storeCommand,
		//				v_execute,
		//				sd,
		//				componentsMessages));
		problem.post(new MessageFollows(
			v_newCommand,
			v_execute,
			sd,
			componentsMessages));
		problem.post(new MessageFollows(
			v_execute,
			v_action_1,
			sd,
			componentsMessages));
		problem.post(new MessageFollows(
			v_action_1,
			v_action_2,
			sd,
			componentsMessages));

		problem.post(new Created(
			v_newCommand,
			v_aCommand,
			sd,
			componentsMessages,
			allClassifiers));
		problem.post(new Creator(
			v_aClient,
			v_newCommand,
			sd,
			componentsMessages,
			allClassifiers));
		//		problem.post(
		//			new Caller(
		//				v_aClient,
		//				v_storeCommand,
		//				sd,
		//				componentsMessages,
		//				allClassifiers));
		//		problem.post(
		//			new Callee(
		//				v_storeCommand,
		//				v_anInvoker,
		//				sd,
		//				componentsMessages,
		//				allClassifiers));
		problem.post(new Caller(
			v_anInvoker,
			v_execute,
			sd,
			componentsMessages,
			allClassifiers));
		problem.post(new Callee(
			v_execute,
			v_aCommand,
			sd,
			componentsMessages,
			allClassifiers));
		problem.post(new Caller(
			v_aCommand,
			v_action_1,
			sd,
			componentsMessages,
			allClassifiers));
		problem.post(new Callee(
			v_action_1,
			v_aClient,
			sd,
			componentsMessages,
			allClassifiers));
		problem.post(new Caller(
			v_aClient,
			v_action_2,
			sd,
			componentsMessages,
			allClassifiers));
		problem.post(new Callee(
			v_action_2,
			v_aReceiver,
			sd,
			componentsMessages,
			allClassifiers));

		problem.post(new ParameterCalleeSameType(
			v_action_1,
			v_action_2,
			sd,
			componentsMessages,
			subclasses));

		problem.post(new IsContainedInMessage(
			v_action_1,
			v_execute,
			sd,
			componentsMessages));

		problem.post(new IsContainedInMessage(
			v_action_2,
			v_action_1,
			sd,
			componentsMessages));

		problem.logger.setLevel(Level.INFO);
		problem.logger.addHandler(new Handler() {

			public void close() throws SecurityException {
			}
			public void flush() {
			}
			public void publish(LogRecord record) {
				if (record.getMessage().equals("A solution was found.")) {
					try {
						final Writer fstream =
							ProxyDisk
								.getInstance()
								.fileAbsoluteOutput(
									"C:/Documents and Settings/ngjanice/Eclipse/workspace/MoDeC Bytecode Instrumentation Tests/Instrumented Classes/QuickUML 2001/Solution_QuickUML_Command_ResizeADiagram.txt",
									true);
						BufferedWriter out = new BufferedWriter(fstream);

						final List solutions =
							problem.getPalmSolver().solutions;
						out.write(solutions.size() + "\n");
						final PalmSolution solution =
							(PalmSolution) solutions.get(solutions.size() - 1);

						out.write("OPERATION [new Command]: "
								+ sd.getIdxMessage(solution.getValue(4))
								+ "OPERATION [execute]: "
								+ sd.getIdxMessage(solution.getValue(5))
								+ "OPERATION [action_1]: "
								+ sd.getIdxMessage(solution.getValue(6))
								+ "OPERATION [action_2]: "
								+ sd.getIdxMessage(solution.getValue(7))
								+ "PARTICIPANT [aReceiver]: "
								+ sd.getIdxClassifier(solution.getValue(0))
								+ "\nPARTICIPANT [aClient]: "
								+ sd.getIdxClassifier(solution.getValue(1))
								+ "\nPARTICIPANT [aCommand]: "
								+ sd.getIdxClassifier(solution.getValue(2))
								+ "\nPARTICIPANT [anInvoker]: "
								+ sd.getIdxClassifier(solution.getValue(3))
								+ "\n\n");
						out.close();
					}
					catch (Exception e) { //Catch exception if any
						System.err
							.println("Error while writing in file : " + e);
					}
				}
			}
		});

		problem.solve(true);
		System.out.println("*** the end : "
				+ problem.getPalmSolver().solutions.size());
	}
}
