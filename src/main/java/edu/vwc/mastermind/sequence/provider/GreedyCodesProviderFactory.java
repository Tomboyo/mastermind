package edu.vwc.mastermind.sequence.provider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.IntStream;

import edu.vwc.mastermind.sequence.Code;
import edu.vwc.mastermind.sequence.Response;

/**
 * This code provider strategy emulates that of Donald Knuth. We choose
 * exactly one code to use as the next guess. We pick the code as the next
 * guess which guarantees a maximal number of remaining answers will be
 * eliminated. We prefer to pick codes which are themselves possible answers.
 * We consider each code from the universe as a candidate.
 *
 * This algorithm will guarantee a 5-turn game in 6 colors and 4 pegs using
 * the starting input [1, 1, 0, 0].
 */
public final class GreedyCodesProviderFactory implements CodesProviderFactory {

	private final Collection<Code> universe;
	private final int sizeOfUniverse;
	
	public GreedyCodesProviderFactory(Collection<Code> universe) {
		this.universe = universe;
		sizeOfUniverse = universe.size();
	}
	
	@Override
	public CodesProvider getInstance(
			Set<Code> alreadyGuessed,
			Set<Code> answersRemaining) {
		if (answersRemaining.isEmpty())
			throw new NoSuchElementException("There are no answers left");

		if (alreadyGuessed.equals(universe))
			throw new NoSuchElementException("There are no guesses left");

		Iterator<Code> iter = universe.iterator();
		Code bestGuess = iter.next();
		int bestScore = score(getBins(bestGuess, answersRemaining));
		boolean guessIsAnswer = answersRemaining.contains(bestGuess);

		while (iter.hasNext()) {
			Code next = iter.next();
			int score = score(getBins(next, answersRemaining));
			if (score > bestScore) {
				bestScore = score;
				bestGuess = next;
				guessIsAnswer = answersRemaining.contains(bestGuess);
			} else if (score == bestScore && !guessIsAnswer) {
				bestScore = score;
				bestGuess = next;
				guessIsAnswer = true;
			}
		}

		Set<Code> codes = new LinkedHashSet<>();
		codes.add(bestGuess);
		CodesProvider provider = new SimpleCodesProvider(codes);
		return provider;
	}
	
	private Map<Response, CountingNumber> getBins(
			Code guess, Collection<Code> answers) {
		Map<Response, CountingNumber> bins = new HashMap<>();
		
		for (Code answer : answers) {
			Response key = guess.compareTo(answer);
			CountingNumber size = bins.putIfAbsent(key, new CountingNumber(1));
			if (size != null)
				size.increment();
		};
		
		return bins;
	}
	
	private int score(
			Map<Response, CountingNumber> bins) {
		return sizeOfUniverse - bins.values().stream()
				.flatMapToInt(it -> IntStream.of(it.get()))
				.max()
				.orElse(0); // This should not occur
	}
	
	private static class CountingNumber {
		private int value;
		
		public CountingNumber(int initialValue) {
			value = initialValue;
		}
		
		public void increment() {
			value += 1;
		}
		
		public int get() {
			return value;
		}
		
		@Override
		public String toString() {
			return String.valueOf(value);
		}
	}

}
