package edu.vwc.mastermind.sequence.provider;

import java.util.Set;

import edu.vwc.mastermind.sequence.Code;

/**
 * Creates {@link CodesProvider} instances whose
 * {@link CodesProvider#getCodes()} method will return results intelligently
 * aggregated based on contextual parameters. This can be used to filter out
 * certain {@link Code} instances from the returned collection, or favor one
 * permutation of Codes over another.
 */
public interface CodesProviderFactory {

    /**
     * Get a CodesProvider whose {@link CodesProvider#getCodes()} method results
     * are based on provided context.
     *
     * @param blackList
     *            A collection of Codes that should be omitted from the
     *            Codesprovider's getCodes results.
     * @param hint
     *            A hint to the CodesProvider that can be used to decide what
     *            codes to produce.
     * @return A CodesProvider that will filter or augment the results of its
     *         getCodes() method based on the provided contextual parameters.
     */
    public CodesProvider getInstance(Set<Code> blackList, Set<Code> hint);
}
