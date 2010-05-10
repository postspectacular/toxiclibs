package toxi.util.datatypes;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.xml.bind.annotation.XmlAttribute;

import toxi.math.MathUtils;

public class DoubleRange {

    @XmlAttribute
    public double min, max;

    @XmlAttribute(name = "default")
    public double currValue;

    protected Random random = new Random();

    public DoubleRange() {
        this(0d, 1d);
    }

    public DoubleRange(double min, double max) {
        this.min = min;
        this.max = max;
        this.currValue = min;
    }

    public double adjustCurrentBy(double val) {
        return setCurrent(currValue + val);
    }

    public DoubleRange copy() {
        DoubleRange range = new DoubleRange(min, max);
        range.currValue = currValue;
        range.random = random;
        return range;
    }

    public double getCurrent() {
        return currValue;
    }

    public double getMedian() {
        return (min + max) * 0.5f;
    }

    public boolean isValueInRange(float val) {
        return val >= min && val <= max;
    }

    public double pickRandom() {
        currValue = MathUtils.random(random, (float) min, (float) max);
        return currValue;
    }

    public DoubleRange seed(long seed) {
        random.setSeed(seed);
        return this;
    }

    public double setCurrent(double val) {
        currValue = MathUtils.clip(val, min, max);
        return currValue;
    }

    public DoubleRange setRandom(Random rnd) {
        random = rnd;
        return this;
    }

    public Double[] toArray(double step) {
        List<Double> range = new LinkedList<Double>();
        double v = min;
        while (v < max) {
            range.add(v);
            v += step;
        }
        return range.toArray(new Double[0]);
    }

    @Override
    public String toString() {
        return "DoubleRange: " + min + " -> " + max;
    }
}