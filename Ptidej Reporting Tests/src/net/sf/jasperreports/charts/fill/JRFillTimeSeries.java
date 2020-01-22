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

import java.util.Date;

import net.sf.jasperreports.charts.JRTimeSeries;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRHyperlinkHelper;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillHyperlinkHelper;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRFillTimeSeries.java,v 1.1 2008/09/29 16:21:41 guehene Exp $
 */
public class JRFillTimeSeries implements JRTimeSeries
{

	/**
	 *
	 */
	protected JRTimeSeries parent = null;

	private Comparable series = null;
	private Date timePeriod = null;
	private Number value = null;
	private String label = null;
	private JRPrintHyperlink itemHyperlink;
	
	
	/**
	 *
	 */
	public JRFillTimeSeries(
		JRTimeSeries timeSeries, 
		JRFillObjectFactory factory
		)
	{
		factory.put(timeSeries, this);

		this.parent = timeSeries;
	}


	/**
	 *
	 */
	public JRExpression getSeriesExpression()
	{
		return this.parent.getSeriesExpression();
	}
		
	/**
	 *
	 */
	public JRExpression getTimePeriodExpression()
	{
		return this.parent.getTimePeriodExpression();
	}
		
	/**
	 *
	 */
	public JRExpression getValueExpression()
	{
		return this.parent.getValueExpression();
	}
		
	/**
	 *
	 */
	public JRExpression getLabelExpression()
	{
		return this.parent.getLabelExpression();
	}
	
	
	/**
	 *
	 */
	protected Comparable getSeries()
	{
		return this.series;
	}
		
	/**
	 *
	 */
	protected Date getTimePeriod()
	{
		return this.timePeriod;
	}
		
	/**
	 *
	 */
	protected Number getValue()
	{
		return this.value;
	}
		
	/**
	 *
	 */
	protected String getLabel()
	{
		return this.label;
	}
	
	
	/**
	 *
	 */
	protected void evaluate(JRCalculator calculator) throws JRExpressionEvalException
	{
		this.series = (Comparable)calculator.evaluate(getSeriesExpression()); 
		this.timePeriod = (Date)calculator.evaluate(getTimePeriodExpression()); 
		this.value = (Number)calculator.evaluate(getValueExpression());
		this.label = (String)calculator.evaluate(getLabelExpression());
	
		if (hasItemHyperlink())
		{
			evaluateItemHyperlink(calculator);
		}
	}


	protected void evaluateItemHyperlink(JRCalculator calculator) throws JRExpressionEvalException
	{
		try
		{
			this.itemHyperlink = JRFillHyperlinkHelper.evaluateHyperlink(getItemHyperlink(), calculator, JRExpression.EVALUATION_DEFAULT);
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


	public JRHyperlink getItemHyperlink()
	{
		return this.parent.getItemHyperlink();
	}


	public boolean hasItemHyperlink()
	{
		return !JRHyperlinkHelper.isEmpty(getItemHyperlink()); 
	}

	
	public JRPrintHyperlink getPrintItemHyperlink()
	{
		return this.itemHyperlink;
	}

	/**
	 *
	 */
	public Object clone() 
	{
		return null;
	}
}
