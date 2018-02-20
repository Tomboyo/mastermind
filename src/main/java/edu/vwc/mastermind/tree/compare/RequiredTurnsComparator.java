package edu.vwc.mastermind.tree.compare;

import java.util.Comparator;

import edu.vwc.mastermind.tree.Tree;

/**
 * Sorts a Tree based on the maximum required turns it takes to guess any
 * arbitrary answer. If strategy Tree A takes at most 6 turns to guess any
 * arbitrary answer within the universe of possible answers, but tree B takes 7,
 * comparator.compare(A, B) == -1.
 */
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
