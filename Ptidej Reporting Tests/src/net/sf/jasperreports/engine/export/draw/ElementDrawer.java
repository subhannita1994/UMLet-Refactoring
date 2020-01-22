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
 * Eugene D - eugenedruy@users.sourceforge.net 
 * Adrian Jackson - iapetus@users.sourceforge.net
 * David Taylor - exodussystems@users.sourceforge.net
 * Lars Kristensen - llk@users.sourceforge.net
 */
package net.sf.jasperreports.engine.export.draw;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.export.legacy.BorderOffset;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: ElementDrawer.java,v 1.1 2008/09/29 16:22:02 guehene Exp $
 */
public abstract class ElementDrawer
{

	/**
	 *
	 */
	public abstract void draw(Graphics2D grx, JRPrintElement element, int offsetX, int offsetY) throws JRException;
	
	
	/**
	 *
	 */
	protected void drawBox(Graphics2D grx, JRLineBox box, JRPrintElement element, int offsetX, int offsetY)
	{
		drawTopPen(
			grx, 
			box.getTopPen(), 
			box.getLeftPen(), 
			box.getRightPen(), 
			element, 
			offsetX, 
			offsetY
			);
		drawLeftPen(
			grx, 
			box.getTopPen(), 
			box.getLeftPen(), 
			box.getBottomPen(), 
			element, 
			offsetX, 
			offsetY
			);
		drawBottomPen(
			grx, 
			box.getLeftPen(), 
			box.getBottomPen(), 
			box.getRightPen(), 
			element, 
			offsetX, 
			offsetY
			);
		drawRightPen(
			grx, 
			box.getTopPen(), 
			box.getBottomPen(), 
			box.getRightPen(), 
			element, 
			offsetX, 
			offsetY
			);
	}

	
	/**
	 *
	 */
	protected void drawPen(Graphics2D grx, JRPen pen, JRPrintElement element, int offsetX, int offsetY)
	{
		drawTopPen(grx, pen, pen, pen, element, offsetX, offsetY);
		drawLeftPen(grx, pen, pen, pen, element, offsetX, offsetY);
		drawBottomPen(grx, pen, pen, pen, element, offsetX, offsetY);
		drawRightPen(grx, pen, pen, pen, element, offsetX, offsetY);
	}

	
	/**
	 *
	 */
	protected void drawTopPen(
		Graphics2D grx, 
		JRPen topPen, 
		JRPen leftPen, 
		JRPen rightPen, 
		JRPrintElement element, 
		int offsetX, 
		int offsetY
		)
	{
		Stroke topStroke = getStroke(topPen, BasicStroke.CAP_BUTT);
		int width = element.getWidth();
		float leftOffset = leftPen.getLineWidth().floatValue() / 2 - BorderOffset.getOffset(leftPen);
		float rightOffset = rightPen.getLineWidth().floatValue() / 2 - BorderOffset.getOffset(rightPen);
		
		if (topStroke != null && width > 0)
		{
			grx.setStroke(topStroke);
			grx.setColor(topPen.getLineColor());
	
			AffineTransform oldTx = grx.getTransform();

			if (topPen.getLineStyle().byteValue() == JRPen.LINE_STYLE_DOUBLE)
			{
				float topPenWidth = topPen.getLineWidth().floatValue();

				grx.translate(
					element.getX() + offsetX - leftOffset, 
					element.getY() + offsetY - topPenWidth / 3
					);
				grx.scale(
					(width + leftOffset + rightOffset) 
						/ width, 
					1
					);
				grx.drawLine(
					0, 
					0, 
					width,
					0
					);

				grx.setTransform(oldTx);

				grx.translate(
					element.getX() + offsetX + leftOffset / 3, 
					element.getY() + offsetY + topPenWidth / 3
					);
				if(width > (leftOffset + rightOffset) / 3)
				{
					grx.scale(
						(width - (leftOffset + rightOffset) / 3) 
							/ width, 
						1
						);
				}
				grx.drawLine(
					0, 
					0, 
					width,
					0
					);
			}
			else
			{
				grx.translate(
					element.getX() + offsetX - leftOffset, 
					element.getY() + offsetY + BorderOffset.getOffset(topPen)
					);
				grx.scale(
					(width + leftOffset + rightOffset) 
						/ width, 
					1
					);
				grx.drawLine(
					0, 
					0, 
					width,
					0
					);
			}
			
			grx.setTransform(oldTx);
		}
	}

	
	/**
	 *
	 */
	protected void drawLeftPen(
		Graphics2D grx, 
		JRPen topPen, 
		JRPen leftPen, 
		JRPen bottomPen, 
		JRPrintElement element, 
		int offsetX, 
		int offsetY
		)
	{
		Stroke leftStroke = getStroke(leftPen, BasicStroke.CAP_BUTT);
		int height = element.getHeight();
		float topOffset = topPen.getLineWidth().floatValue() / 2 - BorderOffset.getOffset(topPen);
		float bottomOffset = bottomPen.getLineWidth().floatValue() / 2 - BorderOffset.getOffset(bottomPen);
		
		if (leftStroke != null && height > 0)
		{
			grx.setStroke(leftStroke);
			grx.setColor(leftPen.getLineColor());
	
			AffineTransform oldTx = grx.getTransform();

			if (leftPen.getLineStyle().byteValue() == JRPen.LINE_STYLE_DOUBLE)
			{
				float leftPenWidth = leftPen.getLineWidth().floatValue();

				grx.translate(
					element.getX() + offsetX - leftPenWidth / 3, 
					element.getY() + offsetY - topOffset
					);
				grx.scale(
						1,
						(height + (topOffset + bottomOffset)) 
							/ height 
						);
				grx.drawLine(
					0, 
					0, 
					0,
					height
					);

				grx.setTransform(oldTx);

				grx.translate(
					element.getX() + offsetX + leftPenWidth / 3, 
					element.getY() + offsetY + topOffset / 3
					);
				if(height > (topOffset + bottomOffset) / 3)
				{
					grx.scale(
						1,
						(height - (topOffset + bottomOffset) / 3) 
							/ height
						);
				}
				grx.drawLine(
					0, 
					0, 
					0,
					height
					);
			}
			else
			{
				grx.translate(
					element.getX() + offsetX + BorderOffset.getOffset(leftPen), 
					element.getY() + offsetY - topOffset
					);
				grx.scale(
					1,
					(height + topOffset + bottomOffset) 
						/ height
					);
				grx.drawLine(
					0, 
					0, 
					0,
					height
					);
			}

			grx.setTransform(oldTx);
		}
	}

	
	/**
	 *
	 */
	protected void drawBottomPen(
		Graphics2D grx, 
		JRPen leftPen, 
		JRPen bottomPen, 
		JRPen rightPen, 
		JRPrintElement element, 
		int offsetX, 
		int offsetY
		)
	{
		Stroke bottomStroke = getStroke(bottomPen, BasicStroke.CAP_BUTT);
		int width = element.getWidth();
		int height = element.getHeight();
		float leftOffset = leftPen.getLineWidth().floatValue() / 2 - BorderOffset.getOffset(leftPen);
		float rightOffset = rightPen.getLineWidth().floatValue() / 2 - BorderOffset.getOffset(rightPen);
		
		if (bottomStroke != null && width > 0)
		{
			grx.setStroke(bottomStroke);
			grx.setColor(bottomPen.getLineColor());
	
			AffineTransform oldTx = grx.getTransform();

			if (bottomPen.getLineStyle().byteValue() == JRPen.LINE_STYLE_DOUBLE)
			{
				float bottomPenWidth = bottomPen.getLineWidth().floatValue();

				grx.translate(
					element.getX() + offsetX - leftOffset, 
					element.getY() + offsetY + height + bottomPenWidth / 3
					);
				grx.scale(
					(width + leftOffset + rightOffset) 
						/ width, 
					1
					);
				grx.drawLine(
					0, 
					0,
					width,
					0
					);

				grx.setTransform(oldTx);

				grx.translate(
					element.getX() + offsetX + leftOffset / 3, 
					element.getY() + offsetY + height - bottomPenWidth / 3
					);
				if(width > (leftOffset + rightOffset) / 3)
				{
					grx.scale(
						(width - (leftOffset + rightOffset) / 3) 
							/ width, 
						1
						);
				}
				grx.drawLine(
					0, 
					0,
					width,
					0
					);
			}
			else
			{
				grx.translate(
					element.getX() + offsetX - leftOffset, 
					element.getY() + offsetY + height - BorderOffset.getOffset(bottomPen)
					);
				grx.scale(
					(width + leftOffset + rightOffset) 
						/ width, 
					1
					);
				grx.drawLine(
					0, 
					0,
					width,
					0
					);
			}

			grx.setTransform(oldTx);
		}
	}

	
	/**
	 *
	 */
	protected void drawRightPen(
		Graphics2D grx, 
		JRPen topPen, 
		JRPen bottomPen, 
		JRPen rightPen, 
		JRPrintElement element, 
		int offsetX, 
		int offsetY
		)
	{
		Stroke rightStroke = getStroke(rightPen, BasicStroke.CAP_BUTT);
		int height = element.getHeight();
		int width = element.getWidth();
		float topOffset = topPen.getLineWidth().floatValue() / 2 - BorderOffset.getOffset(topPen);
		float bottomOffset = bottomPen.getLineWidth().floatValue() / 2 - BorderOffset.getOffset(bottomPen);
		
		if (rightStroke != null && height > 0)
		{
			grx.setStroke(rightStroke);
			grx.setColor(rightPen.getLineColor());
	
			AffineTransform oldTx = grx.getTransform();

			if (rightPen.getLineStyle().byteValue() == JRPen.LINE_STYLE_DOUBLE)
			{
				float rightPenWidth = rightPen.getLineWidth().floatValue();

				grx.translate(
					element.getX() + offsetX + width + rightPenWidth / 3, 
					element.getY() + offsetY - topOffset
					);
				grx.scale(
					1,
					(height + topOffset + bottomOffset) 
						/ height 
					);
				grx.drawLine(
					0,
					0,
					0,
					height
					);

				grx.setTransform(oldTx);

				grx.translate(
					element.getX() + offsetX + width - rightPenWidth / 3, 
					element.getY() + offsetY + topOffset / 3
					);
				if(height > (topOffset + bottomOffset) / 3)
				{
					grx.scale(
						1,
						(height - (topOffset + bottomOffset) / 3) 
							/ height 
						);
				}
				grx.drawLine(
					0,
					0,
					0,
					height
					);
			}
			else
			{
				grx.translate(
					element.getX() + offsetX + width - BorderOffset.getOffset(rightPen), 
					element.getY() + offsetY - topOffset
					);
				grx.scale(
					1,
					(height + topOffset + bottomOffset) 
						/ height 
					);
				grx.drawLine(
					0,
					0,
					0,
					height
					);
			}

			grx.setTransform(oldTx);
		}
	}
	
