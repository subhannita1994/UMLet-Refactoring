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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRWrappingSvgRenderer.java,v 1.1 2008/09/29 16:20:41 guehene Exp $
 */
public class JRWrappingSvgRenderer extends JRAbstractSvgRenderer
{

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	private JRRenderable renderer = null;
	private Dimension2D dimension = null;
	private Color backcolor = null;

	
	/**
	 *
	 */
	public JRWrappingSvgRenderer(
		JRRenderable renderer, 
		Dimension2D dimension,
		Color backcolor
		)
	{
		this.renderer = renderer;
		this.dimension = dimension;
		this.backcolor = backcolor;
	}


	/**
	 *
	 */
	public Dimension2D getDimension()
	{
		return this.dimension;
	}


	/**
	 *
	 */
	public Color getBackcolor()
	{
		return this.backcolor;
	}


	/**
	 *
	 */
	public void render(Graphics2D grx, Rectangle2D rectangle) throws JRException
	{
		this.renderer.render(grx, rectangle);
	}

	protected Graphics2D createGraphics(BufferedImage bi)
	{
		if (this.renderer instanceof JRAbstractSvgRenderer)
		{
			return ((JRAbstractSvgRenderer) this.renderer).createGraphics(bi);
		}
		
		return super.createGraphics(bi);
	}

}
