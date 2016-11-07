package edu.vwc.sequence;

import java.util.Arrays;

public class Code {

	private int[] sequence;

	public Code(int... sequence) {
		this.sequence = sequence;
	}

	/*
	 * Get the feedback for this code when compared against a given key
	 * EXPECTS Code key, same length as this code.
	 * RESPONSE is a short[]
	 * 2 = peg in correct place, 1 = peg out of place, 0 = no feedback
	 * Response pegs sorted in descending order (e.g, [2,2,2,1,1,0,0,0])
	 */
	public Response compareTo(Code answer) throws IllegalArgumentException {
		if (answer.sequence.length != sequence.length) {
			throw new IllegalArgumentException("Length of Codes are unequal.");
		}

		// 0:x 1:w 2:r
		int[] response = new int[sequence.length];
		boolean[] gmask = new boolean[sequence.length];
		boolean[] cmask = new boolean[sequence.length];
		short index = 0;

		// Pass 1: Count correct pegs
		for (int i = 0; i < sequence.length; i++) {
			if (sequence[i] == answer.sequence[i]) {
				response[index++] = 2;
				gmask[i] = true;
				cmask[i] = true;
			}
		}

		// Pass 2: Count misplaced pegs
		for (int i = 0; i < sequence.length; i++) {
			if (gmask[i] != false) continue;

			for (int j = 0; j < answer.sequence.length; j++) {
				if (cmask[j] != false) continue;

				if (sequence[i] == answer.sequence[j]) {
					response[index++] = 1;
					cmask[j] = true;
				}
			}
		}

		return new Response(response); 
	}

	public boolean equals(Code other) {
		return Arrays.equals(this.sequence, other.sequence);
	}
	
	@Override
	public String toString() {
		return Arrays.toString(sequence);
	}
}
