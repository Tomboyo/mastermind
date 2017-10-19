package edu.vwc.mastermind.core;

import static edu.vwc.mastermind.TestHelper.setOfCodes;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import edu.vwc.mastermind.core.TreeFactory;
import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.Response;
import edu.vwc.mastermind.sequence.provider.CodesProviderFactory;
import edu.vwc.mastermind.tree.Tree;
import edu.vwc.mastermind.tree.TreeBuilder;

public class TreeFactoryTest extends EasyMockSupport {
	
	private Comparator<Tree> comparator;
	private CodesProviderFactory providerFactory;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		comparator = createMock(Comparator.class);
		providerFactory = createMock(CodesProviderFactory.class);
	}
	
	private TreeFactory newTreeFactory(Response correct) {
		return new TreeFactory(correct, comparator, providerFactory);
	}
	
	@Test
	public void testCorrectGuessHasNoChildren() {
		Code guess = Code.valueOf(0, 1);
		Set<Code> alreadyGuessed = new HashSet<>();
		Set<Code> answersRemaining = setOfCodes(guess);
		TreeFactory factory = newTreeFactory(Response.valueOf(2, 0, 0));
		
		Tree expected = new TreeBuilder()
				.addLine("[0, 1]")
				.build();
		replayAll();
		Tree actual = factory.newTree(
				guess, alreadyGuessed, answersRemaining);
		verifyAll();
		assertThat(actual, equalTo((expected)));
	}
	
	@Test
	public void onlyRemainingAnswerInGroupIsNextGuess() {
		Code guess = Code.valueOf(0, 1);
		Set<Code> alreadyGuessed = new HashSet<>();
		Set<Code> answersRemaining = setOfCodes(Code.valueOf(2, 2));
		TreeFactory factory = newTreeFactory(Response.valueOf(2, 0, 0));
		
		Tree expected = new TreeBuilder()
				.addLine("[0, 1]->[0, 0, 2][2, 2]")
				.build();
		
		replayAll();
		Tree actual = factory.newTree(
				guess,
				alreadyGuessed,
				answersRemaining);
		verifyAll();
		assertThat(actual, equalTo(expected));
	}
	
	@Test
	public void testAnswersInGroupAreAllGuessedAndCompared() {
		Code guess = Code.valueOf(0, 1);
		Set<Code> alreadyGuessed = new HashSet<>();
		Set<Code> answersRemaining = setOfCodes(
				Code.valueOf(2, 2), Code.valueOf(3, 3));
		TreeFactory factory = newTreeFactory(Response.valueOf(2, 0, 0));
		
		expect(providerFactory.getInstance(alreadyGuessed, answersRemaining))
				.andReturn(() -> new HashSet<>(answersRemaining));
		expect(comparator.compare(eq(null), anyObject()))
				.andReturn(1);
		expect(comparator.compare(
				Tree.fromString("[3, 3]->[0, 0, 2][2, 2]"),
				Tree.fromString("[2, 2]->[0, 0, 2][3, 3]")))
				.andReturn(-1);
		
		Tree expected = new TreeBuilder()
				.addLine("[0, 1]->[0, 0, 2][3, 3]->[0, 0, 2][2, 2]")
				.build();
		
		replayAll();
		Tree actual = factory.newTree(
				guess,
				alreadyGuessed,
				answersRemaining);
		verifyAll();
		assertThat(actual, equalTo(expected));
	}

}
