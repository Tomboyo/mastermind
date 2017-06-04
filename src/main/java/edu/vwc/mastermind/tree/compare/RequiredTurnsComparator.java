package edu.vwc.mastermind.tree.compare;

import java.util.Comparator;

import edu.vwc.mastermind.tree.Tree;

public class RequiredTurnsComparator implements Comparator<Tree> {
	
	@Override
	public int compare(Tree a, Tree b) {
		if (a != null && b != null) {
			return a.depth() - b.depth();
		} else if (b != null) {
			return 1;
		} else {
			return -1;
		}
	}

}
