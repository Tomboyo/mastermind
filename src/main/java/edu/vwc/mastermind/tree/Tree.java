package edu.vwc.mastermind.tree;

import java.util.Iterator;
import java.util.LinkedList;

public class Tree<T> implements Iterable<Tree<T>> {

	T data;

	LinkedList<Tree<T>> children;

	public Tree() {
		children = new LinkedList<Tree<T>>();
	}

	public Tree(T data) {
		this();
		this.data = data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}
	
	public boolean add(Tree<T> child) {
		return children.add(child);
	}
	
	public void accept(TreeVisitor<T, ?> visitor) {
		visitor.visit(this);
	}

	@Override
	public Iterator<Tree<T>> iterator() {
		return children.iterator();
	}

}
