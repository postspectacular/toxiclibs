package toxi.util.datatypes;

import javax.xml.bind.annotation.XmlAttribute;

import toxi.math.MathUtils;

public class BiasedFloatRange extends FloatRange {

    @XmlAttribute
    protected float bias = 0.5f;

    @XmlAttribute
    protected float standardDeviation = bias * 0.5f;

    public BiasedFloatRange() {
        this(0, 1, 0.5f, 1);
    }

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
        setBias(bias);
        setStandardDeviation(sd);
    }

    public BiasedFloatRange copy() {
        BiasedFloatRange r =
                new BiasedFloatRange(min, max, bias, standardDeviation * 2);
        r.currValue = currValue;
        return r;
    }

    /**
     * @return the bias
     */
    public float getBias() {
        return bias;
    }

    /**
     * @return the standardDeviation
     */
    public float getStandardDeviation() {
        return standardDeviation;
    }

    @Override
    public float pickRandom() {
        do {
            currValue =
                    (float) (random.nextGaussian() * standardDeviation * (max - min))
                            + bias;
        } while (currValue < min || currValue >= max);
        return currValue;
    }

    /**
     * @param bias
     *            the bias to set
     */
    public void setBias(float bias) {
        this.bias = MathUtils.clip(bias, min, max);
    }

    /**
     * @param sd
     *            the standardDeviation to set
     */
    public void setStandardDeviation(float sd) {
        this.standardDeviation = MathUtils.clip(sd, 0, 1.0f) * 0.5f;
    }

    @Override
    public String toString() {
        return "BiasedFloatRange: " + min + " -> " + max + " bias: " + bias
                + " q: " + standardDeviation;
    }
}
