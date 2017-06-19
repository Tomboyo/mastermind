package edu.vwc.mastermind.sequence.provider;

import edu.vwc.mastermind.sequence.Code;

/**
 * Static factory class that provides access to {@link CodesProvider} concrete
 * implementations.
 */
public class Providers {

	private Providers() {}
	
	/**
	 * Provides a set of ${@link Code}s that enumerates over all permutations of
	 * Code with a configured number of {@code pegs} and {@code colors}. There
	 * will be {@code colors ^ pegs} total Code instances in the collection.
	 * 
	 * @param colors
	 *            How many distinctly-valued pegs a Code may be composed of;
	 *            i.e, the number of colors of pegs in the current game of
	 *            Mastermind.
	 * @param pegs
	 *            How many pegs there are in a code.
	 * @return A CodesProvider that creates a complete enumeration of all
	 *         permutations of Codes with the given number of {@code colors} and
	 *         {@code pegs}.
	 */
	public static final CodesProvider allCodes(int colors, int pegs) {
		return new AllCodesProvider(colors, pegs);
	}
}
