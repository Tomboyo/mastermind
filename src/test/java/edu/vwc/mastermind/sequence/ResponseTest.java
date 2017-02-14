package edu.vwc.mastermind.sequence;

import org.junit.Test;

import edu.vwc.mastermind.sequence.Response;

import static org.junit.Assert.*;

public class ResponseTest {

	/**
	 * Responses must be instantiated with an int[] representing the feedback from a guess code.
	 * new Response(1, 1, 2, 2) generates a four-peg Response.
	 * Response pegs should be numbered 0, 1, or 2.
	 * 2 indicates a peg in a guess code was correctly placed,
	 * 1 indicates a peg in a guess code was in the wrong position, and
	 * 0 indicates no particular feedback (this peg of the response says nothing)
	 * Note that Responses should have pegs ordered in descending number: 2, 2, 1, 0, 0
	 * Responses code format corresponds to traditional mastermind rules.
	 */
	@Test
	public void testResponse() {
		assertNotNull(new Response(1));
		assertNotNull(new Response(1, 2, 3));
	}

	/**
	 * A response's hash code is the concatenation of its code.
	 * Ex: new Response(new int[]{2, 2, 1,0}) should hash to 2210
	 * This is used to key Hashtables in Tree.java
	 */
	@Test
	public void testHashCode() {
		assertEquals(1, new Response(1).hashCode());
		assertEquals(12, new Response(12).hashCode());
		assertEquals(456, new Response(456).hashCode());
	}

	/**
	 * Verify that two responses are considered equal if their pegs match (by length and color)
	 */
	@Test
	public void testEqualsCode() {
		assertTrue(new Response(1).equals(new Response(1)));
		assertTrue(new Response(1, 2).equals(new Response(1, 2)));
		
		assertFalse(new Response(1, 2).equals(new Response(2, 1)));
		assertFalse(new Response(2, 1).equals(new Response(1, 2)));
	}

}
