package edu.vwc.mastermind.tree;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.Response;

/**
 * When a guess is compared against all the possible answers in a branch of the
 * game, each comparison garners a Response. This object holds  collection of
 * answers grouped by the Response they garnered in comparison to a particular
 * guess.
 * @author Tom
 *
 */
public class ResponseGroup {
	public final Code guess;
	public final Response response;
	public final Code[] answers;
	
	public ResponseGroup(Code guess, Response response, Code[] answers) {
		this.guess = guess;
		this.response = response;
		this.answers = answers;
	}
}
