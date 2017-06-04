package edu.vwc.mastermind.tree;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.Response;

/**
 * Composite tree data structure for recording data.
 * 
 * @author Tom Simmons
 */
public class Node implements Iterable<Entry<Response, Node>> {

	private final LinkedHashMap<Response, Node> children;
	private final Code guess;

	public Node(Code guess) {
		this.guess = guess;
		children = new LinkedHashMap<>();
	}
	
	public Code getGuess() {
		return guess;
	}

	public void add(Response key, Node child) {
		children.put(key, child);
	}

	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Iterator<Entry<Response, Node>> iterator() {
		return children.entrySet().iterator();
	}

}
