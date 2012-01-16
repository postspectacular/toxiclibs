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

import java.util.List;
import java.util.Random;

import javax.xml.bind.annotation.XmlAttribute;

import toxi.math.MathUtils;

public class IntegerRange {

    public static IntegerRange fromSamples(int... samples) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int s : samples) {
            min = MathUtils.min(min, s);
            max = MathUtils.max(max, s);
        }
        return new IntegerRange(min, max);
    }

    public static IntegerRange fromSamples(List<Integer> samples) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int s : samples) {
            min = MathUtils.min(min, s);
            max = MathUtils.max(max, s);
        }
        return new IntegerRange(min, max);
    }

    @XmlAttribute
    public int min, max;

    @XmlAttribute(name = "default")
    public int currValue;

    protected Random random = new Random();

    public IntegerRange() {
        this(0, 100);
    }

    public IntegerRange(int min, int max) {
        // swap if necessary...
        if (min > max) {
            max ^= min;
            min ^= max;
            max ^= min;
        }
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

    /**
     * Returns the value at the normalized position
     * <code>(0.0 = min ... 1.0 = max-1)</code> within the range. Since the max
     * value is exclusive, the value returned for position 1.0 is the range max
     * value minus 1. Also note the given position is not being clipped to the
     * 0.0-1.0 interval, so when passing in values outside that interval will
     * produce out-of-range values too.
     * 
     * @param perc
     * @return value within the range
     */
    public final int getAt(float perc) {
        return (int) (min + (max - min - 1) * perc);
    }

    public final int getCurrent() {
        return currValue;
    }

    public final int getMedian() {
        return (min + max) / 2;
    }

    public final int getRange() {
        return max - min;
    }

    public final boolean isValueInRange(int val) {
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

    public Integer[] toArray() {
        Integer[] range = new Integer[max - min];
        for (int i = 0, j = min; j < max; i++, j++) {
            range[i] = j;
        }
        return range;
    }

    @Override
    public String toString() {
        return "IntegerRange: " + min + " -> " + max;
    }
}
