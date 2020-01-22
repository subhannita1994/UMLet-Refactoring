package fr.emn.oadymppac.graph.aggregation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Mohammad Ghoniem
 * @version $Revision: 1.1 $
 *
 * This class provides a unique factory for handling aggregation functions. 
 * The user can associate data types and aggregation functions as he wills.
 */
public abstract class AbstractAggregationFunctionFactory {

	Map functionMap = new HashMap();;

	/**
	 * Adds an aggregation function class to the catalog of
	 * functions that support the passed dataType class 
	 * (e.g. Numbers, Strings etc.).
	 */
	public void addFunction(final Class dataType, final Class funcClass) {
		if (!this.functionMap.containsKey(dataType)) {
			this.functionMap.put(dataType, new HashSet());
		}
		final HashSet functions = (HashSet) this.functionMap.get(dataType);
		functions.add(funcClass);
	}

	/**
	 * Clears all the data types and associated functions
	 * stored in the factory.
	 */
	public void clear() {
		this.functionMap.clear();
	}

	protected void createDefaultTypes() {
		this.addFunction(Object.class, ObjectCount.class);
	}

	/**
	 * Returns the description of all the aggregation functions
	 * associated with the given data type class, usually the class 
	 * name.
	 */
	public String[] getAllFunctionDescription(final Class dataType) {
		final Object[] f = ((Set) this.functionMap.get(dataType)).toArray();
		final String[] res = new String[f.length];
		for (int i = 0; i < f.length; i++) {
			res[i] = ((AggregationFunction) f[i]).getFunctionDescription();
		}
		return res;
	}

	/**
	 * Returns instances of all the aggregation functions 
	 * associated with the given data type class.
	 */
	public AggregationFunction[] getFunctions(final Class dataType) {
		if (!this.functionMap.containsKey(dataType)) {
			System.out.println("data type " + dataType + " not found.");
			this.printAll();
			//return null;
			System.out.println("looking for a generic type");
			final Class[] interfaces = dataType.getInterfaces();
			Class found = null;
			for (int i = 0; i < interfaces.length && found != null; i++) {
				if (this.hasType(interfaces[i])) {
					found = interfaces[i];
					System.out.println("substitution candidate : " + found);
				}
			}
			if (found != null) {
				return this.getFunctions(found);
			}
		}
		else {
			final Object[] classes =
				((Set) this.functionMap.get(dataType)).toArray();
			final AggregationFunction[] functions =
				new AggregationFunction[classes.length];
			for (int i = 0; i < classes.length; i++) {
				try {
					functions[i] =
						(AggregationFunction) ((Class) classes[i])
							.newInstance();
				}
				catch (final IllegalAccessException ex) {
					ex.printStackTrace();
				}
				catch (final InstantiationException ex) {
					ex.printStackTrace();
				}
			}

			return functions;
		}

		return null;
	}

	/**
	 * Returns an array containing all the registered data types.
	 */
	public Class[] getTypes() {
		final Class[] classes = new Class[this.functionMap.size()];
		final Object[] res = this.functionMap.keySet().toArray();
		for (int i = 0; i < classes.length; i++) {
			classes[i] = (Class) res[i];
		}
		return classes;
	}

	public boolean hasType(final Class dataType) {
		return this.functionMap.containsKey(dataType);
	}

	public void printAll() {
		System.out.println("Aggregation function factory contents:");
		Map.Entry entry;
		Class tmp;
		for (final Iterator iter = this.functionMap.entrySet().iterator(); iter
			.hasNext();) {
			entry = (Map.Entry) iter.next();
			tmp = (Class) entry.getKey();
			for (final Iterator iter2 = ((HashSet) entry.getValue()).iterator(); iter2
				.hasNext();) {
				System.out.println("(" + tmp + ", " + iter2.next() + ")");
			}
		}
	}

	/**
	 * Removes the passed aggregation function class from the
	 * catalog of supported functions for the passed data type.
	 * If the catalog becomes empty, the dataType is removed from
	 * the factory.
	 */
	public void removeFunction(final Class dataType, final Class funcClass) {
		if (!this.functionMap.containsKey(dataType)) {
			return;
		}

		final Set functions = (Set) this.functionMap.get(dataType);
		functions.remove(funcClass);
		if (functions.size() == 0) {
			this.functionMap.remove(dataType);
		}
	}

	/**
	 * Removes the passed dataType and all the functions
	 * associated to it.
	 */
	public void removeType(final Class dataType) {
		this.functionMap.remove(dataType);
	}
}
