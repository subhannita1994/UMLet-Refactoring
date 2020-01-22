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
package squad.quality.qmood.repository;

import padl.kernel.IAbstractModel;
import padl.kernel.IFirstClassEntity;
import pom.metrics.IUnaryMetric;
import pom.metrics.MetricsRepository;
import squad.quality.AbstractQualityAttribute;
import squad.quality.INumericQualityAttribute;
import squad.quality.IQualityAttribute;
import util.help.IHelpURL;

public final class Effectiveness extends AbstractQualityAttribute implements
		IQualityAttribute, INumericQualityAttribute, IHelpURL {

	public double concretelyComputeNumValue(
		final IAbstractModel anAbstractModel,
		final IFirstClassEntity entity) {

		final double result =
			(0.2)
					* ((IUnaryMetric) MetricsRepository
						.getInstance()
						.getMetric("ANA")).compute(anAbstractModel, entity)
					+ (0.2)
					* ((IUnaryMetric) MetricsRepository
						.getInstance()
						.getMetric("DAM")).compute(anAbstractModel, entity)
					+ (0.2)
					* ((IUnaryMetric) MetricsRepository
						.getInstance()
						.getMetric("MOA")).compute(anAbstractModel, entity)
					+ (0.2)
					* ((IUnaryMetric) MetricsRepository
						.getInstance()
						.getMetric("MFA")).compute(anAbstractModel, entity)
					+ 0.2
					* ((IUnaryMetric) MetricsRepository
						.getInstance()
						.getMetric("NOPM")).compute(anAbstractModel, entity);
		return result;
	}
	public String getDefinition() {
		final String def =
			"Design's ability to achieve the desired functionality and behaviour using object oriented design concepts and techniques.";
		return def;
	}
	public String getHelpURL() {
		return "http://dl.acm.org/citation.cfm?id=513066";
	}
}
