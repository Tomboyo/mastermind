package edu.vwc.mastermind.sequence.provider;

import java.util.List;
import java.util.Set;

import edu.vwc.mastermind.sequence.Code;

class AllCodesProviderFactory implements CodesProviderFactory {
	
	private final AllCodesProvider provider;
	
	AllCodesProviderFactory(int colors, int pegs) {
		provider = new AllCodesProvider(colors, pegs);
	}
	
	@Override
	public CodesProvider getInstance(List<Code> blackList, List<Code> hint) {
		final Set<Code> codes = provider.getCodes();
		for (Code code : blackList) {
			codes.remove(code);
		}
		
		return new SimpleCodesProvider(codes);
	}

}
