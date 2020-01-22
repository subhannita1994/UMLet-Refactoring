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
package net.sf.jasperreports.engine.fill;

import java.awt.Color;
import java.io.Serializable;
import java.util.Random;

import net.sf.jasperreports.engine.JRCommonElement;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.util.JRStyleResolver;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRTemplateElement.java,v 1.1 2008/09/29 16:20:00 guehene Exp $
 */
public abstract class JRTemplateElement implements JRCommonElement, Serializable, JRPropertiesHolder
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private static final Random randomId = new Random();

	/**
	 *
	 */
	private String key;
	private Byte mode = null;
	private Color forecolor = null;
	private Color backcolor = null;

	protected JROrigin origin = null;

	protected JRDefaultStyleProvider defaultStyleProvider;
	protected JRStyle parentStyle = null;

	private final String id;
	
	private JRPropertiesMap propertiesMap;
	
	/**
	 *
	 */
	protected JRTemplateElement(JROrigin origin, JRDefaultStyleProvider defaultStyleProvider)
	{
		this.origin = origin;
		this.defaultStyleProvider = defaultStyleProvider;
		this.id = createId();
	}

	/**
	 *
	 */
	protected JRTemplateElement(JROrigin origin, JRElement element)
	{
		this.origin = origin;
		setElement(element);
		this.id = createId();
	}

	protected JRTemplateElement(String id)
	{
		this.id = id;
	}
	
	private String createId()
	{
		return System.identityHashCode(this) + "_" + System.currentTimeMillis() + "_" + randomId.nextInt();
	}


	/**
	 *
	 */
	protected void setElement(JRElement element)
	{
		this.parentStyle = element.getStyle();
		
		setKey(element.getKey());
		
		this.mode = element.getOwnMode();
		this.forecolor = element.getOwnForecolor();
		this.backcolor = element.getOwnBackcolor();
	}
	
	/**
	 *
	 */
	public JROrigin getOrigin()
	{
		return this.origin;
	}

	/**
	 *
	 */
	public JRDefaultStyleProvider getDefaultStyleProvider()
	{
		return this.defaultStyleProvider;
	}

	/**
	 *
	 */
	public JRStyle getStyle()
	{
		return this.parentStyle;
	}

	/**
	 *
	 */
	protected JRStyle getBaseStyle()
	{
		if (this.parentStyle != null)
			return this.parentStyle;
		if (this.defaultStyleProvider != null)
			return this.defaultStyleProvider.getDefaultStyle();
		return null;
	}

	/**
	 *
	 */
	public byte getMode()
	{
		return JRStyleResolver.getMode(this, JRElement.MODE_OPAQUE);
	}
		
	/**
	 *
	 */
	public Byte getOwnMode()
	{
		return this.mode;
	}
	
	/**
	 *
	 */
	protected void setMode(byte mode)
	{
		this.mode = new Byte(mode);
	}
	
	/**
	 *
	 */
	protected void setMode(Byte mode)
	{
		this.mode = mode;
	}
	
	/**
	 *
	 */
	public Color getForecolor()
	{
		return JRStyleResolver.getForecolor(this);
	}
	
	/**
	 *
	 */
	public Color getOwnForecolor()
	{
		return this.forecolor;
	}
	
	/**
	 *
	 */
	protected void setForecolor(Color forecolor)
	{
		this.forecolor = forecolor;
	}
	
	/**
	 *
	 */
	public Color getBackcolor()
	{
		return JRStyleResolver.getBackcolor(this);
	}
	
	/**
	 *
	 */
	public Color getOwnBackcolor()
	{
		return this.backcolor;
	}
	
	/**
	 *
	 */
	protected void setBackcolor(Color backcolor)
	{
		this.backcolor = backcolor;
	}
	
	/**
	 *
	 */
	public String getId()
	{
		return this.id;
	}

	
	public String getKey()
	{
		return this.key;
	}

	
	public void setKey(String key)
	{
		this.key = key;
	}


	/**
	 * Returns null as external style references are not allowed for print objects.
	 */
	public String getStyleNameReference()
	{
		return null;
	}

	
	public synchronized boolean hasProperties()
	{
		return this.propertiesMap != null && this.propertiesMap.hasProperties();
	}

	public synchronized JRPropertiesMap getPropertiesMap()
	{
		if (this.propertiesMap == null)
		{
			this.propertiesMap = new JRPropertiesMap();
		}
		return this.propertiesMap;
	}

	public JRPropertiesHolder getParentProperties()
	{
		return null;
	}
}