	/**
	 * 
	 */
	protected static Stroke getStroke(JRPen pen, int lineCap)
	{
		float lineWidth = pen.getLineWidth().floatValue();
		
		if (lineWidth > 0f)
		{
			byte lineStyle = pen.getLineStyle().byteValue();
			
			switch (lineStyle)
			{
				case JRPen.LINE_STYLE_DOUBLE :
				{
					return 
						new BasicStroke(
							lineWidth / 3,
							lineCap,
							BasicStroke.JOIN_MITER
							);
				}
				case JRPen.LINE_STYLE_DOTTED :
				{
					switch (lineCap)
					{
						case BasicStroke.CAP_SQUARE :
						{
							return
								new BasicStroke(
									lineWidth,
									lineCap,
									BasicStroke.JOIN_MITER,
									10f,
									new float[]{0, 2 * lineWidth},
									0f
									);
						}
						case BasicStroke.CAP_BUTT :
						{
							return
								new BasicStroke(
									lineWidth,
									lineCap,
									BasicStroke.JOIN_MITER,
									10f,
									new float[]{lineWidth, lineWidth},
									0f
									);
						}
					}
				}
				case JRPen.LINE_STYLE_DASHED :
				{
					switch (lineCap)
					{
						case BasicStroke.CAP_SQUARE :
						{
							return
								new BasicStroke(
									lineWidth,
									lineCap,
									BasicStroke.JOIN_MITER,
									10f,
									new float[]{4 * lineWidth, 4 * lineWidth},
									0f
									);
						}
						case BasicStroke.CAP_BUTT :
						{
							return
								new BasicStroke(
									lineWidth,
									lineCap,
									BasicStroke.JOIN_MITER,
									10f,
									new float[]{5 * lineWidth, 3 * lineWidth},
									0f
									);
						}
					}
				}
				case JRPen.LINE_STYLE_SOLID :
				default :
				{
					return 
						new BasicStroke(
							lineWidth,
							lineCap,
							BasicStroke.JOIN_MITER
							);
				}
			}
		}
		
		return null;
	}

	
}
