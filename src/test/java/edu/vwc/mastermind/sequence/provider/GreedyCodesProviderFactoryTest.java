package edu.vwc.mastermind.sequence.provider;

import static edu.vwc.mastermind.TestHelper.setOfCodes;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeThat;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import edu.vwc.mastermind.sequence.Code;

public class GreedyCodesProviderFactoryTest {

	GreedyCodesProviderFactory factory;
	Collection<Code> universe;
	
	@Before
	public void setUp() {
		universe = new HashSet<>();
		factory = new GreedyCodesProviderFactory(universe);
	}
	
	@Test
	public void onlyRemainingGuessIsReturned() {
		universe.addAll(setOfCodes(
				Code.valueOf(0, 0), Code.valueOf(0, 1),
				Code.valueOf(1, 0), Code.valueOf(1, 1),
				Code.valueOf(2, 3)));
		
		Set<Code> answersRemaining = new HashSet<>(universe);
		
		for (Code remainingCode : universe) {
			Set<Code> alreadyGuessed = newSetOfAllCodesExcept(
					universe, remainingCode);
			testOnlyRemainingGuessIsAlwaysReturned(
					universe,
					answersRemaining,
					alreadyGuessed);
		}
	}
	
	private Set<Code> newSetOfAllCodesExcept(
			Collection<Code> all, Code except) {
		return all.stream()
				.filter(code -> !code.equals(except))
				.collect(toSet());
	}
	
	private void testOnlyRemainingGuessIsAlwaysReturned(
			Collection<Code> universe,
			Set<Code> answersRemaining,
			Set<Code> alreadyGuessed) {
		Collection<Code> copyOfUniverse = new HashSet<>(universe);
		copyOfUniverse.removeAll(alreadyGuessed);
		assumeThat(copyOfUniverse.size(), equalTo(1));
		
		Code remaining = copyOfUniverse.iterator().next();
		
		Collection<Code> result = factory
				.getInstance(alreadyGuessed, answersRemaining)
				.getCodes();
		assertThat(result.size(), equalTo(1));
		assertThat(result, hasItem(remaining));
	}
	
	@Test(expected = NoSuchElementException.class)
	public void noGuessesRemaining() {
		universe = setOfCodes(Code.valueOf(1));
		Set<Code> alreadyGuessed = new HashSet<>(universe);
		Set<Code> answersRemaining = setOfCodes(Code.valueOf(2));

		factory.getInstance(alreadyGuessed, answersRemaining);
	}
	
	@Test(expected = NoSuchElementException.class)
	public void noAnswersLeft() {
		universe = setOfCodes(Code.valueOf(1));
		Set<Code> alreadyGuessed = Collections.emptySet();
		Set<Code> answersRemaining = Collections.emptySet();
		
		factory.getInstance(alreadyGuessed, answersRemaining);
	}
	
	@Test
	public void preferGuessesWithSmallestPeakAnswerGroupSize() {
		Code worseChoice = Code.valueOf(1, 1);
		Code betterChoice = Code.valueOf(2, 3);
		
		// All remaining answers are lumped under the response (0, 0, 2) when
		// the worse choice is guessed. When the better choice is guessed, they
		// are divided between (1, 0, 1) and (0, 1, 1), so there are fewer
		// answers per response group.
		universe.addAll(setOfCodes(
				worseChoice, betterChoice,
				Code.valueOf(2, 2), Code.valueOf(3, 3)));
		
		Set<Code> alreadyGuessed = universe.stream()
				.filter(code -> !code.equals(worseChoice)
						&& !code.equals(betterChoice))
				.collect(toSet());
		Set<Code> answersRemaining = new HashSet<>(alreadyGuessed);
		
		Set<Code> actual = factory
				.getInstance(alreadyGuessed, answersRemaining)
				.getCodes();
		assertThat(actual, equalTo(setOfCodes(betterChoice)));
	}
}
