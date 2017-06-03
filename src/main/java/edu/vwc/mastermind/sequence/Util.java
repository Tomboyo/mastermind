package edu.vwc.mastermind.sequence;

/**
 * A helper class for the sequence package
 * @author tom
 *
 */
final class Util {

	private Util() {}
	
	/**
	 * Concatenates elements of an integer array, returning an integer. For
	 * example, the array [1, 2, 5, 4] would produce the integer 1254 .
	 *
	 * @param sequence
	 *            A sequence of integers to concatenate
	 * @return An integer derived by concatenating the elements of the provided
	 *         sequence
	 */
	static final int sequenceToInt(int... sequence) {
		int sum = 0;
		for(int i = 0; i < sequence.length; i++) {
			sum = sum * 10 + sequence[i];
		}
		return sum;
	}
}
