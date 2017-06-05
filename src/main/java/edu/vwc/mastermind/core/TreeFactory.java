package edu.vwc.mastermind.core;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.Response;
import edu.vwc.mastermind.sequence.provider.CodesProviderFactory;
import edu.vwc.mastermind.tree.Tree;

public class TreeFactory {
	
	private Response correct;
	private Comparator<Tree> comparator;
	private CodesProviderFactory guessProviderFactory;
	
	// private GuessProvider
	
	public TreeFactory(Response correct, Comparator<Tree> comparator,
			CodesProviderFactory guessProviderFactory) {
		this.correct = correct;
		this.comparator = comparator;
		this.guessProviderFactory = guessProviderFactory;
	}
	
	/**
	 * Determine the best moves to make in order to win a game of mastermind.
	 * The comparator this class is configured with determines what the "best"
	 * game looks like (e.g, shortest turns to win, or shortest average turns to
	 * win). The evaluation begins with the given guess, will not repeat any
	 * codes that have already been guessed, and will eventually reach all of
	 * the provided answers.
	 * 
	 * @param guess
	 *            The first guess to make in evaluating the game
	 * @param guessed
	 *            Codes that have already been guessed
	 * @param answers
	 *            Possible answers remaining
	 * @return The optimized tree of moves
	 */
	public Tree newTree(Code guess, Set<Code> guessed, Set<Code> answers) {
		Tree root = new Tree(guess);
		guessed.add(guess);
		
		/*
		 * Compare guess to answers, then group the answers by the response they
		 * yield.
		 */
		Map<Response, Set<Code>> answerGroups = new LinkedHashMap<>();
		for (Code answer : answers) {
			Response key = guess.compareTo(answer);
			
			Set<Code> group = answerGroups.get(key);
			if (group == null) {
				group = new LinkedHashSet<>();
				answerGroups.put(key, group);
			}
			group.add(answer);
		}
		
		/*
		 * Build out child nodes
		 */
		for (Entry<Response, Set<Code>> group : answerGroups.entrySet()) {
			Set<Code> answersLeft = group.getValue();
			
			if (group.getKey() == correct) {
				// do not add a child node in this case
			} else if (answersLeft.size() == 1) {
				// Add the correct answer node
				root.add(group.getKey(),
						new Tree(answersLeft.iterator().next()));
			} else {
				// Compare the possible outcomes and pick the "best" one
				Tree preferred = null;
				Set<Code> nextGuesses = guessProviderFactory
						.getInstance(guessed, answersLeft)
						.getCodes();
				for (Code nextGuess : nextGuesses) {
					Set<Code> nextGuessed = new LinkedHashSet<>();
					nextGuessed.addAll(guessed);
					Set<Code> nextAnswers = new LinkedHashSet<>();
					nextAnswers.addAll(answersLeft);
					nextAnswers.remove(guess);
					Tree next = newTree(nextGuess, nextGuessed, nextAnswers);
					if (comparator.compare(preferred, next) > 0) {
						preferred = next;
					}
				}
				root.add(group.getKey(), preferred);
			}
		}

		return root;
	}
	
}
