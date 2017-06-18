package edu.vwc.mastermind.sequence;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the feedback from comparing a guess code against the unknown
 * answer code in a game of Mastermind.
 * 
 * <p>
 * In Mastermind, feedback indicates three things: (1) The number of pegs in the
 * guess that precisely matched pegs in the answer, (2) the number of the
 * remaining pegs that were of the right color but wrong position, and (3) the
 * number of the remaining pegs that were the wrong color. These three values
 * are represented by a sequence of pegs. The order of the pegs in the feedback
 * is, however, entirely irrelevant, and may be arbitrarily sorted.
 * 
 * <p>
 * The Response class represents feedback using a sequence of integers, and is
 * produced by the {@link Code#compareTo(Code)} method.
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
	 * Static flyweight factory for Response objects
	 * @param sequence
	 * @return
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
	 * Two responses are considered equal if they would have the same numbers in
	 * the same positions once sorted (i.e, set equality).
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
