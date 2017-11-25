package edu.vwc.mastermind.sequence.provider;

import static edu.vwc.mastermind.TestHelper.setOfCodes;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import java.util.Set;
import java.util.stream.Stream;

import org.junit.Test;

import edu.vwc.mastermind.TestHelper;
import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.provider.AllCodesProvider;

public class AllCodesProviderTest {
	
	private AllCodesProvider provider;

	@Test
	public void testGetCodes() {
		provider = new AllCodesProvider(3, 2);
		
		Set<Code> actual = provider.getCodes();
		Set<Code> expected = setOfCodes(
				Code.valueOf(0, 0), Code.valueOf(0, 1), Code.valueOf(0, 2),
				Code.valueOf(1, 0), Code.valueOf(1, 1), Code.valueOf(1, 2),
				Code.valueOf(2, 0), Code.valueOf(2, 1), Code.valueOf(2, 2));
		
		assertThat(actual, equalTo(expected));
	}

}
