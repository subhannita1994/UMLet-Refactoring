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
package sad.detection.generators.old;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import padl.analysis.UnsupportedSourceModelException;
import padl.analysis.repository.AACRelationshipsAnalysis;
import padl.analysis.repository.ModelAnnotatorLOCAnalysis;
import padl.creator.classfile.CompleteClassFileCreator;
import padl.kernel.ICodeLevelModel;
import padl.kernel.IIdiomLevelModel;
import padl.kernel.exception.CreationException;
import padl.kernel.impl.Factory;
import ptidej.solver.Occurrence;
import ptidej.solver.OccurrenceBuilder;
import ptidej.solver.OccurrenceComponent;
import sad.codesmell.detection.ICodeSmellDetection;
import util.io.ProxyConsole;
import util.io.ProxyDisk;
import util.io.ReaderInputStream;

/**
 * @author Yann-Ga�l Gu�h�neuc
 * @since  2007/03/02
 * @author Naouel Moha
 * @since  2008/07/15
 * Modified to extract the name of classes and number of defects 
 * for the association between defects and bugs
 */
public class CodeSmellDetectionCaller {
	private static final String ANTI_PATTERN = "Blob";
	// "FunctionalDecomposition";
	//"SpaghettiCode";
	// "SwissArmyKnife";

	private static final String[] CODE_SMELLS = new String[] {
	//"ControllerClass",
	//"DataClass",
	"LargeClass",
	// "LowCohesion"
		};

	public static void main(final String[] args) {
		final CodeSmellDetectionCaller sadDetection =
			new CodeSmellDetectionCaller();
		//	sadDetection.analyseCodeLevelMode(
		//		"../DPL - JHotDraw v5.1/bin/",
		//		"JHotDraw v5.1");
		//		sadDetection.analyseCodeLevelModel(
		//			"../DPL - Xerces v2.7.0 (dom2)/bin/",
		//			"Xerces v2.7.0 (dom2)");
		//	sadDetection.analyseCodeLevelModel(
		//		"../DPL - Rhino v1.6R5/bin/",
		//		"Rhino v1.6R5");
		//	sadDetection.analyseCodeLevelMode(
		//		"../DPL - JUnit v3.7/bin/",
		//		"JUnit v3.7");
		sadDetection.analyseCodeLevelModel(
			"../../P-MARt Workspace/ArgoUML v0.10/",
			"ArgoUML v0.10");
		sadDetection.analyseCodeLevelModel(
			"../../P-MARt Workspace/ArgoUML v0.15.6/",
			"ArgoUML v0.15.6");
		sadDetection.analyseCodeLevelModel(
			"../../P-MARt Workspace/ArgoUML v0.16.1/",
			"ArgoUML v0.16.1");
		sadDetection.analyseCodeLevelModel(
			"../../P-MARt Workspace/ArgoUML v0.17.5/",
			"ArgoUML v0.17.5");
		sadDetection.analyseCodeLevelModel(
			"../../P-MARt Workspace/ArgoUML v0.19.8/",
			"ArgoUML v0.19.8");
	}

	/**
	 * Map<Name of Class ; Number of Defects>
	 */
	private final Map classesNbDefects;

	public CodeSmellDetectionCaller() {
		this.classesNbDefects = new HashMap();
	}

