package edu.vwc.mastermind.core;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.Response;
import edu.vwc.mastermind.sequence.provider.CodesProvider;
import edu.vwc.mastermind.sequence.provider.CodesProviderFactory;
import edu.vwc.mastermind.tree.Tree;

/**
 * Manages construction of a Mastermind game strategy tree in a multithreaded
 * environment.
 */
public class Controller {

	private final Response correctResponse;
	private final Comparator<Tree> comparator;
	private final CodesProvider firstGuessProvider;
	private final CodesProvider goalCodeProvider;
	private final CodesProviderFactory guessCodesProviderFactory;

	/**
	 * 
	 * @param correctResponse
	 *            The canonical instance of Response which represents a correct
	 *            answer for this game. This should be provided via
	 *            {@code Response.valueOf(pegs, 0, 0)}.
	 * @param comparator
	 *            A comparator used to sort {@link Tree}s by some arbitrary
	 *            logic. Lower-valued Tree instances are preferred by the
	 *            TreeFactory.
	 * @param guessCodesProviderFactory
	 *            The CodesProvider instances produced by this factory are
	 *            responsible for suggesting sets of Code instances to use as
	 *            future guesses during Tree creation.
	 * @param firstGuessProvider
	 *            Provides a set of Codes guessed first. For each code, a
	 *            complete game tree will be generated. All the trees are
	 *            compared by the comparator to determine which is "best."
	 * @param goalCodeProvider
	 *            Provides a Set of all codes that must be eventually guessed in
	 *            the strategy tree.
	 */
	public Controller(Response correctResponse, Comparator<Tree> comparator,
			CodesProviderFactory guessCodesProviderFactory,
			CodesProvider firstGuessProvider, CodesProvider goalCodeProvider) {
		this.correctResponse = correctResponse;
		this.comparator = comparator;
		this.firstGuessProvider = firstGuessProvider;
		this.goalCodeProvider = goalCodeProvider;
		this.guessCodesProviderFactory = guessCodesProviderFactory;
	}
	
	/**
	 * Builds a Mastermind strategy tree. Note that repeated calls to this
	 * method can return entirely different Tree structures, but the configured
	 * comparator will evaluate all returned trees as equivalent.
	 * 
	 * @throws ExecutionException
	 *             If a simulation branch encounters error
	 * @throws InterruptedException
	 *             If a simulation branch is interrupted
	 */
	public Tree simulate()
			throws ExecutionException, InterruptedException {
		ExecutorService executorService = Executors
				.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		CompletionService<Tree> completionService =
				new ExecutorCompletionService<Tree>(executorService);
		
		int count = 0;
		for (Code guess : firstGuessProvider.getCodes()) {
			TreeFactory factory = new TreeFactory(correctResponse, comparator,
					guessCodesProviderFactory);
			completionService.submit(
					new Branch(factory, guess, goalCodeProvider.getCodes()));
			count++;
		}
		executorService.shutdown();

		Tree result = null;
		for (int i = 0; i < count; i++) {
			Tree next = completionService.take().get();
			if (comparator.compare(next,  result) < 0) {
				result = next;
			}
		}
		
		return result;
	}
	
	private class Branch implements Callable<Tree> {
		private final TreeFactory factory;
		private final Code guess;
		private final Set<Code> answers;
		
		private Branch(TreeFactory factory, Code guess, Set<Code> answers) {
			this.factory = factory;
			this.guess = guess;
			this.answers = answers;
		}
		
		public Tree call() {
			return factory.newTree(guess,
					new LinkedHashSet<>(),
					answers);
		}
	}
}
