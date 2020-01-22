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
package ptidej.ui.analysis.repository;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import padl.kernel.IAbstractModel;
import ptidej.ui.analysis.IUIAnalysis;
import ptidej.ui.analysis.repository.comparator.HighlighterFromMethods;
import ptidej.ui.kernel.builder.Builder;
import ptidej.ui.primitive.IPrimitiveFactory;
import util.io.ExtensionBasedFilenameFilter;
import util.io.ProxyConsole;
import util.multilingual.MultilingualManager;

/**
 * @author Yann-Ga�l Gu�h�neuc
 * @since  2004/12/20
 */
public class DifferenceHighlighterFromMethods implements IUIAnalysis {
	public Builder createBuilder(final IPrimitiveFactory aPrimitiveFactory) {
		return ptidej
			.ui
			.analysis
			.repository
			.comparator
			.AnalysesBuilder
			.getCurrentBuilder(
			aPrimitiveFactory);
	}
	public String getName() {
		return MultilingualManager.getString(
			"NAME",
			DifferenceHighlighterFromMethods.class);
	}
	public IAbstractModel invoke(final IAbstractModel abstractModel) {
		final FileDialog fileDialog = new FileDialog(new Frame());
		fileDialog.setTitle(
			MultilingualManager.getString(
				"DIALOG_TITLE",
				DifferenceHighlighterFromMethods.class));
		fileDialog.setFilenameFilter(
			new ExtensionBasedFilenameFilter(".ini"));
		fileDialog.setFile("*.ini");
		fileDialog.setMode(FileDialog.LOAD);
		fileDialog.setModal(true);
		fileDialog.setVisible(true);
		if (fileDialog.getFile() == null) {
			return abstractModel;
		}
		final String path =
			util.io.Files.normalizePath(
				fileDialog.getDirectory() + fileDialog.getFile());

		final Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(path));
			return (IAbstractModel) abstractModel.walk(
				new HighlighterFromMethods(properties));
		}
		catch (final FileNotFoundException e) {
			e.printStackTrace(ProxyConsole.getInstance().errorOutput());
		}
		catch (final IOException e) {
			e.printStackTrace(ProxyConsole.getInstance().errorOutput());
		}

		return abstractModel;
	}
}