	private void analyseCodeLevelModel(
		final String aClassPath,
		final String aName) {

		System.out.print("Analysing ");
		System.out.print(aName);
		System.out.println("...");

		try {
			final ICodeLevelModel codeLevelModel =
				Factory.getInstance().createCodeLevelModel(aName);
			codeLevelModel.create(new CompleteClassFileCreator(
				new String[] { aClassPath },
				true));
			final ModelAnnotatorLOCAnalysis annotator =
				new ModelAnnotatorLOCAnalysis();
			annotator.annotateFromDirs(
				new String[] { aClassPath },
				codeLevelModel);
			final IIdiomLevelModel idiomLevelModel =
				(IIdiomLevelModel) new AACRelationshipsAnalysis()
					.invoke(codeLevelModel);

			for (int i = 0; i < CodeSmellDetectionCaller.CODE_SMELLS.length; i++) {
				final String codesmellName =
					CodeSmellDetectionCaller.CODE_SMELLS[i];

				final long startTime = System.currentTimeMillis();
				final Class detectionClass =
					Class.forName("sad.codesmell.detection."
							+ CodeSmellDetectionCaller.ANTI_PATTERN + '.'
							+ codesmellName + "Detection");

				//				final ICodeSmellDetection detection =
				//					(ICodeSmellDetection) detectionClass
				//						.getConstructor(
				//							new Class[] { IAbstractLevelModel.class })
				//						.newInstance(new Object[] { idiomLevelModel });
				//						
				final ICodeSmellDetection detection =
					(ICodeSmellDetection) detectionClass.newInstance();
				try {
					detection.detect(idiomLevelModel);
				}
				catch (final RuntimeException e1) {
					e1.printStackTrace();
				}

				final String path =
					"rsc/evolution/DetectionResults in " + aName + " for "
							+ codesmellName + ".ini";
				detection.output(new PrintWriter(ProxyDisk
					.getInstance()
					.fileTempOutput(path)));

				final Properties properties = new Properties();
				properties.load(new ReaderInputStream(new FileReader(path)));
				final OccurrenceBuilder solutionBuilder =
					OccurrenceBuilder.getInstance();
				final Occurrence[] solutions =
					solutionBuilder.getCanonicalOccurrences(properties);

				System.out.print(solutions.length);
				System.out.print(" solutions for ");
				System.out.print(codesmellName);
				System.out.print(" in ");
				System.out.print(aName);
				System.out.print(" in ");
				System.out.print(System.currentTimeMillis() - startTime);
				System.out.println(" ms.");

				final CodeSmellDetectionCaller callCodeSmell =
					new CodeSmellDetectionCaller();

				callCodeSmell.extractNameClasses(
					aName,
					idiomLevelModel,
					codesmellName,
					properties,
					solutionBuilder);

				callCodeSmell.writeOuputFileClassesDefects();

			}
		}
		catch (final SecurityException e) {
			e.printStackTrace(ProxyConsole.getInstance().errorOutput());
		}
		catch (final IllegalArgumentException e) {
			e.printStackTrace(ProxyConsole.getInstance().errorOutput());
		}
		//	catch (final FileNotFoundException e) {
		//		e.printStackTrace();
		//	}
		catch (final CreationException e) {
			e.printStackTrace(ProxyConsole.getInstance().errorOutput());
		}
		//	catch (final NoSuchMethodException e) {
		//		e.printStackTrace();
		//	}
		catch (final IllegalAccessException e) {
			e.printStackTrace(ProxyConsole.getInstance().errorOutput());
		}
		catch (final IOException e) {
			e.printStackTrace(ProxyConsole.getInstance().errorOutput());
		}
		catch (final ClassNotFoundException e) {
			e.printStackTrace(ProxyConsole.getInstance().errorOutput());
		}
		catch (final InstantiationException e) {
			e.printStackTrace(ProxyConsole.getInstance().errorOutput());
		}
		catch (final UnsupportedSourceModelException e) {
			e.printStackTrace(ProxyConsole.getInstance().errorOutput());
		}
	}
	private void extractNameClasses(
		final String aName,
		final IIdiomLevelModel idiomLevelModel,
		final String codesmellName,
		final Properties properties,
		final OccurrenceBuilder solutionBuilder) throws IOException {
		final String path2 =
			"rsc/evolution/DetectionClasses in " + aName + " for "
					+ codesmellName + ".csv";
		final PrintWriter w =
			new PrintWriter(ProxyDisk.getInstance().fileTempOutput(path2));

		final Occurrence[] allOcc =
			solutionBuilder.getAllOccurrences(properties);
		final int nbAllOcc = allOcc.length;
		for (int j = 0; j < nbAllOcc; j++) {
			final Occurrence occ = allOcc[j];
			final List listOccComponents = occ.getComponents();
			if (!listOccComponents.isEmpty()) {
				final OccurrenceComponent solutionComponent =
					(OccurrenceComponent) listOccComponents.get(0);
				final String nameOfClass = solutionComponent.getDisplayValue();
				w.print(nameOfClass);
				if (this.classesNbDefects.containsKey(nameOfClass)) {
					final Integer currentNbBugs =
						(Integer) this.classesNbDefects.get(nameOfClass);
					final Integer anIntegerNbBugs =
						new Integer(currentNbBugs.intValue() + 1);
					this.classesNbDefects.put(nameOfClass, anIntegerNbBugs);
				}
				else {
					final Integer anIntegerNbBugs = new Integer(1);
					this.classesNbDefects.put(nameOfClass, anIntegerNbBugs);
				}
			}
		}
		w.close();
	}
	public Map getClassesNbDefects() {
		return this.classesNbDefects;
	}

	public void writeOuputFileClassesDefects() {
		try {
			final String outputPath = "rsc/evolution/DetectionClasses.csv";

			final File outputfile = new File(outputPath);
			if (!outputfile.exists()) {
				outputfile.createNewFile();
			}

			final PrintWriter aWriter =
				new PrintWriter(ProxyDisk.getInstance().fileTempOutput(
					outputPath));
			final Iterator iter = this.classesNbDefects.keySet().iterator();
			while (iter.hasNext()) {
				final String nameOfClass = (String) iter.next();
				final Integer nbBugs =
					(Integer) this.classesNbDefects.get(nameOfClass);
				aWriter.println(nameOfClass + "; " + nbBugs);
			}
			aWriter.close();

		}
		catch (final IOException e) {
			e.printStackTrace();
		}
	}

}
