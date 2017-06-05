package edu.vwc.mastermind.sequence.provider;

public class ProviderFactories {

	private ProviderFactories() {}
	
	public static final CodesProviderFactory allCodes(
			CodesProvider provider) {
		return new AllCodesProviderFactory(provider);
	}
}
