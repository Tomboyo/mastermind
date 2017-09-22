package edu.vwc.mastermind.core;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

import edu.vwc.mastermind.core.TreeFactory;
import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.Response;
import edu.vwc.mastermind.sequence.provider.CodesProvider;
import edu.vwc.mastermind.sequence.provider.CodesProviderFactory;
import edu.vwc.mastermind.tree.Tree;

public class TreeFactoryTest {
	
	/**
	 * This is an end-to-end examination of the {@link TreeFactory}. We seed the
	 * factory with mock decision making entities like {@link Comparator<Tree>}s
	 * and {@link CodesProvider}s so as to precisely control the decision-making
	 * process of the TreeFactory. After defining a carefully-chosen universe of
	 * {@link Code}s and a first guess, we instruct the mocks to guide the
	 * TreeFactory towards producing a desired Tree, which we build explicitly
	 * and use as the expected value at the end of the test.
	 */
	@Test
	public void test() {
		/*
		 * The game's universe of Codes will consist of two isomorphic sets. Any
		 * code in setA will garner a response of (0, 0) when compared to any
		 * code in setB. Since they are isomorphic, comparing two codes from
		 * setA will garner the same Response object as if their isomorph
		 * counterparts from setB had been compared.
		 */
		Set<Code> setA = new LinkedHashSet<>();
		Set<Code> setB = new LinkedHashSet<>();
		Collections.addAll(setA,
				Code.valueOf(0, 1),
				Code.valueOf(0, 2),
				Code.valueOf(1, 0),
				Code.valueOf(1, 2),
				Code.valueOf(2, 0),
				Code.valueOf(2, 1));
		for (Code code : setA) {
			setB.add(isomorphCode(code, 3));
		}
		setA.addAll(setB);
		
		// The first guess we want the TreeFactory to make.
		Code firstGuess = Code.valueOf(0, 1);
		assertTrue("The first guess [0,1] is an element of setA",
				setA.contains(firstGuess));
		
		/*
		 * When the first guess [0,1] is made against the answer set defined by
		 * setA, one of four Responses could be garnered: [2,0], [1,0], [1,1],
		 * or [0,0]. Below we group the elements of setA by the Response they
		 * yield when compared against the first guess:
		 */
		// @formatter:off
		/*
		 * response(codes that garner the response)
		 * [2,0]([0,2], [2,1])
		 * [1,0]([2,0], [1,2])
		 * [1,1]([1,0])
		 * [0,0](setB)
		 *
		 */
		// @formatter:on
		/*
		 * Below we construct every single Tree object we expect the exhaustive
		 * simulation to generate. Not all of them will be a part of the final
		 * answer, but each one should be considered and compared to competing
		 * trees when determining which is "best".
		 *
		 * This is a rough depiction of the trees:
		 */
		// @formatter:off
		/*
		 * guess: response(answers grouped by response) -> next guess: ...
		 * [0,1]: [1,0,1]([0,2], [2,1]) -> [0,2]: [0,1,1]([2,1]) -> [2,1]
		 *                                 [2,1]: [0,1,1]([0,2]) -> [0,2]
		 *      : [0,1,1]([2,0], [1,2]) -> [2,0]: [0,1,1]([1,2]) -> [1,2]
		 *                              -> [1,2]: [0,1,1]([2,0]) -> [2,0]
		 *      : [0,2,0]([1,0]) -> [1,0]
		 *      : [0,0,2](setB) -> (* see below)
		 *
		 */
		//@formatter:on
		/*
		 * *: When the guess [3,4] is made, this section of the tree is
		 * identical to the part of the tree depicted already, with two notable
		 * exceptions: The codes are isomorphs of those used above, and there is
		 * no [0,0] group. We will guarantee this happens with mocks.
		 * 
		 * To simplify this test case, we will use a mock object to guarantee
		 * that particular guesses are made and particular sub-trees that result
		 * are favored, allowing us to construct the below expected result as
		 * explicitly as we do. We still construct the branches that will be
		 * discarded by the mock comparator to verify that they are considered
		 * by the TreeFactory, however.
		 */
		
		Tree expected = new Tree(firstGuess);
		
		/*
		 * Here we define the mock objects that will enable the expected product
		 * of the TreeFactory to be controlled precisely.
		 */
		@SuppressWarnings("unchecked")
		// Decides what trees are "best"
		Comparator<Tree> comparator = createMock(Comparator.class);
		// Decides what guesses to consider in what order when building trees;
		// trees thus produced are compared by the comparator, and the "best"
		// one is kept around.
		CodesProviderFactory providerFactory =
				createMock(CodesProviderFactory.class);
		
		// The mock should consider null trees to be the worst.
		expect(comparator.compare(isNull(), anyObject(Tree.class)))
				.andReturn(1).anyTimes();
		
		// First guess has one correct peg, Response [1, 0, 1].
		// Possible answers are [0, 2] and [2, 1].
		// Evaluate [0, 2] and [2, 1] as guesses.
		setExpectation(providerFactory,
				new Code[]{ firstGuess },
				new Code[]{ Code.valueOf(0, 2), Code.valueOf(2, 1) },
				new Code[]{ Code.valueOf(0, 2), Code.valueOf(2, 1) });
		Tree treeA = new Tree(Code.valueOf(0, 2));
		Tree treeB = new Tree(Code.valueOf(2, 1));
		Tree tmp = copyOf(treeA);
		treeA.add(Response.valueOf(0, 1, 1), copyOf(treeB));
		treeB.add(Response.valueOf(0, 1, 1), copyOf(tmp));
		expect(comparator.compare(eq(treeA), eq(treeB)))
				.andReturn(-1);		
		expected.add(Response.valueOf(1, 0, 1), treeA);
		
		// First guess has one misplaced peg, Response [0, 1, 1]
		// Possible answers are [2, 0] and [1, 2]
		// Evaluate [2, 0] and [1, 2] as guesses
		setExpectation(providerFactory,
				new Code[]{ firstGuess },
				new Code[]{ Code.valueOf(2, 0), Code.valueOf(1, 2) },
				new Code[]{ Code.valueOf(2, 0), Code.valueOf(1, 2) });
		Tree treeC = new Tree(Code.valueOf(2, 0));
		Tree treeD = new Tree(Code.valueOf(1, 2));
		tmp = copyOf(treeC);
		treeC.add(Response.valueOf(0, 1, 1), copyOf(treeD));
		treeD.add(Response.valueOf(0, 1, 1), copyOf(tmp));
		expect(comparator.compare(eq(treeC), eq(treeD)))
				.andReturn(-1);
		expected.add(Response.valueOf(0, 1, 1), treeC);
		
		// First guess has two misplaced pegs, Response [0, 2, 0]
		// The only possible answer is [1, 0]
		Tree treeE = new Tree(Code.valueOf(1, 0));
		expected.add(Response.valueOf(0, 2, 0), treeE);
		
		// First guess shares no similarities with the answer, Response
		// [0, 0, 2].
		// The possible answers are all Codes in setB
		// Only evaluate the next guess of [3, 4]
		setExpectation(providerFactory,
				new Code[]{ firstGuess },
				setB.toArray(new Code[]{}),
				new Code[]{ Code.valueOf(3, 4) });
		// Isomorphism of [1,0,1]([0,2], [2,1]) tree
		setExpectation(providerFactory,
				new Code[]{ firstGuess, Code.valueOf(3, 4) },
				new Code[]{ Code.valueOf(3, 5), Code.valueOf(5, 4) },
				new Code[]{ Code.valueOf(3, 5), Code.valueOf(5, 4) });
		// Isomorphism of [0,1,1]([2,0], [1,2]) tree
		setExpectation(providerFactory,
				new Code[]{ firstGuess, Code.valueOf(3, 4) },
				new Code[]{ Code.valueOf(5, 3), Code.valueOf(4, 5) },
				new Code[]{ Code.valueOf(5, 3), Code.valueOf(4, 5) });
		expected.add(Response.valueOf(0, 0, 2),
				isomorphTree(expected, 3));
		expect(comparator.compare(
				eq(isomorphTree(treeA, 3)),
				eq(isomorphTree(treeB, 3))))
		.andReturn(-1);
		expect(comparator.compare(
				eq(isomorphTree(treeC, 3)),
				eq(isomorphTree(treeD, 3))))
		.andReturn(-1);
		
		replay(comparator, providerFactory);
		TreeFactory factory = new TreeFactory(
				Response.valueOf(2, 0, 0),
				comparator,
				providerFactory);
		assertEquals(expected,
				factory.newTree(firstGuess, new LinkedHashSet<Code>(), setA));
		verify(comparator, providerFactory);
	}
	
