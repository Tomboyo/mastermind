package edu.vwc.mastermind.sequence.provider;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import edu.vwc.mastermind.sequence.Code;

class TestUtil {

	private static final Set<Code> TEST_SET = new LinkedHashSet<Code>();
	static {
		Collections.addAll(TEST_SET,
				Code.valueOf(0, 0),
				Code.valueOf(0, 1),
				Code.valueOf(0, 2),
				Code.valueOf(1, 0),
				Code.valueOf(1, 1),
				Code.valueOf(1, 2),
				Code.valueOf(2, 0),
				Code.valueOf(2, 1),
				Code.valueOf(2, 2));
	}
	
	/**
	 * Will return the set of codes for a game with 3 colors and 2 pegs.
	 * 
	 * @return The set
	 */
	static Set<Code> canonicalSet() {
		return new LinkedHashSet<>(TEST_SET);
	}
}
