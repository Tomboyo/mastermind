package edu.vwc.mastermind.sequence;

/**
 * Responsible for producing collections of codes, such as those necessary to
 * seed a simulation, or subsets of full enumerations used to produce logically
 * complete (not redundant) games.
 * 
 * @author Tomboyo
 *
 */
public interface CodesProvider {

	/**
	 * Get a set of Codes. This might be a complete enumeration of all codes
	 * possible in a game, or some logical subset.
	 * 
	 * @return collection of Codes
	 */
	Code[] getCodes();

}