package edu.vwc.mastermind.sequence.provider;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.provider.AllCodesProvider;

import static org.hamcrest.CoreMatchers.*;

public class AllCodesProviderTest {
	
	private AllCodesProvider provider;

	/**
	 * Verify that the AllCodesProvider returns a complete enumeration of Code
	 * permutations.
	 */
	@Test
	public void testGetCodes() {
		
		// Tell the provider our game has 3 colors and 2 pegs allowed per code
		provider = new AllCodesProvider(3, 2);
		
		// get the collection
		Set<Code> actual = provider.getCodes();
		
		assertEquals(9, actual.size());
		
		assertThat(actual, hasItems(
				Code.valueOf(0, 0),
				Code.valueOf(0, 1),
				Code.valueOf(0, 2),
				Code.valueOf(1, 0),
				Code.valueOf(1, 1),
				Code.valueOf(1, 2),
				Code.valueOf(2, 0),
				Code.valueOf(2, 1),
				Code.valueOf(2, 2)));
	}

}