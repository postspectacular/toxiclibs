package toxi.test.math;

import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.TreeSet;

public class NonLinearScaleMap {

    public class Sample implements Comparable<Sample> {

        public final double x, y;

        public Sample(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public int compareTo(Sample b) {
            return (int) Math.signum(x - b.x);
        }
    }

    private TreeSet<Sample> samples;

    private double rangeMin = Float.MAX_VALUE;
    private double rangeMax = Float.MIN_VALUE;

    public NonLinearScaleMap() {
        samples = new TreeSet<Sample>();
    }

    public NonLinearScaleMap addSample(double x, double y) {
        samples.add(new Sample(x, y));
        rangeMin = Math.min(y, rangeMin);
        rangeMax = Math.max(y, rangeMax);
        return this;
    }

    public NavigableSet<Sample> getSamples() {
        return samples;
    }

    public double map(float x) {
        Sample t = new Sample(x, 0);
        SortedSet<Sample> aset = samples.headSet(t);
        SortedSet<Sample> bset = samples.tailSet(t);
        if (aset.isEmpty()) {
            return bset.first().y;
        } else {
            if (bset.isEmpty()) {
                return aset.last().y;
            } else {
                Sample a = aset.last();
                Sample b = bset.first();
                return a.y + (b.y - a.y) * (x - a.x) / (b.x - a.x);
            }
        }
    }
}