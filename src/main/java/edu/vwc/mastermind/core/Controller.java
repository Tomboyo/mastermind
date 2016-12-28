package edu.vwc.mastermind.core;

import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.CodeFilter;
import edu.vwc.mastermind.sequence.CodesProvider;
import edu.vwc.mastermind.tree.GameTree;
import edu.vwc.mastermind.tree.ResultProcessor;

public class Controller {

	private CodesProvider codesProvider;
	private CodeFilter firstGuessFilter;
	private ResultProcessor processor;
	private ExecutorService threadPool;
	
	public Controller(CodesProvider codesProvider,
			CodeFilter firstGuessFilter, ResultProcessor processor) {
		this.codesProvider = codesProvider;
		this.firstGuessFilter = firstGuessFilter;
		this.processor = processor;
		
		threadPool = Executors.newFixedThreadPool(
				Runtime.getRuntime().availableProcessors());
	}
	
	public void run() throws ExecutionException {
		Code[] firstGuesses = codesProvider.getSubset(firstGuessFilter);
		Code[] allCodes = codesProvider.getCodes();
		Future<GameTree>[] branches = new Future[firstGuesses.length];

		// Start generation of game trees
		for (int i = 0; i < firstGuesses.length; i++) {
			branches[i] = threadPool.submit(new Branch(firstGuesses[i],
					allCodes, new boolean[allCodes.length]));
		}
		
		// Schedule processing of finished trees
		Future<?>[] waitFor = new Future[branches.length];
		for (int i = 0; i < branches.length; i++) {
			final Future<GameTree> f = branches[i];
			waitFor[i] = threadPool.submit(() -> {
				try {
					processor.receive(f.get());
				} catch (InterruptedException e) {
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			});
		}
		branches = null;
		
		// Wait on execution of processor tasks
		for (Future<?> f : waitFor) {
			try {
				f.get();
			} catch (InterruptedException e) {
			} catch (ExecutionException e2) {
				System.err.println("Could not evaluate branch of game");
				throw e2;
			}
		}
		waitFor = null;
		
		// Display results of mastermind simulation
		processor.printResults(new PrintWriter(
				new BufferedOutputStream(System.out)));
		
	}
}
