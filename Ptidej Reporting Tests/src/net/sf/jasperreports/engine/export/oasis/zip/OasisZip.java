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
package net.sf.jasperreports.engine.export.oasis.zip;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.sf.jasperreports.engine.export.oasis.JROpenDocumentExporterNature;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: OasisZip.java,v 1.1 2008/09/29 16:21:52 guehene Exp $
 */
public abstract class OasisZip
{

	/**
	 * 
	 */
	private List oasisZipEntries = null;

	/**
	 * 
	 */
	private OasisZipEntry contentEntry = null;
	private OasisZipEntry stylesEntry = null;
	
	public OasisZip() throws IOException
	{
		this(JROpenDocumentExporterNature.ODT_NATURE);
	}
	/**
	 * 
	 */
	public OasisZip(byte openDocumentNature) throws IOException
	{
		
		this.oasisZipEntries = new ArrayList();

		this.contentEntry = createEntry("content.xml");
		this.oasisZipEntries.add(this.contentEntry);
		
		this.oasisZipEntries.add(new EmptyOasisZipEntry("meta.xml"));
		this.oasisZipEntries.add(new EmptyOasisZipEntry("settings.xml"));

		this.stylesEntry = createEntry("styles.xml");
		this.oasisZipEntries.add(this.stylesEntry);
		
		String mimetype;
		
		switch(openDocumentNature)
		{
			case JROpenDocumentExporterNature.ODS_NATURE:
				mimetype = "spreadsheet";
				break;
			case JROpenDocumentExporterNature.ODT_NATURE:
			default:
				mimetype = "text";
		}
		OasisZipEntry mimeEntry = createEntry("mimetype");
		Writer mimeWriter = null;
		try
		{
			mimeWriter = mimeEntry.getWriter();
			mimeWriter.write("application/vnd.oasis.opendocument."+mimetype);
			mimeWriter.flush();
			this.oasisZipEntries.add(mimeEntry);
		}
		finally
		{
			if (mimeWriter != null)
			{
				try
				{
					mimeWriter.close();
				}
				catch (IOException e)
				{
				}
			}
		}

		OasisZipEntry manifestEntry = createEntry("META-INF/manifest.xml");
		Writer manifestWriter = null;
		try
		{
			manifestWriter = manifestEntry.getWriter();
			manifestWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?> \r\n");
			manifestWriter.write("<!DOCTYPE manifest:manifest PUBLIC \"-//OpenOffice.org//DTD Manifest 1.0//EN\" \"Manifest.dtd\"> \r\n");
			manifestWriter.write("<manifest:manifest xmlns:manifest=\"urn:oasis:names:tc:opendocument:xmlns:manifest:1.0\"> \r\n");
			manifestWriter.write("  <manifest:file-entry manifest:media-type=\"application/vnd.oasis.opendocument." + mimetype + "\" manifest:full-path=\"/\"/> \r\n");
			manifestWriter.write("  <manifest:file-entry manifest:media-type=\"application/vnd.sun.xml.ui.configuration\" manifest:full-path=\"Configurations2/\"/> \r\n");
			manifestWriter.write("  <manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Pictures/\"/> \r\n");
			manifestWriter.write("  <manifest:file-entry manifest:media-type=\"text/xml\" manifest:full-path=\"content.xml\"/> \r\n");
			manifestWriter.write("  <manifest:file-entry manifest:media-type=\"text/xml\" manifest:full-path=\"styles.xml\"/> \r\n");
			manifestWriter.write("  <manifest:file-entry manifest:media-type=\"text/xml\" manifest:full-path=\"meta.xml\"/> \r\n");
			manifestWriter.write("  <manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Thumbnails/thumbnail.png\"/> \r\n");
			manifestWriter.write("  <manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Thumbnails/\"/> \r\n");
			manifestWriter.write("  <manifest:file-entry manifest:media-type=\"text/xml\" manifest:full-path=\"settings.xml\"/> \r\n");
			manifestWriter.write("</manifest:manifest> \r\n");
			manifestWriter.flush();
			this.oasisZipEntries.add(manifestEntry);
		}
		finally
		{
			if (mimeWriter != null)
			{
				try
				{
					mimeWriter.close();
				}
				catch (IOException e)
				{
				}
			}
		}
	}
	
	/**
	 *
	 */
	public abstract OasisZipEntry createEntry(String name);
	
	/**
	 *
	 */
	public OasisZipEntry getContentEntry()
	{
		return this.contentEntry;
	}
	
	/**
	 *
	 */
	public OasisZipEntry getStylesEntry()
	{
		return this.stylesEntry;
	}
	
	/**
	 *
	 */
	public void addEntry(OasisZipEntry entry)
	{
		this.oasisZipEntries.add(entry);
	}
	
	/**
	 *
	 */
	public void zipEntries(OutputStream os) throws IOException
	{
		ZipOutputStream zipos = new ZipOutputStream(os);
		zipos.setMethod(ZipOutputStream.DEFLATED);
		
		for (int i = 0; i < this.oasisZipEntries.size(); i++) 
		{
			OasisZipEntry oasisZipEntry = (OasisZipEntry)this.oasisZipEntries.get(i);
			ZipEntry zipEntry = new ZipEntry(oasisZipEntry.getName());
			zipos.putNextEntry(zipEntry);
			oasisZipEntry.writeData(zipos);
		}
		
		zipos.flush();
		zipos.finish();
	}
	
	/**
	 *
	 */
	public void dispose()
	{
		for (int i = 0; i < this.oasisZipEntries.size(); i++) 
		{
			OasisZipEntry oasisZipEntry = (OasisZipEntry)this.oasisZipEntries.get(i);
			oasisZipEntry.dispose();
		}
	}
	
}
