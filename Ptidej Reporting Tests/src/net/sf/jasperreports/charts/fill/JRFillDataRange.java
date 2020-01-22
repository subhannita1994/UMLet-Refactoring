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

import net.sf.jasperreports.charts.JRDataRange;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

/**
 * @author Barry Klawans (bklawans@users.sourceforge.net)
 * @version $Id: JRFillDataRange.java,v 1.1 2008/09/29 16:21:42 guehene Exp $
 */
public class JRFillDataRange  implements JRDataRange
{
	protected  JRDataRange parent = null;

	/**
	 *
	 */
	public JRFillDataRange(JRDataRange dataRange, JRFillObjectFactory factory)
	{
		factory.put(dataRange, this);
		this.parent = dataRange;
	}

	/**
	 *
	 */
	public JRExpression getLowExpression()
	{
		return this.parent.getLowExpression();
	}

	/**
	 *
	 */
	public JRExpression getHighExpression()
	{
		return this.parent.getHighExpression();
	}
	
	/**
	 *
	 */
	public Object clone() 
	{
		return null;
	}
}
