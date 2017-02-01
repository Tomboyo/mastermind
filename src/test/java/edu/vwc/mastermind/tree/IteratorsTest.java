package edu.vwc.mastermind.tree;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class IteratorsTest {

	Tree<String> root;

	@Before
	public void beforeTest() {
		// R( B(LA, LB), LC )
		root = new Tree<String>("R");
		Tree<String> branch = new Tree<String>("B");
		Tree<String> leafA = new Tree<String>("LA");
		Tree<String> leafB = new Tree<String>("LB");
		Tree<String> leafC = new Tree<String>("LC");

		root.add(branch);
		root.add(leafC);
		branch.add(leafA);
		branch.add(leafB);
	}

	@Test
	public void testPreorderIterator() {
		StringBuilder sb = new StringBuilder();
		Iterator<Tree<String>> iterator =
				new InorderIterator<Tree<String>>(root);

		while (iterator.hasNext()) {
			sb.append(iterator.next().getData()).append(",");
		}

		assertEquals("R,B,LA,LB,LC,", sb.toString());
	}

}
