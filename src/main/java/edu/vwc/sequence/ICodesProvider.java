package edu.vwc.sequence;

/**
 * Responsible for providing the set of Codes for a game.
 * @author Tom Simmons
 */
public interface ICodesProvider {
	
	/**
	 * Supply game parameters
	 * @param colors Number of colors allowed per code
	 * @param pegs Number of pegs allowed in a code
	 */
	public void set(int colors, int pegs);
	
	/**
	 * Get a subset of all permutations of Codes.
	 * This is intended to be either a complete enumeration, or a logically complete subset
	 * that omits Codes that add no complexity to the game but waste computation time.
	 * @return subset of all Code permutations
	 */
	public Code[] getCodes();

}
