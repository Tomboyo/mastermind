package edu.vwc.mastermind;

public final class Integers {

	private Integers() {}

	/**
	 * @param input
	 *            A comma-separated sequence of numbers. All characters which
	 *            are not commas or decimal numbers are stripped. Like the
	 *            results of Arrays.toString(arr).
	 * @return An int array of parsed numbers
	 */
	public static int[] fromStringifiedArray(String input) {
		input = input.replaceAll("[^\\d,]", "");
		String[] numbers = input.split(",");

		int[] result = new int[numbers.length];
		int index = 0;
		for (String number : numbers) {
			result[index++] = Integer.parseInt(number);
		}

		return result;
	}

}
