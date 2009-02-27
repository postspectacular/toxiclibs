package toxi.util.datatypes;

import toxi.math.MathUtils;

public class BiasedFloatRange extends FloatRange {

	public float bias;
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
	public BiasedFloatRange(float min, float max, float bias, float sd) {
		super(min, max);
		this.bias = bias;
		this.standardDeviation = sd * 0.5f;
	}

	@Override
	public float pickRandom() {
		current = (float) (random.nextGaussian() * standardDeviation * (max - min))
				+ bias;
		current = MathUtils.clip(current, min, max);
		return current;
	}

	@Override
	public String toString() {
		return "BiasedFloatRange: " + min + " -> " + max + " bias: " + bias
				+ " q: " + standardDeviation;
	}
}
