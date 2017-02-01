package edu.vwc.mastermind.tree;

import java.util.List;
import java.util.Map;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.Response;

/**
 * Holds data pertaining to a turn in the mastermind game tree
 * 
 * @author tomsi
 *
 */
public class TurnData {

	public final Code guess;
	public final Map<Response, List<Code>> answerGroups;

	public TurnData(Code guess, Map<Response, List<Code>> answerGroups) {
		this.guess = guess;
		this.answerGroups = answerGroups;
	}

}
