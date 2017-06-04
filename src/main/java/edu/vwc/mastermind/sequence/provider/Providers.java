package edu.vwc.mastermind.sequence.provider;

public class Providers {

	private Providers() {}
	
	public static CodesProvider allCodes(int colors, int pegs) {
		return new AllCodesProvider(colors, pegs);
	}
}
