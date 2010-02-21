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
        currValue = MathUtils.clip(currValue + val, min, max);
        return currValue;
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
        currValue = MathUtils.random(min, max);
        return currValue;
    }

    public int setCurrent(int val) {
        currValue = MathUtils.clip(val, min, max);
        return currValue;
    }

    public void setRandom(Random rnd) {
        random = rnd;
    }

    @Override
    public String toString() {
        return "IntegerRange: " + min + " -> " + max;
    }
}
