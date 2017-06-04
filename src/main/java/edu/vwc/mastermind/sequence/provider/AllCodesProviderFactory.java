package edu.vwc.mastermind.sequence.provider;

import java.util.LinkedHashSet;
import java.util.Set;

import edu.vwc.mastermind.sequence.Code;

class AllCodesProviderFactory implements CodesProviderFactory {
	
	private final AllCodesProvider provider;
	
	AllCodesProviderFactory(int colors, int pegs) {
		provider = new AllCodesProvider(colors, pegs);
	}
	
	@Override
	public CodesProvider getInstance(Set<Code> blackList, Set<Code> hint) {
		final Set<Code> codes = new LinkedHashSet<>(); 
		codes.addAll(provider.getCodes());
		for (Code code : blackList) {
			codes.remove(code);
		}
		
		return new SimpleCodesProvider(codes);
	}

}
