package edu.vwc.mastermind.tree;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Composite tree data structure for recording data. The data reference is
 * immutable; all state operations are dictated to it. This data structure is
 * both iterable and visitable.
 * 
 * @author tomboyo
 *
 * @param <T>
 *            The type of stored data
 */
public class Node<T> implements Iterable<Node<T>> {

	public final T data;

	LinkedList<Node<T>> children;

	public Node(T data) {
		children = new LinkedList<Node<T>>();
		this.data = data;
	}

	public boolean add(Node<T> child) {
		return children.add(child);
	}

	public void accept(NodeVisitor<T, ?> visitor) {
		visitor.visit(this);
	}

	@Override
	public Iterator<Node<T>> iterator() {
		return children.iterator();
	}

}
