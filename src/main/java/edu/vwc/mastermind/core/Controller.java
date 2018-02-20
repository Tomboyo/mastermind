package edu.vwc.mastermind.core;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.tree.Tree;

/**
 * Factory for Mastermind strategy trees. Generates result in a parallel
 * environment.
 */
public class Controller {

    private final ParallelizedTreeFactory parallelFactory;
    private final Comparator<Tree> comparator;

    public Controller(
            ParallelizedTreeFactory factory,
            Comparator<Tree> comparator) {
        this.parallelFactory = factory;
        this.comparator = comparator;
    }

    /**
     * Builds a Mastermind strategy tree. Note that repeated calls to this method
     * can return different Tree structures, but all such Trees should be
     * considered equivalent by the Comparator.
     */
    public Tree newOptimizedStrategyTree(
            Collection<Code> guesses,
            Set<Code> answers)
            throws ExecutionException, InterruptedException {
        Set<Code> alreadyGuessed = new HashSet<>();

        guesses.forEach(guess -> parallelFactory.submitGuess(
                guess, alreadyGuessed, answers));

        Tree bestResult = parallelFactory.next();
        while (parallelFactory.hasNext()) {
            Tree result = parallelFactory.next();
            if (comparator.compare(result, bestResult) < 0)
                bestResult = result;
        }

        return bestResult;
    }

}
