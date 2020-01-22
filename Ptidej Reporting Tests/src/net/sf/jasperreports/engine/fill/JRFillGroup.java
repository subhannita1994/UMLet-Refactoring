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

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRVariable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRFillGroup.java,v 1.1 2008/09/29 16:20:01 guehene Exp $
 */
public class JRFillGroup implements JRGroup
{


	/**
	 *
	 */
	protected JRGroup parent = null;

	/**
	 *
	 */
	private JRFillBand groupHeader = null;
	private JRFillBand groupFooter = null;
	private JRVariable countVariable = null;

	/**
	 *
	 */
	private boolean hasChanged = true;
	private boolean isTopLevelChange = false;
	private boolean isHeaderPrinted = false;
	private boolean isFooterPrinted = true;
	

	/**
	 *
	 */
	public JRFillGroup(
		JRGroup group, 
		JRFillObjectFactory factory
		)
	{
		factory.put(group, this);

		this.parent = group;

		String reportName = factory.getFiller().isSubreport() ? factory.getFiller().getJasperReport().getName() : null;
		
		this.groupHeader = factory.getBand(group.getGroupHeader());
		if (this.groupHeader != factory.getFiller().missingFillBand)
		{
			this.groupHeader.setOrigin(
				new JROrigin(
					reportName,
					group.getName(),
					JROrigin.GROUP_HEADER
					)
				);
		}

		this.groupFooter = factory.getBand(group.getGroupFooter());
		if (this.groupFooter != factory.getFiller().missingFillBand)
		{
			this.groupFooter.setOrigin(
				new JROrigin(
					reportName,
					group.getName(),
					JROrigin.GROUP_FOOTER
					)
				);
		}

		this.countVariable = factory.getVariable(group.getCountVariable());
	}


	/**
	 *
	 */
	public String getName()
	{
		return this.parent.getName();
	}
	
	/**
	 *
	 */
	public JRExpression getExpression()
	{
		return this.parent.getExpression();
	}
		
	/**
	 *
	 */
	public boolean isStartNewColumn()
	{
		return this.parent.isStartNewColumn();
	}
		
	/**
	 *
	 */
	public void setStartNewColumn(boolean isStart)
	{
		this.parent.setStartNewColumn(isStart);
	}
		
	/**
	 *
	 */
	public boolean isStartNewPage()
	{
		return this.parent.isStartNewPage();
	}
		
	/**
	 *
	 */
	public void setStartNewPage(boolean isStart)
	{
		this.parent.setStartNewPage(isStart);
	}
		
	/**
	 *
	 */
	public boolean isResetPageNumber()
	{
		return this.parent.isResetPageNumber();
	}
		
	/**
	 *
	 */
	public void setResetPageNumber(boolean isReset)
	{
		this.parent.setResetPageNumber(isReset);
	}
		
	/**
	 *
	 */
	public boolean isReprintHeaderOnEachPage()
	{
		return this.parent.isReprintHeaderOnEachPage();
	}
		
	/**
	 *
	 */
	public void setReprintHeaderOnEachPage(boolean isReprint)
	{
	}
		
	/**
	 *
	 */
	public int getMinHeightToStartNewPage()
	{
		return this.parent.getMinHeightToStartNewPage();
	}
		
	/**
	 *
	 */
	public void setMinHeightToStartNewPage(int minHeight)
	{
	}
		
	/**
	 *
	 */
	public JRBand getGroupHeader()
	{
		return this.groupHeader;
	}
		
	/**
	 *
	 */
	public JRBand getGroupFooter()
	{
		return this.groupFooter;
	}
		
	/**
	 *
	 */
	public JRVariable getCountVariable()
	{
		return this.countVariable;
	}
	
	/**
	 *
	 */
	public boolean hasChanged()
	{
		return this.hasChanged;
	}
		
	/**
	 *
	 */
	public void setHasChanged(boolean hasChanged)
	{
		this.hasChanged = hasChanged;
	}

	/**
	 *
	 */
	public boolean isTopLevelChange()
	{
		return this.isTopLevelChange;
	}
		
	/**
	 *
	 */
	public void setTopLevelChange(boolean isTopLevelChange)
	{
		this.isTopLevelChange = isTopLevelChange;
	}

	/**
	 *
	 */
	public boolean isHeaderPrinted()
	{
		return this.isHeaderPrinted;
	}
			
	/**
	 *
	 */
	public void setHeaderPrinted(boolean isHeaderPrinted)
	{
		this.isHeaderPrinted = isHeaderPrinted;
	}

	/**
	 *
	 */
	public boolean isFooterPrinted()
	{
		return this.isFooterPrinted;
	}
		
	/**
	 *
	 */
	public void setFooterPrinted(boolean isFooterPrinted)
	{
		this.isFooterPrinted = isFooterPrinted;
	}

	/**
	 *
	 */
	public Object clone() 
	{
		return null;
	}

}
