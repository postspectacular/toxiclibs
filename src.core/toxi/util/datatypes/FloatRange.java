/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
 *
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

public class FloatRange {

    public static FloatRange fromSamples(float... samples) {
        float min = Float.MAX_VALUE;
        float max = Float.MIN_VALUE;
        for (float s : samples) {
            min = MathUtils.min(min, s);
            max = MathUtils.max(max, s);
        }
        return new FloatRange(min, max);
    }

    public static FloatRange fromSamples(List<Float> samples) {
        float min = Float.MAX_VALUE;
        float max = Float.MIN_VALUE;
        for (float s : samples) {
            min = MathUtils.min(min, s);
            max = MathUtils.max(max, s);
        }
        return new FloatRange(min, max);
    }

    @XmlAttribute
    public float min, max;

    @XmlAttribute(name = "default")
    public float currValue;

    protected Random random = new Random();

    public FloatRange() {
        this(0f, 1f);
    }

    public FloatRange(float min, float max) {
        // swap if necessary...
        if (min > max) {
            float t = max;
            max = min;
            min = t;
        }
        this.min = min;
        this.max = max;
        this.currValue = min;
    }

    public float adjustCurrentBy(float val) {
        return setCurrent(currValue + val);
    }

    public FloatRange copy() {
        FloatRange range = new FloatRange(min, max);
        range.currValue = currValue;
        range.random = random;
        return range;
    }

    /**
     * Returns the value at the normalized position <code>(0.0 = min ... 1.0 =
     * max-EPS)</code> within the range. Since the max value is exclusive, the
     * value returned for position 1.0 is the range max value minus
     * {@link MathUtils#EPS}. Also note the given position is not being clipped
     * to the 0.0-1.0 interval, so when passing in values outside that interval
     * will produce out-of-range values too.
     * 
     * @param perc
     * @return value within the range
     */
    public final float getAt(float perc) {
        return min + (max - min - MathUtils.EPS) * perc;
    }

    public float getCurrent() {
        return currValue;
    }

    public float getMedian() {
        return (min + max) * 0.5f;
    }

    public float getRange() {
        return max - min;
    }

    public boolean isValueInRange(float val) {
        return val >= min && val <= max;
    }

    public float pickRandom() {
        currValue = MathUtils.random(random, min, max);
        return currValue;
    }

    public FloatRange seed(long seed) {
        random.setSeed(seed);
        return this;
    }

    public float setCurrent(float val) {
        currValue = MathUtils.clip(val, min, max);
        return currValue;
    }

    public FloatRange setRandom(Random rnd) {
        random = rnd;
        return this;
    }

    public Float[] toArray(float step) {
        List<Float> range = new LinkedList<Float>();
        double v = min;
        while (v < max) {
            range.add((float) v);
            v += step;
        }
        return range.toArray(new Float[0]);
    }

    @Override
    public String toString() {
        return "FloatRange: " + min + " -> " + max;
    }
}
