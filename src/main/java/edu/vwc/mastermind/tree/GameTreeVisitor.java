package edu.vwc.mastermind.tree;

/**
 * Visit the nodes of a Game Tree and calculate some information based on them.
 * After visiting, get() may be called to retrieve the data of type T.
 * @author Tom
 *
 * @param <T> Type of data returned after inspecting the Game Tree
 */
public interface GameTreeVisitor<T> {

	/**
	 * Visit every node in the GameTree starting with "it".
	 * @param it
	 */
	public void visit(GameTree it);
	
	/**
	 * Get the value computer and stored away by visit()ing
	 * @return the stored value
	 */
	public T get();

}
