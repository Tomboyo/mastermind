package edu.vwc.mastermind.sequence;

import org.junit.Test;

import edu.vwc.mastermind.sequence.Response;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class ResponseTest {

	@Test
	public void testEquality() {
		assertThat(Response.valueOf(1, 2, 3),
				equalTo(Response.valueOf(1, 2, 3)));
		
		assertThat(Response.valueOf(1, 2, 3),
				not(equalTo(Response.valueOf(0, 2, 3))));
		assertThat(Response.valueOf(1, 2, 3),
				not(equalTo(Response.valueOf(1, 0, 3))));
		assertThat(Response.valueOf(1, 2, 3),
				not(equalTo(Response.valueOf(1, 2, 0))));
	}
	
	@Test
	public void testGetters() {
		Response response = Response.valueOf(1, 2, 3);
		assertThat(response.getExact(), equalTo(1));
		assertThat(response.getInexact(), equalTo(2));
		assertThat(response.getWrong(), equalTo(3));
	}
	
	@Test
	public void testInstancesAreCanonical() {
		assertTrue(Response.valueOf(1, 2, 3) == Response.valueOf(1, 2, 3));
		assertTrue(Response.valueOf(7, 0, 4) == Response.valueOf(7, 0, 4));
	}
	
	@Test
	public void testHashCodeEqualityContract() {
		assertThat(Response.valueOf(1, 2, 3).hashCode(),
				equalTo(Response.valueOf(1, 2, 3).hashCode()));
		
		assertThat(Response.valueOf(7, 1, 4).hashCode(),
				equalTo(Response.valueOf(7, 1, 4).hashCode()));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNegativeValueForExactParameterIsIllegal() {
		Response.valueOf(-1, 0, 0);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNegativeValueForInexactParameterIsIllegal() {
		Response.valueOf(0, -1, 0);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNegativeValueForWrongParameterIsIllegal() {
		Response.valueOf(0, 0, -1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAtLeastOnePositiveParameterIsRequired() {
		Response.valueOf(0, 0, 0);
	}

}
