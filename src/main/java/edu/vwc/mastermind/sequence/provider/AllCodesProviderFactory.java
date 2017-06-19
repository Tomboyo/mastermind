package edu.vwc.mastermind.sequence.provider;

import java.util.LinkedHashSet;
import java.util.Set;

import edu.vwc.mastermind.sequence.Code;

/**
 * Given an instance of {@link CodesProvider}, this class produces proxy
 * CodesProvider instances that delegate to the given provider when
 * {@link CodesProvider#getCodes()} is invoked.
 * 
 * <p>
 * The proxy instances returned by {@link #getIntance} will filter {@link Code}s
 * from the results of the delegate provider's own getCodes method. The value
 * provided to {@link #getInstance}'s blacklist parameter determines which Codes
 * are filtered. The hint parameter is never used.
 */
class AllCodesProviderFactory implements CodesProviderFactory {
	
	private final CodesProvider provider;
	
	AllCodesProviderFactory(CodesProvider provider) {
		this.provider = provider;
	}
	
	@Override
	public CodesProvider getInstance(Set<Code> blackList, Set<Code> hint) {
		final Set<Code> codes = new LinkedHashSet<>(provider.getCodes()); 
		for (Code code : blackList) {
			codes.remove(code);
		}
		
		return new SimpleCodesProvider(codes);
	}

}
