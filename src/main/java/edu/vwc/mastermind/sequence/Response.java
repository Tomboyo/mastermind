package edu.vwc.mastermind.sequence;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.collections4.map.MultiKeyMap;

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
public final class Response {

	private static final MultiKeyMap<Integer, Response> cache =
			MultiKeyMap.multiKeyMap(new HashedMap<>());
	
	private final int exact;
	private final int inexact;
	private final int wrong;

	private Response(int exact, int inexact, int wrong) {
		this.exact = exact;
		this.inexact = inexact;
		this.wrong = wrong;
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
		if (exact < 0 || inexact < 0 || wrong < 0)
			throw new IllegalArgumentException(
					"Feedback can not contain negative values");
		if (exact == 0 && inexact == 0 && wrong == 0)
			throw new IllegalArgumentException(
					"Feedback must contain a positive value");
		
		Response response = cache.get(exact, inexact, wrong);
		if (response == null) {
			synchronized (cache) {
				if ((response = cache.get(exact, inexact, wrong)) == null) {
					response = new Response(exact, inexact, wrong);
					cache.put(exact, inexact, wrong, response);
				}
			}
		}
		
		return response;
	}
	
	public int getExact() {
		return exact;
	}
	
	public int getInexact() {
		return inexact;
	}
	
	public int getWrong() {
		return wrong;
	}
	
	public boolean isCorrect() {
		return wrong == 0
				&& inexact == 0;
	}
	
	@Override
	public String toString() {
		return String.format("[%s, %s, %s]", exact, inexact, wrong);
	}
}
