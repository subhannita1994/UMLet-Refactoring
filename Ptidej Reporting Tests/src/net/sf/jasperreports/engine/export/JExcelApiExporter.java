/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 *
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */

/*
 * Contributor: Manuel Paul <mpaul@ratundtat.com>,
 *				Rat & Tat Beratungsgesellschaft mbH,
 *				Muehlenkamp 6c,
 *				22303 Hamburg,
 *				Germany.
 */
package net.sf.jasperreports.engine.export;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import jxl.CellView;
import jxl.JXLException;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.biff.DisplayFormat;
import jxl.format.Alignment;
import jxl.format.BoldStyle;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.Orientation;
import jxl.format.PageOrientation;
import jxl.format.PaperSize;
import jxl.format.Pattern;
import jxl.format.RGB;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Blank;
import jxl.write.DateTime;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableHyperlink;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.CellValue;
import jxl.write.biff.RowsExceededException;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRCommonGraphicElement;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintGraphicElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.data.BooleanTextValue;
import net.sf.jasperreports.engine.export.data.DateTextValue;
import net.sf.jasperreports.engine.export.data.NumberTextValue;
import net.sf.jasperreports.engine.export.data.StringTextValue;
import net.sf.jasperreports.engine.export.data.TextValue;
import net.sf.jasperreports.engine.export.data.TextValueHandler;
import net.sf.jasperreports.engine.util.JRImageLoader;
import net.sf.jasperreports.engine.util.JRStyledText;

import org.apache.commons.collections.ReferenceMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Manuel Paul (mpaul@ratundtat.com)
 * @version $Id: JExcelApiExporter.java,v 1.1 2008/09/29 16:20:46 guehene Exp $
 */
public class JExcelApiExporter extends JRXlsAbstractExporter
{

	private static final Log log = LogFactory.getLog(JExcelApiExporter.class);

	protected static final Colour WHITE = Colour.WHITE;
	protected static final Colour BLACK = Colour.BLACK;

	protected static final String EMPTY_SHEET_NAME = "Sheet1";

	private static Map colorsCache = new ReferenceMap();

	private static Colour[] FIXED_COLOURS = new Colour[] {WHITE, BLACK, Colour.PALETTE_BLACK,
		Colour.DEFAULT_BACKGROUND, Colour.DEFAULT_BACKGROUND1, Colour.AUTOMATIC, Colour.UNKNOWN};

	private Map loadedCellStyles = new HashMap();

	private WritableWorkbook workbook = null;

	private WritableSheet sheet = null;

	private Pattern backgroundMode = Pattern.SOLID;

	private Map numberFormats;
	private Map dateFormats;

	protected Map formatPatternsMap = null;

	protected boolean createCustomPalette;
	protected Map workbookColours = new HashMap();
	protected Map usedColours = new HashMap();
	protected String password;
	
	protected ExporterNature nature = null;
	

	public JExcelApiExporter()
	{
		this.numberFormats = new HashMap();
		this.dateFormats = new HashMap();
	}

	protected void setParameters()
	{
		super.setParameters();

		this.formatPatternsMap = (Map)getParameter(JRXlsAbstractExporterParameter.FORMAT_PATTERNS_MAP);

		this.createCustomPalette = 
			getBooleanParameter(
				JExcelApiExporterParameter.CREATE_CUSTOM_PALETTE, 
				JExcelApiExporterParameter.PROPERTY_CREATE_CUSTOM_PALETTE, 
				false
				); 
			
		if (this.createCustomPalette)
		{
			initCustomPalette();
		}

		this.password = 
			getStringParameter(
				JExcelApiExporterParameter.PASSWORD,
				JExcelApiExporterParameter.PROPERTY_PASSWORD
				);
		
		this.nature = new JExcelApiExporterNature(this.filter, this.isIgnoreGraphics, this.isIgnorePageMargins);
	}

	protected void initCustomPalette()
	{
		//mark "fixed" colours as always used
		for (int i = 0; i < FIXED_COLOURS.length; i++)
		{
			Colour colour = FIXED_COLOURS[i];
			setColourUsed(colour);
		}
	}

	protected void setColourUsed(Colour colour)
	{
		this.usedColours.put(colour, colour.getDefaultRGB());
	}

