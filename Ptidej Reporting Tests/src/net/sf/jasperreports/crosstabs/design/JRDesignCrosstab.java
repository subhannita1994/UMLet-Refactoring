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
package net.sf.jasperreports.crosstabs.design;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TimeZone;

import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.crosstabs.JRCrosstabBucket;
import net.sf.jasperreports.crosstabs.JRCrosstabCell;
import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabDataset;
import net.sf.jasperreports.crosstabs.JRCrosstabGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabMeasure;
import net.sf.jasperreports.crosstabs.JRCrosstabParameter;
import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.base.JRBaseCrosstab;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.util.FileResolver;
import net.sf.jasperreports.engine.util.FormatFactory;
import net.sf.jasperreports.engine.util.JRStyleResolver;
import net.sf.jasperreports.engine.util.Pair;

import org.apache.commons.collections.SequencedHashMap;

/**
 * Design-time {@link net.sf.jasperreports.crosstabs.JRCrosstab crosstab} implementation.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JRDesignCrosstab.java,v 1.1 2008/09/29 16:21:17 guehene Exp $
 */
public class JRDesignCrosstab extends JRDesignElement implements JRCrosstab
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_COLUMN_BREAK_OFFSET = "columnBreakOffset";
	
	public static final String PROPERTY_DATASET = "dataset";
	
	public static final String PROPERTY_HEADER_CELL = "headerCell";
	
	public static final String PROPERTY_PARAMETERS_MAP_EXPRESSION = "parametersMapExpression";
	
	public static final String PROPERTY_REPEAT_COLUMN_HEADERS = "repeatColumnHeaders";
	
	public static final String PROPERTY_REPEAT_ROW_HEADERS = "repeatRowHeaders";
	
	public static final String PROPERTY_WHEN_NO_DATA_CELL = "whenNoDataCell";
	
	public static final String PROPERTY_CELLS = "cells";
	
	public static final String PROPERTY_ROW_GROUPS = "rowGroups";
	
	public static final String PROPERTY_COLUMN_GROUPS = "columnGroups";
	
	public static final String PROPERTY_MEASURES = "measures";
	
	public static final String PROPERTY_PARAMETERS = "parameters";

	protected List parametersList;
	protected Map parametersMap;
	protected SequencedHashMap variablesList;
	protected JRExpression parametersMapExpression;
	protected JRDesignCrosstabDataset dataset;
	protected List rowGroups;
	protected List columnGroups;
	protected List measures;
	protected Map rowGroupsMap;
	protected Map columnGroupsMap;
	protected Map measuresMap;
	protected int columnBreakOffset = DEFAULT_COLUMN_BREAK_OFFSET;
	protected boolean repeatColumnHeaders = true;
	protected boolean repeatRowHeaders = true;
	protected byte runDirection;
	protected List cellsList;
	protected Map cellsMap;
	protected JRDesignCrosstabCell[][] crossCells;
	protected JRDesignCellContents whenNoDataCell;
	protected JRDesignCellContents headerCell;
	
	
	private class MeasureClassChangeListener implements PropertyChangeListener, Serializable
	{
		private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

		public void propertyChange(PropertyChangeEvent evt)
		{
			measureClassChanged((JRDesignCrosstabMeasure) evt.getSource(), (String) evt.getNewValue());
		}
	}

	private PropertyChangeListener measureClassChangeListener = new MeasureClassChangeListener();
	
	private static final Object[] BUILT_IN_PARAMETERS = new Object[] { 
		JRParameter.REPORT_PARAMETERS_MAP, java.util.Map.class, 
		JRParameter.REPORT_LOCALE, Locale.class, 
		JRParameter.REPORT_RESOURCE_BUNDLE, ResourceBundle.class,
		JRParameter.REPORT_TIME_ZONE, TimeZone.class, 
		JRParameter.REPORT_FORMAT_FACTORY, FormatFactory.class, 
		JRParameter.REPORT_CLASS_LOADER, ClassLoader.class,
		JRParameter.REPORT_URL_HANDLER_FACTORY, URLStreamHandlerFactory.class,
		JRParameter.REPORT_FILE_RESOLVER, FileResolver.class};
	
	private static final Object[] BUILT_IN_VARIABLES = new Object[] { 
		JRCrosstab.VARIABLE_ROW_COUNT, Integer.class, 
		JRCrosstab.VARIABLE_COLUMN_COUNT, Integer.class};

	
	/**
	 * Creates a new crosstab.
	 * 
	 * @param defaultStyleProvider default style provider
	 */
	public JRDesignCrosstab(JRDefaultStyleProvider defaultStyleProvider)
	{
		super(defaultStyleProvider);
		
		this.parametersList = new ArrayList();
		this.parametersMap = new HashMap();
		
		this.rowGroupsMap = new HashMap();
		this.rowGroups = new ArrayList();
		this.columnGroupsMap = new HashMap();
		this.columnGroups = new ArrayList();
		this.measuresMap = new HashMap();
		this.measures = new ArrayList();
		
		this.cellsMap = new HashMap();
		this.cellsList = new ArrayList();
		
		addBuiltinParameters();
		
		this.variablesList = new SequencedHashMap();
		addBuiltinVariables();
		
		this.dataset = new JRDesignCrosstabDataset();
	}

	private void addBuiltinParameters()
	{
		for (int i = 0; i < BUILT_IN_PARAMETERS.length; i++)
		{
			JRDesignCrosstabParameter parameter = new JRDesignCrosstabParameter();
			parameter.setName((String) BUILT_IN_PARAMETERS[i++]);
			parameter.setValueClass((Class) BUILT_IN_PARAMETERS[i]);
			parameter.setSystemDefined(true);
			try
			{
				addParameter(parameter);
			}
			catch (JRException e)
			{
				// never reached
			}
		}
	}

	private void addBuiltinVariables()
	{
		for (int i = 0; i < BUILT_IN_VARIABLES.length; ++i)
		{
			JRDesignVariable variable = new JRDesignVariable();
			variable.setName((String) BUILT_IN_VARIABLES[i]);
			variable.setValueClass((Class) BUILT_IN_VARIABLES[++i]);
			variable.setCalculation(JRVariable.CALCULATION_SYSTEM);
			variable.setSystemDefined(true);
			addVariable(variable);
		}
	}
	
	/**
	 * Creates a new crosstab.
	 */
	public JRDesignCrosstab()
	{
		this(null);
	}
	
	
	/**
	 * The ID of the crosstab is only generated at compile time.
	 */
	public int getId()
	{
		return 0;
	}

	public JRCrosstabDataset getDataset()
	{
		return this.dataset;
	}

	
	/**
	 * Returns the crosstab dataset object to be used for report designing.
	 * 
	 * @return the crosstab dataset design object
	 */
	public JRDesignCrosstabDataset getDesignDataset()
	{
		return this.dataset;
	}

	public JRCrosstabRowGroup[] getRowGroups()
	{
		JRCrosstabRowGroup[] groups = new JRCrosstabRowGroup[this.rowGroups.size()];
		this.rowGroups.toArray(groups);
		return groups;
	}

	public JRCrosstabColumnGroup[] getColumnGroups()
	{
		JRCrosstabColumnGroup[] groups = new JRCrosstabColumnGroup[this.columnGroups.size()];
		this.columnGroups.toArray(groups);
		return groups;
	}

	public JRCrosstabMeasure[] getMeasures()
	{
		JRCrosstabMeasure[] measureArray = new JRCrosstabMeasure[this.measures.size()];
		this.measures.toArray(measureArray);
		return measureArray;
	}

	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}

	/**
	 *
	 */
	public void visit(JRVisitor visitor)
	{
		visitor.visitCrosstab(this);
	}

	
	/**
	 * Sets the crosstab input dataset.
	 * 
	 * @param dataset the dataset
	 * @see JRCrosstab#getDataset()
	 */
	public void setDataset(JRDesignCrosstabDataset dataset)
	{
		Object old = this.dataset;
		this.dataset = dataset;
		getEventSupport().firePropertyChange(PROPERTY_DATASET, old, this.dataset);
	}
	
	
	/**
	 * Adds a row group.
	 * <p>
	 * This group will be a sub group of the last row group, if any.
	 * 
	 * @param group the group
	 * @throws JRException
	 * @see JRCrosstab#getRowGroups()
	 */
	public void addRowGroup(JRDesignCrosstabRowGroup group) throws JRException
	{
		String groupName = group.getName();
		if (this.rowGroupsMap.containsKey(groupName) ||
				this.columnGroupsMap.containsKey(groupName) ||
				this.measuresMap.containsKey(groupName))
		{
			throw new JRException("A group or measure having the same name already exists in the crosstab.");
		}
		
		this.rowGroupsMap.put(groupName, new Integer(this.rowGroups.size()));
		this.rowGroups.add(group);
		
		addRowGroupVars(group);
		
		setParent(group);
		
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_ROW_GROUPS, group, this.rowGroups.size() - 1);
	}

	
	protected void addRowGroupVars(JRDesignCrosstabRowGroup rowGroup)
	{
		addVariable(rowGroup.getVariable());
		
		for (Iterator measureIt = this.measures.iterator(); measureIt.hasNext();)
		{
			JRCrosstabMeasure measure = (JRCrosstabMeasure) measureIt.next();
			addTotalVar(measure, rowGroup, null);
			
			for (Iterator colIt = this.columnGroups.iterator(); colIt.hasNext();)
			{
				JRCrosstabColumnGroup colGroup = (JRCrosstabColumnGroup) colIt.next();
				addTotalVar(measure, rowGroup, colGroup);
			}
		}
	}
	
	
	/**
	 * Adds a column group.
	 * <p>
	 * This group will be a sub group of the last column group, if any.
	 * 
	 * @param group the group
	 * @throws JRException
	 * @see JRCrosstab#getColumnGroups()
	 */
	public void addColumnGroup(JRDesignCrosstabColumnGroup group) throws JRException
	{
		String groupName = group.getName();
		if (this.rowGroupsMap.containsKey(groupName) ||
				this.columnGroupsMap.containsKey(groupName) ||
				this.measuresMap.containsKey(groupName))
		{
			throw new JRException("A group or measure having the same name already exists in the crosstab.");
		}
		
		this.columnGroupsMap.put(groupName, new Integer(this.columnGroups.size()));
		this.columnGroups.add(group);
		
		addColGroupVars(group);
		
		setParent(group);
		
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_COLUMN_GROUPS, group, this.columnGroups.size() - 1);
	}
	
	
	protected void addColGroupVars(JRDesignCrosstabColumnGroup colGroup)
	{
		addVariable(colGroup.getVariable());
		
		for (Iterator measureIt = this.measures.iterator(); measureIt.hasNext();)
		{
			JRCrosstabMeasure measure = (JRCrosstabMeasure) measureIt.next();
			addTotalVar(measure, null, colGroup);

			for (Iterator rowIt = this.rowGroups.iterator(); rowIt.hasNext();)
			{
				JRCrosstabRowGroup rowGroup = (JRCrosstabRowGroup) rowIt.next();
				addTotalVar(measure, rowGroup, colGroup);
			}
		}
	}

	/**
	 * Adds a measure to the crosstab.
	 * 
	 * @param measure the measure
	 * @throws JRException
	 * @see JRCrosstab#getMeasures()
	 */
	public void addMeasure(JRDesignCrosstabMeasure measure) throws JRException
	{
		String measureName = measure.getName();
		if (this.rowGroupsMap.containsKey(measureName) ||
				this.columnGroupsMap.containsKey(measureName) ||
				this.measuresMap.containsKey(measureName))
		{
			throw new JRException("A group or measure having the same name already exists in the crosstab.");
		}
		
		measure.addPropertyChangeListener(JRDesignCrosstabMeasure.PROPERTY_VALUE_CLASS, this.measureClassChangeListener);
		
		this.measuresMap.put(measureName, new Integer(this.measures.size()));
		this.measures.add(measure);
		
		addMeasureVars(measure);
		
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_MEASURES, measure, this.measures.size() - 1);
	}

	protected void addMeasureVars(JRDesignCrosstabMeasure measure)
	{
		addVariable(measure.getVariable());
		
		for (Iterator colIt = this.columnGroups.iterator(); colIt.hasNext();)
		{
			JRCrosstabColumnGroup colGroup = (JRCrosstabColumnGroup) colIt.next();
			addTotalVar(measure, null, colGroup);
		}
		
		for (Iterator rowIt = this.rowGroups.iterator(); rowIt.hasNext();)
		{
			JRCrosstabRowGroup rowGroup = (JRCrosstabRowGroup) rowIt.next();
			addTotalVar(measure, rowGroup, null);
			
			for (Iterator colIt = this.columnGroups.iterator(); colIt.hasNext();)
			{
				JRCrosstabColumnGroup colGroup = (JRCrosstabColumnGroup) colIt.next();
				addTotalVar(measure, rowGroup, colGroup);
			}
		}
	}


	protected void addTotalVar(JRCrosstabMeasure measure, JRCrosstabRowGroup rowGroup, JRCrosstabColumnGroup colGroup)
	{
		JRDesignVariable var = new JRDesignVariable();
		var.setCalculation(JRVariable.CALCULATION_SYSTEM);
		var.setSystemDefined(true);
		var.setName(getTotalVariableName(measure, rowGroup, colGroup));
		var.setValueClassName(measure.getValueClassName());
		addVariable(var);
	}


	protected void removeTotalVar(JRCrosstabMeasure measure, JRCrosstabRowGroup rowGroup, JRCrosstabColumnGroup colGroup)
	{
		String varName = getTotalVariableName(measure, rowGroup, colGroup);
		removeVariable(varName);
	}

	
	public static String getTotalVariableName(JRCrosstabMeasure measure, JRCrosstabRowGroup rowGroup, JRCrosstabColumnGroup colGroup)
	{
		StringBuffer name = new StringBuffer();
		name.append(measure.getName());
		if (rowGroup != null)
		{
			name.append('_');
			name.append(rowGroup.getName());
		}
		if (colGroup != null)
		{
			name.append('_');
			name.append(colGroup.getName());
		}
		name.append("_ALL");
		return name.toString();
	}


	/**
	 * Removes a row group.
	 * 
	 * @param groupName the group name
	 * @return the removed group
	 */
	public JRCrosstabRowGroup removeRowGroup(String groupName)
	{
		JRCrosstabRowGroup removed = null;
		
		Integer idx = (Integer) this.rowGroupsMap.remove(groupName);
		if (idx != null)
		{
			removed = (JRCrosstabRowGroup) this.rowGroups.remove(idx.intValue());
			
			for (ListIterator it = this.rowGroups.listIterator(idx.intValue()); it.hasNext();)
			{
				JRCrosstabRowGroup group = (JRCrosstabRowGroup) it.next();
				this.rowGroupsMap.put(group.getName(), new Integer(it.previousIndex()));
			}
			
			for (Iterator it = this.cellsList.iterator(); it.hasNext();)
			{
				JRCrosstabCell cell = (JRCrosstabCell) it.next();
				String rowTotalGroup = cell.getRowTotalGroup();
				if (rowTotalGroup != null && rowTotalGroup.equals(groupName))
				{
					it.remove();
					this.cellsMap.remove(new Pair(rowTotalGroup, cell.getColumnTotalGroup()));
					getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_CELLS, cell, -1);
				}
			}
			
			removeRowGroupVars(removed);
			
			getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_ROW_GROUPS, removed, idx.intValue());
		}
		
		return removed;
	}

	protected void removeRowGroupVars(JRCrosstabRowGroup rowGroup)
	{
		removeVariable(rowGroup.getVariable());
		
		for (Iterator measureIt = this.measures.iterator(); measureIt.hasNext();)
		{
			JRCrosstabMeasure measure = (JRCrosstabMeasure) measureIt.next();
			removeTotalVar(measure, rowGroup, null);
			
			for (Iterator colIt = this.columnGroups.iterator(); colIt.hasNext();)
			{
				JRCrosstabColumnGroup colGroup = (JRCrosstabColumnGroup) colIt.next();
				removeTotalVar(measure, rowGroup, colGroup);
			}
		}
	}


	/**
	 * Removes a row group.
	 * 
	 * @param group the group to be removed
	 * @return the removed group
	 */
	public JRCrosstabRowGroup removeRowGroup(JRCrosstabRowGroup group)
	{
		return removeRowGroup(group.getName());
	}
	
	
	/**
	 * Removes a column group.
	 * 
	 * @param groupName the group name
	 * @return the removed group
	 */
	public JRCrosstabColumnGroup removeColumnGroup(String groupName)
	{
		JRCrosstabColumnGroup removed = null;
		
		Integer idx = (Integer) this.columnGroupsMap.remove(groupName);
		if (idx != null)
		{
			removed = (JRCrosstabColumnGroup) this.columnGroups.remove(idx.intValue());
			
			for (ListIterator it = this.columnGroups.listIterator(idx.intValue()); it.hasNext();)
			{
				JRCrosstabColumnGroup group = (JRCrosstabColumnGroup) it.next();
				this.columnGroupsMap.put(group.getName(), new Integer(it.previousIndex()));
			}
			
			for (Iterator it = this.cellsList.iterator(); it.hasNext();)
			{
				JRCrosstabCell cell = (JRCrosstabCell) it.next();
				String columnTotalGroup = cell.getColumnTotalGroup();
				if (columnTotalGroup != null && columnTotalGroup.equals(groupName))
				{
					it.remove();
					this.cellsMap.remove(new Pair(cell.getRowTotalGroup(), columnTotalGroup));
					getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_CELLS, cell, -1);
				}
			}
			
			removeColGroupVars(removed);
			
			getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_COLUMN_GROUPS, removed, idx.intValue());
		}
		
		return removed;
	}

	
	protected void removeColGroupVars(JRCrosstabColumnGroup colGroup)
	{
		removeVariable(colGroup.getVariable());
		
		for (Iterator measureIt = this.measures.iterator(); measureIt.hasNext();)
		{
			JRCrosstabMeasure measure = (JRCrosstabMeasure) measureIt.next();
			removeTotalVar(measure, null, colGroup);

			for (Iterator rowIt = this.rowGroups.iterator(); rowIt.hasNext();)
			{
				JRCrosstabRowGroup rowGroup = (JRCrosstabRowGroup) rowIt.next();
				removeTotalVar(measure, rowGroup, colGroup);
			}
		}
	}
	
	
	/**
	 * Removes a column group.
	 * 
	 * @param group the group
	 * @return the removed group
	 */
	public JRCrosstabColumnGroup removeColumnGroup(JRCrosstabColumnGroup group)
	{
		return removeColumnGroup(group.getName());
	}
	
	
	/**
	 * Removes a measure.
	 * 
	 * @param measureName the measure name
	 * @return the removed measure
	 */
	public JRCrosstabMeasure removeMeasure(String measureName)
	{
		JRDesignCrosstabMeasure removed = null;
		
		Integer idx = (Integer) this.measuresMap.remove(measureName);
		if (idx != null)
		{
			removed = (JRDesignCrosstabMeasure) this.measures.remove(idx.intValue());
			
			for (ListIterator it = this.measures.listIterator(idx.intValue()); it.hasNext();)
			{
				JRCrosstabMeasure group = (JRCrosstabMeasure) it.next();
				this.measuresMap.put(group.getName(), new Integer(it.previousIndex()));
			}
			
			removeMeasureVars(removed);
			
			removed.removePropertyChangeListener(JRDesignCrosstabMeasure.PROPERTY_VALUE_CLASS, this.measureClassChangeListener);
			
			getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_MEASURES, removed, idx.intValue());
		}
		
		return removed;
	}

	protected void removeMeasureVars(JRDesignCrosstabMeasure measure)
	{
		removeVariable(measure.getVariable());
		
		for (Iterator colIt = this.columnGroups.iterator(); colIt.hasNext();)
		{
			JRCrosstabColumnGroup colGroup = (JRCrosstabColumnGroup) colIt.next();
			removeTotalVar(measure, null, colGroup);
		}
		
		for (Iterator rowIt = this.rowGroups.iterator(); rowIt.hasNext();)
		{
			JRCrosstabRowGroup rowGroup = (JRCrosstabRowGroup) rowIt.next();
			removeTotalVar(measure, rowGroup, null);
			
			for (Iterator colIt = this.columnGroups.iterator(); colIt.hasNext();)
			{
				JRCrosstabColumnGroup colGroup = (JRCrosstabColumnGroup) colIt.next();
				removeTotalVar(measure, rowGroup, colGroup);
			}
		}
	}
	
	
	/**
	 * Removes a measure.
	 * 
	 * @param measure the measure
	 * @return the removed measure
	 */
	public JRCrosstabMeasure removeMeasure(JRCrosstabMeasure measure)
	{
		return removeMeasure(measure.getName());
	}

	public boolean isRepeatColumnHeaders()
	{
		return this.repeatColumnHeaders;
	}

	
	/**
	 * Sets the repeat column headers flag.
	 * 
	 * @param repeatColumnHeaders whether to repeat the column headers on row breaks
	 * @see JRCrosstab#isRepeatColumnHeaders()
	 */
	public void setRepeatColumnHeaders(boolean repeatColumnHeaders)
	{
		boolean old = this.repeatColumnHeaders;
		this.repeatColumnHeaders = repeatColumnHeaders;
		getEventSupport().firePropertyChange(PROPERTY_REPEAT_COLUMN_HEADERS, old, this.repeatColumnHeaders);
	}

	public boolean isRepeatRowHeaders()
	{
		return this.repeatRowHeaders;
	}

	
	/**
	 * Sets the repeat row headers flag.
	 * 
	 * @param repeatRowHeaders whether to repeat the row headers on column breaks
	 * @see JRCrosstab#isRepeatRowHeaders()
	 */
	public void setRepeatRowHeaders(boolean repeatRowHeaders)
	{
		boolean old = this.repeatRowHeaders;
		this.repeatRowHeaders = repeatRowHeaders;
		getEventSupport().firePropertyChange(PROPERTY_REPEAT_ROW_HEADERS, old, this.repeatRowHeaders);
	}

	public JRCrosstabCell[][] getCells()
	{
		return this.crossCells;
	}

	
	/**
	 * Returns the data cells list.
	 * 
	 * @return the data cells list
	 * @see #addCell(JRDesignCrosstabCell)
	 */
	public List getCellsList()
	{
		return this.cellsList;
	}
	
	
	/**
	 * Returns the crosstab cells indexed by corresponding row total group/
	 * column total group {@link Pair pairs}.
	 * 
	 * @return the crosstab cells indexed by row/column total groups
	 * @see JRCrosstabCell#getRowTotalGroup()
	 * @see JRCrosstabCell#getColumnTotalGroup()
	 */
	public Map getCellsMap()
	{
		return this.cellsMap;
	}
	
	/**
	 * Adds a data cell to the crosstab.
	 * 
	 * @param cell the cell
	 * @throws JRException
	 * @see JRCrosstab#getCells()
	 */
	public void addCell(JRDesignCrosstabCell cell) throws JRException
	{
		String rowTotalGroup = cell.getRowTotalGroup();		
		if (rowTotalGroup != null && !this.rowGroupsMap.containsKey(rowTotalGroup))
		{
			throw new JRException("Row group " + rowTotalGroup + " does not exist.");
		}
		
		String columnTotalGroup = cell.getColumnTotalGroup();
		if (columnTotalGroup != null && !this.columnGroupsMap.containsKey(columnTotalGroup))
		{
			throw new JRException("Row group " + columnTotalGroup + " does not exist.");
		}
		
		Object cellKey = new Pair(rowTotalGroup, columnTotalGroup);
		if (this.cellsMap.containsKey(cellKey))
		{
			throw new JRException("Duplicate cell in crosstab.");
		}
		
		this.cellsMap.put(cellKey, cell);
		this.cellsList.add(cell);
		
		setCellOrigin(cell.getContents(),
				new JRCrosstabOrigin(this, JRCrosstabOrigin.TYPE_DATA_CELL,
						rowTotalGroup, columnTotalGroup));
		
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_CELLS, cell, this.cellsList.size() - 1);
	}
	
	
	/**
	 * Removes a data cell.
	 * 
	 * @param rowTotalGroup the cell's total row group 
	 * @param columnTotalGroup the cell's total column group
	 * @return the removed cell
	 */
	public JRCrosstabCell removeCell(String rowTotalGroup, String columnTotalGroup)
	{
		Object cellKey = new Pair(rowTotalGroup, columnTotalGroup);
		
		JRCrosstabCell cell = (JRCrosstabCell) this.cellsMap.remove(cellKey);
		if (cell != null)
		{
			this.cellsList.remove(cell);
			getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_CELLS, cell, -1);
		}
		
		return cell;
	}
	
	
	/**
	 * Removes a data cell.
	 * 
	 * @param cell the cell to be removed
	 * @return the removed cell
	 */
	public JRCrosstabCell removeCell(JRCrosstabCell cell)
	{
		return removeCell(cell.getRowTotalGroup(), cell.getColumnTotalGroup());
	}
	

	public JRCrosstabParameter[] getParameters()
	{
		JRCrosstabParameter[] parameters = new JRCrosstabParameter[this.parametersList.size()];
		this.parametersList.toArray(parameters);
		return parameters;
	}
	
	
	/**
	 * Returns the paremeters list.
	 * 
	 * @return the paremeters list
	 */
	public List getParametersList()
	{
		return this.parametersList;
	}
	
	
	/**
	 * Returns the parameters indexed by names.
	 * 
	 * @return the parameters indexed by names
	 */
	public Map getParametersMap()
	{
		return this.parametersMap;
	}

	public JRExpression getParametersMapExpression()
	{
		return this.parametersMapExpression;
	}
	
	
	/**
	 * Adds a parameter to the crosstab.
	 * 
	 * @param parameter the parameter
	 * @throws JRException
	 * @see JRCrosstab#getMeasures()
	 */
	public void addParameter(JRCrosstabParameter parameter) throws JRException
	{
		if (this.parametersMap.containsKey(parameter.getName()))
		{
			if (this.parametersMap.containsKey(parameter.getName()))
			{
				throw new JRException("Duplicate declaration of parameter : " + parameter.getName());
			}
		}
		
		this.parametersMap.put(parameter.getName(), parameter);
		this.parametersList.add(parameter);
		
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_PARAMETERS, parameter, this.parametersList.size() - 1);
	}
	
	
	/**
	 * Removes a parameter.
	 * 
	 * @param parameterName the name of the parameter to be removed
	 * @return the removed parameter
	 */
	public JRCrosstabParameter removeParameter(String parameterName)
	{
		JRCrosstabParameter param = (JRCrosstabParameter) this.parametersMap.remove(parameterName);
		
		if (param != null)
		{
			int idx = this.parametersList.indexOf(param);
			if (idx >= 0)
			{
				this.parametersList.remove(idx);
			}
			getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_PARAMETERS, param, idx);
		}
		
		return param;
	}
	
	
	/**
	 * Removes a parameter.
	 * 
	 * @param parameter the parameter to be removed
	 * @return the removed parameter
	 */
	public JRCrosstabParameter removeParameter(JRCrosstabParameter parameter)
	{
		return removeParameter(parameter.getName());
	}
	
	
	/**
	 * Sets the parameters map expression.
	 * 
	 * @param expression the parameters map expression
	 * @see JRCrosstab#getParametersMapExpression()
	 */
	public void setParametersMapExpression(JRExpression expression)
	{
		Object old = this.parametersMapExpression;
		this.parametersMapExpression = expression;
		getEventSupport().firePropertyChange(PROPERTY_PARAMETERS_MAP_EXPRESSION, old, this.parametersMapExpression);
	}
	
	
	/**
	 * Returns the variables of this crosstab indexed by name.
	 * 
	 * @return the variables of this crosstab indexed by name
	 */
	public Map getVariablesMap()
	{
		JRVariable[] variables = getVariables();
		Map variablesMap = new HashMap();
		
		for (int i = 0; i < variables.length; i++)
		{
			variablesMap.put(variables[i].getName(), variables[i]);
		}
		
		return variablesMap;
	}
	
	
	/**
	 * Returns the list of variables created for this crosstab.
	 * 
	 * @return the list of variables created for this crosstab
	 * @see JRCrosstabGroup#getVariable()
	 * @see JRCrosstabMeasure#getVariable()
	 * @see JRCrosstab#VARIABLE_ROW_COUNT
	 * @see JRCrosstab#VARIABLE_COLUMN_COUNT
	 */
	public JRVariable[] getVariables()
	{
		JRVariable[] variables = new JRVariable[this.variablesList.size()];
		this.variablesList.values().toArray(variables);
		return variables;
	}
	

	public int getColumnBreakOffset()
	{
		return this.columnBreakOffset;
	}

	
	/**
	 * Sets the column break offset.
	 * 
	 * @param columnBreakOffset the offset
	 * @see JRCrosstab#getColumnBreakOffset()
	 */
	public void setColumnBreakOffset(int columnBreakOffset)
	{
		int old = this.columnBreakOffset;
		this.columnBreakOffset = columnBreakOffset;
		getEventSupport().firePropertyChange(PROPERTY_COLUMN_BREAK_OFFSET, (float) old, (float) this.columnBreakOffset);
	}

	
	/**
	 * Performs all the calculations required for report compilation.
	 */
	public void preprocess()
	{
		setGroupVariablesClass(this.rowGroups);
		setGroupVariablesClass(this.columnGroups);
		
		calculateSizes();
	}

	
	protected void setGroupVariablesClass(List groups)
	{
		for (Iterator it = groups.iterator(); it.hasNext();)
		{
			JRDesignCrosstabGroup group = (JRDesignCrosstabGroup) it.next();
			JRCrosstabBucket bucket = group.getBucket();
			if (bucket != null)
			{
				JRExpression expression = bucket.getExpression();
				if (expression != null)
				{
					group.designVariable.setValueClassName(expression.getValueClassName());
				}
			}
		}
	}

	protected void calculateSizes()
	{
		setWhenNoDataCellSize();
		
		createCellMatrix();
		
		int rowHeadersWidth = calculateRowHeadersSizes();
		int colHeadersHeight = calculateColumnHeadersSizes();
		
		if (this.headerCell != null)
		{
			this.headerCell.setWidth(rowHeadersWidth);
			this.headerCell.setHeight(colHeadersHeight);
		}
	}

	protected void setWhenNoDataCellSize()
	{
		if (this.whenNoDataCell != null)
		{
			this.whenNoDataCell.setWidth(getWidth());
			this.whenNoDataCell.setHeight(getHeight());
		}
	}

	protected void createCellMatrix()
	{
		this.crossCells = new JRDesignCrosstabCell[this.rowGroups.size() + 1][this.columnGroups.size() + 1];
		for (Iterator it = this.cellsList.iterator(); it.hasNext();)
		{
			JRDesignCrosstabCell crosstabCell = (JRDesignCrosstabCell) it.next();
			JRDesignCellContents contents = (JRDesignCellContents) crosstabCell.getContents();
			
			String rowTotalGroup = crosstabCell.getRowTotalGroup();
			int rowGroupIndex = rowTotalGroup == null ? rowGroupIndex = this.rowGroups.size() : ((Integer) this.rowGroupsMap.get(rowTotalGroup)).intValue();
			
			Integer cellWidth = crosstabCell.getWidth();
			if (cellWidth != null)
			{
				contents.setWidth(cellWidth.intValue());
			}

			String columnTotalGroup = crosstabCell.getColumnTotalGroup();
			int columnGroupIndex = columnTotalGroup == null ? columnGroupIndex = this.columnGroups.size() : ((Integer) this.columnGroupsMap.get(columnTotalGroup)).intValue();
			Integer cellHeight = crosstabCell.getHeight();
			if (cellHeight != null)
			{
				contents.setHeight(cellHeight.intValue());
			}

			this.crossCells[rowGroupIndex][columnGroupIndex] = crosstabCell;
		}
		
		inheritCells();
	}

	protected JRDesignCrosstabRowGroup getRowGroup(int rowGroupIndex)
	{
		return (JRDesignCrosstabRowGroup) this.rowGroups.get(rowGroupIndex);
	}

	protected JRDesignCrosstabColumnGroup getColumnGroup(int columnGroupIndex)
	{
		return (JRDesignCrosstabColumnGroup) this.columnGroups.get(columnGroupIndex);
	}

	protected void inheritCells()
	{
		for (int i = this.rowGroups.size(); i >= 0 ; --i)
		{
			for (int j = this.columnGroups.size(); j >= 0 ; --j)
			{
				boolean used = (i == this.rowGroups.size() || getRowGroup(i).hasTotal()) &&
					(j == this.columnGroups.size() || getColumnGroup(j).hasTotal());
				
				if (used)
				{
					if (this.crossCells[i][j] == null)
					{
						inheritCell(i, j);
						
						if (this.crossCells[i][j] == null)
						{
							this.crossCells[i][j] = emptyCell(i, j);
							inheritCellSize(i, j);
						}
					}
					else
					{
						inheritCellSize(i, j);
					}
				}
				else
				{
					this.crossCells[i][j] = null;
				}
			}
		}
	}

	private JRDesignCrosstabCell emptyCell(int i, int j)
	{
		JRDesignCrosstabCell emptyCell = new JRDesignCrosstabCell();
		if (i < this.rowGroups.size())
		{
			emptyCell.setRowTotalGroup(((JRCrosstabRowGroup) this.rowGroups.get(i)).getName());
		}
		if (j < this.columnGroups.size())
		{
			emptyCell.setColumnTotalGroup(((JRCrosstabColumnGroup) this.columnGroups.get(j)).getName());
		}
		return emptyCell;
	}

	protected void inheritCellSize(int i, int j)
	{
		JRDesignCrosstabCell cell = this.crossCells[i][j];
		
		JRDesignCellContents contents = (JRDesignCellContents) cell.getContents();
		
		if (contents.getWidth() == JRCellContents.NOT_CALCULATED)
		{
			if (i < this.rowGroups.size())
			{
				JRDesignCrosstabCell rowCell = this.crossCells[this.rowGroups.size()][j];
				if (rowCell != null)
				{
					contents.setWidth(rowCell.getContents().getWidth());
				}
			}
			else
			{
				for (int k = j + 1; k <= this.columnGroups.size(); ++k)
				{
					if (this.crossCells[i][k] != null)
					{
						contents.setWidth(this.crossCells[i][k].getContents().getWidth());
						break;
					}
				}
			}
		}
		
		if (contents.getHeight() == JRCellContents.NOT_CALCULATED)
		{
			if (j < this.columnGroups.size())
			{
				JRDesignCrosstabCell colCell = this.crossCells[i][this.columnGroups.size()];
				if (colCell != null)
				{
					contents.setHeight(colCell.getContents().getHeight());
				}
			}
			else
			{
				for (int k = i + 1; k <= this.rowGroups.size(); ++k)
				{
					if (this.crossCells[k][j] != null)
					{
						contents.setHeight(this.crossCells[k][j].getContents().getHeight());
					}
				}
			}
		}
	}

	
	protected void inheritCell(int i, int j)
	{
		JRDesignCrosstabCell inheritedCell = null;
		
		if (j < this.columnGroups.size())
		{
			JRDesignCrosstabCell colCell = this.crossCells[this.rowGroups.size()][j];
			JRDesignCellContents colContents = colCell == null ? null : (JRDesignCellContents) colCell.getContents();
			for (int k = j + 1; inheritedCell == null && k <= this.columnGroups.size(); ++k)
			{
				JRDesignCrosstabCell cell = this.crossCells[i][k];
				if (cell != null)
				{
					JRDesignCellContents contents = (JRDesignCellContents) cell.getContents();
					if (colContents == null || contents.getWidth() == colContents.getWidth())
					{
						inheritedCell = cell;
					}
				}
			}
		}
		
		if (inheritedCell == null && i < this.rowGroups.size())
		{
			JRDesignCrosstabCell rowCell = this.crossCells[i][this.columnGroups.size()];
			JRDesignCellContents rowContents = rowCell == null ? null : (JRDesignCellContents) rowCell.getContents();
			for (int k = i + 1; inheritedCell == null && k <= this.rowGroups.size(); ++k)
			{
				JRDesignCrosstabCell cell = this.crossCells[k][j];
				if (cell != null)
				{
					JRDesignCellContents contents = (JRDesignCellContents) cell.getContents();
					if (rowContents == null || contents.getHeight() == rowContents.getHeight())
					{
						inheritedCell = cell;
					}
				}
			}
		}
		
		this.crossCells[i][j] = inheritedCell;
	}

	protected int calculateRowHeadersSizes()
	{
		int widthSum = 0;
		for (int i = this.rowGroups.size() - 1, heightSum = 0; i >= 0; --i)
		{
			JRDesignCrosstabRowGroup group = (JRDesignCrosstabRowGroup) this.rowGroups.get(i);

			widthSum += group.getWidth();
			
			JRDesignCrosstabCell cell = this.crossCells[i + 1][this.columnGroups.size()];
			if (cell != null)
			{
				heightSum += cell.getContents().getHeight();
			}

			JRDesignCellContents header = (JRDesignCellContents) group.getHeader();
			header.setHeight(heightSum);
			header.setWidth(group.getWidth());

			if (group.hasTotal())
			{
				JRDesignCellContents totalHeader = (JRDesignCellContents) group.getTotalHeader();
				totalHeader.setWidth(widthSum);
				JRDesignCrosstabCell totalCell = this.crossCells[i][this.columnGroups.size()];
				if (totalCell != null)
				{
					totalHeader.setHeight(totalCell.getContents().getHeight());
				}
			}
		}
		return widthSum;
	}

	protected int calculateColumnHeadersSizes()
	{
		int heightSum = 0;
		for (int i = this.columnGroups.size() - 1, widthSum = 0; i >= 0; --i)
		{
			JRDesignCrosstabColumnGroup group = (JRDesignCrosstabColumnGroup) this.columnGroups.get(i);

			heightSum += group.getHeight();
			JRDesignCrosstabCell cell = this.crossCells[this.rowGroups.size()][i + 1];
			if (cell != null)
			{
				widthSum += cell.getContents().getWidth();
			}

			JRDesignCellContents header = (JRDesignCellContents) group.getHeader();
			header.setHeight(group.getHeight());
			header.setWidth(widthSum);

			if (group.hasTotal())
			{
				JRDesignCellContents totalHeader = (JRDesignCellContents) group.getTotalHeader();
				totalHeader.setHeight(heightSum);
				JRDesignCrosstabCell totalCell = this.crossCells[this.rowGroups.size()][i];
				if (totalCell != null)
				{
					totalHeader.setWidth(totalCell.getContents().getWidth());
				}
			}
		}
		return heightSum;
	}

	public JRCellContents getWhenNoDataCell()
	{
		return this.whenNoDataCell;
	}

	
	/**
	 * Sets the "No data" cell.
	 * 
	 * @param whenNoDataCell the cell
	 * @see JRCrosstab#getWhenNoDataCell()
	 */
	public void setWhenNoDataCell(JRDesignCellContents whenNoDataCell)
	{
		Object old = this.whenNoDataCell;
		this.whenNoDataCell = whenNoDataCell;
		setCellOrigin(this.whenNoDataCell, new JRCrosstabOrigin(this, JRCrosstabOrigin.TYPE_WHEN_NO_DATA_CELL));
		getEventSupport().firePropertyChange(PROPERTY_WHEN_NO_DATA_CELL, old, this.whenNoDataCell);
	}

	
	public JRElement getElementByKey(String elementKey)
	{
		return JRBaseCrosstab.getElementByKey(this, elementKey);
	}
	
	
	public byte getMode()
	{
		return JRStyleResolver.getMode(this, MODE_TRANSPARENT);
	}

	public JRCellContents getHeaderCell()
	{
		return this.headerCell;
	}
	
	
	/**
	 * Sets the crosstab header cell (this cell will be rendered at the upper-left corder of the crosstab).
	 * 
	 * @param headerCell the cell
	 * @see JRCrosstab#getHeaderCell()
	 */
	public void setHeaderCell(JRDesignCellContents headerCell)
	{
		Object old = this.headerCell;
		this.headerCell = headerCell;
		setCellOrigin(this.headerCell, new JRCrosstabOrigin(this, JRCrosstabOrigin.TYPE_HEADER_CELL));
		getEventSupport().firePropertyChange(PROPERTY_HEADER_CELL, old, this.headerCell);
	}

	
	protected void measureClassChanged(JRDesignCrosstabMeasure measure, String valueClassName)
	{
		for (Iterator colIt = this.columnGroups.iterator(); colIt.hasNext();)
		{
			JRCrosstabColumnGroup colGroup = (JRCrosstabColumnGroup) colIt.next();
			setTotalVarClass(measure, null, colGroup, valueClassName);
		}
		
		for (Iterator rowIt = this.rowGroups.iterator(); rowIt.hasNext();)
		{
			JRCrosstabRowGroup rowGroup = (JRCrosstabRowGroup) rowIt.next();
			setTotalVarClass(measure, rowGroup, null, valueClassName);
			
			for (Iterator colIt = this.columnGroups.iterator(); colIt.hasNext();)
			{
				JRCrosstabColumnGroup colGroup = (JRCrosstabColumnGroup) colIt.next();
				setTotalVarClass(measure, rowGroup, colGroup, valueClassName);
			}
		}
	}
	
	protected void setTotalVarClass(JRCrosstabMeasure measure, JRCrosstabRowGroup rowGroup, JRCrosstabColumnGroup colGroup, String valueClassName)
	{
		JRDesignVariable variable = getVariable(getTotalVariableName(measure, rowGroup, colGroup));
		variable.setValueClassName(valueClassName);
	}

	private void addVariable(JRVariable variable)
	{
		this.variablesList.put(variable.getName(), variable);
	}

	private void removeVariable(JRVariable variable)
	{
		removeVariable(variable.getName());
	}

	private void removeVariable(String varName)
	{
		this.variablesList.remove(varName);
	}
	
	private JRDesignVariable getVariable(String varName)
	{
		return (JRDesignVariable) this.variablesList.get(varName);
	}
	
	public byte getRunDirection()
	{
		return this.runDirection;
	}
	
	public void setRunDirection(byte runDirection)
	{
		byte old = this.runDirection;
		this.runDirection = runDirection;
		getEventSupport().firePropertyChange(JRBaseCrosstab.PROPERTY_RUN_DIRECTION, (float) old, (float) this.runDirection);
	}
	
	protected void setCellOrigin(JRCellContents cell, JRCrosstabOrigin origin)
	{
		if (cell instanceof JRDesignCellContents)
		{
			setCellOrigin((JRDesignCellContents) cell, origin);
		}
	}
	
	protected void setCellOrigin(JRDesignCellContents cell, JRCrosstabOrigin origin)
	{
		if (cell != null)
		{
			cell.setOrigin(origin);
		}
	}
	
	protected void setParent(JRDesignCrosstabGroup group)
	{
		if (group != null)
		{
			group.setParent(this);
		}
	}
	
	/**
	 * 
	 */
	public Object clone() 
	{
		JRDesignCrosstab clone = (JRDesignCrosstab)super.clone();
		
		if (this.parametersList != null)
		{
			clone.parametersList = new ArrayList(this.parametersList.size());
			clone.parametersMap = new HashMap(this.parametersList.size());
			for(int i = 0; i < this.parametersList.size(); i++)
			{
				JRCrosstabParameter parameter = 
					(JRCrosstabParameter)((JRCrosstabParameter)this.parametersList.get(i)).clone();
				clone.parametersList.add(parameter);
				clone.parametersMap.put(parameter.getName(), parameter);
			}
		}
		
		if (this.variablesList != null)
		{
			clone.variablesList = new SequencedHashMap(this.variablesList.size());
			for(Iterator it = this.variablesList.sequence().iterator(); it.hasNext();)
			{
				Object key = it.next();
				JRVariable variable = (JRVariable)this.variablesList.get(key);
				clone.variablesList.put(variable.getName(), variable);
			}
		}
		
		if (this.parametersMapExpression != null)
		{
			clone.parametersMapExpression = (JRExpression)this.parametersMapExpression.clone();
		}
		if (this.dataset != null)
		{
			clone.dataset = (JRDesignCrosstabDataset)this.dataset.clone();
		}
		
		if (this.rowGroups != null)
		{
			clone.rowGroups = new ArrayList(this.rowGroups.size());
			clone.rowGroupsMap = new HashMap(this.rowGroups.size());
			for(int i = 0; i < this.rowGroups.size(); i++)
			{
				JRCrosstabRowGroup group = 
					(JRCrosstabRowGroup)((JRCrosstabRowGroup)this.rowGroups.get(i)).clone();
				clone.rowGroups.add(group);
				clone.rowGroupsMap.put(group.getName(), new Integer(i));
			}
		}
		
		if (this.columnGroups != null)
		{
			clone.columnGroups = new ArrayList(this.columnGroups.size());
			clone.columnGroupsMap = new HashMap(this.columnGroups.size());
			for(int i = 0; i < this.columnGroups.size(); i++)
			{
				JRCrosstabColumnGroup group = 
					(JRCrosstabColumnGroup)((JRCrosstabColumnGroup)this.columnGroups.get(i)).clone();
				clone.columnGroups.add(group);
				clone.columnGroupsMap.put(group.getName(), new Integer(i));
			}
		}
		
		if (this.measures != null)
		{
			clone.measures = new ArrayList(this.measures.size());
			clone.measuresMap = new HashMap(this.measures.size());
			for(int i = 0; i < this.measures.size(); i++)
			{
				JRCrosstabMeasure measure = 
					(JRCrosstabMeasure)((JRCrosstabMeasure)this.measures.get(i)).clone();
				clone.measures.add(measure);
				clone.measuresMap.put(measure.getName(), new Integer(i));
			}
		}
		
		if (this.cellsList != null)
		{
			clone.cellsList = new ArrayList(this.cellsList.size());
			clone.cellsMap = new HashMap(this.cellsList.size());
			for(int i = 0; i < this.cellsList.size(); i++)
			{
				JRDesignCrosstabCell cell = 
					(JRDesignCrosstabCell)((JRDesignCrosstabCell)this.cellsList.get(i)).clone();
				clone.cellsList.add(cell);
				clone.cellsMap.put(new Pair(cell.getRowTotalGroup(), cell.getColumnTotalGroup()), cell);
			}
		}
		
		if (this.whenNoDataCell != null)
		{
			clone.whenNoDataCell = (JRDesignCellContents)this.whenNoDataCell.clone();
		}
		if (this.headerCell != null)
		{
			clone.headerCell = (JRDesignCellContents)this.headerCell.clone();
		}

		return clone;
	}
	
	public List getRowGroupsList()
	{
		return this.rowGroups;
	}
	
	public Map getRowGroupIndicesMap()
	{
		return this.rowGroupsMap;
	}
	
	public List getColumnGroupsList()
	{
		return this.columnGroups;
	}
	
	public Map getColumnGroupIndicesMap()
	{
		return this.columnGroupsMap;
	}
	
	public List getMesuresList()
	{
		return this.measures;
	}
	
	public Map getMeasureIndicesMap()
	{
		return this.measuresMap;
	}
	
}
