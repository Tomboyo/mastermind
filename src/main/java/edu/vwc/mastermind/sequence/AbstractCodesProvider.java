package edu.vwc.mastermind.sequence;

import edu.vwc.mastermind.core.IFilter;

/**
 * Responsible for providing the set of Codes for a game.
 * @author Tom Simmons
 */
public abstract class AbstractCodesProvider {
	
	protected int colors;
	protected int pegs;
	protected Code[] codes;
	
	/**
	 * Supply game parameters
	 * @param colors Number of colors allowed per code
	 * @param pegs Number of pegs allowed in a code
	 */
	public AbstractCodesProvider (int colors, int pegs) {
		if (colors == 0 || pegs == 0) {
			throw new IllegalArgumentException("Colors and Pegs must be greater than zero");
		}
		
		this.colors = colors;
		this.pegs = pegs;
	}
	
	/**
	 * Get a subset of all permutations of Codes.
	 * This is intended to be either a complete enumeration, or a logically complete subset
	 * that omits Codes that add no complexity to the game but waste computation time.
	 * Should cache result to codes variable.
	 * @return subset of all Code permutations
	 */
	public abstract Code[] getCodes();
	
	/**
	 * Get a subset of getCodes() based on filter logic provided by the
	 * IFilter implementer.
	 * @param filter that controls which Codes go into the subset
	 * @return the filtered subset of getCodes()
	 */
	public Code[] getSubset(IFilter<Code> filter) {
		if (codes == null) {
			codes = getCodes();
		}
		
		return filter.filter(codes);
	}

}
