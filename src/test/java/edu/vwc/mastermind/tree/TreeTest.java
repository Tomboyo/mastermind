package edu.vwc.mastermind.tree;

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
		
		root.add(Response.valueOf(0, 1), new Tree(Code.valueOf(0, 1)));
		root.add(Response.valueOf(0, 2), new Tree(Code.valueOf(0, 2)));
		root.add(Response.valueOf(1, 0), new Tree(Code.valueOf(1, 2)));
		
		Tree a = new Tree(Code.valueOf(1, 1));
		a.add(Response.valueOf(0, 0), new Tree(Code.valueOf(0, 0)));
		root.add(Response.valueOf(1, 1), a);
		
		Tree b = new Tree(Code.valueOf(1, 2));
		b.add(Response.valueOf(0, 0), new Tree(Code.valueOf(0, 0)));
		b.add(Response.valueOf(0, 1), new Tree(Code.valueOf(0, 1)));
		root.add(Response.valueOf(1, 2), b);
		
		return root;
	}

}
