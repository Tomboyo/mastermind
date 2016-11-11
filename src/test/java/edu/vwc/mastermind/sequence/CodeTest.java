package edu.vwc.mastermind.sequence;

import static org.junit.Assert.*;
import org.junit.Test;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.Response;

public class CodeTest {

	@Test
	public void testGuess() {
		assertNotNull(new Code(1));
		assertNotNull(new Code(1, 1, 2, 2));
	}

	@Test
	public void testCompareTo() {
		try{
			new Code(1, 1, 1).compareTo(new Code(1));
			fail("compareTo should throw when codes have unequal length");
		} catch (IllegalArgumentException shouldBeThrown) {}
		
		try{
			new Code(2).compareTo(new Code(1, 1, 1));
			fail("compareTo should throw when codes have unequal length");
		} catch (IllegalArgumentException shouldBeThrown) {}
		
		// The guess has pegs all of the correct color and placement
		assertEquals(new Response(2), new Code(1).compareTo(new Code(1)));
		assertEquals(new Response(2, 2), new Code(1, 2).compareTo(new Code(1, 2)));
		
		// The guess has misplaced pegs, and possibly some correct pegs.
		assertEquals(new Response(1, 1), new Code(1, 2).compareTo(new Code(2, 1)));
		assertEquals(new Response(1, 1), new Code(2, 1).compareTo(new Code(1, 2)));
		// Correct pegs float left in the response sequence
		assertEquals(new Response(2, 1, 1), new Code(2, 1, 1).compareTo(new Code(1, 2, 1)));
		assertEquals(new Response(2, 1, 1), new Code(1, 1, 2).compareTo(new Code(1, 2, 1)));
		
		// Guess has no pegs of the correct color
		assertEquals(new Response(0, 0, 0), new Code(1, 1, 1).compareTo(new Code(2, 2, 2)));
	}

	@Test
	public void testEquals() {
		assertTrue(new Code(1).equals(new Code(1)));
		assertTrue(new Code(3, 4).equals(new Code(3, 4)));
		assertFalse(new Code(1).equals(new Code(2)));
	}

}
