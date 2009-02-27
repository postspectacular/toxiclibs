package toxi.math;

/**
 * Defines a generic function to interpolate 2 float values.
 * 
 * @author toxi
 * 
 */
public interface InterpolateStrategy {

	/**
	 * Implements an interpolation equation.
	 * 
	 * @param a
	 *            current value
	 * @param b
	 *            target value
	 * @param f
	 *            normalized interpolation factor (0.0 .. 1.0)
	 * @return interpolated value
	 */
	public float interpolate(float a, float b, float f);
}
