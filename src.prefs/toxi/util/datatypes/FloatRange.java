package toxi.util.datatypes;

import java.util.Random;

import toxi.math.MathUtils;

public class FloatRange {

	public float min, max;
	public float current;

	protected Random random = new Random();

	public FloatRange(float min, float max) {
		this.min = min;
		this.max = max;
	}

	public void setRandom(Random rnd) {
		random = rnd;
	}

	public float pickRandom() {
		current = MathUtils.random(random, min, max);
		return current;
	}

	public float getCurrent() {
		return current;
	}

	@Override
	public String toString() {
		return "FloatRange: " + min + " -> " + max;
	}

	public FloatRange copy() {
		FloatRange range = new FloatRange(min, max);
		range.current = current;
		range.random = random;
		return range;
	}

	public boolean isValueInRange(float val) {
		return val >= min && val <= max;
	}
}
