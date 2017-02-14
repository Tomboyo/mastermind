package edu.vwc.mastermind.sequence;

import java.util.Arrays;

/**
 * Represents the response given by a COde Maker in a game of mastermind to the
 * Code Breaker's guess. 2s represent "correct color peg in correct position",
 * 1s represent "correct color peg in wrong position in code", and 0s represent
 * no additional feedback.
 * 
 * @author Tomboyo
 *
 */
public class Response {

	private int[] sequence;

	public Response(int... sequence) {
		this.sequence = sequence;
	}

	@Override
	public int hashCode() {
		int sum = 0;
		for(int i = 0; i < sequence.length; i++) {
			sum = sum * 10 + sequence[i];
		}
		return sum;
	}

	/**
	 * Two responses are considered equal if they would have the same numbers in
	 * the same positions once sorted (i.e, set equality).
	 */
	@Override
	public final boolean equals(Object other) {
		if (other == this) return true;
		if (other == null) return false;
		if (this.getClass() != other.getClass()) return false;

		return Arrays.equals(sequence, ((Response) other).sequence);
	}
	
	@Override
	public String toString() {
		return Arrays.toString(sequence);
	}
}
