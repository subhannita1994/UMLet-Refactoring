/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.charts.base;

import net.sf.jasperreports.charts.JRPieDataset;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.base.JRBaseChartDataset;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.design.JRVerifier;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRBasePieDataset.java,v 1.1 2008/09/29 16:20:32 guehene Exp $
 */
public class JRBasePieDataset extends JRBaseChartDataset implements JRPieDataset
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	protected JRExpression keyExpression = null;
	protected JRExpression valueExpression = null;
	protected JRExpression labelExpression = null;
	private JRHyperlink sectionHyperlink;

	
	/**
	 *
	 */
	public JRBasePieDataset(JRChartDataset dataset)
	{
		super(dataset);
	}
	
	
	/**
	 *
	 */
	public JRBasePieDataset(JRPieDataset dataset, JRBaseObjectFactory factory)
	{
		super(dataset, factory);

		this.keyExpression = factory.getExpression(dataset.getKeyExpression());
		this.valueExpression = factory.getExpression(dataset.getValueExpression());
		this.labelExpression = factory.getExpression(dataset.getLabelExpression());
		this.sectionHyperlink = factory.getHyperlink(dataset.getSectionHyperlink());
	}

	
	/**
	 *
	 */
	public JRExpression getKeyExpression()
	{
		return this.keyExpression;
	}
		
	/**
	 *
	 */
	public JRExpression getValueExpression()
	{
		return this.valueExpression;
	}
		
	/**
	 *
	 */
	public JRExpression getLabelExpression()
	{
		return this.labelExpression;
	}


	/** 
	 * 
	 */
	public byte getDatasetType() {
		return JRChartDataset.PIE_DATASET;
	}
		

	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}


	public JRHyperlink getSectionHyperlink()
	{
		return this.sectionHyperlink;
	}


	public void validate(JRVerifier verifier)
	{
		verifier.verify(this);
	}

	/**
	 * 
	 */
	public Object clone() 
	{
		JRBasePieDataset clone = (JRBasePieDataset)super.clone();
		
		if (this.keyExpression != null)
		{
			clone.keyExpression = (JRExpression)this.keyExpression.clone();
		}
		if (this.valueExpression != null)
		{
			clone.valueExpression = (JRExpression)this.valueExpression.clone();
		}
		if (this.labelExpression != null)
		{
			clone.labelExpression = (JRExpression)this.labelExpression.clone();
		}
		if (this.sectionHyperlink != null)
		{
			clone.sectionHyperlink = (JRHyperlink)this.sectionHyperlink.clone();
		}
		
		return clone;
	}

}
