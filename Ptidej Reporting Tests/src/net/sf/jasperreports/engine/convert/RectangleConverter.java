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

/*
 * Contributors:
 * Eugene D - eugenedruy@users.sourceforge.net 
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 */
package net.sf.jasperreports.engine.convert;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.base.JRBasePrintRectangle;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: RectangleConverter.java,v 1.1 2008/09/29 16:20:07 guehene Exp $
 */
public class RectangleConverter extends ElementConverter
{

	/**
	 *
	 */
	private final static RectangleConverter INSTANCE = new RectangleConverter();
	
	/**
	 *
	 */
	private RectangleConverter()
	{
	}

	/**
	 *
	 */
	public static RectangleConverter getInstance()
	{
		return INSTANCE;
	}
	
	/**
	 *
	 */
	public JRPrintElement convert(ReportConverter reportConverter, JRElement element)
	{
		JRBasePrintRectangle printRectangle = new JRBasePrintRectangle(reportConverter.getDefaultStyleProvider());
		JRRectangle rectangle = (JRRectangle)element;
		
		copyGraphicElement(reportConverter, rectangle, printRectangle);
		
		printRectangle.setRadius(rectangle.getOwnRadius());
		
		return printRectangle;
	}

}
