package edu.vwc.mastermind.core;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
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
    public Tree newTree(Code guess, Set<Code> guessed, Set<Code> answers) {
        final Set<Code> copyOfGuessed = new LinkedHashSet<>(guessed);
        Tree root = new Tree(guess);
        copyOfGuessed.add(guess);

        Map<Response, Set<Code>> answerGroups =
                groupAnswersByResponseToGuess(answers, guess);

        answerGroups.forEach((response, answersLeft) -> {
            if (response.isCorrect())
                return;

            if (answersLeft.size() == 1) {
                root.add(response, createLeafNode(answersLeft));
            } else {
                root.add(response,
                        createInteriorNode(guess, copyOfGuessed, answersLeft));
            }
        });

        return root;
    }

    private Tree createLeafNode(Collection<Code> answersLeft) {
        return new Tree(answersLeft.iterator().next());
    }

    private Tree createInteriorNode(
            Code guess,
            Set<Code> guessed,
            Set<Code> answersLeft) {
        Set<Code> nextGuesses = guessProviderFactory
                .getInstance(guessed, answersLeft)
                .getCodes();
        Set<Code> nextAnswers = new LinkedHashSet<>(answersLeft);
        nextAnswers.remove(guess);

        return findOptimalTree(nextGuesses, guessed, answersLeft);
    }

    private Tree findOptimalTree(
            Collection<Code> guessesRemaining,
            Set<Code> guessed,
            Set<Code> answersRemaining) {
        Iterator<Code> guesses = guessesRemaining.iterator();
        Tree optimal = newTree(guesses.next(), guessed, answersRemaining);

        while (guesses.hasNext()) {
            Tree other = newTree(guesses.next(), guessed, answersRemaining);
            if (comparator.compare(optimal, other) > 0)
                optimal = other;
        }

        return optimal;
    }

    private Map<Response, Set<Code>> groupAnswersByResponseToGuess(
            Collection<Code> answers,
            Code guess) {
        Map<Response, Set<Code>> answerGroups = new HashMap<>();
        for (Code answer : answers) {
            Response key = guess.compareTo(answer);
            answerGroups.putIfAbsent(key, new HashSet<>());
            answerGroups.get(key).add(answer);
        }
        return answerGroups;
    }

}
