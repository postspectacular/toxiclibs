package toxi.math;

/**
 * Bezier curve interpolation with configurable coefficients. The curve
 * parameters need to be normalized offsets relative to the start and end values
 * passed to the {@link #interpolate(float, float, float)} method, but can
 * exceed the normal 0 .. 1.0 interval. Use symmetrical offsets to create a
 * symmetrical curve, e.g. this will create a curve with 2 dips reaching the
 * minimum and maximum values at 25% and 75% of the interval...
 * 
 * <p>
 * <code>BezierInterpolation b=new BezierInterpolation(3,-3);</code>
 * </p>
 * 
 * The curve will be a straight line with this configuration:
 * 
 * <p>
 * <code>BezierInterpolation b=new BezierInterpolation(1f/3,-1f/3);</code>
 * </p>
 */
public class BezierInterpolation implements InterpolateStrategy {

    public float c1;
    public float c2;

    public BezierInterpolation(float h1, float h2) {
        this.c1 = h1;
        this.c2 = h2;
    }

    public float interpolate(float a, float b, float t) {
        float tSquared = t * t;
        float invT = 1.0f - t;
        float invTSquared = invT * invT;
        return (a * invTSquared * invT)
                + (3 * (c1 * (b - a) + a) * t * invTSquared)
                + (3 * (c2 * (b - a) + b) * tSquared * invT)
                + (b * tSquared * t);
    }

    public void setCoefficients(float a, float b) {
        c1 = a;
        c2 = b;
    }

}
