package edu.vwc.mastermind.sequence;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

	private static Map<Integer, Response> cache = new HashMap<>();
	
	private int[] sequence;

	private Response(int... sequence) {
		this.sequence = sequence;
	}
	
	/**
	 * Static flyweight factory for Response objects
	 * @param sequence
	 * @return
	 */
	public static Response valueOf(int... sequence) {
		int key = hashCode(sequence);
		
		Response response = cache.get(key);
		if (response == null) {
			response = new Response(sequence);
			cache.put(key, response);
		}
		
		return response;
	}
	
	private static int hashCode(int... sequence) {
		int sum = 0;
		for(int i = 0; i < sequence.length; i++) {
			sum = sum * 10 + sequence[i];
		}
		return sum;
	}

	@Override
	public int hashCode() {
		return hashCode(sequence);
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
