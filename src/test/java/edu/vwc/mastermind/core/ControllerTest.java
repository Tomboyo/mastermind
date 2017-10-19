package edu.vwc.mastermind.core;

import static edu.vwc.mastermind.TestHelper.setOfCodes;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.Response;
import edu.vwc.mastermind.sequence.provider.CodesProvider;
import edu.vwc.mastermind.sequence.provider.CodesProviderFactory;
import edu.vwc.mastermind.tree.Tree;

public class ControllerTest extends EasyMockSupport {

	private Controller controller;
	private Response correct;
	private Comparator<Tree> comparator;
	private CodesProviderFactory factory;
	private CodesProvider firstGuesses;
	private CodesProvider goalCodes;
	
	
	@Before
	public void setUp() {
		correct = Response.valueOf(1, 0, 0);
		comparator = createMock(Comparator.class);
		factory = createMock(CodesProviderFactory.class);
		firstGuesses = createMock(CodesProvider.class);
		goalCodes = createMock(CodesProvider.class);
	}
	
	@Test
	public void testDegenerateGame() throws ExecutionException, InterruptedException {
		expect(firstGuesses.getCodes()).andReturn(
				setOfCodes(Code.valueOf(1), Code.valueOf(2)))
				.anyTimes();
		expect(goalCodes.getCodes()).andReturn(
				setOfCodes(Code.valueOf(1)))
				.anyTimes();
		expect(comparator.compare(anyObject(), isNull()))
				.andReturn(-1)
				.anyTimes();
		
		// The comparison happens asynchronously, so the order is NOT guaranteed.
		expect(comparator.compare(
				Tree.fromString("[2]->[0, 0, 1][1]"),
				Tree.fromString("[1]")))
				.andReturn(1)
				.times(0, 1);
		expect(comparator.compare(
				Tree.fromString("[1]"),
				Tree.fromString("[2]->[0, 0, 1][1]")))
				.andReturn(-1)
				.times(0, 1);
		
		controller = new Controller(
				correct,
				comparator,
				factory,
				firstGuesses,
				goalCodes);
		Tree expected = Tree.fromString("[1]");
		replayAll();
		assertThat(controller.simulate(), equalTo(expected));
		verifyAll();
	}
}
