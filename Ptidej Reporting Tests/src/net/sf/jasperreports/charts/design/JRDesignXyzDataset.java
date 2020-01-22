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
package net.sf.jasperreports.charts.design;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.charts.JRXySeries;
import net.sf.jasperreports.charts.JRXyzDataset;
import net.sf.jasperreports.charts.JRXyzSeries;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.design.JRDesignChartDataset;
import net.sf.jasperreports.engine.design.JRVerifier;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id: JRDesignXyzDataset.java,v 1.1 2008/09/29 16:21:32 guehene Exp $ 
 */
public class JRDesignXyzDataset extends JRDesignChartDataset implements JRXyzDataset {
	
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_XYZ_SERIES = "xyzSeries";
	
	private List xyzSeriesList = new ArrayList();


	/**
	 *
	 */
	public JRDesignXyzDataset(JRChartDataset dataset)
	{
		super(dataset);
	}


	/**
	 * 
	 */
	public JRXyzSeries[] getSeries()
	{
		JRXyzSeries[] xyzSeriesArray = new JRXyzSeries[ this.xyzSeriesList.size() ];
		this.xyzSeriesList.toArray( xyzSeriesArray );
		
		return xyzSeriesArray;
	}
	
	/**
	 * 
	 */
	public List getSeriesList()
	{
		return this.xyzSeriesList;
	}

	/**
	 * 
	 */
	public void addXyzSeries( JRXyzSeries xyzSeries ) 
	{
		this.xyzSeriesList.add( xyzSeries );
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_XYZ_SERIES, 
				xyzSeries, this.xyzSeriesList.size() - 1);
	}
	
	/**
	 * 
	 */
	public JRXyzSeries removeXyzSeries( JRXyzSeries xyzSeries ) 
	{
		if( xyzSeries != null ){
			int idx = this.xyzSeriesList.indexOf(xyzSeries);
			if (idx >= 0) {
				this.xyzSeriesList.remove(idx);
				getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_XYZ_SERIES, xyzSeries, idx);
			}
		}
		
		return xyzSeries;
	}
	
	/** 
	 * 
	 */
	public byte getDatasetType() {
		return JRChartDataset.XYZ_DATASET;
	}
	
	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
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
		JRDesignXyzDataset clone = (JRDesignXyzDataset)super.clone();
		
		if (this.xyzSeriesList != null)
		{
			clone.xyzSeriesList = new ArrayList(this.xyzSeriesList.size());
			for(int i = 0; i < this.xyzSeriesList.size(); i++)
			{
				clone.xyzSeriesList.add(((JRXySeries)this.xyzSeriesList.get(i)).clone());
			}
		}

		return clone;
	}
}
