package edu.vwc.mastermind.sequence;

/**
 * Responsible for producing subsets of Code collections, such as those used to
 * make the first guesses in a game. This could be implemented to create a
 * logically complete subset of codes such that all games terminate in the same
 * length, but fewer comparisons have to be made.
 * 
 * @author Tomboyo
 *
 */
public interface CodesFilter {

	/**
	 * Produce a filtered subset of Codes based on the Codes produced by some other provider.
	 * @param supersetProvider
	 * @return
	 */
	public Code[] getCodes(CodesProvider supersetProvider);
}
