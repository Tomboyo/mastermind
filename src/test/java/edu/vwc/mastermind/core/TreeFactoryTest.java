package edu.vwc.mastermind.core;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.easymock.IExpectationSetters;
import org.junit.Before;
import org.junit.Test;

import edu.vwc.mastermind.core.TreeFactory;
import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.Response;
import edu.vwc.mastermind.sequence.provider.CodesProvider;
import edu.vwc.mastermind.sequence.provider.CodesProviderFactory;
import edu.vwc.mastermind.tree.Tree;
import edu.vwc.mastermind.tree.TreeBuilder;

public class TreeFactoryTest {
	
	private Code firstGuess;
	private Comparator<Tree> comparator;
	private CodesProviderFactory providerFactory;
	private ComparatorPreference comparatorPreference;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		firstGuess = Code.valueOf(0, 1);
		comparator = createMock(Comparator.class);
		providerFactory = createMock(CodesProviderFactory.class);
		
		comparatorPreference = new ComparatorPreference(comparator);
	}
	
	@Test
	public void test() {
		setUpMockComparator();
		setUpMockProviderFactory();
		
		Tree expected = new TreeBuilder()
				.addLine("[0, 1]->[1, 0, 1][0, 2]->[0, 1, 1][2, 1]")
				.addLine("[0, 1]->[0, 1, 1][2, 0]->[0, 1, 1][1, 2]")
				.addLine("[0, 1]->[0, 2, 0][1, 0]")
				.addLine("[0, 1]->[0, 0, 2][3, 4]->[1, 0, 1][3, 5]->"
							+ "[0, 1, 1][5, 4]")
				.addLine("[0, 1]->[0, 0, 2][3, 4]->[0, 1, 1][5, 3]->"
							+ "[0, 1, 1][4, 5]")
				.addLine("[0, 1]->[0, 0, 2][3, 4]->[0, 2, 0][4, 3]")
				.build();
		
		replay(comparator, providerFactory);
		TreeFactory factory = new TreeFactory(
				Response.valueOf(2, 0, 0),
				comparator,
				providerFactory);
		
		Set<Code> allCodes = toSet(
				Code.valueOf(0, 1), Code.valueOf(0, 2), Code.valueOf(1, 0),
				Code.valueOf(1, 2), Code.valueOf(2, 0), Code.valueOf(2, 1),
				Code.valueOf(3, 4), Code.valueOf(3, 5), Code.valueOf(4, 3),
				Code.valueOf(4, 5), Code.valueOf(5, 3), Code.valueOf(5, 4));
		assertEquals(expected,
				factory.newTree(firstGuess, new LinkedHashSet<Code>(),
						allCodes));
		verify(comparator, providerFactory);
	}
	
	// The preferred tree is kept and may be incorporated into the result; the
	// other tree is discarded.
	private void setUpMockComparator() {
		expect(comparator.compare(isNull(), anyObject(Tree.class)))
				.andReturn(1).anyTimes();
		comparatorPreference
				.prefer(Tree.fromString("[0, 2]->[0, 1, 1][2, 1]"))
				.to(Tree.fromString("[2, 1]->[0, 1, 1][0, 2]"));
		comparatorPreference
				.prefer(Tree.fromString("[2,0]->[0,1,1][1,2]"))
				.to(Tree.fromString("[1,2]->[0,1,1][2,0]"));
		comparatorPreference
				.prefer(Tree.fromString("[3, 5]->[0, 1, 1][5, 4]"))
				.to(Tree.fromString("[5, 4]->[0, 1, 1][3, 5]"));
		comparatorPreference
				.prefer(Tree.fromString("[5,3]->[0,1,1][4,5]"))
				.to(Tree.fromString("[4,5]->[0,1,1][5,3]"));
	}
	
	private void setUpMockProviderFactory() {
		expectBlackListAndHint(
				toSet(firstGuess),
				toSet(Code.valueOf(0, 2), Code.valueOf(2, 1)))
				.andReturn(() -> toSet(
						Code.valueOf(0, 2), Code.valueOf(2, 1)));
		expectBlackListAndHint(
				toSet(firstGuess),
				toSet(Code.valueOf(2, 0), Code.valueOf(1, 2)))
				.andReturn(() -> toSet(
						Code.valueOf(2, 0), Code.valueOf(1, 2)));
		expectBlackListAndHint(
				toSet(firstGuess),
				toSet(Code.valueOf(3, 4), Code.valueOf(3, 5),
						Code.valueOf(4, 3), Code.valueOf(4, 5),
						Code.valueOf(5, 3), Code.valueOf(5, 4)))
				.andReturn(() -> toSet(Code.valueOf(3, 4)));
		expectBlackListAndHint(
				toSet(firstGuess, Code.valueOf(3, 4)),
				toSet(Code.valueOf(3, 5), Code.valueOf(5, 4)))
				.andReturn(() -> toSet(Code.valueOf(3, 5), Code.valueOf(5, 4)));
		expectBlackListAndHint(
				toSet(firstGuess, Code.valueOf(3, 4)),
				toSet(Code.valueOf(5, 3), Code.valueOf(4, 5)))
				.andReturn(() -> toSet(Code.valueOf(5, 3), Code.valueOf(4, 5)));
	}
	
	private IExpectationSetters<CodesProvider> expectBlackListAndHint(
			Set<Code> blackList, Set<Code> hint) {
		return expect(providerFactory.getInstance(
				eq(blackList),
				eq(hint)));
	}
	
	private Set<Code> toSet(Code...codes) {
		// Set implementation choice can change the order in which Trees are
		// generated and compared during the test. Prefer the linkedHashSet.
		Set<Code> set = new LinkedHashSet<>();
		for (Code c : codes) {
			set.add(c);
		}
		return set;
	}
	
	// TODO: Can we bind the preference such that order of arguments does not
	// matter?
	private static class ComparatorPreference {
		public interface PreferToSetter {
			public void to(Tree worse);
		}
		
		private final Comparator<Tree> mock;
		
		public ComparatorPreference(Comparator<Tree> mock) {
			this.mock = mock;
		}
		
		public PreferToSetter prefer(final Tree better) {
			return (final Tree worse) -> {
				expect(mock.compare(eq(better), eq(worse)))
						.andReturn(-1);
			};
		}
	}

}
