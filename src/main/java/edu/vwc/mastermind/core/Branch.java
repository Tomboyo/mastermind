package edu.vwc.mastermind.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.Response;
import edu.vwc.mastermind.tree.GameTree;

public class Branch implements Callable<GameTree> {

	private final Code guess;
	private final Code[] possibleAnswers;
	private final Comparator<GameTree> branchSelector;
	private final boolean[] guessMask;

	/**
	 * Initialize computational branch
	 * 
	 * @param guess
	 *            What Code to compare against all possible answers
	 * @param possibleAnswers
	 *            Codes that could be the answer to this game so far
	 * @param guessed
	 *            What COdes have already been guessed
	 */
	public Branch(Code guess, Code[] possibleAnswers,
			Comparator<GameTree> branchSelector, boolean[] guessMask) {
		this.guess = guess;
		this.possibleAnswers = possibleAnswers;
		this.branchSelector = branchSelector;
		this.guessMask = guessMask;
	}

	/**
	 * Create a GameTree object from the configuration provided to the
	 * constructor.
	 * @return The complete game tree.
	 */
	public GameTree call() throws Exception {
		Map<Response, List<Code>> answerGroups = 
				new HashMap<>(possibleAnswers.length);

		/*
		 * Compare the given guess against every possible answer to the game.
		 * Group the possible answers by the response the guess would garner.
		 */
		for (Code answer : possibleAnswers) {
			Response response = guess.compareTo(answer);

			List<Code> answerGroup = answerGroups.get(response);
			if (answerGroup == null) {
				answerGroup = new ArrayList<Code>(possibleAnswers.length);
			}
			answerGroup.add(answer);
		}

		/*
		 * Create the root node of the Game Tree record
		 */
		GameTree root = new GameTree(guess, answerGroups);

		/*
		 * Iterate over the answer groups and begin recursive subcomputation.
		 * For every group, we treat the answers as the remaining possible
		 * answers in the child branch. The Game Tree that each Branch.call()
		 * returns will be complete. We should select the "best" of the child
		 * branches and discard others (per answer group). The best child
		 * branch is then recorded under root.
		 */
		Iterator<Entry<Response, List<Code>>> iter = 
				answerGroups.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<Response, List<Code>> answerGroup = iter.next();

			GameTree best = null;
			Code[] nextPossibleAnswers = 
					answerGroup.getValue().toArray(new Code[] {});
			for (int i = 0; i < nextPossibleAnswers.length; i++) {
				// Ignore previously guessed codes
				if (guessMask[i]) continue;
				
				// Update mask for child branch
				boolean[] nextGuessMask = 
						Arrays.copyOf(guessMask, guessMask.length);
				nextGuessMask[i] = true;
				
				// Simulate a subordinate branch with a new guess
				Code nextGuess = nextPossibleAnswers[i];
				GameTree alternative = new Branch(nextGuess, nextPossibleAnswers,
						branchSelector, nextGuessMask).call();
				
				// If the simulated branch is "better" than the current best,
				// prefer it.
				if (branchSelector.compare(best, alternative) > 0) {
					best = alternative;
				}
			}
			
			// Append the best sub child of this answer group to the root Game
			// Tree.
			root.addChild(best);
		}

		return root;
	}

}
