package edu.vwc.mastermind.sequence;

public class NoopCodeFilter implements CodeFilter {

	@Override
	public Code[] filter(Code[] array) {
		return array;
	}

}
