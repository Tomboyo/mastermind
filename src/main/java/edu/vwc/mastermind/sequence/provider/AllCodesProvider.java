package edu.vwc.mastermind.sequence.provider;

import java.util.LinkedHashSet;
import java.util.Set;

import edu.vwc.mastermind.sequence.Code;

/**
 * Provides a set of ${@link Code}s that enumerates over all permutations of
 * Code with a configured number of pegs and colors. There will be colors^pegs
 * total Code instances in the collection.
 */
public class AllCodesProvider implements CodesProvider {

	private Set<Code> codes;
	
	public AllCodesProvider(int colors, int pegs) {
		generateCodes(colors, pegs);
	}

	@Override
	public Set<Code> getCodes() {
		return codes;
	}
	
	private void generateCodes(int colors, int pegs) {
		int numCodes = (int) Math.pow(colors,  pegs);
		// See https://stackoverflow.com/questions/7115445/what-is-the-optimal-capacity-and-load-factor-for-a-fixed-size-hashmap
		// We know the precise size, so this seems appropriate.
		codes = new LinkedHashSet<>(numCodes, 1);

		// Tracker holds the code we are generating.
		// We treat its 'pegs' like base-<colors> numbers.
		int[] tracker = new int[pegs];
		int max_digit = colors - 1;

		for (int i = 0; i < numCodes; i++) {
			codes.add(Code.valueOf(tracker));
			
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
	}

}
