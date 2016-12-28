package edu.vwc.mastermind.sequence;

/**
 * Responsible for providing the set of Codes for a game.
 * @author Tom Simmons
 */
public abstract class AbstractCodesProvider implements CodesProvider {
	
	protected int colors;
	protected int pegs;
	protected Code[] codes;
	
	/**
	 * Supply game parameters
	 * @param colors Number of colors allowed per code
	 * @param pegs Number of pegs allowed in a code
	 */
	public AbstractCodesProvider (int colors, int pegs) {
		if (colors <= 0 || pegs <= 0) {
			throw new IllegalArgumentException("Colors and Pegs must be greater than zero");
		}
		
		this.colors = colors;
		this.pegs = pegs;
	}
	
	/* (non-Javadoc)
	 * @see edu.vwc.mastermind.sequence.CodesProvider#getCodes()
	 */
	@Override
	public abstract Code[] getCodes();
	
	/* (non-Javadoc)
	 * @see edu.vwc.mastermind.sequence.CodesProvider#getSubset(edu.vwc.mastermind.sequence.CodeFilter)
	 */
	@Override
	public Code[] getSubset(CodeFilter filter) {
		if (codes == null) {
			codes = getCodes();
		}
		
		return filter.filter(codes);
	}

}
