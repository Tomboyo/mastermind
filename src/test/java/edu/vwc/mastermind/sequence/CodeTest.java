package edu.vwc.mastermind.sequence;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.Response;

public class CodeTest {

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
	public void testValueOf() {
		// Code.valueOf() returns the canonical instance
		assertTrue(Code.valueOf(1, 2, 3) == Code.valueOf(1, 2, 3));
	}
	
	@Test
	public void testImmutability() {
		int[] sequence = new int[]{1, 2, 3};
		Code code = Code.valueOf(sequence);
		sequence[0] = 7;
		
		// Code's internal representation is detached from the array used to
		// create it, so altering sequence does not alter Code
		assertTrue(code == Code.valueOf(1, 2, 3));
	}
	
	@Test
	public void testToArray() {
		Code code = Code.valueOf(1, 2, 3);
		int[] asArray = code.toArray();
		asArray[0] = 17;
		
		// Code's internal representation is detached from the array view it
		// returns, so altering asArray does not alter Code
		assertTrue(Arrays.equals(code.toArray(), new int[] { 1, 2, 3 }));
	}

	@Test
	public void testEquality() {
		Code code1 = Code.valueOf(1, 9, 3, 1);
		Code code2 = Code.valueOf(1, 9, 3, 1);
		
		// All instances are canonical and therefore may be compared with object
		// equality
		assertTrue(code1 == code2);
		assertTrue(code1.equals(code2));
		assertTrue(code1.hashCode() == code2.hashCode());
		
		/*
		 * Create and compare some random codes
		 */
		Random rand = new Random();
		for (int i = 0; i < 100; i++) {
			final int length = rand.nextInt(10) + 1;
			final int colors = rand.nextInt(1000) + 1;
			
			// Create a sequence based on the random parameters
			int[] sequence = new int[length];
			for (int j = 0; j < length; j++) {
				sequence[j] = rand.nextInt(colors) + 1;
			}
			
			code1 = Code.valueOf(sequence);
			code2 = Code.valueOf(sequence);
			assertTrue(code1 == code2);
			assertTrue(code1.equals(code2));
			assertTrue(code1.hashCode() == code2.hashCode());
			
			// A Code can have negative pegs, but the above logic never uses
			// them. This therefore makes the codes unequal.
			sequence[rand.nextInt(length)] = -1;
			code2 = Code.valueOf(sequence);
			
			assertFalse(String.format("%s == %s", code1, code2),
					code1 == code2);
			assertFalse(String.format("%s .equals %s", code1, code2),
					code1.equals(code2));
		}
	}

}
