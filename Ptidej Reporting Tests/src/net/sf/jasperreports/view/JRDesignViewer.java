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

package net.sf.jasperreports.view;

import java.io.InputStream;

import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.convert.ReportConverter;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRDesignViewer.java,v 1.1 2008/09/29 16:21:04 guehene Exp $
 */
public class JRDesignViewer extends JRViewer
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	/** Creates new form JRDesignViewer */
	public JRDesignViewer(String fileName, boolean isXML) throws JRException
	{
		super(fileName, isXML);
		hideUnusedComponents();
	}
	
	/** Creates new form JRDesignViewer */
	public JRDesignViewer(InputStream is, boolean isXML) throws JRException
	{
		super(is, isXML);
		hideUnusedComponents();
	}
	
	/** Creates new form JRDesignViewer */
	public JRDesignViewer(JRReport report) throws JRException
	{
		super(new ReportConverter(report, false, true).getJasperPrint());
		//reconfigureReloadButton();
		hideUnusedComponents();
	}
	
	private void hideUnusedComponents()
	{
		this.btnFirst.setVisible(false);
		this.btnLast.setVisible(false);
		this.btnPrevious.setVisible(false);	
		this.btnNext.setVisible(false);
		this.txtGoTo.setVisible(false);
		this.pnlStatus.setVisible(false);
	}

	void btnReloadActionPerformed(java.awt.event.ActionEvent evt)
	{
		if (this.type == TYPE_FILE_NAME)
		{
			try
			{
				loadReport(this.reportFileName, this.isXML);
				forceRefresh();
			}
			catch (JRException e)
			{
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error loading report design. See console for details.");
			}
		}
	}


	/**
	*/
	protected void loadReport(String fileName, boolean isXmlReport) throws JRException
	{
		if (isXmlReport)
		{
			JasperDesign jasperDesign = JRXmlLoader.load(fileName);
			setReport(jasperDesign);
		}
		else
		{
			setReport((JRReport) JRLoader.loadObject(fileName));
		}
		this.type = TYPE_FILE_NAME;
		this.isXML = isXmlReport;
		this.reportFileName = fileName;
	}


	/**
	*/
	protected void loadReport(InputStream is, boolean isXmlReport) throws JRException
	{
		if (isXmlReport)
		{
			JasperDesign jasperDesign = JRXmlLoader.load(is);
			setReport(jasperDesign);
		}
		else
		{
			setReport((JRReport) JRLoader.loadObject(is));
		}
		this.type = TYPE_INPUT_STREAM;
		this.isXML = isXmlReport;
	}


	/**
	*/
	protected void loadReport(JRReport rep) throws JRException
	{
		setReport(rep);
		this.type = TYPE_OBJECT;
		this.isXML = false;
	}
	
	private void setReport(JRReport report) throws JRException
	{
		this.jasperPrint = new ReportConverter(report, false, true).getJasperPrint();		
	}

	/**
	*/
	protected JRGraphics2DExporter getGraphics2DExporter() throws JRException
	{
		return 
			new JRGraphics2DExporter()
			{
				protected void setDrawers()
				{
					super.setDrawers();
					this.frameDrawer.setClip(true);//FIXMENOW thick border of margin elements is clipped
				}
			};
	}
	
}
