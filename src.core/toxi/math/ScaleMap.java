package toxi.math;

import toxi.util.datatypes.DoubleRange;

/**
 * This class maps values from one interval into another. By default the mapping
 * is using linear projection, but can be changed by using alternative
 * {@link toxi.math.InterpolateStrategy} implementations to achieve a
 * non-regular mapping.
 */
public class ScaleMap {

	/**
	 * The actual mapping behaviour function.
	 * 
	 * @see toxi.math.InterpolateStrategy
	 */
	protected InterpolateStrategy mapFunction = new LinearInterpolation();

	protected double interval;
	protected double mapRange;

	protected DoubleRange in, out;

	/**
	 * Creates a new instance to map values between the 2 number ranges
	 * specified. By default linear projection is used.
	 * 
	 * @param minIn
	 * @param maxIn
	 * @param minOut
	 * @param maxOut
	 */
	public ScaleMap(double minIn, double maxIn, double minOut, double maxOut) {
		setInputRange(minIn, maxIn);
		setOutputRange(minOut, maxOut);
	}

	/**
	 * Computes mapped value in the target interval and ensures the input value
	 * is clipped to source interval.
	 * 
	 * @param val
	 * @return mapped value
	 */
	public double getClippedValueFor(double val) {
		float t = MathUtils.clipNormalized((float) ((val - in.min) / interval));
		return mapFunction.interpolate(0, (float) mapRange, t) + out.min;
	}

	/**
	 * Computes mapped value in the target interval. Does check if input value
	 * is outside the input range.
	 * 
	 * @param val
	 * @return mapped value
	 */
	public double getMappedValueFor(double val) {
		float t = (float) ((val - in.min) / interval);
		return mapFunction.interpolate(0, (float) mapRange, t) + out.min;
	}

	/**
	 * Sets new minimum & maximum values for the input range
	 * 
	 * @param min
	 * @param max
	 */
	public void setInputRange(double min, double max) {
		in = new DoubleRange(min, max);
		interval = max - min;
	}

	/**
	 * Overrides the mapping function used for the scale conversion.
	 * 
	 * @param func
	 *            interpolate strategy implementation
	 */
	public void setMapFunction(InterpolateStrategy func) {
		mapFunction = func;
	}

	/**
	 * Sets new minimum & maximum values for the output/target range
	 * 
	 * @param min
	 *            new min output value
	 * @param max
	 *            new max output value
	 */
	public void setOutputRange(double min, double max) {
		out = new DoubleRange(min, max);
		mapRange = max - min;
	}
}
