package toxi.util.datatypes;

import javax.xml.bind.annotation.XmlAttribute;

import toxi.math.MathUtils;

public class BiasedFloatRange extends FloatRange {

    @XmlAttribute
    public float bias = 0.5f;

    @XmlAttribute
    public float standardDeviation = bias * 0.5f;

    public BiasedFloatRange() {
        this(0, 1, 0.5f, 0.5f);
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
        this.bias = bias;
        this.standardDeviation = sd * 0.5f;
    }

    @Override
    public float pickRandom() {
        currValue =
                (float) (random.nextGaussian() * standardDeviation * (max - min))
                        + bias;
        currValue = MathUtils.clip(currValue, min, max);
        return currValue;
    }

    @Override
    public String toString() {
        return "BiasedFloatRange: " + min + " -> " + max + " bias: " + bias
                + " q: " + standardDeviation;
    }
}
