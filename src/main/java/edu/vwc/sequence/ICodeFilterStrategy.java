package edu.vwc.sequence;

/**
 * Contains logic to filter a set of codes into a subset, such as to create a
 * smaller collection of codes to use as first guesses.
 * 
 * @author Tom Simmons
 */
public interface ICodeFilterStrategy {
	
	/**
	 * Consume a collection of codes and return a subset.
	 * Original collection should not be modified.
	 * @param codes to filter
	 * @return the subset of codes
	 */
	public Code[] filter(Code[] codes);
}
