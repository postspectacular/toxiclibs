package toxi.math;

/**
 * Exponential curve interpolation with adjustable exponent. Use exp in the
 * following ranges to achieve these effects:
 * <ul>
 * <li>0.0 &lt; x &lt; 1.0 : ease in (steep changes towards b)</li>
 * <li>1.0 : same as {@link LinearInterpolation}</li>
 * <li>&gt; 1.0 : ease-out (steep changes from a)</li>
 * </ul>
 */
public class ExponentialInterpolation implements InterpolateStrategy {

    private float exponent;

    /**
     * Default constructor uses square parabola (exp=2)
     */
    public ExponentialInterpolation() {
        this(2);
    }

    /**
     * @param exp
     *            curve exponent
     */
    public ExponentialInterpolation(float exp) {
        this.exponent = exp;
    }

    public float interpolate(float a, float b, float f) {
        return a + (b - a) * (float) Math.pow(f, exponent);
    }

}
