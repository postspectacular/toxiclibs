package toxi.math;

/**
 * Delivers a number of decimated/stepped values for a given interval. E.g. by
 * using 5 steps the interpolation factor is decimated to: 0, 20, 40, 60, 80 and
 * 100%. By default {@link LinearInterpolation} is used, however any other
 * {@link InterpolateStrategy} can be specified via the constructor.
 */
public class DecimatedInterpolation implements InterpolateStrategy {

    public int numSteps;
    public InterpolateStrategy strategy;

    public DecimatedInterpolation(int steps) {
        this(steps, new LinearInterpolation());
    }

    public DecimatedInterpolation(int steps, InterpolateStrategy strategy) {
        this.numSteps = steps;
        this.strategy = strategy;
    }

    public float interpolate(float a, float b, float f) {
        float fd = (int) (f * numSteps) / (float) numSteps;
        return strategy.interpolate(a, b, fd);
    }

}
