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
package ptidej.solver.helper;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Make for a version a unique list of classes playing a role in patterns form lists generated by EntitiesPlayingRolesExtractorPerVersion
 * @author Imane
 *
 */
public class DesignPatternClassesExtractor {
	//	final static String[] motifs = new String[] { "Builder", "Command",
	//			"Composite", "Decorator", "FactoryMethod", "Observer1",
	//			"Observer2", "Prototype", "State", "TemplateMethod", "Visitor" };

	final static String[] motifs = new String[] { "Composite", "Decorator",
			"Observer1", "Observer2", "Visitor" };

	/**
	 * 
	 * @param aClassesList
	 * @return
	 */
	private static Map initialiseDesignPatternsMap(final List aClassesList) {

		final Map patternsMap = new LinkedHashMap();
		final Iterator iterator = aClassesList.iterator();
		while (iterator.hasNext()) {

			final String tab[] =
				new String[DesignPatternClassesExtractor.motifs.length + 1];
			for (int k = 0; k < tab.length; k++) {
				tab[k] = "NO";
			}
			final String clazz = (String) iterator.next();
			patternsMap.put(clazz, tab);
		}

		return patternsMap;
	}
	/**
	* Has to be updated to the analysed project - setup the following variables
	* designPatternsFilesPath
	* antipatternsFilesPath
	* outputBasePath
	* releases
	* @param args
	*/
	public static void main(final String[] args) {

		//		String patternsMapTitle =
		//			new StringBuffer()
		//				.append("Class;")
		//				.append(
		//					"Builder;Command;Composite;Decorator;FactoryMethod;Observer1;")
		//				.append("Observer2;Prototype;State;TemplateMethod;Visitor;InDP")
		//				.toString();

		final StringBuffer buffer = new StringBuffer().append("Class;");
		for (int i = 0; i < DesignPatternClassesExtractor.motifs.length; i++) {
			buffer.append(DesignPatternClassesExtractor.motifs[i]).append(";");
		}

		buffer.append("InDP");

		final String patternsMapTitle = buffer.toString();

		//		String designPatternsFilesPath =
		//			"../Ptidej Solver Data/Pattern Classes/Checkstyle/DesignPatterns/";
		//
		//		String outputBasePath =
		//			"/Polymtl/Data/PatternsAndTests/checkstyle/outputs/DPcrossData/";
		//		String classesListFolderPath =
		//			"/Polymtl/Data/PatternsAndTests/checkstyle/outputs/classesPerVersion/";

		final String designPatternsFilesPath =
			"../Ptidej Solver Data/Pattern Classes/ArgoUml/DesignPatterns/";

		final String outputBasePath =
			"/Polymtl/Data/PatternsAndTests/argouml/outputs/DPcrossData/";

		final String classesListFolderPath =
			"/Polymtl/Data/PatternsAndTests/argouml/outputs/classesPerVersion/";

		final String[] designPatternsFolders =
			new File(designPatternsFilesPath).list();
		//for each version, take its design pattern classes files and cross it with the list of classes to build for this version its DPCrossData
		for (int i = 0; i < designPatternsFolders.length; i++) {
			final String currentFolderPath =
				new StringBuffer()
					.append(designPatternsFilesPath)
					.append(File.separatorChar)
					.append(designPatternsFolders[i])
					.toString();
			final String releaseName = designPatternsFolders[i];
			//take the list of classes of the current version
			final String[] designPatternsFiles =
				new File(currentFolderPath).list();
			final String classesListPath =
				new StringBuffer()
					.append(classesListFolderPath)
					.append(releaseName)
					.append(".csv")
					.toString();
			final List classesList = Utils.readIntoList(classesListPath, true);

			Map designPatternsMap =
				DesignPatternClassesExtractor
					.initialiseDesignPatternsMap(classesList);

			designPatternsMap =
				DesignPatternClassesExtractor.updateMapWithDP_Classes(
					designPatternsMap,
					designPatternsFiles,
					currentFolderPath);

			final String mapPath =
				new StringBuffer()
					.append(outputBasePath)
					.append(releaseName)
					.append("_DPCrossData.csv")
					.toString();
			Utils.writeMapInCSV(designPatternsMap, mapPath, patternsMapTitle);

		}

		System.out
			.println("DesignPatternClassesExtractor: End of the extraction");

	}

	/**
	 * 
	 * @param patternsMap
	 * @param designPatternsFiles
	 * @return
	 */
	private static Map updateMapWithDP_Classes(
		final Map patternsMap,
		final String[] designPatternsFiles,
		final String sourceBasePath) {

		for (int i = 0; i < designPatternsFiles.length; i++) {
			final String currentFilePath =
				new StringBuffer()
					.append(sourceBasePath)
					.append(File.separator)
					.append(designPatternsFiles[i])
					.toString();
			final List dpClassesList =
				Utils.readIntoList(currentFilePath, false);
			final Iterator iterator = dpClassesList.iterator();
			int missedClasses = 0;
			while (iterator.hasNext()) {
				final String dpClass = (String) iterator.next();
				if (patternsMap.containsKey(dpClass)) {
					((String[]) patternsMap.get(dpClass))[i] = "YES";
					((String[]) patternsMap.get(dpClass))[DesignPatternClassesExtractor.motifs.length] =
						"YES";
				}
				else {
					missedClasses++;
				}
			}
			//interfaces are missed
			System.out.println("MissedClasses " + designPatternsFiles[i] + " "
					+ missedClasses);
		}

		return patternsMap;
	}

}
