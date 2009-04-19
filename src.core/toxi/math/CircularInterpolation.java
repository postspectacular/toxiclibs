package toxi.math;

/**
 * Implementation of the circular interpolation function.
 * 
 * i = a-(b-a) * (sqrt(1 - (1 - f) * (1 - f) ))
 * 
 * @author kschmidt
 * 
 */
public class CircularInterpolation implements InterpolateStrategy {

	protected boolean isFlipped;

	public CircularInterpolation() {
		this(false);
	}

	/**
	 * The interpolation slope can be flipped to have its steepest ascent
	 * towards the end value, rather than at the beginning in the default
	 * configuration.
	 * 
	 * @param isFlipped
	 *            true, if slope is inverted
	 */
	public CircularInterpolation(boolean isFlipped) {
		this.isFlipped = isFlipped;
	}

	public float interpolate(float a, float b, float f) {
		if (isFlipped) {
			return a - (b - a) * ((float) Math.sqrt(1 - f * f) - 1);
		}
		else {
			f = 1 - f;
			return a + (b - a) * ((float) Math.sqrt(1 - f * f));
		}
	}

	public void setFlipped(boolean isFlipped) {
		this.isFlipped = isFlipped;
	}
}
