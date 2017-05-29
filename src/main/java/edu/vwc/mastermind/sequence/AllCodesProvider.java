package edu.vwc.mastermind.sequence;

/**
 * Generates all Code permutations (colors^pegs codes in total)
 * @author Tomboyo
 * 
 */
public class AllCodesProvider implements CodesProvider {
	
	private int colors;
	private int pegs;
	private Code[] codes;
	
	public AllCodesProvider(int colors, int pegs) {
		this.colors = colors;
		this.pegs = pegs;
	}

	/**
	 * Get a complete enumeration of codes based on configured number of colors
	 * and pegs. The codes collection is lazily initialized and does not exist
	 * until this method is called. The result is cached, so future calls to
	 * this method do not waste computation time.
	 * 
	 * @return Code collection containing colors^pegs codes.
	 */
	@Override
	public Code[] getCodes() {
		if (codes != null) {
			return codes;
		}
		
		int numCodes = (int) Math.pow(colors,  pegs);
		codes = new Code[numCodes];
		int codesIndex = 0;

		// Tracker holds the code we are generating.
		// We treat its 'pegs' like base-<colors> numbers.
		int[] tracker = new int[pegs];
		int max_digit = colors - 1;

		for (int i = 0; i < numCodes; i++) {
			codes[codesIndex++] = Code.valueOf(tracker.clone());
			
			// Increment the 0th digit
			tracker[0] ++;

			// Perform carry operation. Stop if the tracker holds its maximum value
			int carry_index = 0;
			while (tracker[carry_index] > max_digit) {
				tracker[carry_index] = 0;
				carry_index ++;

				// Unsigned overflow (carry)
				if (carry_index == pegs) {
					break;
				}

				tracker[carry_index] ++;
			}
		}
		
		return codes;
	}

}
