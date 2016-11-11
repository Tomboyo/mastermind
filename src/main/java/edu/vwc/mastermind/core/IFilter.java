package edu.vwc.mastermind.core;

/**
 * A filter is given a collection of items, and returns a subset of those
 * items.
 * @author Tom Simmons
 *
 * @param <T> The type of the collection
 */
public interface IFilter<T> {

	/**
	 * Return a subset of the given array. The original array should be
	 * unmodified, and the subset should contain references to the same
	 * instances. The filter need not respect item order.
	 * @param array to be filtered
	 * @return a subset of the array.
	 */
	public T[] filter(T[] array);
}
