package edu.vwc.mastermind.core;

import java.util.concurrent.ExecutionException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import edu.vwc.mastermind.tree.Tree;

public class Main {

	public static void main(String[] args) {
		String configuration = System.getProperty("edu.vwc.mastermind.config",
				"../conf/mastermind.xml");
		
		System.out.println("Attempting to load configuration from "
				+ configuration);
		
		FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext(configuration);
		
		Controller controller = context.getBean("controller", Controller.class);
		
		try {
			Tree tree = controller.simulate();
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
