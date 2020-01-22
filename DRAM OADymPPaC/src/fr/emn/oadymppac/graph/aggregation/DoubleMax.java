package fr.emn.oadymppac.graph.aggregation;

/**
 * @author Mohammad Ghoniem
 * @version $Revision: 1.1 $
 *
 * This class encapsulates the max of an
 * array of <code>Double</code>s. Better use 
 * <code>NumberMax</code>.
 */
public class DoubleMax implements AggregationFunction {

	/**
	 * @see fr.emn.oadymppac.graph.aggregation.AggregationFunction#compute(Object[])
	 */
	public Object compute(final Object[] args) {
		double max = Double.MIN_VALUE;
		try {
			for (int i = 0; i < args.length; i++) {
				max = Math.max(max, ((Double) args[i]).doubleValue());
			}
		}
		catch (final ClassCastException ex) {
			ex.printStackTrace();
		}
		return new Double(max);
	}

	/**
	 * @see fr.emn.oadymppac.graph.aggregation.AggregationFunction#getFunctionDescription()
	 */
	public String getFunctionDescription() {
		return this
			.getClass()
			.getName()
			.substring(this.getClass().getName().lastIndexOf("."));
	}

}
