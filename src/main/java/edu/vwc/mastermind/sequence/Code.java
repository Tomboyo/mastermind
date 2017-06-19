package edu.vwc.mastermind.sequence;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the colored-peg sequences that players make in a game of
 * Mastermind. In the context of a game, all such sequences must be composed of
 * a specific number of pegs within a specific range of colors.
 * 
 * <p>
 * Code instances represent the colored pegs with integers, which are generally
 * (but not necessarily) positive for simplicity. In the context of a single
 * Mastermind game (i.e, a single strategy tree), all Code instances must have a
 * specific length (equal to the game's configured number of pegs) and must be
 * composed of integers whose values are drawn from a finite collection of
 * distinct integers, the number of which must be equal to the game's colors
 * parameter. The specific integers used is otherwise arbitrary, but for
 * simplicity we assume that they are drawn from a consecutive range.
 * 
 * <p>
 * Code instances are immutable and thread-safe.
 */
public class Code {

	private static Map<Integer, Code> cache = new HashMap<>();
	
	private final int[] sequence;
	private final int hash;

	private Code(int... sequence) {
		this.sequence = sequence.clone();
		this.hash = hash(this.sequence);
	}
	
	/**
	 * Static factory for getting canonical Code instances.
	 * 
	 * @param sequence
	 *            The sequence of pegs this code represents, where each integer
	 *            represents a particular color of peg.
	 * @return The canonical Code instance for this sequence
	 */
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
	 * Get a view of this Code as an integer array.
	 * 
	 * @return An array representation of this code
	 */
	public int[] toArray() {
		return sequence.clone();
	}
	
	/**
	 * Compare this code against another and return the {@link Response} with
	 * the results of the comparison.
	 * 
	 * @param other
	 *            A Code to compare this instance against
	 * @return A Response object indicating the results of the comparison
	 */
	public Response compareTo(Code other) {
		if (other.sequence.length != sequence.length) {
			throw new IllegalArgumentException(
					"Codes of unequal length are incomperable");
		}

		int exact, inexact;
		exact = inexact = 0;
		
		boolean[] selfMask = new boolean[sequence.length];
		boolean[] otherMask = new boolean[sequence.length];

		// Pass 1: Count correct pegs
		for (int i = 0; i < sequence.length; i++) {
			if (sequence[i] == other.sequence[i]) {
				exact++;
				selfMask[i] = true;
				otherMask[i] = true;
			}
		}

		// Pass 2: Count misplaced pegs
		for (int i = 0; i < sequence.length; i++) {
			if (selfMask[i] == true) continue;

			for (int j = 0; j < other.sequence.length; j++) {
				if (otherMask[j] == true) continue;

				if (sequence[i] == other.sequence[j]) {
					inexact++;
					otherMask[j] = true;
				}
			}
		}

		return Response.valueOf(
				exact, inexact, sequence.length - exact - inexact);
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
