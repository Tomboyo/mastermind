package edu.vwc.mastermind.core;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.Response;
import edu.vwc.mastermind.tree.Node;
import edu.vwc.mastermind.tree.TurnData;

public class TreeFactory {
	
	private Response correct;
	private Comparator<Node> comparator;
	
	// private GuessProvider
	
	public TreeFactory() {
		
	}
	
	public Node newTree(Code guess, List<Code> answers) {
		Node root = new Node(guess);
		
		/*
		 * Compare guess to answers, then group the answers by the response they
		 * yield.
		 */
		Map<Response, List<Code>> answerGroups = new LinkedHashMap<>();
		for (Code answer : answers) {
			Response key = guess.compareTo(answer);
			
			List<Code> group = answerGroups.get(key);
			if (group == null) {
				group = new LinkedList<>();
				answerGroups.put(key, group);
			}
			group.add(answer);
		}
		
		/*
		 * Build out child nodes
		 */
		for (Entry<Response, List<Code>> group : answerGroups.entrySet()) {
			List<Code> answersLeft = group.getValue();
			
			if (group.getKey() == correct) {
				// do not add a child node in this case
			} else if (answersLeft.size() == 1) {
				// Add the correct answer node
				root.add(correct, new Node(answersLeft.get(0)));
			} else {
				// Compare the possible outcomes and pick the "best" one
				Node preferred = null;
				// TODO: DI guess picker. For now, we pick among possible
				// answers, which is sub-optimal.
				for (Code nextGuess : answersLeft) {
					Node next = newTree(nextGuess, answersLeft);
					if (comparator.compare(preferred, next) < 0) {
						preferred = next;
					}
				}
				root.add(group.getKey(), preferred);
			}
		}

		return root;
	}
	
}
