package edu.vwc.mastermind.sequence;

import static org.junit.Assert.*;
import org.junit.Test;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.Response;

public class CodeTest {

	@Test
	public void testGuess() {
		assertNotNull(Code.valueOf(1));
		assertNotNull(Code.valueOf(1, 1, 2, 2));
	}

	@Test
	public void testCompareTo() {
		try{
			Code.valueOf(1, 1, 1).compareTo(Code.valueOf(1));
			fail("compareTo should throw when codes have unequal length");
		} catch (IllegalArgumentException shouldBeThrown) {}
		
		try{
			Code.valueOf(2).compareTo(Code.valueOf(1, 1, 1));
			fail("compareTo should throw when codes have unequal length");
		} catch (IllegalArgumentException shouldBeThrown) {}
		
		// The guess has pegs all of the correct color and placement
		assertEquals(Response.valueOf(2), Code.valueOf(1).compareTo(Code.valueOf(1)));
		assertEquals(Response.valueOf(2, 2), Code.valueOf(1, 2).compareTo(Code.valueOf(1, 2)));
		
		// The guess has misplaced pegs, and possibly some correct pegs.
		assertEquals(Response.valueOf(1, 1), Code.valueOf(1, 2).compareTo(Code.valueOf(2, 1)));
		assertEquals(Response.valueOf(1, 1), Code.valueOf(2, 1).compareTo(Code.valueOf(1, 2)));
		// Correct pegs float left in the response sequence
		assertEquals(Response.valueOf(2, 1, 1), Code.valueOf(2, 1, 1).compareTo(Code.valueOf(1, 2, 1)));
		assertEquals(Response.valueOf(2, 1, 1), Code.valueOf(1, 1, 2).compareTo(Code.valueOf(1, 2, 1)));
		
		// Guess has no pegs of the correct color
		assertEquals(Response.valueOf(0, 0, 0), Code.valueOf(1, 1, 1).compareTo(Code.valueOf(2, 2, 2)));
	}

	@Test
	public void testEquals() {
		assertTrue(Code.valueOf(1).equals(Code.valueOf(1)));
		assertTrue(Code.valueOf(3, 4).equals(Code.valueOf(3, 4)));
		assertFalse(Code.valueOf(1).equals(Code.valueOf(2)));
	}

}
