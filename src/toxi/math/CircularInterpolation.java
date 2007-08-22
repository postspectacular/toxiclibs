package toxi.math;

/**
 * Implementation of the circular interpolation function.
 * i = a-(b-a) * (sqrt(1 - f * f) - 1)
 * 
 * @author kschmidt
 * 
 */
public class CircularInterpolation implements InterpolateStrategy {

	public float interpolate(float a, float b, float f) {
		return a - (b - a) * ((float) Math.sqrt(1 - f * f) - 1);
	}
}
