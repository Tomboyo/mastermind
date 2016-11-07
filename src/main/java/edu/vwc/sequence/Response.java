package edu.vwc.sequence;

import java.util.Arrays;

public class Response {

	private int[] sequence;

	public Response(int... sequence) {
		this.sequence = sequence;
	}

	@Override
	public int hashCode() {
		int sum = 0;
		for(int i = 0; i < sequence.length; i++) {
			sum = sum * 10 + sequence[i];
		}
		return sum;
	}

	@Override
	public final boolean equals(Object other) {
		if (other == this) return true;
		if (other == null) return false;
		if (this.getClass() != other.getClass()) return false;

		return Arrays.equals(sequence, ((Response) other).sequence);
	}
	
	@Override
	public String toString() {
		return Arrays.toString(sequence);
	}
}
