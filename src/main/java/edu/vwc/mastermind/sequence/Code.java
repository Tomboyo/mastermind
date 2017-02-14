package edu.vwc.mastermind.sequence;

import java.util.Arrays;

/**
 * Represents a colored peg sequence guessed by the Code Breaker in a game of Mastermind.
 * @author Tomboyo
 *
 */
public class Code {

	private int[] sequence;

	public Code(int... sequence) {
		this.sequence = sequence;
	}
	
	/**
	 * Get the feedback for this code when compared against a given key
	 * @param answer
	 *            Code to compare this one against
	 * @return A Response object indicating the results of the comparison
	 * @throws IllegalArgumentException
	 *             When this and the answer code can not be compared (their
	 *             lengths differ)
	 */
	public Response compareTo(Code answer) throws IllegalArgumentException {
		if (answer.sequence.length != sequence.length) {
			throw new IllegalArgumentException("Length of Codes are unequal.");
		}

		// 0:x 1:w 2:r
		int[] response = new int[sequence.length];
		boolean[] gmask = new boolean[sequence.length];
		boolean[] cmask = new boolean[sequence.length];
		short index = 0;

		// Pass 1: Count correct pegs
		for (int i = 0; i < sequence.length; i++) {
			if (sequence[i] == answer.sequence[i]) {
				response[index++] = 2;
				gmask[i] = true;
				cmask[i] = true;
			}
		}

		// Pass 2: Count misplaced pegs
		for (int i = 0; i < sequence.length; i++) {
			if (gmask[i] != false) continue;

			for (int j = 0; j < answer.sequence.length; j++) {
				if (cmask[j] != false) continue;

				if (sequence[i] == answer.sequence[j]) {
					response[index++] = 1;
					cmask[j] = true;
				}
			}
		}

		return new Response(response); 
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
	 * Two Codes are considered equal if and only if they are the same length
	 * and have identical numbers ("pegs") in identical positions.
	 */
	@Override
	public final boolean equals(Object other) {
		if (other == this) return true;
		if (other == null) return false;
		if (this.getClass() != other.getClass()) return false;

		return Arrays.equals(sequence, ((Code) other).sequence);
	}
	
	@Override
	public String toString() {
		return Arrays.toString(sequence);
	}
}
