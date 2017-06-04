package edu.vwc.mastermind.core;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.Response;
import edu.vwc.mastermind.sequence.provider.CodesProviderFactory;
import edu.vwc.mastermind.tree.Tree;

public class TreeFactory {
	
	private Response correct;
	private Comparator<Tree> comparator;
	private CodesProviderFactory guessProvider;
	
	// private GuessProvider
	
	public TreeFactory() {
		
	}
	
	public Tree newTree(Code guess, List<Code> guessed, List<Code> answers) {
		Tree root = new Tree(guess);
		guessed.add(guess);
		
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
				root.add(correct, new Tree(answersLeft.get(0)));
			} else {
				// Compare the possible outcomes and pick the "best" one
				Tree preferred = null;
				Code[] nextGuesses = guessProvider.getInstance(guessed, answersLeft)
						.getCodes();
				for (Code nextGuess : nextGuesses) {
					Tree next = newTree(nextGuess, guessed, answersLeft);
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
