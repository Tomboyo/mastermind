package edu.vwc.mastermind.core;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.tree.Tree;

public class ParallelizedTreeFactory {
	private final CompletionService<Tree> completionService;
	private final TreeFactory treeFactory;
	private volatile int guessesMade;
	
	public ParallelizedTreeFactory(
			ExecutorService executor,
			TreeFactory treeFactory) {
		this.completionService = new ExecutorCompletionService<>(executor);
		this.treeFactory = treeFactory;
		guessesMade = 0;
	}
	
	void submitGuess(
			Code guess,
			Set<Code> alreadyGuessed,
			Set<Code> answersRemaining) {
		completionService.submit(() -> treeFactory.newTree(
				guess, alreadyGuessed, answersRemaining));
		guessesMade += 1;
	}
	
	boolean hasNext() {
		return guessesMade > 0;
	}

	Tree next() {
		if (--guessesMade < 0)
			throw new NoSuchElementException();
		
		try {
			return completionService.take().get();
		} catch (ExecutionException | InterruptedException e) {
			throw new RuntimeException(
					"Captured exception from computation", e);
		}
	}
}