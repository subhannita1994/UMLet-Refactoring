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
package net.sf.jasperreports.charts.fill;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.charts.JRPieDataset;
import net.sf.jasperreports.charts.util.PieLabelGenerator;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRHyperlinkHelper;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.design.JRVerifier;
import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillChartDataset;
import net.sf.jasperreports.engine.fill.JRFillHyperlinkHelper;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

import org.jfree.data.general.Dataset;
import org.jfree.data.general.DefaultPieDataset;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRFillPieDataset.java,v 1.1 2008/09/29 16:21:42 guehene Exp $
 */
public class JRFillPieDataset extends JRFillChartDataset implements JRPieDataset
{

	/**
	 *
	 */
	private DefaultPieDataset dataset = new DefaultPieDataset();
	private Map labels = null;
	
	private Comparable key = null;
	private Number value = null;
	private String label = null;
	
	private Map sectionHyperlinks;
	private JRPrintHyperlink sectionHyperlink;
	
	
	/**
	 *
	 */
	public JRFillPieDataset(
		JRPieDataset pieDataset, 
		JRFillObjectFactory factory
		)
	{
		super(pieDataset, factory);
	}


	/**
	 *
	 */
	public JRExpression getKeyExpression()
	{
		return ((JRPieDataset)this.parent).getKeyExpression();
	}
		
	/**
	 *
	 */
	public JRExpression getValueExpression()
	{
		return ((JRPieDataset)this.parent).getValueExpression();
	}
		
	/**
	 *
	 */
	public JRExpression getLabelExpression()
	{
		return ((JRPieDataset)this.parent).getLabelExpression();
	}
	
	
	/**
	 *
	 */
	protected void customInitialize()
	{
		this.dataset = new DefaultPieDataset();
		this.labels = new HashMap();
		this.sectionHyperlinks = new HashMap();
	}

	/**
	 *
	 */
	protected void customEvaluate(JRCalculator calculator) throws JRExpressionEvalException
	{
		this.key = (Comparable)calculator.evaluate(getKeyExpression()); 
		this.value = (Number)calculator.evaluate(getValueExpression());
		this.label = (String)calculator.evaluate(getLabelExpression());
		
		if (hasSectionHyperlinks())
		{
			evaluateSectionHyperlink(calculator);
		}		
	}


	protected void evaluateSectionHyperlink(JRCalculator calculator) throws JRExpressionEvalException
	{
		try
		{
			this.sectionHyperlink = JRFillHyperlinkHelper.evaluateHyperlink(getSectionHyperlink(), calculator, JRExpression.EVALUATION_DEFAULT);
		}
		catch (JRExpressionEvalException e)
		{
			throw e;
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	/**
	 *
	 */
	protected void customIncrement()
	{
		this.dataset.setValue(this.key, this.value);
		this.labels.put( this.key, this.label );
		
		if (hasSectionHyperlinks())
		{
			this.sectionHyperlinks.put(this.key, this.sectionHyperlink);
		}		
	}

	/**
	 *
	 */
	public Dataset getCustomDataset()
	{
		return this.dataset;
	}


	/* (non-Javadoc)
	 * @see net.sf.jasperreports.engine.JRChartDataset#getDatasetType()
	 */
	public byte getDatasetType() {
		return JRChartDataset.PIE_DATASET;
	}


	public PieLabelGenerator getLabelGenerator(){
		return (getLabelExpression() == null) ? null : new PieLabelGenerator( this.labels );
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
		return ((JRPieDataset) this.parent).getSectionHyperlink();
	}

	
	public boolean hasSectionHyperlinks()
	{
		return !JRHyperlinkHelper.isEmpty(getSectionHyperlink()); 
	}
	
	public Map getSectionHyperlinks()
	{
		return this.sectionHyperlinks;
	}


	public void validate(JRVerifier verifier)
	{
		verifier.verify(this);
	}

}
