package toxi.util.datatypes;

import toxi.math.MathUtils;

public class BiasedIntegerRange extends IntegerRange {

	public int bias;
	public float standardDeviation;

	/**
	 * @param min
	 *            min value (inclusive)
	 * @param max
	 *            max value (inclusive)
	 * @param bias
	 *            bias value (can be outside the min/max range, but values will
	 *            be clipped)
	 * @param sd
	 *            standard deviation (if bias at range mean sd=1.0, the entire
	 *            range will be covered)
	 */
	public BiasedIntegerRange(int min, int max, int bias, float sd) {
		super(min, max);
		this.bias = bias;
		this.standardDeviation = sd * 0.5f;
	}

	@Override
	public int pickRandom() {
		current = (int) (random.nextGaussian() * standardDeviation * (max - min))
				+ bias;
		current = MathUtils.clip(current, min, max);
		return current;
	}

	@Override
	public String toString() {
		return "BiasedIntegerRange: " + min + " -> " + max + " bias: " + bias
				+ " q: " + standardDeviation;
	}
}
