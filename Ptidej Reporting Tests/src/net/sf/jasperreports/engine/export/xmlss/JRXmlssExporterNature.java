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

package net.sf.jasperreports.engine.export.xmlss;

import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.export.ExporterFilter;
import net.sf.jasperreports.engine.export.ExporterNature;
import net.sf.jasperreports.engine.util.JRProperties;

/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id: JRXmlssExporterNature.java,v 1.1 2008/09/29 16:22:11 guehene Exp $
 */
public class JRXmlssExporterNature implements ExporterNature
{
	
	public static final String PROPERTY_BREAK_BEFORE_ROW = JRProperties.PROPERTY_PREFIX + "export.xls.break.before.row";
	public static final String PROPERTY_BREAK_AFTER_ROW = JRProperties.PROPERTY_PREFIX + "export.xls.break.after.row";

	private ExporterFilter filter;
	private boolean isIgnorePageMargins;

	/**
	 * 
	 */
	public JRXmlssExporterNature(ExporterFilter filter)
	{
		this.filter = filter;
	}
	
	/**
	 * 
	 */
	protected JRXmlssExporterNature(ExporterFilter filter, boolean isIgnorePageMargins)
	{
		this.filter = filter;
		this.isIgnorePageMargins = isIgnorePageMargins;
	}

	/**
	 * 
	 */
	public boolean isToExport(JRPrintElement element)
	{
		return 
			(element instanceof JRPrintText || element instanceof JRPrintFrame)
			&& (this.filter == null || this.filter.isToExport(element));
	}
	
	/**
	 * 
	 */
	public boolean isDeep()
	{
		return true;
	}
	
	/**
	 * 
	 */
	public boolean isSplitSharedRowSpan()
	{
		return true;
	}

	/**
	 * 
	 */
	public boolean isSpanCells()
	{
		return true;
	}
	
	/**
	 * 
	 */
	public boolean isIgnoreLastRow()
	{
		return true;
	}

	public boolean isHorizontallyMergeEmptyCells()
	{
		return false;
	}

	/**
	 * Specifies whether empty page margins should be ignored
	 */
	public boolean isIgnorePageMargins()
	{
		return this.isIgnorePageMargins;
	}

	/**
	 *
	 */
	public boolean isBreakBeforeRow(JRPrintElement element)
	{
		return Boolean.valueOf(element.getPropertiesMap().getProperty(PROPERTY_BREAK_BEFORE_ROW)).booleanValue();
	}
	
	/**
	 *
	 */
	public boolean isBreakAfterRow(JRPrintElement element)
	{
		return Boolean.valueOf(element.getPropertiesMap().getProperty(PROPERTY_BREAK_AFTER_ROW)).booleanValue();
	}
	
}
