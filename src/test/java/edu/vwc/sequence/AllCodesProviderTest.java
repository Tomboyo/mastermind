package edu.vwc.sequence;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

public class AllCodesProviderTest {

	AllCodesProvider provider = AllCodesProvider.getInstance();

	@Test
	public void testGetCodes() {
		
		// Tell the provider our game has 3 colors and 2 pegs allowed per code
		provider.set(3, 2);
		
		// get the collection
		Code[] actual = provider.getCodes();
		
		assertEquals(9, actual.length);
		
		assertThat(Arrays.asList(actual), hasItems(
				new Code(0, 0),
				new Code(0, 1),
				new Code(0, 2),
				new Code(1, 0),
				new Code(1, 1),
				new Code(1, 2),
				new Code(2, 0),
				new Code(2, 1),
				new Code(2, 2)));
	}

}
