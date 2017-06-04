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
public class Tree implements Iterable<Entry<Response, Tree>> {

	private final LinkedHashMap<Response, Tree> children;
	private final Code guess;

	public Tree(Code guess) {
		this.guess = guess;
		children = new LinkedHashMap<>();
	}
	
	public Code getGuess() {
		return guess;
	}

	public void add(Response key, Tree child) {
		children.put(key, child);
	}

	public void accept(TreeVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Iterator<Entry<Response, Tree>> iterator() {
		return children.entrySet().iterator();
	}

}
