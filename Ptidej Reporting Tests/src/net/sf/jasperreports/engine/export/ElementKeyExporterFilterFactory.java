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
package net.sf.jasperreports.engine.export;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.util.JRProperties;

/**
 * Factory of {@link ElementKeyExporterFilter} instances.
 * 
 * The factory uses report properties to decide which element keys are to
 * be filtered on export.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: ElementKeyExporterFilterFactory.java,v 1.1 2008/09/29 16:20:46 guehene Exp $
 */
public class ElementKeyExporterFilterFactory implements ExporterFilterFactory
{

	/**
	 * The prefix of element exclusion properties.
	 * 
	 * This prefix is appended to the exporter properties prefix, resulting
	 * in element exclusion properties such as
	 * <code>net.sf.jasperreports.export.xls.exclude.key.*</code>. 
	 */
	public static final String PROPERTY_EXCLUDED_KEY_PREFIX = "exclude.key.";
	
	/**
	 * The exported report is searched for element exclusion properties, and
	 * if any is found a {@link ElementKeyExporterFilter} instance is returned.
	 * 
	 * Each property results in a excluded element key in the following manner:
	 * <ul>
	 * 	<li>If the property value is not empty, it is used as excluded element key.</li>
	 * 	<li>Otherwise, the property suffix is used as element key.</li>
	 * </ul>
	 * 
	 * @see #PROPERTY_EXCLUDED_KEY_PREFIX
	 */
	public ExporterFilter getFilter(JRExporterContext exporterContext)
			throws JRException
	{
		String excludeKeyPrefix = 
			exporterContext.getExportPropertiesPrefix() + PROPERTY_EXCLUDED_KEY_PREFIX;
		List props = JRProperties.getProperties(
				exporterContext.getExportedReport(), excludeKeyPrefix);
		ExporterFilter filter;
		if (props.isEmpty())
		{
			filter = null;
		}
		else
		{
			Set excludedKeys = new HashSet();
			for (Iterator it = props.iterator(); it.hasNext();)
			{
				JRProperties.PropertySuffix prop = (JRProperties.PropertySuffix) it.next();
				String key = prop.getValue();
				if (key == null || key.length() == 0)
				{
					key = prop.getSuffix();
				}
				excludedKeys.add(key);
			}
			
			filter = new ElementKeyExporterFilter(excludedKeys);
		}
		return filter;
	}

}
