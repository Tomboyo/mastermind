package edu.vwc.mastermind.tree;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.Response;

/**
 * Composite tree data structure for recording Mastermind strategy diagrams.
 */
public class Tree implements Iterable<Entry<Response, Tree>> {

	private final LinkedHashMap<Response, Tree> children;
	private final Code guess;

	/**
	 * Create a new Tree instance with no children.
	 * 
	 * @param guess
	 *            A {@link Code} representing a guess in a Mastermind strategy
	 *            tree.
	 */
	public Tree(Code guess) {
		this.guess = guess;
		children = new LinkedHashMap<>();
	}
	
	/**
	 * @return This instance's {@code guess} Code
	 */
	public Code getGuess() {
		return guess;
	}

	/**
	 * Add a child to this tree.
	 * 
	 * <p>
	 * A child represents the following progression within the complete strategy
	 * tree: If this Tree's {@code guess} is made during play and it results in
	 * feedback encoded by ${code key}, the {@code child} Tree's {@code guess}
	 * should be made next.
	 * 
	 * @param key
	 *            A {@link Response} object that, when encountered after making
	 *            this Tree's {@code guess}, indicates the {@code child}'s
	 *            {@code guess} should be made next.
	 * @param child
	 *            A tree object containing a progression of future moves to make
	 *            in the strategy tree.
	 */
	public void add(Response key, Tree child) {
		children.put(key, child);
	}
	
	/**
	 * Get the child tree associated with the given {@code key}.
	 * 
	 * @param key
	 *            The Response associated with a child Tree instance.
	 * @return The child Tree or null if no Tree is associated with the given
	 *         {@code key}.
	 */
	public Tree get(Response key) {
		return children.get(key);
	}
	
	/**
	 * Enumerate over all Response-to-child Tree bindings contained by this
	 * Tree.
	 * 
	 * @return An entry-set containing all child Tree instances and the
	 *         {@link Response}s that key to them.
	 */
	public Set<Entry<Response, Tree>> children() {
		return children.entrySet();
	}
	
	/**
	 * Get an integer representation of the depth of this Tree. A tree with no
	 * children has depth 1, a tree with one child has depth of at least 2, and
	 * a tree with a child that has its own child has a depth of at least 3.
	 * 
	 * @return The depth of this tree.
	 */
	public int depth() {
		if (children.isEmpty()) {
			return 1;
		}
		int depth = 0;
		for (Entry<Response, Tree> entry : children()) {
			int d = entry.getValue().depth();
			if (d > depth) {
				depth = d; 
			}
		}
		return 1 + depth;
	}
	
	/**
	 * @return The number of children this tree has.
	 */
	public int size() {
		return children.size();
	}
	
	public static Tree copyOf(Tree other) {
		Tree root = new Tree(other.guess);
		other.children.forEach((response, tree) -> {
			root.add(response, copyOf(tree));
		});
		return root;
	}
	
	public static Tree union(final Tree a, final Tree b) {
		if (a == null || b == null)
			throw new NullPointerException();
		if (a == b)
			return copyOf(a);
		if (a.guess != b.guess)
			throw new IllegalArgumentException(
					"Trees must share a common root");
		
		Tree newTree = new Tree(a.guess);
		Stream.concat(
				a.children.entrySet().stream(),
				b.children.entrySet().stream())
				.forEach(entry -> {
					Response key = entry.getKey();
					Tree value = entry.getValue();
					Tree valueInA = a.get(key);
					Tree valueInB = b.get(key);
					
					if (valueInA != null && valueInB != null) {
						newTree.add(key, union(valueInA, valueInB));
					} else {
						newTree.add(key, copyOf(value));
					}
				});
		
		return newTree;
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
						.append("->")
						.append(entry.getKey());
				builder.append(entry.getValue().toString(
						prefixBuilder.toString()));
			}
		}
		
		return builder.toString();
	}
	
	/**
	 * Tree#fromString(aTree#toString()).equals(aTree); // true
	 * @param input A stringified Tree, as created by {@link Tree#toString()}
	 * @return The parsed tree
	 */
	public static Tree fromString(String input) {
		TreeBuilder parser = new TreeBuilder();
		String[] lines = input.split(System.lineSeparator());
		for (String line : lines) {
			parser.addLine(line);
		}
		return parser.build();
	}

}
