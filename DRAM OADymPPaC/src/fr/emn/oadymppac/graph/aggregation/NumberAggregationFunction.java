package fr.emn.oadymppac.graph.aggregation;

/**
 * @author Mohammad Ghoniem
 * @version $Revision: 1.1 $
 *
 * Abstract class for handling operations on
 * numbers.
 */
public abstract class NumberAggregationFunction implements AggregationFunction {

	public static final String PROPERTY_NAME = "NumberFunction";

	abstract double compute(double[] args);

	abstract float compute(float[] args);

	abstract int compute(int[] args);
	/**
	 * Tests the type of the arguments and calls
	 * the appropriate method.
	 * 
	 * @see fr.emn.oadymppac.graph.aggregation.AggregationFunction#compute(Object[])
	 */
	public Object compute(final Object[] args) {
		if (!(args[0] instanceof Number)) {
			throw new ClassCastException();
		}

		if (args[0] instanceof Integer) {
			final int[] args2 = new int[args.length];
			for (int i = 0; i < args.length; i++) {
				args2[i] = ((Integer) args[i]).intValue();
			}
			return new Integer(this.compute(args2));
		}

		if (args[0] instanceof Float) {
			final float[] args2 = new float[args.length];
			for (int i = 0; i < args.length; i++) {
				args2[i] = ((Float) args[i]).intValue();
			}
			return new Float(this.compute(args2));
		}

		if (args[0] instanceof Double) {
			final double[] args2 = new double[args.length];
			for (int i = 0; i < args.length; i++) {
				args2[i] = ((Double) args[i]).doubleValue();
			}
			return new Double(this.compute(args2));
		}

		return null;
	}
	public String getFunctionDescription() {
		return NumberAggregationFunction.PROPERTY_NAME;
	}

}
