package edu.vwc.mastermind.core;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.Response;
import edu.vwc.mastermind.sequence.provider.CodesProviderFactory;
import edu.vwc.mastermind.tree.Tree;

/**
 * The TreeFactory is responsible for generating a Mastermind strategy tree
 * beginning with a provided guess. TreeFactories are instantiated with
 * decision-making objects that define what an optimal strategy tree looks like,
 * to which TreeFactories defer during the tree-building process in order to
 * produce a strategy tree that adheres to any arbitrary criteria.
 */
public class TreeFactory {
	
	private Comparator<Tree> comparator;
	private CodesProviderFactory guessProviderFactory;
	
	public TreeFactory(
			Comparator<Tree> comparator,
			CodesProviderFactory guessProviderFactory) {
		this.comparator = comparator;
		this.guessProviderFactory = guessProviderFactory;
	}
	
	/**
	 * Create a strategy tree that begins with the provided guess Code.
	 * 
	 * @param guess
	 *            The first guess to make in evaluating the game
	 * @param guessed
	 *            Codes that have already been guessed, which may or may not be
	 *            guessed again at the discrimination of the configured
	 *            CodesProviderFactory.
	 * @param answers
	 *            Possible answers remaining, which may be used by the
	 *            configured CodesProviderFactory as a hint to determine what
	 *            codes to evaluate as guesses in what order.
	 * @return A strategy tree beginning with {@code guess} and proceeding based
	 *         on decisions made by the configured {@link Comparator<Tree>} and
	 *         {@link CodesProviderFactory}.
	 */
	public Tree newTree(Code guess, Set<Code> alreadyGuessed, Set<Code> answers) {
		Set<Code> guessed = new LinkedHashSet<>(alreadyGuessed);
		Tree root = new Tree(guess);
		guessed.add(guess);
		
		/*
		 * Compare guess to answers, then group the answers by the response they
		 * yield.
		 */
		Map<Response, Set<Code>> answerGroups = new HashMap<>();
		for (Code answer : answers) {
			Response key = guess.compareTo(answer);
			answerGroups.putIfAbsent(key, new HashSet<>());
			answerGroups.get(key).add(answer);
		}
		
		/*
		 * Build out child nodes
		 */
		for (Entry<Response, Set<Code>> group : answerGroups.entrySet()) {
			Set<Code> answersLeft = group.getValue();
			
			if (group.getKey().isCorrect())
				continue;
			
			if (answersLeft.size() == 1) {
				root.add(group.getKey(),
						new Tree(answersLeft.iterator().next()));
			} else {
				// Compare the possible outcomes and pick the "best" one
				Tree preferred = null;
				Set<Code> nextGuesses = guessProviderFactory
						.getInstance(guessed, answersLeft)
						.getCodes();
				for (Code nextGuess : nextGuesses) {
					Set<Code> nextGuessed = new HashSet<>();
					nextGuessed.addAll(guessed);
					Set<Code> nextAnswers = new HashSet<>();
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
