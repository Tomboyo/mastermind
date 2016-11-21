package edu.vwc.mastermind.tree;

import java.io.PrintWriter;

/**
 * 
 * @author Tom
 *
 * @param <T>
 */
public interface ResultProcessor {
	
	/**
	 * Accept a completed game tree to process. Implementers are responsible
	 * for caching any results they made need locally, because all external
	 * references will be deleted in order to reduce memory overhead.
	 * @param root of game tree to process
	 */
	public void receive(GameTree root);
	
	/**
	 * Print the results of analysis.
	 * @param writer
	 */
	public void printResults(PrintWriter writer);
}
