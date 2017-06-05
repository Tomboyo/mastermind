package edu.vwc.mastermind.core;

import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.LinkedHashSet;

import org.junit.Test;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.Response;
import edu.vwc.mastermind.sequence.provider.CodesProviderFactory;
import edu.vwc.mastermind.sequence.provider.ProviderFactories;
import edu.vwc.mastermind.sequence.provider.Providers;
import edu.vwc.mastermind.tree.Tree;
import edu.vwc.mastermind.tree.compare.Comparators;

public class TreeFactoryTest {

	@Test
	public void test() {
		Comparator<Tree> comparator = Comparators.minRequiredTurns();
		CodesProviderFactory providerFactory = ProviderFactories.allCodes(
				Providers.allCodes(2, 2));
		
		TreeFactory factory = new TreeFactory(
				Response.valueOf(2, 2), comparator, providerFactory);
		Tree actual = factory.newTree(Code.valueOf(0, 0),
				new LinkedHashSet<Code>(),
				Providers.allCodes(2, 2).getCodes());
		
		/*
		 * Expected game tree:
		 * 00[22].
		 * 00[01]->10[22].
		 * 00[01]->10[11]->01[22].
		 * 00[00]->11[22].
		 * 
		 * It would also be correct for the 00[01]->10 branch to be 00[10]->01,
		 * since both are as fast. It depends on the comparator.
		 */
		Tree expected  = new Tree(Code.valueOf(0, 0));
		Tree a = new Tree(Code.valueOf(1, 0));
		a.add(Response.valueOf(1, 1), new Tree(Code.valueOf(0, 1)));
		expected.add(Response.valueOf(2, 0), a);
		expected.add(Response.valueOf(0, 0), new Tree(Code.valueOf(1, 1)));
		
		assertTrue(expected.equals(actual));
	}

}
