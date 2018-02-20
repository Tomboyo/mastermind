package edu.vwc.mastermind.sequence.provider;

import static edu.vwc.mastermind.TestHelper.setOfCodes;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.CoreMatchers.anyOf;
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

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import edu.vwc.mastermind.sequence.Code;

public class GreedyCodesProviderFactoryTest {

	GreedyCodesProviderFactory factory;

	@Test(expected = NoSuchElementException.class)
	public void noGuessesRemaining() {
		Set<Code> universe = setOfCodes(Code.valueOf(1));
		Set<Code> alreadyGuessed = new HashSet<>(universe);
		Set<Code> answersRemaining = setOfCodes(Code.valueOf(2));

		factory = new GreedyCodesProviderFactory(universe);

		factory.getInstance(alreadyGuessed, answersRemaining);
	}

	@Test(expected = NoSuchElementException.class)
	public void noAnswersLeft() {
		Set<Code> universe = setOfCodes(Code.valueOf(1));
		Set<Code> alreadyGuessed = emptySet();
		Set<Code> answersRemaining = emptySet();

		factory = new GreedyCodesProviderFactory(universe);

		factory.getInstance(alreadyGuessed, answersRemaining);
	}

	@Test
	public void maximizeMinimumNumberOfEliminatedAnswers() {
		Set<Code> universe = setOfCodes(
				Code.valueOf(1, 1, 1),
				Code.valueOf(1, 2, 1),
				Code.valueOf(1, 2, 3));
		Set<Code> alreadyGuessed = emptySet();
		Set<Code> answersRemaining = universe;

		factory = new GreedyCodesProviderFactory(universe);

		Collection<Code> codes = factory.getInstance(
				alreadyGuessed,
				answersRemaining)
				.getCodes();

		// [1, 1, 1] and [1, 2, 3] always eliminate two possibilities.
		// [1, 2, 1] Might only eliminate one, as when it receives the
		// feedback [2, 0, 1].
		assertThat(codes, anyOf(
				hasItem(Code.valueOf(1, 1, 1)),
				hasItem(Code.valueOf(1, 2, 3))));
	}

	@Test
	public void preferPossibleAnswersAsGuessesDuringTies() {
		Set<Code> universe = setOfCodes(
				Code.valueOf(1, 1, 1),
				Code.valueOf(1, 2, 1),
				Code.valueOf(1, 2, 3));
		Set<Code> alreadyGuessed = emptySet();
		Set<Code> answersRemaining = setOfCodes(
				Code.valueOf(1, 1, 1),
				Code.valueOf(1, 2, 1));

		factory = new GreedyCodesProviderFactory(universe);

		Collection<Code> codes = factory.getInstance(alreadyGuessed,
				answersRemaining)
				.getCodes();

		// [1, 1, 1] and [1, 2, 3] are the best choices; only [1, 1, 1] is a
		// possible answer, so we prefer that.
		assertThat(codes, hasItem(Code.valueOf(1, 1, 1)));
	}
}
