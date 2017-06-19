package edu.vwc.mastermind.tree.compare;

import java.util.Comparator;

import edu.vwc.mastermind.tree.Tree;

/**
 * Static factory class that provides access to various implementations of
 * {@link Comparator<Tree>}.
 */
public class Comparators {

	private Comparators() {}
	
	/**
	 * Get a comparator that sorts a Tree based on the maximum required turns it
	 * takes to guess any arbitrary answer. If strategy Tree A takes at most 6
	 * turns to guess any arbitrary answer within the universe of possible
	 * answers, but tree B takes 7, comparator.compare(A, B) == -1.
	 * 
	 * @return The comparator
	 */
	public static final Comparator<Tree> minRequiredTurns() {
		return new RequiredTurnsComparator();
	}
}
