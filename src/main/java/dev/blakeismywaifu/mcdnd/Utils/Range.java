package dev.blakeismywaifu.mcdnd.Utils;

import java.util.Map;

public class Range {

	private final int low;
	private final int high;

	public Range(int low, int high) {
		this.low = low;
		this.high = high;
	}

	public static Integer contains(Map<Range, Integer> map, Integer number) {
		return Range.contains(map, number, 0);
	}

	public static Integer contains(Map<Range, Integer> map, Integer number, Integer fallbackValue) {
		final Integer[] test = {fallbackValue};
		map.forEach((k, v) -> {
			if (k.contains(number)) {
				test[0] = v;
			}
		});
		return test[0];
	}

	public boolean contains(int number) {
		return (number >= low && number <= high);
	}
}
