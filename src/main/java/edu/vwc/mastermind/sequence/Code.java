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
public final class Code {

	private static final Map<Integer, Code> cache = new HashMap<>();
	
	private final int[] sequence;
	private final int hash;

	private Code(int... sequence) {
		this.sequence = sequence.clone();
		hash = generateHash(this.sequence);
	}
	
	/**
	 * Get a canonical Code instance.
	 * 
	 * @param sequence
	 *            The integer sequence which describes this code.
	 * @return The canonical Code instance for this sequence
	 */
	public static Code valueOf(int[] sequence) {
		int key = generateHash(sequence);
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
	 * Get a canonical Code instance
	 * 
	 * @param first
	 *            The first integer in the sequence that describes this Code
	 * @param rest
	 *            The remaining integers in the sequence
	 * @return The canonical ode instance for this Sequence
	 */
	public static Code valueOf(int first, int... rest) {
		return valueOf(join(first, rest));
	}
	
	private static int[] join(int first, int[] rest) {
		int[] joinedArray = new int[rest.length + 1];
		joinedArray[0] = first;
		System.arraycopy(rest,  0, joinedArray, 1, rest.length);
		return joinedArray;
	}
	
	/**
	 * Get a view of this Code as an integer array.
	 * 
	 * @return An array representation of this code
	 */
	public int[] toArray() {
		return sequence.clone();
	}
	
	public int length() {
		return sequence.length;
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
	 * Because response objects are canonical, we use reference equality.
	 */
	@Override
	public final boolean equals(Object other) {
		return this == other;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(sequence);
	}
	
	private static int generateHash(int[] sequence) {
		return Arrays.hashCode(sequence);
	}
}
