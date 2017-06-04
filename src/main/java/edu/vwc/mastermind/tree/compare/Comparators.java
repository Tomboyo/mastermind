package edu.vwc.mastermind.tree.compare;

import java.util.Comparator;

import edu.vwc.mastermind.tree.Tree;

public class Comparators {

	private Comparators() {}
	
	public static final Comparator<Tree> minRequiredTurns() {
		return new RequiredTurnsComparator();
	}
}
