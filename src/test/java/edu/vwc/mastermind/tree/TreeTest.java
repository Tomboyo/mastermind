package edu.vwc.mastermind.tree;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.hamcrest.Matcher;
import org.junit.Test;



public class TreeTest {
	
	@Test
	public void testEquals() {
		assertThat(Tree.fromString("[1, 1]"),
				equalTo(Tree.fromString("[1, 1]")));

		// Note: An impossible tree, but convenient for testing
		Tree actual = Tree.fromString("[1, 1]->[1, 1, 1][1, 1]");
		
		assertThat(actual, not(anyOf(Arrays.asList(
				equalToTree("[0, 1]->[1, 1, 1][1, 1]"),
				equalToTree("[1, 0]->[1, 1, 1][1, 1]"),
				equalToTree("[1, 1]->[0, 1, 1][1, 1]"),
				equalToTree("[1, 1]->[1, 0, 1][1, 1]"),
				equalToTree("[1, 1]->[1, 1, 0][1, 1]"),
				equalToTree("[1, 1]->[1, 1, 1][0, 1]"),
				equalToTree("[1, 1]->[1, 1, 1][1, 0]")))));
	}
	
	private Matcher<Tree> equalToTree(String encoding) {
		return equalTo(Tree.fromString(encoding));
	}
	
	@Test
	public void testToStringCompatibleWithFromString() {
		Tree tree = new TreeBuilder()
				.addLine("[0,1]->[0,2,0][1,0]")
				.addLine("[0,1]->[0,0,2][2,2]")
				.addLine("[0,1]->[0,1,1][0,0]->[1,0,1][2,0]")
				.addLine("[0,1]->[0,1,1][0,0]->[0,0,2][1,2]")
				.build();
		
		assertThat(Tree.fromString(tree.toString()), equalTo(tree));
	}

}
