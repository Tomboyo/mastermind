package edu.vwc.mastermind.core;

import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.provider.CodesProviderFactory;
import edu.vwc.mastermind.tree.Tree;

public class Main {

	private static int pegs, colors;
	private static Comparator<Tree> comparator;
	private static ExecutorService executor;
	private static CodesProviderFactory factory;
	private static Set<Code> firstGuesses;
	private static Set<Code> allAnswers;
	private static TreeFactory treeFactory;
	private static ParallelizedTreeFactory parallelizedTreeFactory;
	private static Controller controller;


	public static void main(String[] args) {
		if (args.length == 0 || args[0].isEmpty())
			throw new IllegalArgumentException(
					"Missing path argument to configuration file");
		String configuration = args[0];

		loadDependenciesFromFile(configuration);
		bindDependencies();
		displayOptimizedStrategyTree();
	}

	private static void loadDependenciesFromFile(String path) {
		System.out.println("Reading configuration from " + path);
		FileSystemXmlApplicationContext context =
				new FileSystemXmlApplicationContext(path);
		loadDependencies(context);
		context.close();
	}

	@SuppressWarnings("unchecked")
	private static void loadDependencies(ApplicationContext context) {
		pegs = context.getBean("pegs", Integer.class);
		colors = context.getBean("colors", Integer.class);
		comparator = context.getBean("comparator", Comparator.class);
		executor = context.getBean("executor", ExecutorService.class);
		factory = context.getBean("factory", CodesProviderFactory.class);
		firstGuesses = context.getBean("first_guesses", Set.class);
		allAnswers = context.getBean("all_answers", Set.class);
	}

	private static void bindDependencies() {
		treeFactory = new TreeFactory(comparator, factory);
		parallelizedTreeFactory =
				new ParallelizedTreeFactory(executor, treeFactory);
		controller = new Controller(parallelizedTreeFactory, comparator);
	}

	private static void displayOptimizedStrategyTree() {
		try {
			System.out.println(String.format(
					"Generating a tree with %d pegs and %d colors",
					pegs, colors));

			Tree tree = controller.newOptimizedStrategyTree(
					firstGuesses, allAnswers);
			System.out.println("Strategy tree maximum turns: " + tree.depth());
			System.out.println(tree);

			executor.shutdown();
		} catch (ExecutionException | InterruptedException e) {
			System.err.println("Failed to create strategy tree: " + e);
			e.printStackTrace();
		}
	}

}
