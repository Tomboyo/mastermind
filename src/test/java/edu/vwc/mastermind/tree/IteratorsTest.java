package edu.vwc.mastermind.tree;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class IteratorsTest {

	Node<String> root;

	@Before
	public void beforeTest() {
		// R( B(LA, LB), LC )
		root = new Node<String>("R");
		Node<String> branch = new Node<String>("B");
		Node<String> leafA = new Node<String>("LA");
		Node<String> leafB = new Node<String>("LB");
		Node<String> leafC = new Node<String>("LC");

		root.add(branch);
		root.add(leafC);
		branch.add(leafA);
		branch.add(leafB);
	}

	@Test
	public void testPreorderIterator() {
		StringBuilder sb = new StringBuilder();
		Iterator<Node<String>> iterator =
				new PreorderIterator<Node<String>>(root);

		while (iterator.hasNext()) {
			sb.append(iterator.next().data).append(",");
		}

		assertEquals("R,B,LA,LB,LC,", sb.toString());
	}

}
