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
package squad.test;

import java.util.Iterator;
import padl.creator.classfile.CompleteClassFileCreator;
import padl.kernel.ICodeLevelModel;
import padl.kernel.IFirstClassEntity;
import padl.kernel.IGhost;
import padl.kernel.exception.CreationException;
import padl.kernel.impl.Factory;
import squad.quality.INominalQualityAttribute;
import squad.quality.pqmod.PQMODRepository;

public class TestSQUAD {
	public static void main(final String[] args) {
		String root = "D:/Software/P-MARt Workspace/JHotDraw v5.1/bin/";

		final ICodeLevelModel codeLevelModel =
			Factory.getInstance().createCodeLevelModel("Model");
		try {
			codeLevelModel.create(new CompleteClassFileCreator(
				new String[] { root },
				true));
		}
		catch (final CreationException e) {
			e.printStackTrace();

		}

		final PQMODRepository qualityRepository = PQMODRepository.getInstance();

		final Iterator entityIterator =
			codeLevelModel.getIteratorOnTopLevelEntities();
		while (entityIterator.hasNext()) {
			final IFirstClassEntity firstClassEntity =
				(IFirstClassEntity) entityIterator.next();
			if (!(firstClassEntity instanceof IGhost)) {
				System.out.println("Computing the reusability for: "
						+ firstClassEntity.getDisplayName());
				System.out
					.println(((INominalQualityAttribute) qualityRepository
						.getQualityAttribute("Effectiveness"))
						.computeNominalValue(codeLevelModel, firstClassEntity));

			}
		}
	}
}
