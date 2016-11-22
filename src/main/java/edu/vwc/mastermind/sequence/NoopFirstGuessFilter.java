package edu.vwc.mastermind.sequence;

import edu.vwc.mastermind.core.IFilter;

public class NoopFirstGuessFilter implements IFilter<Code> {

	@Override
	public Code[] filter(Code[] array) {
		return array;
	}

}
