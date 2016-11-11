package edu.vwc.sequence;

import java.util.Arrays;

/**
 * Generates all Code permutations
 * @author Tom Simmons
 * 
 */
public class AllCodesProvider extends AbstractCodesProvider {
	
	private Code[] codes;
	
	public AllCodesProvider(int colors, int pegs) {
		super(colors, pegs);
	}

	/**
	 * Get complete enumeration of Codes.
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
			codes[codesIndex++] = new Code(tracker.clone());
			
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
