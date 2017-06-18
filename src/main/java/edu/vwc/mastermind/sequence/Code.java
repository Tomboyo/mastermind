package edu.vwc.mastermind.sequence;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a colored peg sequence guessed by the Code Breaker in a1 game of Mastermind.
 * @author Tomboyo
 *
 */
public class Code {

	private static Map<Integer, Code> cache = new HashMap<>();
	
	private final int[] sequence;
	private final int hash;

	private Code(int... sequence) {
		this.sequence = sequence.clone();
		this.hash = hash(this.sequence);
	}
	
	public static Code valueOf(int... sequence) {
		int key = hash(sequence);
		Code code = cache.get(key);
		if (code == null) {
			synchronized (cache) {
				if ((code = cache.get(key)) == null) {
					code = new Code(sequence);
					cache.put(key, code);
				}
			}
		}
		return code;
	}
	
	/**
	 * Get a view of this Code as an integer array. Note that modifying the
	 * array will have no impact on the code instance.
	 * 
	 * @return An array representation of this code
	 */
	public int[] toArray() {
		return sequence.clone();
	}
	
	/**
	 * Get the feedback for this code when compared against a given answer
	 * 
	 * @param answer
	 *            Code to compare this one against
	 * @return A Response object indicating the results of the comparison
	 */
	public Response compareTo(Code answer) {
		if (answer.sequence.length != sequence.length) {
			throw new IllegalArgumentException(
					"Codes of unequal length are incomperable");
		}

		// 0: no feedback
		// 1: misplaced peg
		// 2: correct peg
		int[] response = new int[sequence.length];
		boolean[] this_mask = new boolean[sequence.length];
		boolean[] answer_mask = new boolean[sequence.length];
		short index = 0;

		// Pass 1: Count correct pegs
		for (int i = 0; i < sequence.length; i++) {
			if (sequence[i] == answer.sequence[i]) {
				response[index++] = 2;
				this_mask[i] = true;
				answer_mask[i] = true;
			}
		}

		// Pass 2: Count misplaced pegs
		for (int i = 0; i < sequence.length; i++) {
			if (this_mask[i] == true) continue;

			for (int j = 0; j < answer.sequence.length; j++) {
				if (answer_mask[j] == true) continue;

				if (sequence[i] == answer.sequence[j]) {
					response[index++] = 1;
					answer_mask[j] = true;
				}
			}
		}

		return Response.valueOf(response);
	}

	@Override
	public int hashCode() {
		return hash;
	}

	/**
	 * Two Codes are considered equal if and only if they are the same length
	 * and have identical numbers ("pegs") in identical positions.
	 */
	@Override
	public final boolean equals(Object other) {
		if (other == this) return true;
		if (!(other instanceof Code)) return false;
		return Arrays.equals(sequence, ((Code) other).sequence);
	}
	
	@Override
	public String toString() {
		return Arrays.toString(sequence);
	}
	
	private static int hash(int... sequence) {
		int h = 17;
		for (int i : sequence) {
			h = 31 * h + i;
		}
		return h;
	}
}
