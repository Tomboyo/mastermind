package edu.vwc.mastermind.core;

import java.util.concurrent.ExecutionException;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import edu.vwc.mastermind.tree.Tree;

public class Main {

	public static void main(String[] args) {
		String configuration = System.getProperty("edu.vwc.mastermind.config");
		
		if (configuration.isEmpty()) {
			System.err.println("No configuration file provided."
					+ " Set with -Dedu.vwc.mastermind.config=\"path/to/xml\"");
		}
		System.out.println("Attempting to load configuration from "
				+ configuration);
		
		FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext(configuration);
		
		Controller controller = context.getBean("controller", Controller.class);
		
		try {
			Tree tree = controller.newOptimizedStrategyTree();
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
