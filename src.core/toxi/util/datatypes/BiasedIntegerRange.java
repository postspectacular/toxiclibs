package toxi.util.datatypes;

import javax.xml.bind.annotation.XmlAttribute;

import toxi.math.MathUtils;

public class BiasedIntegerRange extends IntegerRange {

    @XmlAttribute
    public int bias;

    @XmlAttribute
    public float standardDeviation;

    public BiasedIntegerRange() {
        this(0, 100, 50, 1.0f);
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
     *            standard deviation (if bias at range means sd=1.0, the entire
     *            range will be covered)
     */
    public BiasedIntegerRange(int min, int max, int bias, float sd) {
        super(min, max);
        this.bias = bias;
        this.standardDeviation = sd * 0.5f;
    }

    @Override
    public int pickRandom() {
        currValue =
                (int) (random.nextGaussian() * standardDeviation * (max - min))
                        + bias;
        currValue = MathUtils.clip(currValue, min, max);
        return currValue;
    }

    @Override
    public String toString() {
        return "BiasedIntegerRange: " + min + " -> " + max + " bias: " + bias
                + " q: " + standardDeviation;
    }
}