	protected void setColourUsed(Colour colour, Color reportColour)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Modifying palette colour " + colour.getValue() + " to " + reportColour);
		}

		int red = reportColour.getRed();
		int green = reportColour.getGreen();
		int blue = reportColour.getBlue();

		this.workbook.setColourRGB(colour, red, green, blue);

		RGB customRGB = new RGB(red, green, blue);
		this.usedColours.put(colour, customRGB);
	}

	protected void setBackground()
	{
		if (this.isWhitePageBackground)
		{
			this.backgroundMode = Pattern.SOLID;
		}
		else
		{
			this.backgroundMode = Pattern.NONE;
		}
	}

	protected void openWorkbook(OutputStream os) throws JRException
	{
		try
		{
			this.workbook = Workbook.createWorkbook(os);
		}
		catch (IOException e)
		{
			throw new JRException("Error generating XLS report : " + this.jasperPrint.getName(), e);
		}
	}

	protected void createSheet(String name)
	{
		this.sheet = this.workbook.createSheet(name, Integer.MAX_VALUE);
		setSheetSettings(this.sheet);
	}

	protected void closeWorkbook(OutputStream os) throws JRException
	{
		if (this.sheet == null)//empty document
		{
			//creating an empty sheet so that write() doesn't fail
			this.workbook.createSheet(EMPTY_SHEET_NAME, Integer.MAX_VALUE);
		}

		try
		{
			this.workbook.write();
			this.workbook.close();
		}
		catch (IOException e)
		{
			throw new JRException("Error generating XLS report : " + this.jasperPrint.getName(), e);
		}
		catch (WriteException e)
		{
			throw new JRException("Error generating XLS report : " + this.jasperPrint.getName(), e);
		}
	}

	protected void setColumnWidth(int col, int width)
	{
		CellView cv = new CellView();
		cv.setSize(width);
		this.sheet.setColumnView(col, cv);
	}

	protected void setRowHeight(int y, int lastRowHeight) throws JRException
	{
		try
		{
			this.sheet.setRowView(y, (lastRowHeight * 20)); // twips
		}
		catch (RowsExceededException e)
		{
			throw new JRException("Error generating XLS report : " + this.jasperPrint.getName(), e);
		}
	}

	protected void setCell(int x, int y)
	{
	}

	protected void removeColumn(int col)
	{
		this.sheet.removeColumn(col);
	}

	protected void addBlankCell(JRExporterGridCell gridCell, int colIndex, int rowIndex) throws JRException
	{
		Colour forecolor = BLACK;
		if (gridCell.getForecolor() != null)
		{
			forecolor = getWorkbookColour(gridCell.getForecolor());
		}

		Pattern mode = this.backgroundMode;
		Colour backcolor = WHITE;
		if (gridCell.getCellBackcolor() != null)
		{
			mode = Pattern.SOLID;
			backcolor = getWorkbookColour(gridCell.getCellBackcolor());
		}

		WritableFont cellFont = getLoadedFont(getDefaultFont(), forecolor.getValue());
		WritableCellFormat cellStyle = 
			getLoadedCellStyle(
				mode, 
				backcolor,
				cellFont, 
				gridCell
				);

		try
		{
			this.sheet.addCell(new Blank(colIndex, rowIndex, cellStyle));
		}
		catch (RowsExceededException e)
		{
			throw new JRException("Error generating XLS report : " + this.jasperPrint.getName(), e);//FIXMENOW raise same exception everywhere
		}
		catch (WriteException e)
		{
			throw new JRException("Error generating XLS report : " + this.jasperPrint.getName(), e);
		}
	}

	protected void exportLine(JRPrintLine line, JRExporterGridCell gridCell, int col, int row) throws JRException
	{
		addMergeRegion(gridCell, col, row);

		Colour forecolor2 = getWorkbookColour(line.getLinePen().getLineColor());
		WritableFont cellFont2 = getLoadedFont(getDefaultFont(), forecolor2.getValue());
		
		Colour backcolor = WHITE;
		Pattern mode = this.backgroundMode;

		if (gridCell.getCellBackcolor() != null)
		{
			mode = Pattern.SOLID;
			backcolor = getWorkbookColour(gridCell.getCellBackcolor());
		}

		int side = BoxStyle.TOP;
		float ratio = line.getWidth() / line.getHeight();
		if (ratio > 1)
		{
			if (line.getDirection() == JRLine.DIRECTION_TOP_DOWN)
			{
				side = BoxStyle.TOP;
			}
			else
			{
				side = BoxStyle.BOTTOM;
			}
		}
		else
		{
			if (line.getDirection() == JRLine.DIRECTION_TOP_DOWN)
			{
				side = BoxStyle.LEFT;
			}
			else
			{
				side = BoxStyle.RIGHT;
			}
		}
		BoxStyle boxStyle = new BoxStyle(side, line.getLinePen());
		
		WritableCellFormat cellStyle2 = 
			getLoadedCellStyle(
				mode, 
				backcolor, 
				cellFont2, 
				boxStyle
				);

		Blank cell2 = new Blank(col, row, cellStyle2);

		try
		{
			this.sheet.addCell(cell2);
		}
		catch (Exception e)
		{
			throw new JRException("Can't add cell.", e);
		}
	}

	protected void exportRectangle(JRPrintGraphicElement element, JRExporterGridCell gridCell, int col, int row) throws JRException
	{
		addMergeRegion(gridCell, col, row);

		Colour backcolor = WHITE;
		Pattern mode = this.backgroundMode;

		if (gridCell.getCellBackcolor() != null)
		{
			mode = Pattern.SOLID;
			backcolor = getWorkbookColour(gridCell.getCellBackcolor());
		}

		Colour forecolor = getWorkbookColour(element.getLinePen().getLineColor());
		WritableFont cellFont2 = getLoadedFont(getDefaultFont(), forecolor.getValue());
		WritableCellFormat cellStyle2 = 
			getLoadedCellStyle(
				mode, 
				backcolor, 
				cellFont2, 
				gridCell
				);
		
		Blank cell2 = new Blank(col, row, cellStyle2);

		try
		{
			this.sheet.addCell(cell2);
		}
		catch (Exception e)
		{
			throw new JRException("Can't add cell.", e);
		}
	}

	protected void exportText(JRPrintText text, JRExporterGridCell gridCell, int col, int row) throws JRException
	{
		addMergeRegion(gridCell, col, row);

		JRStyledText styledText = getStyledText(text);

		if (styledText != null)
		{
			Colour forecolor = getWorkbookColour(text.getForecolor());
			WritableFont cellFont = this.getLoadedFont(text, forecolor.getValue());

			TextAlignHolder alignment = getTextAlignHolder(text);
			int horizontalAlignment = getHorizontalAlignment(alignment);
			int verticalAlignment = getVerticalAlignment(alignment);
			int rotation = getRotation(alignment);

			Pattern mode = this.backgroundMode;
			Colour backcolor = WHITE;

			if (gridCell.getCellBackcolor() != null)
			{
				mode = Pattern.SOLID;
				backcolor = getWorkbookColour(gridCell.getCellBackcolor());
			}

			StyleInfo baseStyle =
				new StyleInfo(
					mode, 
					backcolor,
					horizontalAlignment, 
					verticalAlignment,
					rotation, 
					cellFont,
					gridCell
					);

			String textStr = styledText.getText();

			String href = null;
			JRHyperlinkProducer customHandler = getCustomHandler(text);
			if (customHandler == null)
			{
				switch (text.getHyperlinkType())
				{
					case JRHyperlink.HYPERLINK_TYPE_REFERENCE:
					{
						href = text.getHyperlinkReference();
						break;
					}
					case JRHyperlink.HYPERLINK_TYPE_LOCAL_ANCHOR :
					case JRHyperlink.HYPERLINK_TYPE_LOCAL_PAGE :
					case JRHyperlink.HYPERLINK_TYPE_REMOTE_ANCHOR :
					case JRHyperlink.HYPERLINK_TYPE_REMOTE_PAGE :
					case JRHyperlink.HYPERLINK_TYPE_NONE:
					default:
					{
					}
				}
			}
			else
			{
				href = customHandler.getHyperlink(text);
			}

			try
			{
				if (href != null)
				{
					try
					{
						URL url = new URL(href);
						WritableHyperlink hyperlink = new WritableHyperlink(col, row, col, row, url);
						this.sheet.addHyperlink(hyperlink);
					}
					catch (MalformedURLException e)
					{
						log.warn("Reference \"" + href + "\" could not be parsed as URL: " + e);
					}
				}
				addCell(col, row, text, textStr, baseStyle);
			}
			catch (Exception e)
			{
				throw new JRException("Can't add cell.", e);
			}
		}
	}

	protected void addCell(int x, int y, JRPrintText text, String textStr, StyleInfo baseStyle) throws WriteException, RowsExceededException, JRException
	{
		String textFormula = text.getPropertiesMap().getProperty(JRAbstractExporter.PROPERTY_CELL_FORMULA);
		if( textFormula != null)
		{
			Formula formula = null;
			try
			{
				formula = new Formula(x, y, textFormula, getLoadedCellStyle(baseStyle));
			}
			catch(Exception e)
			{
				if(log.isWarnEnabled())
				{
					log.warn(e.getMessage());
				}
			}
			if(formula != null)
			{
				this.sheet.addCell(formula);
				return;
			}
		}
		CellValue cellValue;
		if (this.isDetectCellType)
		{
			cellValue = getDetectedCellValue(x, y, text, textStr, baseStyle);
		}
		else if (this.isAutoDetectCellType)
		{
			cellValue = getAutoDetectedCellValue(x, y, textStr, baseStyle);
		}
		else
		{
			cellValue = getLabelCell(x, y, textStr, baseStyle);
		}

		this.sheet.addCell(cellValue);
	}


	protected CellValue getDetectedCellValue(int x, int y, JRPrintText text, String textStr, StyleInfo baseStyle) throws JRException
	{
		TextValue textValue = getTextValue(text, textStr);
		CellTextValueHandler handler = new CellTextValueHandler(x, y, baseStyle);
		textValue.handle(handler);
		return handler.getResult();
	}


	protected class CellTextValueHandler implements TextValueHandler
	{
		private final int x;
		private final int y;
		private final StyleInfo baseStyle;

		private CellValue result;

		public CellTextValueHandler(int x, int y, StyleInfo baseStyle)
		{
			this.x = x;
			this.y = y;
			this.baseStyle = baseStyle;
		}

		public void handle(StringTextValue textValue) throws JRException
		{
			WritableCellFormat cellStyle = getLoadedCellStyle(this.baseStyle);
			this.result = new Label(this.x, this.y, textValue.getText(), cellStyle);
		}

		public void handle(NumberTextValue textValue) throws JRException
		{
			if (textValue.getPattern() != null)
			{
				this.baseStyle.setDisplayFormat(getNumberFormat(textValue.getPattern()));
			}

			WritableCellFormat cellStyle = getLoadedCellStyle(this.baseStyle);
			if (textValue.getValue() == null)
			{
				this.result = blank(cellStyle);
			}
			else
			{
				this.result = new Number(this.x, this.y, textValue.getValue().doubleValue(), cellStyle);
			}
		}

		public void handle(DateTextValue textValue) throws JRException
		{
			this.baseStyle.setDisplayFormat(getDateFormat(textValue.getPattern()));
			WritableCellFormat cellStyle = getLoadedCellStyle(this.baseStyle);
			if (textValue.getValue() == null)
			{
				this.result = blank(cellStyle);
			}
			else
			{
				this.result = new DateTime(this.x, this.y, textValue.getValue(), cellStyle);
			}
		}

		public void handle(BooleanTextValue textValue) throws JRException
		{
			WritableCellFormat cellStyle = getLoadedCellStyle(this.baseStyle);
			if (textValue.getValue() == null)
			{
				this.result = blank(cellStyle);
			}
			else
			{
				this.result = new jxl.write.Boolean(this.x, this.y, textValue.getValue().booleanValue(), cellStyle);
			}
		}

		protected Blank blank(WritableCellFormat cellStyle)
		{
			return new Blank(this.x, this.y, cellStyle);
		}

		public CellValue getResult()
		{
			return this.result;
		}
	}

	protected jxl.write.NumberFormat getNumberFormat(String pattern)
	{
		String convertedPattern = getConvertedPattern(pattern);
		jxl.write.NumberFormat cellFormat = (jxl.write.NumberFormat) this.numberFormats.get(convertedPattern);
		if (cellFormat == null)
		{
			cellFormat = new jxl.write.NumberFormat(convertedPattern);
			this.numberFormats.put(convertedPattern, cellFormat);
		}
		return cellFormat;
	}

	protected jxl.write.DateFormat getDateFormat(String pattern)
	{
		String convertedPattern = getConvertedPattern(pattern);
		jxl.write.DateFormat cellFormat = (jxl.write.DateFormat) this.dateFormats.get(convertedPattern);
		if (cellFormat == null)
		{
			cellFormat = new jxl.write.DateFormat(convertedPattern);
			this.dateFormats.put(convertedPattern, cellFormat);
		}
		return cellFormat;
	}

	protected CellValue getAutoDetectedCellValue(int x, int y, String textStr, StyleInfo baseStyle) throws JRException
	{
		CellValue cellValue;
		try
		{
			double d = Double.parseDouble(textStr);
			WritableCellFormat cellStyle = getLoadedCellStyle(baseStyle);
			cellValue = new Number(x, y, d, cellStyle);
		}
		catch (NumberFormatException nfe)
		{
			cellValue = getLabelCell(x, y, textStr, baseStyle);
		}
		return cellValue;
	}

	protected CellValue getLabelCell(int x, int y, String textStr, StyleInfo baseStyle) throws JRException
	{
		WritableCellFormat cellStyle = getLoadedCellStyle(baseStyle);
		CellValue cellValue = new Label(x, y, textStr, cellStyle);
		return cellValue;
	}

	protected void addMergeRegion(JRExporterGridCell gridCell, int x, int y) throws JRException
	{
		if (gridCell.getColSpan() > 1 || gridCell.getRowSpan() > 1)
		{
			try
			{
				if (this.isCollapseRowSpan)
				{
					this.sheet.mergeCells(x, y, (x + gridCell.getColSpan() - 1), y);
				}
				else
				{
					this.sheet.mergeCells(x, y, (x + gridCell.getColSpan() - 1), (y + gridCell.getRowSpan() - 1));
				}
			}
			catch (JXLException e)
			{
				throw new JRException("Can't merge cells.", e);
			}
		}
	}

	private int getHorizontalAlignment(TextAlignHolder alignment)
	{
		switch (alignment.horizontalAlignment)
		{
			case JRAlignment.HORIZONTAL_ALIGN_RIGHT:
				return Alignment.RIGHT.getValue();
			case JRAlignment.HORIZONTAL_ALIGN_CENTER:
				return Alignment.CENTRE.getValue();
			case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED:
				return Alignment.JUSTIFY.getValue();
			case JRAlignment.HORIZONTAL_ALIGN_LEFT:
			default:
				return Alignment.LEFT.getValue();
		}
	}

	private int getVerticalAlignment(TextAlignHolder alignment)
	{
		switch (alignment.verticalAlignment)
		{
			case JRAlignment.VERTICAL_ALIGN_BOTTOM:
				return VerticalAlignment.BOTTOM.getValue();
			case JRAlignment.VERTICAL_ALIGN_MIDDLE:
				return VerticalAlignment.CENTRE.getValue();
			case JRAlignment.VERTICAL_ALIGN_JUSTIFIED:
				return VerticalAlignment.JUSTIFY.getValue();
			case JRAlignment.VERTICAL_ALIGN_TOP:
			default:
				return VerticalAlignment.TOP.getValue();
		}
	}

	private int getRotation(TextAlignHolder alignment)
	{
		switch (alignment.rotation)
		{
			case JRTextElement.ROTATION_LEFT:
				return Orientation.PLUS_90.getValue();
			case JRTextElement.ROTATION_RIGHT:
				return Orientation.MINUS_90.getValue();
			case JRTextElement.ROTATION_UPSIDE_DOWN:
			case JRTextElement.ROTATION_NONE:
			default:
				return Orientation.HORIZONTAL.getValue();
		}
	}

	protected void exportImage(JRPrintImage element, JRExporterGridCell gridCell, int col, int row, int emptyCols) throws JRException
	{
		addMergeRegion(gridCell, col, row);

		int topPadding = 
			Math.max(element.getLineBox().getTopPadding().intValue(), getImageBorderCorrection(element.getLineBox().getTopPen()));
		int leftPadding = 
			Math.max(element.getLineBox().getLeftPadding().intValue(), getImageBorderCorrection(element.getLineBox().getLeftPen()));
		int bottomPadding = 
			Math.max(element.getLineBox().getBottomPadding().intValue(), getImageBorderCorrection(element.getLineBox().getBottomPen()));
		int rightPadding = 
			Math.max(element.getLineBox().getRightPadding().intValue(), getImageBorderCorrection(element.getLineBox().getRightPen()));
		
		int availableImageWidth = element.getWidth() - leftPadding - rightPadding;
		availableImageWidth = availableImageWidth < 0 ? 0 : availableImageWidth;

		int availableImageHeight = element.getHeight() - topPadding - bottomPadding;
		availableImageHeight = availableImageHeight < 0 ? 0 : availableImageHeight;

		JRRenderable renderer = element.getRenderer();

		if (
			renderer != null &&
			availableImageWidth > 0 &&
			availableImageHeight > 0
			)
		{
			if (renderer.getType() == JRRenderable.TYPE_IMAGE)
			{
				// Image renderers are all asked for their image data and dimension at some point. 
				// Better to test and replace the renderer now, in case of lazy load error.
				renderer = JRImageRenderer.getOnErrorRendererForImageData(renderer, element.getOnErrorType());
				if (renderer != null)
				{
					renderer = JRImageRenderer.getOnErrorRendererForDimension(renderer, element.getOnErrorType());
				}
			}
		}
		else
		{
			renderer = null;
		}

		if (renderer != null)
		{
			int normalWidth = availableImageWidth;
			int normalHeight = availableImageHeight;

			Dimension2D dimension = renderer.getDimension();
			if (dimension != null)
			{
				normalWidth = (int) dimension.getWidth();
				normalHeight = (int) dimension.getHeight();
			}

			float xalignFactor = 0f;
			switch (element.getHorizontalAlignment())
			{
				case JRAlignment.HORIZONTAL_ALIGN_RIGHT:
				{
					xalignFactor = 1f;
					break;
				}
				case JRAlignment.HORIZONTAL_ALIGN_CENTER:
				{
					xalignFactor = 0.5f;
					break;
				}
				case JRAlignment.HORIZONTAL_ALIGN_LEFT:
				default:
				{
					xalignFactor = 0f;
					break;
				}
			}

			float yalignFactor = 0f;
			switch (element.getVerticalAlignment())
			{
				case JRAlignment.VERTICAL_ALIGN_BOTTOM:
				{
					yalignFactor = 1f;
					break;
				}
				case JRAlignment.VERTICAL_ALIGN_MIDDLE:
				{
					yalignFactor = 0.5f;
					break;
				}
				case JRAlignment.VERTICAL_ALIGN_TOP:
				default:
				{
					yalignFactor = 0f;
					break;
				}
			}
			
			BufferedImage bi = new BufferedImage(element.getWidth(), element.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D grx = bi.createGraphics();
			
			switch (element.getScaleImage())
			{
				case JRImage.SCALE_IMAGE_CLIP:
				{
					int xoffset = (int) (xalignFactor * (availableImageWidth - normalWidth));
					int yoffset = (int) (yalignFactor * (availableImageHeight - normalHeight));

					Shape oldClipShape = grx.getClip();

					grx.clip(
						new Rectangle(
							leftPadding, 
							topPadding, 
							availableImageWidth, 
							availableImageHeight
							)
						);
					
					try
					{
						renderer.render(
							grx, 
							new Rectangle(
								xoffset + leftPadding, 
								yoffset + topPadding,
								normalWidth, 
								normalHeight
								)
							);
					}
					finally
					{
						grx.setClip(oldClipShape);
					}

					break;
				}
				case JRImage.SCALE_IMAGE_FILL_FRAME:
				{
					renderer.render(
						grx, 
						new Rectangle(
							leftPadding, 
							topPadding, 
							availableImageWidth, 
							availableImageHeight
							)
						);

					break;
				}
				case JRImage.SCALE_IMAGE_RETAIN_SHAPE:
				default:
				{
					if (element.getHeight() > 0)
					{
						double ratio = (double) normalWidth / (double) normalHeight;

						if (ratio > (double) availableImageWidth / (double) availableImageHeight)
						{
							normalWidth = availableImageWidth;
							normalHeight = (int) (availableImageWidth / ratio);
						}
						else
						{
							normalWidth = (int) (availableImageHeight * ratio);
							normalHeight = availableImageHeight;
						}

						int xoffset = leftPadding + (int) (xalignFactor * (availableImageWidth - normalWidth));
						int yoffset = topPadding + (int) (yalignFactor * (availableImageHeight - normalHeight));

						renderer.render(
							grx, 
							new Rectangle(
								xoffset, 
								yoffset,
								normalWidth, 
								normalHeight
								)
							);
					}

					break;
				}
			}

			Pattern mode = this.backgroundMode;
			Colour background = WHITE;

			if (gridCell.getCellBackcolor() != null)
			{
				mode = Pattern.SOLID;
				background = getWorkbookColour(gridCell.getCellBackcolor());
			}

			if(element.getMode() == JRElement.MODE_OPAQUE )
			{
				background = getWorkbookColour(element.getBackcolor());
			}

			Colour forecolor = getWorkbookColour(element.getLineBox().getPen().getLineColor());

			WritableFont cellFont2 = this.getLoadedFont(getDefaultFont(), forecolor.getValue());

			WritableCellFormat cellStyle2 = 
				getLoadedCellStyle(
					mode, 
					background, 
					cellFont2, 
					gridCell
					);

			try
			{
				this.sheet.addCell(new Blank(col, row, cellStyle2));
				WritableImage image =
					new WritableImage(
						col - emptyCols,
						row,
						gridCell.getColSpan(),
						gridCell.getRowSpan(),
						JRImageLoader.loadImageDataFromAWTImage(bi, JRRenderable.IMAGE_TYPE_PNG)
						);
				this.sheet.addImage(image);
			}
			catch (Exception ex)
			{
				throw new JRException("The cell cannot be added", ex);
			}
			catch (Error err)
			{
				throw new JRException("The cell cannot be added", err);
			}
		}
	}

	protected Colour getWorkbookColour(Color awtColor)
	{
		Colour colour;
		if (this.createCustomPalette)
		{
			colour = (Colour) this.workbookColours.get(awtColor);
			if (colour == null)
			{
				colour = determineWorkbookColour(awtColor);
				this.workbookColours.put(awtColor, colour);
			}
		}
		else
		{
			colour = getNearestColour(awtColor);
		}
		return colour;
	}

	protected Colour determineWorkbookColour(Color awtColor)
	{
		//nearest match
		int minDist = 999;
		Colour minColour = null;

		//nearest match among the available (not used) colours
		int minDistAvailable = 999;
		Colour minColourAvailable = null;

		Colour[] colors = Colour.getAllColours();
		for (int i = 0; i < colors.length; i++)
		{
			Colour colour = colors[i];
			RGB customRGB = (RGB) this.usedColours.get(colour);

			RGB rgb = customRGB == null ? colour.getDefaultRGB() : customRGB;
			int dist = rgbDistance(awtColor, rgb);
			if (dist < minDist)
			{
				minDist = dist;
				minColour = colour;
			}

			if (dist == 0)//exact match
			{
				break;
			}

			if (customRGB == null)//the colour is not used
			{
				if (dist < minDistAvailable)
				{
					minDistAvailable = dist;
					minColourAvailable = colour;
				}
			}
		}

		Colour workbookColour;
		if (minDist == 0)//exact match found
		{
			if (!this.usedColours.containsKey(minColour))
			{
				//if the colour is not marked as used, mark it
				setColourUsed(minColour);
			}
			workbookColour = minColour;
		}
		else if (minColourAvailable == null)//all the colours are used
		{
			if (log.isWarnEnabled())
			{
				log.warn("No more available colours in the palette.  Using the nearest match for " + awtColor);
			}
			workbookColour = minColour;
		}
		else
		{
			//modifying the nearest available colour to the report colour
			setColourUsed(minColourAvailable, awtColor);
			workbookColour = minColourAvailable;
		}
		return workbookColour;
	}

	protected static Colour getNearestColour(Color awtColor)
	{
		Colour color = (Colour) colorsCache.get(awtColor);

		if (color == null)
		{
			Colour[] colors = Colour.getAllColours();
			if ((colors != null) && (colors.length > 0))
			{
				int minDiff = 999;

				for (int i = 0; i < colors.length; i++)
				{
					Colour crtColor = colors[i];
					int diff = rgbDistance(awtColor, crtColor.getDefaultRGB());

					if (diff < minDiff)
					{
						minDiff = diff;
						color = crtColor;
					}
				}
			}

			colorsCache.put(awtColor, color);
		}

		return color;
	}

	protected static int rgbDistance(Color awtColor, RGB rgb)
	{
		int diff = Math.abs(rgb.getRed() - awtColor.getRed())
			+ Math.abs(rgb.getGreen() - awtColor.getGreen())
			+ Math.abs(rgb.getBlue() - awtColor.getBlue());
		return diff;
	}


	/*private static Colour getNearestColour(Color awtColor) {
		Colour retVal = null;
		Colour[] colors = Colour.getAllColours();

		int diff = 50;

		if (colors != null && colors.length > 0 ){
			Colour crtColor = null;
			for (int i = 0; i < colors.length; i++) {
				crtColor = colors[i];

				int red = crtColor.getDefaultRGB().getRed();
				if (Math.abs(awtColor.getRed() - red) < diff) {
					int green = crtColor.getDefaultRGB().getGreen();
					if (Math.abs(awtColor.getGreen() - green) < diff) {
						int blue = crtColor.getDefaultRGB().getBlue();
						if (Math.abs(awtColor.getBlue() - blue) < diff) {
							retVal = crtColor;
						}
					}
				}
			}
		}

		return retVal;
	}*/

	private WritableFont getLoadedFont(JRFont font, int forecolor) throws JRException
	{
		WritableFont cellFont = null;

		if (this.loadedFonts != null && this.loadedFonts.size() > 0)
		{
			for (int i = 0; i < this.loadedFonts.size(); i++)
			{
				WritableFont cf = (WritableFont) this.loadedFonts.get(i);

				int fontSize = font.getFontSize();
				if (this.isFontSizeFixEnabled)
					fontSize -= 1;

				String fontName = font.getFontName();
				if (this.fontMap != null && this.fontMap.containsKey(fontName))
				{
					fontName = (String) this.fontMap.get(fontName);
				}

				if ((cf.getName().equals(fontName))
						&& (cf.getColour().getValue() == forecolor)
						&& (cf.getPointSize() == fontSize)
						&& (cf.getUnderlineStyle() == UnderlineStyle.SINGLE ? (font.isUnderline()) : (!font.isUnderline()))
						&& (cf.isStruckout() == font.isStrikeThrough())
						&& (cf.getBoldWeight() == BoldStyle.BOLD.getValue() ? (font.isBold()) : (!font.isBold()))
						&& (cf.isItalic() == font.isItalic()))
				{
					cellFont = cf;
					break;
				}
			}
		}

		try
		{
			if (cellFont == null)
			{
				int fontSize = font.getFontSize();
				if (this.isFontSizeFixEnabled)
					fontSize -= 1;

				String fontName = font.getFontName();
				if (this.fontMap != null && this.fontMap.containsKey(fontName))
				{
					fontName = (String) this.fontMap.get(fontName);
				}

				cellFont =
					new WritableFont(
						WritableFont.createFont(fontName),
						fontSize,
						font.isBold() ? WritableFont.BOLD : WritableFont.NO_BOLD,
						font.isItalic(),
						font.isUnderline() ? UnderlineStyle.SINGLE : UnderlineStyle.NO_UNDERLINE,
						Colour.getInternalColour(forecolor)
						);
				cellFont.setStruckout(font.isStrikeThrough());

				this.loadedFonts.add(cellFont);
			}
		}
		catch (Exception e)
		{
			throw new JRException("Can't get loaded fonts.", e);
		}

		return cellFont;
	}

	protected class BoxStyle
	{
		protected static final int TOP = 0;
		protected static final int LEFT = 1;
		protected static final int BOTTOM = 2;
		protected static final int RIGHT = 3;

		protected BorderLineStyle[] borderStyle = 
			new BorderLineStyle[]{BorderLineStyle.NONE, BorderLineStyle.NONE, BorderLineStyle.NONE, BorderLineStyle.NONE};
		protected Colour[] borderColour = 
			new Colour[]{BLACK, BLACK, BLACK, BLACK};
		private int hash;

		public BoxStyle(int side, JRPen pen)
		{
			this.borderStyle[side] = getBorderLineStyle(pen);
			this.borderColour[side] = getWorkbookColour(pen.getLineColor());

			this.hash = computeHash();
		}

		public BoxStyle(JRExporterGridCell gridCell)
		{
			JRLineBox lineBox = gridCell.getBox();
			if (lineBox != null)
				setBox(lineBox);
			JRPrintElement element = gridCell.getElement();
			if (element instanceof JRCommonGraphicElement)
				setPen(((JRCommonGraphicElement)element).getLinePen());

			this.hash = computeHash();
		}

		public void setBox(JRLineBox box)
		{
			this.borderStyle[TOP] = getBorderLineStyle(box.getTopPen());
			this.borderColour[TOP] = getWorkbookColour(box.getTopPen().getLineColor());

			this.borderStyle[BOTTOM] = getBorderLineStyle(box.getBottomPen());
			this.borderColour[BOTTOM] = getWorkbookColour(box.getBottomPen().getLineColor());

			this.borderStyle[LEFT] = getBorderLineStyle(box.getLeftPen());
			this.borderColour[LEFT] = getWorkbookColour(box.getLeftPen().getLineColor());

			this.borderStyle[RIGHT] = getBorderLineStyle(box.getRightPen());
			this.borderColour[RIGHT] = getWorkbookColour(box.getRightPen().getLineColor());

			this.hash = computeHash();
		}

		public void setPen(JRPen pen)
		{
			if (
				this.borderStyle[TOP] == BorderLineStyle.NONE
				&& this.borderStyle[LEFT] == BorderLineStyle.NONE
				&& this.borderStyle[BOTTOM] == BorderLineStyle.NONE
				&& this.borderStyle[RIGHT] == BorderLineStyle.NONE
				)
			{
				BorderLineStyle style = getBorderLineStyle(pen);
				Colour colour = getWorkbookColour(pen.getLineColor());

				this.borderStyle[TOP] = style;
				this.borderStyle[LEFT] = style;
				this.borderStyle[BOTTOM] = style;
				this.borderStyle[RIGHT] = style;

				this.borderColour[TOP] = colour;
				this.borderColour[LEFT] = colour;
				this.borderColour[BOTTOM] = colour;
				this.borderColour[RIGHT] = colour;
			}

			this.hash = computeHash();
		}

		private int computeHash()
		{
			int hashCode = this.borderStyle[TOP].hashCode();
			hashCode = 31*hashCode + this.borderColour[TOP].hashCode();
			hashCode = 31*hashCode + this.borderStyle[BOTTOM].hashCode();
			hashCode = 31*hashCode + this.borderColour[BOTTOM].hashCode();
			hashCode = 31*hashCode + this.borderStyle[LEFT].hashCode();
			hashCode = 31*hashCode + this.borderColour[LEFT].hashCode();
			hashCode = 31*hashCode + this.borderStyle[RIGHT].hashCode();
			hashCode = 31*hashCode + this.borderColour[RIGHT].hashCode();
			return hashCode;
		}

		public int hashCode()
		{
			return this.hash;
		}

		public boolean equals(Object o)
		{
			BoxStyle b = (BoxStyle) o;

			return
				b.borderStyle[TOP].equals(this.borderStyle[TOP])
				&& b.borderColour[TOP].equals(this.borderColour[TOP])
				&& b.borderStyle[LEFT].equals(this.borderStyle[LEFT])
				&& b.borderColour[LEFT].equals(this.borderColour[LEFT])
				&& b.borderStyle[BOTTOM].equals(this.borderStyle[BOTTOM])
				&& b.borderColour[BOTTOM].equals(this.borderColour[BOTTOM])
				&& b.borderStyle[RIGHT].equals(this.borderStyle[RIGHT])
				&& b.borderColour[RIGHT].equals(this.borderColour[RIGHT]);
		}

		public String toString()
		{
			return "(" +
				this.borderStyle[TOP].getValue() + "/" + this.borderColour[TOP].getValue() + "," +
				this.borderStyle[BOTTOM].getValue() + "/" + this.borderColour[BOTTOM].getValue() + "," +
				this.borderStyle[LEFT].getValue() + "/" + this.borderColour[LEFT].getValue() + "," +
				this.borderStyle[RIGHT].getValue() + "/" + this.borderColour[RIGHT].getValue() + ")";
		}
	}

	protected class StyleInfo
	{
		protected final Pattern mode;
		protected final Colour backcolor;
		protected final int horizontalAlignment;
		protected final int verticalAlignment;
		protected final int rotation;
		protected final WritableFont font;
		protected final BoxStyle box;
		private DisplayFormat displayFormat;
		private int hashCode;

		protected StyleInfo(
			Pattern mode, 
			Colour backcolor, 
			int horizontalAlignment, 
			int verticalAlignment, 
			int rotation, 
			WritableFont font, 
			JRExporterGridCell gridCell
			)
		{
			this(
				mode, 
				backcolor, 
				horizontalAlignment, 
				verticalAlignment, 
				rotation, 
				font, 
				new BoxStyle(gridCell)
				);
		}
		
		protected StyleInfo(
			Pattern mode, 
			Colour backcolor, 
			int horizontalAlignment, 
			int verticalAlignment, 
			int rotation, 
			WritableFont font, 
			BoxStyle box
			)
		{
			this.mode = mode;
			this.backcolor = backcolor;
			this.horizontalAlignment = horizontalAlignment;
			this.verticalAlignment = verticalAlignment;
			this.rotation = rotation;
			this.font = font;
			
			this.box = box;
			
			computeHash();
		}

		protected void computeHash()
		{
			int hash = this.mode.hashCode();
			hash = 31*hash + this.backcolor.hashCode();
			hash = 31*hash + this.horizontalAlignment;
			hash = 31*hash + this.verticalAlignment;
			hash = 31*hash + this.rotation;
			hash = 31*hash + this.font.hashCode();
			hash = 31*hash + (this.box == null ? 0 : this.box.hashCode());
			hash = 31*hash + (this.displayFormat == null ? 0 : this.displayFormat.hashCode());

			this.hashCode = hash;
		}

		public int hashCode()
		{
			return this.hashCode;
		}

		public boolean equals(Object o)
		{
			StyleInfo k = (StyleInfo) o;

			return k.mode.equals(this.mode) && k.backcolor.equals(this.backcolor) &&
				k.horizontalAlignment == this.horizontalAlignment && k.verticalAlignment == this.verticalAlignment &&
				k.rotation == this.rotation && k.font.equals(this.font) &&
				(k.box == null ? this.box == null : (this.box != null && k.box.equals(this.box))) &&
				(k.displayFormat == null ? this.displayFormat == null : (this.displayFormat!= null && k.displayFormat.equals(this.displayFormat)));
		}

		public DisplayFormat getDisplayFormat()
		{
			return this.displayFormat;
		}

		public void setDisplayFormat(DisplayFormat displayFormat)
		{
			this.displayFormat = displayFormat;
			computeHash();
		}

		public String toString()
		{
			return "(" +
				this.mode + "," + this.backcolor + "," +
				this.horizontalAlignment + "," + this.verticalAlignment + "," +
				this.rotation + "," + this.font + "," +
				this.box + "," + this.displayFormat + ")";
		}
	}

	private WritableCellFormat getLoadedCellStyle(
		Pattern mode, 
		Colour backcolor, 
		WritableFont font, 
		JRExporterGridCell gridCell
		) throws JRException
	{
		StyleInfo styleKey = 
			new StyleInfo(
				mode, 
				backcolor, 
				Alignment.LEFT.getValue(), 
				VerticalAlignment.TOP.getValue(), 
				Orientation.HORIZONTAL.getValue(),
				font, 
				gridCell
				);
		return getLoadedCellStyle(styleKey);
	}

	private WritableCellFormat getLoadedCellStyle(
		Pattern mode, 
		Colour backcolor, 
		WritableFont font, 
		BoxStyle box
		) throws JRException
	{
		StyleInfo styleKey = 
			new StyleInfo(
				mode, 
				backcolor, 
				Alignment.LEFT.getValue(), 
				VerticalAlignment.TOP.getValue(), 
				Orientation.HORIZONTAL.getValue(),
				font, 
				box
				);
		return getLoadedCellStyle(styleKey);
	}


	protected WritableCellFormat getLoadedCellStyle(StyleInfo styleKey) throws JRException
	{
		WritableCellFormat cellStyle = (WritableCellFormat) this.loadedCellStyles.get(styleKey);

		if (cellStyle == null)
		{
			try
			{
				if (styleKey.getDisplayFormat() == null)
				{
					cellStyle = new WritableCellFormat(styleKey.font);
				}
				else
				{
					cellStyle = new WritableCellFormat(styleKey.font, styleKey.getDisplayFormat());
				}
				cellStyle.setBackground(styleKey.backcolor, styleKey.mode);
				cellStyle.setAlignment(Alignment.getAlignment(styleKey.horizontalAlignment));
				cellStyle.setVerticalAlignment(VerticalAlignment.getAlignment(styleKey.verticalAlignment));
				cellStyle.setOrientation(Orientation.getOrientation(styleKey.rotation));
				cellStyle.setWrap(true);

				if (!this.isIgnoreCellBorder)
				{
					BoxStyle box = styleKey.box;
					cellStyle.setBorder(Border.TOP, box.borderStyle[BoxStyle.TOP], box.borderColour[BoxStyle.TOP]);
					cellStyle.setBorder(Border.BOTTOM, box.borderStyle[BoxStyle.BOTTOM], box.borderColour[BoxStyle.BOTTOM]);
					cellStyle.setBorder(Border.LEFT, box.borderStyle[BoxStyle.LEFT], box.borderColour[BoxStyle.LEFT]);
					cellStyle.setBorder(Border.RIGHT, box.borderStyle[BoxStyle.RIGHT], box.borderColour[BoxStyle.RIGHT]);
				}
			}
			catch (Exception e)
			{
				throw new JRException("Error setting cellFormat-template.", e);
			}

			this.loadedCellStyles.put(styleKey, cellStyle);
		}

		return cellStyle;
	}

	/**
	 *
	 */
	protected static BorderLineStyle getBorderLineStyle(JRPen pen) 
	{
		float lineWidth = pen.getLineWidth().floatValue();
		
		if (lineWidth > 0f)
		{
			switch (pen.getLineStyle().byteValue())
			{
				case JRPen.LINE_STYLE_DOUBLE :
				{
					return BorderLineStyle.DOUBLE;
				}
				case JRPen.LINE_STYLE_DOTTED :
				{
					return BorderLineStyle.DOTTED;
				}
				case JRPen.LINE_STYLE_DASHED :
				{
					return BorderLineStyle.DASHED;
				}
				case JRPen.LINE_STYLE_SOLID :
				default :
				{
					if (lineWidth >= 2f)
					{
						return BorderLineStyle.THICK;
					}
					else if (lineWidth >= 1f)
					{
						return BorderLineStyle.MEDIUM;//FIXMEBORDER there is also BorderLineStyle.MEDIUM_DASHED available
					}
					else if (lineWidth >= 0.5f)
					{
						return BorderLineStyle.THIN;
					}

					return BorderLineStyle.HAIR;
				}
			}
		}
		
		return BorderLineStyle.NONE;
	}

	private final void setSheetSettings(WritableSheet sheet)
	{
		PageOrientation po;
		PaperSize ps;

		if (this.jasperPrint.getOrientation() == JRReport.ORIENTATION_PORTRAIT)
			po = PageOrientation.PORTRAIT;
		else
			po = PageOrientation.LANDSCAPE;

		if ((ps = getSuitablePaperSize(this.jasperPrint)) != null)
			sheet.setPageSetup(po, ps, 0, 0);
		else
			sheet.setPageSetup(po);

		SheetSettings sheets = sheet.getSettings();

		sheets.setTopMargin(0.0);
		sheets.setLeftMargin(0.0);
		sheets.setRightMargin(0.0);
		sheets.setBottomMargin(0.0);

		sheets.setHeaderMargin(0.0);
		sheets.setFooterMargin(0.0);
		
		if(this.password != null)
		{
			sheets.setPassword(this.password);
			sheets.setProtected(true);
		}
	}

	private final PaperSize getSuitablePaperSize(JasperPrint jasP)
	{

		if (jasP == null)
			return null;

		long width = 0;
		long height = 0;
		PaperSize ps = null;

		if ((jasP.getPageWidth() != 0) && (jasP.getPageHeight() != 0))
		{

			double dWidth = (jasP.getPageWidth() / 72.0);
			double dHeight = (jasP.getPageHeight() / 72.0);

			height = Math.round(dHeight * 25.4);
			width = Math.round(dWidth * 25.4);

			// Compare to ISO 216 A-Series (A3-A5). All other ISO 216 formats
			// not supported by JExcelApi yet.
			for (int i = 3; i < 6; i++)
			{
				int w = calculateWidthForDinAN(i);
				int h = calculateHeightForDinAN(i);

				if (((w == width) && (h == height)) || ((h == width) && (w == height)))
				{
					if (i == 3)
						ps = PaperSize.A3;
					else if (i == 4)
						ps = PaperSize.A4;
					else if (i == 5)
						ps = PaperSize.A5;
					break;
				}
			}

			// Compare to common North American Paper Sizes (ANSI X3.151-1987).
			if (ps == null)
			{
				// ANSI X3.151-1987 - "Letter" (216 � 279 mm)
				if (((width == 216) && (height == 279)) || ((width == 279) && (height == 216)))
				{
					ps = PaperSize.LETTER;
				}
				// ANSI X3.151-1987 - "Legal" (216 � 356 mm)
				if (((width == 216) && (height == 356)) || ((width == 356) && (height == 216)))
				{
					ps = PaperSize.LEGAL;
				}
				// ANSI X3.151-1987 - "Executive" (190 � 254 mm)
				// Not supperted by JExcelApi yet.

				// ANSI X3.151-1987 - "Ledger/Tabloid" (279 � 432 mm)
				// Not supperted by JExcelApi yet.
			}
		}
		return ps;
	}


	public static TextAlignHolder getTextAlignHolder(JRPrintText textElement)
	{
		short horizontalAlignment;
		short verticalAlignment;
		short rotation = textElement.getRotation();

		switch (textElement.getRotation())
		{
			case JRTextElement.ROTATION_LEFT :
			{
				switch (textElement.getHorizontalAlignment())
				{
					case JRAlignment.HORIZONTAL_ALIGN_LEFT :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_BOTTOM;
						break;
					}
					case JRAlignment.HORIZONTAL_ALIGN_CENTER :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_MIDDLE;
						break;
					}
					case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_TOP;
						break;
					}
					case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_JUSTIFIED;
						break;
					}
					default :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_BOTTOM;
					}
				}

				switch (textElement.getVerticalAlignment())
				{
					case JRAlignment.VERTICAL_ALIGN_TOP :
					{
						horizontalAlignment = JRAlignment.HORIZONTAL_ALIGN_LEFT;
						break;
					}
					case JRAlignment.VERTICAL_ALIGN_MIDDLE :
					{
						horizontalAlignment = JRAlignment.HORIZONTAL_ALIGN_CENTER;
						break;
					}
					case JRAlignment.VERTICAL_ALIGN_BOTTOM :
					{
						horizontalAlignment = JRAlignment.HORIZONTAL_ALIGN_RIGHT;
						break;
					}
					default :
					{
						horizontalAlignment = JRAlignment.HORIZONTAL_ALIGN_LEFT;
					}
				}

				break;
			}
			case JRTextElement.ROTATION_RIGHT :
			{
				switch (textElement.getHorizontalAlignment())
				{
					case JRAlignment.HORIZONTAL_ALIGN_LEFT :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_TOP;
						break;
					}
					case JRAlignment.HORIZONTAL_ALIGN_CENTER :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_MIDDLE;
						break;
					}
					case JRAlignment.HORIZONTAL_ALIGN_RIGHT :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_BOTTOM;
						break;
					}
					case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_JUSTIFIED;
						break;
					}
					default :
					{
						verticalAlignment = JRAlignment.VERTICAL_ALIGN_TOP;
					}
				}

				switch (textElement.getVerticalAlignment())
				{
					case JRAlignment.VERTICAL_ALIGN_TOP :
					{
						horizontalAlignment = JRAlignment.HORIZONTAL_ALIGN_RIGHT;
						break;
					}
					case JRAlignment.VERTICAL_ALIGN_MIDDLE :
					{
						horizontalAlignment = JRAlignment.HORIZONTAL_ALIGN_CENTER;
						break;
					}
					case JRAlignment.VERTICAL_ALIGN_BOTTOM :
					{
						horizontalAlignment = JRAlignment.HORIZONTAL_ALIGN_LEFT;
						break;
					}
					default :
					{
						horizontalAlignment = JRAlignment.HORIZONTAL_ALIGN_RIGHT;
					}
				}

				break;
			}
			case JRTextElement.ROTATION_UPSIDE_DOWN:
			case JRTextElement.ROTATION_NONE :
			default :
			{
				horizontalAlignment = textElement.getHorizontalAlignment();
				verticalAlignment = textElement.getVerticalAlignment();
			}
		}

		return new TextAlignHolder(horizontalAlignment, verticalAlignment, rotation);
	}



	// Berechnungsvorschriften f�r die DIN Formate A, B, und C.
	// Die Angabe der Breite/H�he erfolgt in [mm].

	private final int calculateWidthForDinAN(int n)
	{
		return (int) (Math.pow(2.0, (-0.25 - (n / 2.0))) * 1000.0);
	}

	private final int calculateHeightForDinAN(int n)
	{
		return (int) (Math.pow(2.0, (0.25 - (n / 2.0))) * 1000.0);
	}

	private final int calculateWidthForDinBN(int n)
	{
		return (int) (Math.pow(2.0, -(n / 2.0)) * 1000.0);
	}

	private final int calculateHeightForDinBN(int n)
	{
		return (int) (Math.pow(2.0, (0.5 - (n / 2.0))) * 1000.0);
	}

	private final int calculateWidthForDinCN(int n)
	{
		return (int) (Math.pow(2.0, (-0.125 - (n / 2.0))) * 1000.0);
	}

	private final int calculateHeightForDinCN(int n)
	{
		return (int) (Math.pow(2.0, (0.375 - (n / 2.0))) * 1000.0);
	}

	protected void exportFrame(JRPrintFrame frame, JRExporterGridCell gridCell, int col, int row) throws JRException
	{
		addMergeRegion(gridCell, col, row);

		Colour forecolor = getWorkbookColour(frame.getForecolor());
		Colour backcolor = WHITE;
		Pattern mode = this.backgroundMode;

		if (frame.getMode() == JRElement.MODE_OPAQUE)
		{
			mode = Pattern.SOLID;
			backcolor = getWorkbookColour(frame.getBackcolor());
		}

		WritableFont cellFont = getLoadedFont(getDefaultFont(), forecolor.getValue());
		WritableCellFormat cellStyle = 
			getLoadedCellStyle(
				mode, 
				backcolor,
				cellFont, 
				gridCell
				);

		Blank cell = new Blank(col, row, cellStyle);
		try
		{
			this.sheet.addCell(cell);
		}
		catch (JXLException e)
		{
			throw new JRException("Can't add cell.", e);
		}
	}

	/**
	 * This method is intended to modify a given format pattern so to include
	 * only the accepted proprietary format characters. The resulted pattern
	 * will possibly truncate the original pattern
	 * @param pattern
	 * @return pattern converted to accepted proprietary formats
	 */
	private String getConvertedPattern(String pattern)
	{
		if (this.formatPatternsMap != null && this.formatPatternsMap.containsKey(pattern))
		{
			return (String) this.formatPatternsMap.get(pattern);
		}
		return pattern;
	}


	protected ExporterNature getNature()
	{
		return this.nature;
	}

}

