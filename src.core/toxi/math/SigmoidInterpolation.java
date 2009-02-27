package toxi.math;


/**
 * Implements the sigmoid interpolation function with adjustable curve sharpness
 * 
 * @author kschmidt
 */
public class SigmoidInterpolation implements InterpolateStrategy {

	private float sharpness;

	private float sharpPremult;

	public SigmoidInterpolation() {
		this(1f);
	}

	public SigmoidInterpolation(float s) {
		setSharpness(s);
	}

	private void setSharpness(float s) {
		sharpness = s;
		sharpPremult = 5 * s;
	}

	public float getSharpness() {
		return sharpness;
	}
	
	public float interpolate(float a, float b, float f) {
		f = (f * 2 - 1) * sharpPremult;
		f = (float) (1.0f / (1.0f + Math.exp(-f)));
		return a + (b - a) * f;
	}
}
