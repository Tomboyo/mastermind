package edu.vwc.mastermind.sequence;

/**
 * Dummy Codes Filter.
 * @author Tomboyo
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