	/**
	 * Create a new Code by incrementing the values of the pegs of the provided
	 * code by the provided value
	 * 
	 * @param other
	 *            Any code
	 * @param range
	 *            Magnitude by which to increment the values of the provided
	 *            code
	 * @return A new Code generated by incrementing the values of the pegs of
	 *         the provided code by the provided value
	 */
	private Code isomorphCode(Code other, int range) {
		int[] sequence = other.toArray();
		for (int i = 0; i < sequence.length; i++) {
			sequence[i] += range;
		}
		return Code.valueOf(sequence);
	}

	/**
	 * Create a new Tree that is identical to the provided tree, except that all
	 * of the guess codes in the new tree are isomorph codes produces by
	 * isomorphCode(code, range). The new tree is guaranteed to be a valid tree.
	 * 
	 * @param other
	 *            Any Tree
	 * @param range
	 *            As defined by isomorphCode
	 * @return A new tree whose guesses are isomorphs of the original
	 */
	private Tree isomorphTree(Tree other, int range) {
		Tree root = new Tree(isomorphCode(other.getGuess(), range));
		for (Entry<Response, Tree> child : other.children()) {
			root.add(child.getKey(), isomorphTree(child.getValue(), range));
		}
		return root;
	}
	
	private Tree copyOf(Tree other) {
		return isomorphTree(other, 0);
	}
	
