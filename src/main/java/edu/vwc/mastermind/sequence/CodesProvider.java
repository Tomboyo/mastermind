package edu.vwc.mastermind.sequence;

public interface CodesProvider {

	/**
	 * Get a subset of all permutations of Codes.
	 * This is intended to be either a complete enumeration, or a logically complete subset
	 * that omits Codes that add no complexity to the game but waste computation time.
	 * Should cache result to codes variable.
	 * @return subset of all Code permutations
	 */
	Code[] getCodes();

	/**
	 * Get a subset of getCodes() based on filter logic provided by the
	 * IFilter implementer.
	 * @param filter that controls which Codes go into the subset
	 * @return the filtered subset of getCodes()
	 */
	Code[] getSubset(CodeFilter filter);

}