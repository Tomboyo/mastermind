package edu.vwc.mastermind.sequence.provider;

import java.util.Set;

import edu.vwc.mastermind.sequence.Code;

/**
 * Responsible for producing collections of {@link Code}s.
 */
public interface CodesProvider {

    /**
     * Get a set of Codes.
     *
     * @return A set of Codes
     */
    Set<Code> getCodes();

}