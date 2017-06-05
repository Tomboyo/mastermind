package edu.vwc.mastermind.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;

import org.omg.CORBA.Environment;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.Response;
import edu.vwc.mastermind.sequence.provider.CodesProvider;
import edu.vwc.mastermind.sequence.provider.ProviderFactories;
import edu.vwc.mastermind.sequence.provider.Providers;
import edu.vwc.mastermind.tree.Tree;

/**
 * Responsible for running the simulation. Dependencies are injected in the
 * constructor, then the game is run in a multi-threaded environment.
 * 
 * @author Tom Simmons
 *
 */
class Controller {

	private final int colors, pegs;
	private final CompletionService<Tree> cs;
	private final Comparator<Tree> comparator;

	Controller(int colors, int pegs, Comparator<Tree> comparator) {
		this.colors = colors;
		this.pegs = pegs;
		this.comparator = comparator;
		
		cs = new ExecutorCompletionService<Tree>(
				Executors.newFixedThreadPool(Runtime.getRuntime()
						.availableProcessors()));
	}
	
	/**
	 * Builds a strategy tree for a game of mastermind. Note that repeated calls
	 * to this method can return entirely different Tree structures. The only
	 * guarantee is that for any Trees u and v produced by this method with the
	 * same parameters, comparator.compare(u, v) == 0.
	 * 
	 * @throws ExecutionException
	 *             If a simulation branch encounters error
	 * @throws InterruptedException
	 *             If a simulation branch is interrupted
	 */
	public Tree simulate()
			throws ExecutionException, InterruptedException {
		
		int[] a = new int[pegs];
		Arrays.fill(a, 2);
		Response correct = Response.valueOf(a);
		
		CodesProvider allCodesProvider = Providers.allCodes(colors, pegs);
		
		int count = 0;
		for (Code guess : allCodesProvider.getCodes()) {
			TreeFactory factory = new TreeFactory(correct, comparator,
					ProviderFactories.allCodes(allCodesProvider));
			cs.submit(new Branch(factory,guess, allCodesProvider.getCodes()));
			count++;
		}
		
		Tree result = null;
		for (int i = 0; i < count; i++) {
			Tree next = cs.take().get();
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
		
		public Branch(TreeFactory factory, Code guess, Set<Code> answers) {
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
