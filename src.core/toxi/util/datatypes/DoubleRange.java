/*
 * Copyright (c) 2006-2011 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

package toxi.util.datatypes;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.xml.bind.annotation.XmlAttribute;

import toxi.math.MathUtils;

public class DoubleRange {

    public static DoubleRange fromSamples(List<Double> samples) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (double s : samples) {
            min = MathUtils.min(min, s);
            max = MathUtils.max(max, s);
        }
        return new DoubleRange(min, max);
    }

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