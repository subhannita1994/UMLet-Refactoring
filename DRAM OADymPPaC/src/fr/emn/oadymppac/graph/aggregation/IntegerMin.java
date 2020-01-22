package fr.emn.oadymppac.graph.aggregation;

/**
 * @author Mohammad Ghoniem
 * @version $Revision: 1.1 $
 *
 * This class encapsulates the min of an 
 * array of <code>Integer</code>s.
 */
public class IntegerMin implements AggregationFunction {

	/**
	 * @see fr.emn.oadymppac.graph.aggregation.AggregationFunction#compute(Object[])
	 */
	public Object compute(final Object[] args) {
		int min = Integer.MAX_VALUE;
		try {
			for (int i = 0; i < args.length; i++) {
				min = Math.min(min, ((Integer) args[i]).intValue());
			}
		}
		catch (final ClassCastException ex) {
			ex.printStackTrace();
		}
		return new Integer(min);
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
