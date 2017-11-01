package edu.vwc.mastermind.core;

import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.provider.CodesProviderFactory;
import edu.vwc.mastermind.tree.Tree;

public class Main {
	
	public static void main(String[] args) {
		if (args.length == 0 || args[0].isEmpty())
			throw new IllegalArgumentException(
					"missing path argument to configuration file");
		
		String configuration = args[0];
		System.out.println("Attempting to load configuration from "
				+ configuration);
		
		FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext(configuration);
		
		//Controller controller = context.getBean("controller", Controller.class);
		
		Comparator<Tree> comparator =
				context.getBean("comparator", Comparator.class);
		ExecutorService executor =
				context.getBean("executor", ExecutorService.class);
		CodesProviderFactory guessProviderFactory =
				context.getBean("factory", CodesProviderFactory.class);
		Set<Code> firstGuesses = context.getBean("first_guesses", Set.class);
		Set<Code> allAnswers = context.getBean("all_answers", Set.class);
		
		TreeFactory treeFactory = new TreeFactory(
				comparator,
				guessProviderFactory);
		ParallelizedTreeFactory parallelizedTreeFactory =
				new ParallelizedTreeFactory(executor, treeFactory);
		Controller controller = new Controller(
				parallelizedTreeFactory,
				comparator);
		
		try {
			Tree tree = controller.newOptimizedStrategyTree(
					firstGuesses, allAnswers);
			System.out.println("Startegy tree maximum turns: " + tree.depth());
			System.out.println(tree);
		} catch (ExecutionException | InterruptedException e) {
			System.err.println("Failed to create strategy tree: " + e);
			e.printStackTrace();
		} finally {
			context.close();
		}
	}

}
