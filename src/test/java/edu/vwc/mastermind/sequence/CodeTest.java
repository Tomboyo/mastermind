package edu.vwc.mastermind.sequence;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeThat;

import java.util.Arrays;

import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.CodeTheoriesHelper.CodeRange;
import edu.vwc.mastermind.sequence.Response;

@RunWith(Theories.class)
public class CodeTest {
	
	@Theory
	public void testCompareFailsWhenCodesHaveDifferentLength(
			@CodeRange Code a,
			@CodeRange Code b) {
		assumeThat(a.length(), not(equalTo(b.length())));
		
		try {
			a.compareTo(b);
			fail("Codes of unequal length should be incomperable");
		} catch (IllegalArgumentException expected) {}
	}
	
	@Theory
	public void testCompareWithEqualCodesGivesExactMatchResponse(
			@CodeRange Code a,
			@CodeRange Code b) {
		
		assumeThat(a, equalTo(b));
		
		Response actual = a.compareTo(b);
		
		assertThat(actual.getExact(), equalTo(a.length()));
		assertThat(actual.getInexact(), equalTo(0));
		assertThat(actual.getWrong(), equalTo(0));
	}
	
	@Theory
	public void testCompareWithUnequalCodesDoesNotGiveExactMatchResponse(
			@CodeRange Code a,
			@CodeRange Code b) {
		assumeThat(a, not(equalTo(b)));
		assumeThat(a.length(), equalTo(b.length()));
		
		Response actual = a.compareTo(b);
		assertThat(actual.getExact(), lessThan(a.length()));
		assertThat(actual.getInexact(), lessThanOrEqualTo(a.length()));
		assertThat(actual.getWrong(), lessThanOrEqualTo(a.length()));
		assertThat(actual.getExact() + actual.getInexact() + actual.getWrong(),
				equalTo(a.length()));
	}
	
	@Test
	public void testCompareToWithExactMatches() {
		assertThat(Code.valueOf(0).compareTo(Code.valueOf(0)),
				equalTo(Response.valueOf(1, 0, 0)));
		assertThat(Code.valueOf(1).compareTo(Code.valueOf(1)),
				equalTo(Response.valueOf(1, 0, 0)));
		
		assertThat(Code.valueOf(0, 1).compareTo(Code.valueOf(0, 1)),
				equalTo(Response.valueOf(2, 0, 0)));
		
		assertThat(Code.valueOf(5, 8, 1).compareTo(Code.valueOf(5, 8, 1)),
				equalTo(Response.valueOf(3, 0, 0)));
	}
	
	@Test
	public void testCompareToWithInexactMatches() {
		assertThat(Code.valueOf(1, 0).compareTo(Code.valueOf(0, 1)),
				equalTo(Response.valueOf(0, 2, 0)));
		
		assertThat(Code.valueOf(1, 0, 1).compareTo(Code.valueOf(1, 1, 0)),
				equalTo(Response.valueOf(1, 2, 0)));
		
		assertThat(Code.valueOf(1, 2, 3).compareTo(Code.valueOf(3, 1, 2)),
				equalTo(Response.valueOf(0, 3, 0)));
		
		assertThat(Code.valueOf(1, 2, 3, 4).compareTo(Code.valueOf(1, 2, 4, 3)),
				equalTo(Response.valueOf(2, 2, 0)));
	}
	
	@Theory
	public void testAllCodesAreCanonical(@CodeRange Code a, @CodeRange Code b) {
		assumeThat(a, equalTo(b));
		assertTrue(a == b);
	}
	
	@Test
	public void testSourceArrayDoesNotMutateCode() {
		int[] sequence = new int[]{1, 2, 3};
		Code code = Code.valueOf(sequence);
		sequence[0] = 7;
		
		assertThat(code.toArray()[0], equalTo(1));
	}
	
	@Test
	public void testToArray() {
		assertTrue(Arrays.equals(
				Code.valueOf(1, 2, 3).toArray(),
				new int[]{1, 2, 3}));
		
		assertTrue(Arrays.equals(
				Code.valueOf(new int[]{1, 2, 3}).toArray(),
				new int[]{1, 2, 3}));
	}
	
	@Theory
	public void testHashCodeEqualityContract(
			@CodeRange Code a,
			@CodeRange Code b) {
		assumeThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}
	
	@Theory
	public void testCodeLength(@CodeRange Code code) {
		assertThat(code.length(), equalTo(code.toArray().length));
	}
	
	@Test
	public void testCodeLength2() {
		assertThat(Code.valueOf(0).length(), equalTo(1));
		assertThat(Code.valueOf(1, 2, 3, 4).length(), equalTo(4));
	}

}
