package edu.vwc.mastermind.core;

import java.util.Comparator;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.CodesFilter;
import edu.vwc.mastermind.sequence.CodesProvider;
import edu.vwc.mastermind.tree.PreorderIterator;
import edu.vwc.mastermind.tree.Node;
import edu.vwc.mastermind.tree.NodeVisitor;
import edu.vwc.mastermind.tree.TurnData;

/**
 * Responsible for running the simulation. Dependencies are injected in the
 * constructor, then the game in run in a multithreaded environment. branches
 * are started for each initial guess, and those branches are compared to one
 * another once resolved. The optimal branch is kept for further processing.
 * Each controller instance should be responsible for the simulation of exactly
 * one game.
 * 
 * TODO: Ideally this task can be distributed across multiple machines. This is
 * beyond the scope of my current knowledge (and hardware access) but is
 * absolutely on the chalkboard.
 * 
 * TODO: With Node/TUrnData/COmparator/Visitor the way they are, it seems like
 * we've created families of objects that need to work in concert. An abstract
 * factory implementation may be in order, and in any case the Controller needs
 * to be made agnostic to their types.
 * 
 * @author Tomboyo
 *
 */
public class Controller {

	// Configuration
	private CodesProvider codesProvider;
	private CodesFilter firstGuessFilter;
	private Comparator<Node<TurnData>> branchSelector;
	private CompletionService<Node<TurnData>> cs;
	private Node<TurnData> result;

	/**
	 * Configure the controller with simulation dependencies.
	 * 
	 * @param codesProvider
	 *            Instance to generate all the codes used by the game.
	 * @param firstGuessFilter
	 *            Determines which codes to use to make the first guesses of the
	 *            game, filtered from codesProvider
	 * @param branchSelector
	 *            Logic to decide what the optimal branch looks like (e.g,
	 *            shortest game on average, shortest game absolutely)
	 */
	public Controller(CodesProvider codesProvider, CodesFilter firstGuessFilter,
			Comparator<Node<TurnData>> branchSelector) {
		this.codesProvider = codesProvider;
		this.firstGuessFilter = firstGuessFilter;
		this.branchSelector = branchSelector;

		cs = new ExecutorCompletionService<Node<TurnData>>(
				Executors.newFixedThreadPool(
						Runtime.getRuntime().availableProcessors()));

		result = null;
	}

	/**
	 * Begins simulation based on constructor parameters.
	 * 
	 * @throws ExecutionException
	 *             If a simulation branch encounters error
	 * @throws InterruptedException
	 *             If a simulation branch is interrupted
	 */
	public synchronized void run()
			throws ExecutionException, InterruptedException {
		Code[] firstGuessSubset = firstGuessFilter.getCodes(codesProvider);
		Code[] allCodes = codesProvider.getCodes();

		// Start generation of game trees
		for (Code guess : firstGuessSubset) {
			cs.submit(new Branch(guess, allCodes, branchSelector,
					new boolean[allCodes.length]));
		}

		// Select the best branch
		Node<TurnData> localBest = null;
		for (int i = 0; i < firstGuessSubset.length; i++) {
			Node<TurnData> gameTree = cs.take().get();
			// If the gameTree is "better"...
			if (branchSelector.compare(gameTree, localBest) < 0) {
				localBest = gameTree;
			}
		}

		result = localBest;
	}

	public <E> E processResult (NodeVisitor<TurnData, E> visitor) {
		if (result == null)
			throw new IllegalStateException("Result has not yet been computed");

		PreorderIterator<Node<TurnData>> iter = new PreorderIterator<>(result);
		while (iter.hasNext()) {
			visitor.visit(iter.next());
		}
		
		return visitor.value();
	}
}
