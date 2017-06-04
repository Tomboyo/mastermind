package edu.vwc.mastermind.sequence.provider;

import java.util.Set;

import edu.vwc.mastermind.sequence.Code;

/**
 * Creates context-sensitive CodeProviders
 * @author Tom Simmons
 *
 */
public interface CodesProviderFactory {

	/**
	 * @param blackList
	 *            Codes which may never be returned by the new provider
	 * @param hint
	 *            May be used by the provider to determine what codes it should
	 *            produce
	 * @return A CodesProvider
	 */
	public CodesProvider getInstance(Set<Code> blackList, Set<Code> hint);
}
