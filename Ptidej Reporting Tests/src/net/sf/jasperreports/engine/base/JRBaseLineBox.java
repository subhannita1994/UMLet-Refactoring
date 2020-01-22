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
package net.sf.jasperreports.engine.base;

import java.awt.Color;
import java.io.Serializable;

import net.sf.jasperreports.engine.JRBoxContainer;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPenContainer;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.util.JRStyleResolver;


/**
 * This is useful for drawing borders around text elements and images. Boxes can have borders and paddings, which can
 * have different width and colour on each side of the element.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRBaseLineBox.java,v 1.1 2008/09/29 16:21:17 guehene Exp $
 */
public class JRBaseLineBox implements JRLineBox, JRPenContainer, Serializable, Cloneable, JRChangeEventsSupport
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_PADDING = "padding";
	
	public static final String PROPERTY_TOP_PADDING = "topPadding";
	
	public static final String PROPERTY_LEFT_PADDING = "leftPadding";
	
	public static final String PROPERTY_BOTTOM_PADDING = "bottomPadding";
	
	public static final String PROPERTY_RIGHT_PADDING = "rightPadding";
	

	protected JRBoxContainer boxContainer = null;

	/**
	 *
	 */
	protected JRBoxPen pen = null; 
	protected JRBoxPen topPen = null;
	protected JRBoxPen leftPen = null;
	protected JRBoxPen bottomPen = null;
	protected JRBoxPen rightPen = null;

	protected Integer padding = null;
	protected Integer topPadding = null;
	protected Integer leftPadding = null;
	protected Integer bottomPadding = null;
	protected Integer rightPadding = null;

	
	/**
	 *
	 */
	public JRBaseLineBox(JRBoxContainer boxContainer)
	{
		this.boxContainer = boxContainer;

		this.pen = new JRBaseBoxPen(this);
		this.topPen = new JRBaseBoxTopPen(this);
		this.leftPen = new JRBaseBoxLeftPen(this);
		this.bottomPen = new JRBaseBoxBottomPen(this);
		this.rightPen = new JRBaseBoxRightPen(this);
	}
	
	
	/**
	 *
	 */
	public JRDefaultStyleProvider getDefaultStyleProvider() 
	{
		if (this.boxContainer != null)
		{
			return this.boxContainer.getDefaultStyleProvider();
		}
		return null;
	}

	/**
	 *
	 */
	public JRStyle getStyle() 
	{
		if (this.boxContainer != null)
		{
			return this.boxContainer.getStyle();
		}
		return null;
	}

	/**
	 *
	 */
	public String getStyleNameReference()
	{
		if (this.boxContainer != null)
		{
			return this.boxContainer.getStyleNameReference();
		}
		return null;
	}

	/**
	 *
	 */
	public JRBoxContainer getBoxContainer()
	{
		return this.boxContainer;
	}

	/**
	 *
	 */
	public Float getDefaultLineWidth()
	{
		return JRPen.LINE_WIDTH_0;
	}

	/**
	 *
	 */
	public Color getDefaultLineColor()
	{
		if (this.boxContainer != null)
		{
			return this.boxContainer.getDefaultLineColor();
		}
		return Color.black;
	}

	/**
	 *
	 */
	public JRBoxPen getPen()
	{
		return this.pen;
	}

	/**
	 *
	 */
	public void copyPen(JRBoxPen pen)
	{
		this.pen = pen.clone(this);
	}

	/**
	 *
	 */
	public JRBoxPen getTopPen()
	{
		return this.topPen;
	}

	/**
	 *
	 */
	public void copyTopPen(JRBoxPen topPen)
	{
		this.topPen = topPen.clone(this);
	}

	/**
	 *
	 */
	public JRBoxPen getLeftPen()
	{
		return this.leftPen;
	}

	/**
	 *
	 */
	public void copyLeftPen(JRBoxPen leftPen)
	{
		this.leftPen = leftPen.clone(this);
	}

	/**
	 *
	 */
	public JRBoxPen getBottomPen()
	{
		return this.bottomPen;
	}

	/**
	 *
	 */
	public void copyBottomPen(JRBoxPen bottomPen)
	{
		this.bottomPen = bottomPen.clone(this);
	}

	/**
	 *
	 */
	public JRBoxPen getRightPen()
	{
		return this.rightPen;
	}

	/**
	 *
	 */
	public void copyRightPen(JRBoxPen rightPen)
	{
		this.rightPen = rightPen.clone(this);
	}

	/**
	 *
	 */
	public Integer getPadding()
	{
		return JRStyleResolver.getPadding(this);
	}

	public Integer getOwnPadding()
	{
		return this.padding;
	}
	
	/**
	 *
	 */
	public void setPadding(int padding)
	{
		setPadding(new Integer(padding));
	}

	/**
	 *
	 */
	public void setPadding(Integer padding)
	{
		Object old = this.padding;
		this.padding = padding;
		getEventSupport().firePropertyChange(PROPERTY_PADDING, old, this.padding);
	}

	/**
	 *
	 */
	public Integer getTopPadding()
	{
		return JRStyleResolver.getTopPadding(this);
	}

	/**
	 *
	 */
	public Integer getOwnTopPadding()
	{
		return this.topPadding;
	}

	/**
	 *
	 */
	public void setTopPadding(int topPadding)
	{
		setTopPadding(new Integer(topPadding));
	}

	/**
	 *
	 */
	public void setTopPadding(Integer topPadding)
	{
		Object old = this.topPadding;
		this.topPadding = topPadding;
		getEventSupport().firePropertyChange(PROPERTY_TOP_PADDING, old, this.topPadding);
	}

	/**
	 *
	 */
	public Integer getLeftPadding()
	{
		return JRStyleResolver.getLeftPadding(this);
	}

	/**
	 *
	 */
	public Integer getOwnLeftPadding()
	{
		return this.leftPadding;
	}

	/**
	 *
	 */
	public void setLeftPadding(int leftPadding)
	{
		setLeftPadding(new Integer(leftPadding));
	}

	/**
	 *
	 */
	public void setLeftPadding(Integer leftPadding)
	{
		Object old = this.leftPadding;
		this.leftPadding = leftPadding;
		getEventSupport().firePropertyChange(PROPERTY_LEFT_PADDING, old, this.leftPadding);
	}

	/**
	 *
	 */
	public Integer getBottomPadding()
	{
		return JRStyleResolver.getBottomPadding(this);
	}

	/**
	 *
	 */
	public Integer getOwnBottomPadding()
	{
		return this.bottomPadding;
	}

	/**
	 *
	 */
	public void setBottomPadding(int bottomPadding)
	{
		setBottomPadding(new Integer(bottomPadding));
	}

	/**
	 *
	 */
	public void setBottomPadding(Integer bottomPadding)
	{
		Object old = this.bottomPadding;
		this.bottomPadding = bottomPadding;
		getEventSupport().firePropertyChange(PROPERTY_BOTTOM_PADDING, old, this.bottomPadding);
	}

	/**
	 *
	 */
	public Integer getRightPadding()
	{
		return JRStyleResolver.getRightPadding(this);
	}

	/**
	 *
	 */
	public Integer getOwnRightPadding()
	{
		return this.rightPadding;
	}

	/**
	 *
	 */
	public void setRightPadding(int rightPadding)
	{
		setRightPadding(new Integer(rightPadding));
	}

	/**
	 *
	 */
	public void setRightPadding(Integer rightPadding)
	{
		Object old = this.rightPadding;
		this.rightPadding = rightPadding;
		getEventSupport().firePropertyChange(PROPERTY_RIGHT_PADDING, old, this.rightPadding);
	}


	/**
	 * 
	 */
	public JRLineBox clone(JRBoxContainer boxContainer)
	{
		JRBaseLineBox clone = null;
		
		try
		{
			clone = (JRBaseLineBox)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			throw new JRRuntimeException(e);
		}
		
		clone.boxContainer = boxContainer;
		
		clone.pen = this.pen.clone(clone);
		clone.topPen = this.topPen.clone(clone);
		clone.leftPen = this.leftPen.clone(clone);
		clone.bottomPen = this.bottomPen.clone(clone);
		clone.rightPen = this.rightPen.clone(clone);
		
		return clone;
	}
	
	private transient JRPropertyChangeSupport eventSupport;
	
	public JRPropertyChangeSupport getEventSupport()
	{
		synchronized (this)
		{
			if (this.eventSupport == null)
			{
				this.eventSupport = new JRPropertyChangeSupport(this);
			}
		}
		
		return this.eventSupport;
	}

}
