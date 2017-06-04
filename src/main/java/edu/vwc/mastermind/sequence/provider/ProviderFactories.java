package edu.vwc.mastermind.sequence.provider;

public class ProviderFactories {

	private ProviderFactories() {}
	
	public static final CodesProviderFactory allCodes(int colors, int pegs) {
		return new AllCodesProviderFactory(colors, pegs);
	}
}
