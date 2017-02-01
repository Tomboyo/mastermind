package edu.vwc.mastermind.tree;

/**
 * Visits nodes in a GameTree and computes data based on internal logic
 * 
 * @author tomsi
 *
 * @param <T> Type of computed data
 */
public interface GameTreeVisitor<T> {

	/**
	 * Visit a single node
	 * 
	 * @param it
	 */
	public void visit(GameTree it);

	/**
	 * Return computed data
	 * 
	 * @return
	 */
	public T value();
}
