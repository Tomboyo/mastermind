package edu.vwc.mastermind.tree;

/**
 * Interface for visiting a Node or a collection of Nodes and accessing
 * aggregate data.
 * 
 * @author Tom Simmons
 *
 */
public interface NodeVisitor {

	void visit(Node it);
}
