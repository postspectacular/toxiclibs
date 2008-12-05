package toxi.util.datatypes;

import java.util.Random;

import toxi.math.MathUtils;

public class IntegerRange {

	public int min, max;
	public int current;

	protected Random random = new Random();

	public IntegerRange(int min, int max) {
		this.min = min;
		this.max = max;
	}

	public void setRandom(Random rnd) {
		random = rnd;
	}

	public int pickRandom() {
		current = MathUtils.random(min, max);
		return current;
	}

	public int getCurrent() {
		return current;
	}

	@Override
	public String toString() {
		return "IntegerRange: " + min + " -> " + max;
	}

	public boolean isValueInRange(int val) {
		return val >= min && val < max;
	}
}
