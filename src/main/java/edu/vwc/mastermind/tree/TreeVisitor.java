package edu.vwc.mastermind.tree;

public interface TreeVisitor<T, E> {

	void visit(Tree<T> it);
	
	E value();
}
