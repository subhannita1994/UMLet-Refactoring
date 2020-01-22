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
 * Ryan Johnson - delscovich@users.sourceforge.net
 * Carlton Moore - cmoore79@users.sourceforge.net
 *  Petr Michalek - pmichalek@users.sourceforge.net
 */
package net.sf.jasperreports.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyListener;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.util.JRProperties;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRViewer.java,v 1.1 2008/09/29 16:22:11 guehene Exp $
 */
public class JRViewer extends javax.swing.JPanel implements JRViewerListener
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 * Maximum size (in pixels) of a buffered image that would be used by {@link JRViewer JRViewer} to render a report page.
	 * <p>
	 * If rendering a report page would require an image larger than this threshold
	 * (i.e. image width x image height > maximum size), the report page will be rendered directly on the viewer component.
	 * </p>
	 * <p>
	 * If this property is zero or negative, buffered images will never be user to render a report page.
	 * By default, this property is set to 0.
	 * </p>
	 */
	public static final String VIEWER_RENDER_BUFFER_MAX_SIZE = JRProperties.PROPERTY_PREFIX + "viewer.render.buffer.max.size";

	protected JRViewerController viewerContext;

	/** Creates new form JRViewer */
	public JRViewer(String fileName, boolean isXML) throws JRException
	{
		this(fileName, isXML, null);
	}


	/** Creates new form JRViewer */
	public JRViewer(InputStream is, boolean isXML) throws JRException
	{
		this(is, isXML, null);
	}


	/** Creates new form JRViewer */
	public JRViewer(JasperPrint jrPrint)
	{
		this(jrPrint, null);
	}


	/** Creates new form JRViewer */
	public JRViewer(String fileName, boolean isXML, Locale locale) throws JRException
	{
		this(fileName, isXML, locale, null);
	}


	/** Creates new form JRViewer */
	public JRViewer(InputStream is, boolean isXML, Locale locale) throws JRException
	{
		this(is, isXML, locale, null);
	}


	/** Creates new form JRViewer */
	public JRViewer(JasperPrint jrPrint, Locale locale)
	{
		this(jrPrint, locale, null);
	}


	/** Creates new form JRViewer */
	public JRViewer(String fileName, boolean isXML, Locale locale, ResourceBundle resBundle) throws JRException
	{
		initViewerContext(locale, resBundle);

		initComponents();

		this.viewerContext.loadReport(fileName, isXML);
		
		this.tlbToolBar.init();
	}


	/** Creates new form JRViewer */
	public JRViewer(InputStream is, boolean isXML, Locale locale, ResourceBundle resBundle) throws JRException
	{
		initViewerContext(locale, resBundle);

		initComponents();

		this.viewerContext.loadReport(is, isXML);
		
		this.tlbToolBar.init();
	}


	/** Creates new form JRViewer */
	public JRViewer(JasperPrint jrPrint, Locale locale, ResourceBundle resBundle)
	{
		initViewerContext(locale, resBundle);

		initComponents();

		this.viewerContext.loadReport(jrPrint);
		
		this.tlbToolBar.init();
	}

	protected void initViewerContext(Locale locale, ResourceBundle resBundle)
	{
		this.viewerContext = new JRViewerController(locale, resBundle);
		setLocale(this.viewerContext.getLocale());
		this.viewerContext.addListener(this);
	}


	/**
	 *
	 */
	public void clear()
	{
		emptyContainer(this);
		this.viewerContext.clear();
	}


	/**
	 *
	 */
	protected String getBundleString(String key)
	{
		return this.viewerContext.getBundleString(key);
	}


	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
	private void initComponents() {
		this.tlbToolBar = createToolbar();

		this.pnlMain = createViewerPanel();

		this.lblStatus = new javax.swing.JLabel();
		this.pnlStatus = new javax.swing.JPanel();

		this.pnlStatus.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

		this.lblStatus.setFont(new java.awt.Font("Dialog", 1, 10));
		this.lblStatus.setText("Page i of n");
		this.pnlStatus.add(this.lblStatus);

		setLayout(new java.awt.BorderLayout());

		add(this.tlbToolBar, java.awt.BorderLayout.NORTH);
		add(this.pnlMain, java.awt.BorderLayout.CENTER);
		add(this.pnlStatus, java.awt.BorderLayout.SOUTH);
		
		KeyListener keyNavigationListener = this.pnlMain.getKeyNavigationListener();
		addKeyListener(keyNavigationListener);
		this.tlbToolBar.addComponentKeyListener(keyNavigationListener);
	}
	// </editor-fold>//GEN-END:initComponents

	protected JRViewerToolbar createToolbar()
	{
		return new JRViewerToolbar(this.viewerContext);
	}

	protected JRViewerPanel createViewerPanel()
	{
		return new JRViewerPanel(this.viewerContext);
	}

	public void setFitWidthZoomRatio()
	{
		this.pnlMain.setFitWidthZoomRatio();
	}

	public void setFitPageZoomRatio()
	{
		this.pnlMain.setFitPageZoomRatio();
	}

	/**
	*/
	public int getPageIndex()
	{
		return this.viewerContext.getPageIndex();
	}


	/**
	*/
	private void emptyContainer(Container container)
	{
		Component[] components = container.getComponents();

		if (components != null)
		{
			for(int i = 0; i < components.length; i++)
			{
				if (components[i] instanceof Container)
				{
					emptyContainer((Container)components[i]);
				}
			}
		}

		components = null;
		container.removeAll();
		container = null;
	}

	public void setZoomRatio(float zoomRatio)
	{
		this.viewerContext.setZoomRatio(zoomRatio);
	}

	public void pageChanged()
	{
		if (this.viewerContext.hasPages())
		{
			this.lblStatus.setText(
				MessageFormat.format(
					getBundleString("page"),
					new Object[]{new Integer(this.viewerContext.getPageIndex() + 1), 
						new Integer(this.viewerContext.getPageCount())}
					)
				);
		}
		else
		{
			this.lblStatus.setText("");
		}
	}
	
	public void viewerEvent(JRViewerEvent event)
	{
		switch (event.getCode())
		{
		case JRViewerEvent.EVENT_PAGE_CHANGED:
			pageChanged();
			break;
		case JRViewerEvent.EVENT_REPORT_LOAD_FAILED:
			JOptionPane.showMessageDialog(this, getBundleString("error.loading"));
			break;
		}
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	protected javax.swing.JLabel lblStatus;
	private JRViewerPanel pnlMain;
	protected javax.swing.JPanel pnlStatus;
	protected JRViewerToolbar tlbToolBar;
	// End of variables declaration//GEN-END:variables

}
