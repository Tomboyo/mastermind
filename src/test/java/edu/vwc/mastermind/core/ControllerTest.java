package edu.vwc.mastermind.core;

import static edu.vwc.mastermind.TestHelper.setOfCodes;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.tree.Tree;

public class ControllerTest extends EasyMockSupport {

	private ParallelizedTreeFactory factory;
	
	@Before
	public void createMocks() {
		factory = createMock(ParallelizedTreeFactory.class);
	}
	
	@Test
	public void generatesTreeForEachFirstGuess()
			throws ExecutionException, InterruptedException {
		Set<Code> firstGuesses = setOfCodes(
				Code.valueOf(1), Code.valueOf(2), Code.valueOf(3));
		Set<Code> answers = Collections.emptySet();
		
		Controller controller = new Controller(
				factory,
				createNiceMockComparator());
		
		firstGuesses.forEach(guess -> {
			factory.submitGuess(eq(guess), anyObject(), anyObject());
			expect(factory.next()).andReturn(null);
		});
		
		expect(factory.hasNext())
				.andReturn(true).times(2)
				.andReturn(false).once();
		
		replayAll();
		controller.newOptimizedStrategyTree(
				firstGuesses,
				answers);
		verifyAll();
	}
	
	@SuppressWarnings("unchecked")
	private Comparator<Tree> createNiceMockComparator() {
		return createNiceMock(Comparator.class);
	}
	
	@Test
	public void comparesAllGeneratedTrees()
			throws InterruptedException, ExecutionException {
		Set<Code> guesses = Collections.emptySet();
		Set<Code> answers = Collections.emptySet();
		Comparator<Tree> comparator = createMockComparator();
		Controller controller = new Controller(
				factory, comparator);
		
		expect(factory.next()).andReturn(null).times(12);
		expect(factory.hasNext())
				.andReturn(true).times(11)
				.andReturn(false).once();
		
		expect(comparator.compare(isNull(), isNull()))
				.andReturn(0).times(11);
		
		replayAll();
		controller.newOptimizedStrategyTree(guesses, answers);
		verifyAll();
	}
	
	@SuppressWarnings("unchecked")
	private Comparator<Tree> createMockComparator() {
		return createMock(Comparator.class);
	}
	
	@Test
	public void returnsTheOptimalTree()
			throws ExecutionException, InterruptedException {
		Set<Code> guesses = Collections.emptySet();
		Set<Code> answers = Collections.emptySet();
		Comparator<Tree> comparator = createMockComparator();
		Controller controller = new Controller(
				factory, comparator);
		
		expect(factory.next())
				.andReturn(Tree.fromString("[1]"))
				.andReturn(Tree.fromString("[2]"))
				.andReturn(Tree.fromString("[3]"));
		expect(factory.hasNext())
				.andReturn(true).times(2)
				.andReturn(false).once();
		
		expect(comparator.compare(
				eq(Tree.fromString("[2]")), eq(Tree.fromString("[1]"))))
		.andReturn(-1);
		expect(comparator.compare(
				eq(Tree.fromString("[3]")), eq(Tree.fromString("[2]"))))
		.andReturn(1);
		
		replayAll();
		assertThat(
				controller.newOptimizedStrategyTree(guesses, answers),
				equalTo(Tree.fromString("[2]")));
		verifyAll();
	}
	
}
