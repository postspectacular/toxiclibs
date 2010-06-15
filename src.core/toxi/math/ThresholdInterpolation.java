package toxi.math;

/**
 * Defines a single step/threshold function which returns the min value for all
 * factors &lt; threshold and the max value for all others.
 */
public class ThresholdInterpolation implements InterpolateStrategy {

    public float threshold;

    public ThresholdInterpolation(float threshold) {
        this.threshold = threshold;
    }

    public float interpolate(float a, float b, float f) {
        return f < threshold ? a : b;
    }

}
