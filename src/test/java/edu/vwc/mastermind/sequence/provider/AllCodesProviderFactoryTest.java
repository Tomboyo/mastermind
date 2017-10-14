package edu.vwc.mastermind.sequence.provider;

import static java.util.stream.Collectors.toSet;
import static org.easymock.EasyMock.expect;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import edu.vwc.mastermind.sequence.Code;

public class AllCodesProviderFactoryTest extends EasyMockSupport {

	private AllCodesProviderFactory factory;
	private CodesProvider provider;
	
	@Before
	public void setUp() {
		provider = createMock(CodesProvider.class);
		factory = new AllCodesProviderFactory(provider);
	}
	
	@Test
	public void testElementsInBlacklistAreFilteredFromResultSet() {
		Set<Code> mockCodeSet = Stream.of(
				Code.valueOf(0), Code.valueOf(1))
				.collect(toSet());
		expect(provider.getCodes()).andReturn(mockCodeSet);

		replayAll();
		Set<Code> blackList = Stream.of(Code.valueOf(0)).collect(toSet());
		Set<Code> hint = Collections.emptySet();
		Set<Code> actual = factory.getInstance(blackList, hint).getCodes();
		verifyAll();

		Set<Code> expected = Stream.of(Code.valueOf(1)).collect(toSet());
		assertThat(actual, equalTo(expected));
	}
	
	@Test
	public void testEmptyBlacklistDoesNotFilterResultSet() {
		Set<Code> mockCodeSet = Stream.of(
				Code.valueOf(0), Code.valueOf(1))
				.collect(toSet());
		expect(provider.getCodes()).andReturn(mockCodeSet);

		replayAll();
		Set<Code> blackList = Collections.emptySet();
		Set<Code> hint = Collections.emptySet();
		Set<Code> actual = factory.getInstance(blackList, hint).getCodes();
		verifyAll();
		
		assertThat(actual, equalTo(mockCodeSet));
	}

}