	/**
	 * Binds the mock provider factory with an expectation. Namely, this routine
	 * is used to specify the behavior of a provider factory and the provider it
	 * produces, such that when the TreeFactory has already made a set of
	 * guesses and is now evaluating the next guesses it should make for a
	 * particular answer group, the exact set of guess codes to make are
	 * controlled.
	 * 
	 * @param mock
	 *            The CodesProviderFactory mock object to bind expectations to
	 * @param codesGuessed
	 *            To correspond with the contents of
	 *            {@link CodesProviderFactory#getInstance}'s blacklist
	 *            parameter.
	 * @param answerGroup
	 *            To correspond with the contents of
	 *            {@link CodesProviderFactory#getInstance}'s hint parameter.
	 * @param returned
	 *            When the CodesProviderFactory mock is provided a blacklist set
	 *            that corresponds with codesGuessed and a hint set that
	 *            corresponds with answerGroup, a CodesProvider will be returned
	 *            that is configured to return a Set of these codes when
	 *            {@link CodesProvider#getCodes} is invoked.
	 */
	private void setExpectation(CodesProviderFactory mock, Code[] codesGuessed,
			Code[] answerGroup, Code[] returned) {
		Set<Code> blacklist = new LinkedHashSet<>();
		Set<Code> hint = new LinkedHashSet<>();
		Set<Code> value = new LinkedHashSet<>();
		
		Collections.addAll(blacklist, codesGuessed);
		Collections.addAll(hint, answerGroup);
		Collections.addAll(value, returned);
		
		CodesProvider provider = createMock(CodesProvider.class);
		expect(provider.getCodes()).andReturn(value);
		replay(provider);
		
		expect(mock.getInstance(blacklist, hint))
				.andReturn(provider);
	}

}
