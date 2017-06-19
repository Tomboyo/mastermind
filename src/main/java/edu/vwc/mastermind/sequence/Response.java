package edu.vwc.mastermind.sequence;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the feedback from comparing a guess code against the unknown
 * answer code in a game of Mastermind.
 * 
 * <p>
 * In Mastermind, feedback indicates three things: (1) The number of pegs in the
 * guess that precisely match pegs in the answer, (2) the number of the
 * remaining pegs that are the correct color but wrong position, and (3) the
 * number of remaining pegs in neither of the preceding categories. The response
 * does not indicate specifically which pegs in the guess are correct or
 * misplaced, but rather how many. The Response class represents these three
 * elements as integers: "exact", "inexact", and "wrong", to correspond
 * respectively with items 1, 2, and 3 above.
 * 
 * <p>
 * Responses are immutable and thread-safe. Response objects are principally
 * created via {@link Code#compareTo(Code)}.
 */
public class Response {

	private static Map<Integer, Response> cache = new HashMap<>();
	
	private final int exact;
	private final int inexact;
	private final int wrong;
	private final int hash;

	private Response(int exact, int inexact, int wrong) {
		this.exact = exact;
		this.inexact = inexact;
		this.wrong = wrong;
		this.hash = hash(this.exact, this.inexact, this.wrong);
	}
	
	/**
	 * Static flyweight factory for Response objects.
	 * 
	 * @param exact
	 *            The number of pegs in a guessed Code that exactly matched pegs
	 *            in the answer, both in value (color) and position
	 * @param inexact
	 *            The number of pegs in a guessed code that are not exact, but
	 *            if re-arranged would be exact. That is, the number of
	 *            not-exact pegs whose values (colors) are correct but whose
	 *            positions are incorrect.
	 * @param wrong
	 *            The number of pegs that are neither exact nor inexact.
	 * @return A canonical response object for the given feedback values
	 */
	public static Response valueOf(int exact, int inexact, int wrong) {
		int key = hash(exact, inexact, wrong);
		Response response = cache.get(key);
		if (response == null) {
			synchronized (cache) {
				if ((response = cache.get(key)) == null) {
					response = new Response(exact, inexact, wrong);
					cache.put(key, response);
				}
			}
		}
		
		return response;
	}

	@Override
	public int hashCode() {
		return hash;
	}

	/**
	 * Two responses are considered equal if and only if they indicate the same
	 * number of exact, inexact, and wrong pegs in a guess.
	 */
	@Override
	public final boolean equals(Object other) {
		if (other == this) return true;
		if (other == null) return false;
		if (this.getClass() != other.getClass()) return false;
		Response r = (Response) other;
		return this.exact == r.exact
				&& this.inexact == r.inexact
				&& this.wrong == r.wrong;
	}
	
	@Override
	public String toString() {
		return String.format("[%s, %s, %s]", exact, inexact, wrong);
	}
	
	private static int hash(int a, int b, int c) {
		int h = 17;
		h = 31 * h + a;
		h = 31 * h + b;
		h = 31 * h + c;
		return h;
	}
}
