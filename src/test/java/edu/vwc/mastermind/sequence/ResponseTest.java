package edu.vwc.mastermind.sequence;

import org.junit.Test;

import edu.vwc.mastermind.sequence.Response;

import static org.junit.Assert.*;

public class ResponseTest {

	/**
	 * {@link Response#valueOf} returns the canonical instance of the requested
	 * Response, so instances acquired in this way may be compared using object
	 * equality. Two instances of Response are equal if and only if their three
	 * constructor arguments, exact, inexact, and wrong, are also equal.
	 */
	@Test
	public void testEquality() {
		assertTrue(Response.valueOf(1, 2, 3) == Response.valueOf(1, 2, 3));
		assertTrue(Response.valueOf(1, 2, 3).equals(Response.valueOf(1, 2, 3)));
		
		assertFalse(Response.valueOf(1, 2, 3) == Response.valueOf(0, 2, 3));
		assertFalse(Response.valueOf(1, 2, 3) == Response.valueOf(1, 0, 3));
		assertFalse(Response.valueOf(1, 2, 3) == Response.valueOf(1, 2, 0));
		
		assertFalse(
				Response.valueOf(1, 2, 3).equals(Response.valueOf(0, 2, 3)));
		assertFalse(
				Response.valueOf(1, 2, 3).equals(Response.valueOf(1, 0, 3)));
		assertFalse(
				Response.valueOf(1, 2, 3).equals(Response.valueOf(1, 2, 0)));
	}

}
