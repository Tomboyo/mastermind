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

public final class GreedyCodesProviderFactory implements CodesProviderFactory {

	private final Collection<Code> universe;
	
	public GreedyCodesProviderFactory(Collection<Code> universe) {
		this.universe = universe;
	}
	
	@Override
	public CodesProvider getInstance(
			Set<Code> alreadyGuessed,
			Set<Code> answersRemaining) {
		if (answersRemaining.isEmpty())
			throw new NoSuchElementException("There are no answers left");
		
		Set<Code> remainingGuesses = new LinkedHashSet<Code>(universe);
		remainingGuesses.removeAll(alreadyGuessed);
		if (remainingGuesses.isEmpty())
			throw new NoSuchElementException(
					"There are no guesses left to make");
		
		Iterator<Code> iter = remainingGuesses.iterator();
		Code bestGuess = iter.next();
		int largestBin = largestBin(getBins(bestGuess, answersRemaining));
		
		while (iter.hasNext()) {
			Code next = iter.next();
			int localLargestBin = largestBin(getBins(next, answersRemaining));
			if (localLargestBin < largestBin) {
				largestBin = localLargestBin;
				bestGuess = next;
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
	
	private int largestBin(Map<Response, CountingNumber> bins) {
		return bins.values().stream()
				.flatMapToInt(it -> IntStream.of(it.get()))
				.max()
				.orElse(0);
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
