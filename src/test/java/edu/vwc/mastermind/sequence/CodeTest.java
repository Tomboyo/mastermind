package edu.vwc.mastermind.sequence;

import static org.junit.Assert.*;

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
		assertEquals(code.toArray(), new int[] { 1, 2, 3 });
	}

	@Test
	public void testEquality() {
		Random rand = new Random();
		final int maxColor = 9;
		final int maxPegs = 10;
		
		// Make codes with (maxColor + 1) colors and between 1 and maxPegs pegs.
		for (int pegs = 1; pegs <= maxPegs; pegs++) {
			int[] sequence = new int[pegs];
			for (int peg = 0; peg < sequence.length; peg++) {
				for (int i = 0; i <= maxColor; i++) {
					sequence[peg] = i;
				}
			}
			
			// Because instances are canonical, object equality can be used
			Code codeA = Code.valueOf(sequence);
			Code codeB = Code.valueOf(sequence);
			assertTrue(codeA == codeB);
			assertTrue(codeA.equals(codeA));
			assertTrue(codeA.equals(codeB));
			assertTrue(codeA.hashCode() == codeA.hashCode());
			assertTrue(codeA.hashCode() == codeB.hashCode());
			
			// Change some of the pegs
			int numJitter = rand.nextInt(pegs - 1) + 1;
			for (int i = 0; i < numJitter; i++) {
				sequence[rand.nextInt(pegs)] = maxColor + 1;
			}
			
			assertFalse(codeA == Code.valueOf(sequence));
			assertFalse(codeA.equals(Code.valueOf(sequence)));
		}
	}

}
