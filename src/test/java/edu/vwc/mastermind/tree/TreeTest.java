package edu.vwc.mastermind.tree;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Test;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.Response;

public class TreeTest {

	@Test
	public void testEquals() {
		assertEquals(constructTree(), constructTree());
	}
	
	private Tree constructTree() {
		Tree root = new Tree(Code.valueOf(0, 0));
		
		root.add(Response.valueOf(1, 0, 1), new Tree(Code.valueOf(0, 1)));
		root.add(Response.valueOf(1, 0, 1), new Tree(Code.valueOf(0, 2)));
		root.add(Response.valueOf(0, 0, 2), new Tree(Code.valueOf(1, 2)));
		
		Tree a = new Tree(Code.valueOf(1, 1));
		a.add(Response.valueOf(0, 0, 0), new Tree(Code.valueOf(0, 0)));
		root.add(Response.valueOf(0, 0, 2), a);
		
		Tree b = new Tree(Code.valueOf(1, 2));
		b.add(Response.valueOf(0, 0, 2), new Tree(Code.valueOf(0, 0)));
		b.add(Response.valueOf(0, 1, 1), new Tree(Code.valueOf(0, 1)));
		root.add(Response.valueOf(0, 0, 2), b);
		
		return root;
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
