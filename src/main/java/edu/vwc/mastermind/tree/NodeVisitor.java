package edu.vwc.mastermind.tree;

/**
 * Interface for visiting a Node or a collection of Nodes and accessing
 * aggregate data.
 * 
 * @author tomboyo
 *
 * @param <T>
 *            Type of data stored by Node
 * @param <E>
 *            Type if aggregated data
 */
public interface NodeVisitor<T, E> {

	void visit(Node<T> it);

	E value();
}
