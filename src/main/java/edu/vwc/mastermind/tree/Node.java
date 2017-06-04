package edu.vwc.mastermind.tree;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import edu.vwc.mastermind.sequence.Response;

/**
 * Composite tree data structure for recording data.
 * 
 * @author Tom Simmons
 */
public class Node implements Iterable<Entry<Response, Node>> {

	LinkedHashMap<Response, Node> children;

	public Node() {
		children = new LinkedHashMap<>();
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
