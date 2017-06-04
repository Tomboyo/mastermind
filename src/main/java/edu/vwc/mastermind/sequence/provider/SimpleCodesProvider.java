package edu.vwc.mastermind.sequence.provider;

import java.util.Set;

import edu.vwc.mastermind.sequence.Code;

class SimpleCodesProvider implements CodesProvider {

	private final Set<Code> codes;
	
	SimpleCodesProvider(Set<Code> codes) {
		this.codes = codes;
	}
	
	@Override
	public Set<Code> getCodes() {
		return codes;
	}

}
