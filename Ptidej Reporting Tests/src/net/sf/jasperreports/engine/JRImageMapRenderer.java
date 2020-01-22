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
package net.sf.jasperreports.engine;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.List;


/**
 * Image renderer able to produce image maps.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JRImageMapRenderer.java,v 1.1 2008/09/29 16:20:42 guehene Exp $
 */
public interface JRImageMapRenderer extends JRRenderable
{

	/**
	 * Returns the list of {@link JRPrintImageAreaHyperlink image map areas}.
	 *
	 * @deprecated Replaced by {@link #renderWithHyperlinks(Graphics2D, Rectangle2D)}
	 * @param renderingArea the area on which the image would be rendered
	 * @return a list of {@link JRPrintImageAreaHyperlink JRPrintImageAreaHyperlink} instances.
	 * @throws JRException
	 */
	List getImageAreaHyperlinks(Rectangle2D renderingArea) throws JRException;

	/**
	 * Indicates whether the renderer actually includes any image map areas.
	 * 
	 * @return whether the renderer actually includes any image map areas
	 */
	boolean hasImageAreaHyperlinks();
	
	/**
	 * Returns the list of {@link JRPrintImageAreaHyperlink image map areas}.
	 * 
	 * @param rectangle the area on which the image would be rendered
	 * @return a list of {@link JRPrintImageAreaHyperlink JRPrintImageAreaHyperlink} instances.
	 * @throws JRException
	 */
	public List renderWithHyperlinks(Graphics2D grx, Rectangle2D rectangle) throws JRException;

}
