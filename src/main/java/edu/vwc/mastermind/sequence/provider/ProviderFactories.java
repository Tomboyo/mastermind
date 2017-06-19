package edu.vwc.mastermind.sequence.provider;

/**
 * Static factory class for easy access to {@link CodesProviderFactory}
 * implementations.
 */
public class ProviderFactories {

	private ProviderFactories() {
	}

	/**
	 * Create a CodesProfiderFactory. CodesProvider instances created by this
	 * object filter the {@code provider}'s {@link CodesProvider#getCodes}
	 * results to exclude values included in
	 * {@link CodesProviderFactory#getInstance}'s blackList parameter.
	 * 
	 * @param provider
	 *            Delegate CodesProvider whose {@code getCodes} method will be
	 *            filtered.
	 * @return A CodesProvider proxy to the given provider.
	 */
	public static final CodesProviderFactory allCodes(CodesProvider provider) {
		return new AllCodesProviderFactory(provider);
	}
}
