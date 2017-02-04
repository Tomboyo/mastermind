package edu.vwc.mastermind.tree;

import java.util.List;
import java.util.Map;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.Response;

/**
 * State object generated during game tree analysis and stored in a Node
 * structure.
 * 
 * @author tomboyo
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
