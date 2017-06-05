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
	
	// A temporary metric for tree depth. This will eventually be replaced with
	// a much more robust metadata system.
	private int depth;

	public Tree(Code guess) {
		this.guess = guess;
		this.depth = 1;
		children = new LinkedHashMap<>();
	}
	
	public Code getGuess() {
		return guess;
	}

	public void add(Response key, Tree child) {
		children.put(key, child);
		this.depth += child.depth;
	}

	public void accept(TreeVisitor visitor) {
		visitor.visit(this);
	}
	
	public int depth() {
		return depth;
	}
	
	public int size() {
		return children.size();
	}

	@Override
	public Iterator<Entry<Response, Tree>> iterator() {
		return children.entrySet().iterator();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Tree)) return false;
		Tree other = (Tree) o;
		if (guess == other.guess
				&& children.equals(other.children)) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return toString("");
	}
	
	private String toString(String prefix) {
		StringBuilder builder;
		
		if (children.size() == 0) {
			builder = new StringBuilder(prefix);
			builder.append(guess);
			builder.append(System.lineSeparator());
		} else {
			builder = new StringBuilder();
			for (Entry<Response, Tree> entry : children.entrySet()) {
				StringBuilder prefixBuilder = new StringBuilder();
				prefixBuilder.append(prefix)
						.append(guess)
						.append(entry.getKey())
						.append("->");
				builder.append(entry.getValue().toString(
						prefixBuilder.toString()));
			}
		}
		
		return builder.toString();
	}

}
