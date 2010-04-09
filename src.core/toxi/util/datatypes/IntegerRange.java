package toxi.util.datatypes;

import java.util.Random;

import javax.xml.bind.annotation.XmlAttribute;

import toxi.math.MathUtils;

public class IntegerRange {

    @XmlAttribute
    public int min, max;

    @XmlAttribute(name = "default")
    public int currValue;

    protected Random random = new Random();

    public IntegerRange() {
        this(0, 100);
    }

    public IntegerRange(int min, int max) {
        this.min = min;
        this.max = max;
        this.currValue = min;
    }

    public int adjustCurrentBy(int val) {
        return setCurrent(currValue + val);
    }

    public IntegerRange copy() {
        IntegerRange range = new IntegerRange(min, max);
        range.currValue = currValue;
        range.random = random;
        return range;
    }

    public int getCurrent() {
        return currValue;
    }

    public int getMedian() {
        return (min + max) / 2;
    }

    public boolean isValueInRange(int val) {
        return val >= min && val < max;
    }

    public int pickRandom() {
        currValue = MathUtils.random(random, min, max);
        return currValue;
    }

    public IntegerRange seed(long seed) {
        random.setSeed(seed);
        return this;
    }

    public int setCurrent(int val) {
        currValue = MathUtils.clip(val, min, max);
        return currValue;
    }

    public IntegerRange setRandom(Random rnd) {
        random = rnd;
        return this;
    }

    @Override
    public String toString() {
        return "IntegerRange: " + min + " -> " + max;
    }
}
