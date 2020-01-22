package net.sf.jasperreports.charts.base;

import net.sf.jasperreports.charts.JRGanttDataset;
import net.sf.jasperreports.charts.JRGanttSeries;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.base.JRBaseChartDataset;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.design.JRVerifier;

/**
 * @author Peter Risko (peter@risko.hu)
 * @version $Id: JRBaseGanttDataset.java,v 1.1 2008/09/29 16:20:32 guehene Exp $
 */
public class JRBaseGanttDataset extends JRBaseChartDataset implements JRGanttDataset {

    /**
     *
     */
    private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

    protected JRGanttSeries[] ganttSeries = null;

    /**
     *
     */
    public JRBaseGanttDataset(JRChartDataset dataset)
    {
        super(dataset);
    }
    
    /**
     *
     */
    public JRBaseGanttDataset(JRGanttDataset dataset, JRBaseObjectFactory factory)
    {
        super(dataset, factory);

        /*   */
        JRGanttSeries[] srcGanttSeries = dataset.getSeries();
        if (srcGanttSeries != null && srcGanttSeries.length > 0)
        {
            this.ganttSeries = new JRGanttSeries[srcGanttSeries.length];
            for(int i = 0; i < this.ganttSeries.length; i++)
            {
                this.ganttSeries[i] = factory.getGanttSeries(srcGanttSeries[i]);
            }
        }

    }

    
    /**
     *
     */
    public JRGanttSeries[] getSeries()
    {
        return this.ganttSeries;
    }


    /* (non-Javadoc)
     * @see net.sf.jasperreports.engine.JRChartDataset#getDatasetType()
     */
    public byte getDatasetType() {
        return JRChartDataset.GANTT_DATASET;
    }

    
    /**
     *
     */
    public void collectExpressions(JRExpressionCollector collector)
    {
        collector.collect(this);
    }


    public void validate(JRVerifier verifier)
    {
        verifier.verify(this);
    }

}
