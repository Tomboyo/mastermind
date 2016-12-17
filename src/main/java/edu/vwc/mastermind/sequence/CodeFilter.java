package edu.vwc.mastermind.sequence;

/**
 * A filter is given a collection of items, and returns a subset of those
 * items. The original collection and its items are unmodified by filtration,
 * and the filtered subset returned is a new collection with references to the
 * instances that already existed in the original superset.
 * @author Tom Simmons
 *
 * @param <T> The type of the collection
 */
public interface CodeFilter {

	/**
	 * Return a subset of the given array. The original array should be
	 * unmodified, and the subset should contain references to the same
	 * instances. The filter need not respect item order.
	 * @param array to be filtered
	 * @return a subset of the array.
	 */
	public Code[] filter(Code[] superset);
}
