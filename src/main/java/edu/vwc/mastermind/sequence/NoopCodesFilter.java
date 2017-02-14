package edu.vwc.mastermind.sequence;

/**
 * Dummy Codes Filter.
 * @author tom.simmons
 *
 */
public class NoopCodesFilter implements CodesFilter {
	
	/**
	 * Delegate to the superset provider.
	 */
	@Override
	public Code[] getCodes(CodesProvider supersetProvider) {
		return supersetProvider.getCodes();
	}

}
