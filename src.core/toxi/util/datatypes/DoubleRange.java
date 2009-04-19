package toxi.util.datatypes;

import java.util.Random;

import toxi.math.MathUtils;

public class DoubleRange {

	public double min, max;
	public double current;

	protected Random random = new Random();

	public DoubleRange(double min, double max) {
		this.min = min;
		this.max = max;
	}

	public DoubleRange copy() {
		DoubleRange range = new DoubleRange(min, max);
		range.current = current;
		range.random = random;
		return range;
	}

	public double getCurrent() {
		return current;
	}

	public boolean isValueInRange(float val) {
		return val >= min && val <= max;
	}

	public double pickRandom() {
		current = MathUtils.random(random, (float) min, (float) max);
		return current;
	}

	public void setRandom(Random rnd) {
		random = rnd;
	}

	@Override
	public String toString() {
		return "DoubleRange: " + min + " -> " + max;
	}
}