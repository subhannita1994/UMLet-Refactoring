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
package net.sf.jasperreports.engine.export;

/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id: FontKey.java,v 1.1 2008/09/29 16:20:46 guehene Exp $
 */
public class FontKey
{
	private String fontName;
	private boolean isBold;
	private boolean isItalic;

	public FontKey(String fontName, boolean bold, boolean italic)
	{
		this.fontName = fontName;
		this.isBold = bold;
		this.isItalic = italic;
	}

	public String getFontName()
	{
		return this.fontName;
	}

	public boolean isBold()
	{
		return this.isBold;
	}

	public boolean isItalic()
	{
		return this.isItalic;
	}

	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		final FontKey key = (FontKey) o;

		if (this.isBold != key.isBold) return false;
		if (this.isItalic != key.isItalic) return false;
		if (this.fontName != null ? !this.fontName.equals(key.fontName) : key.fontName != null) return false;

		return true;
	}

	public int hashCode()
	{
		int result;
		result = (this.fontName != null ? this.fontName.hashCode() : 0);
		result = 29 * result + (this.isBold ? 1 : 0);
		result = 29 * result + (this.isItalic ? 1 : 0);
		return result;
	}
}
