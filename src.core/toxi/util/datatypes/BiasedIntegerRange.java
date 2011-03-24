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

    public BiasedIntegerRange copy() {
        BiasedIntegerRange r = new BiasedIntegerRange(min, max, bias,
                standardDeviation * 2);
        r.currValue = currValue;
        return r;
    }

    /**
     * @return the bias
     */
    public int getBias() {
        return bias;
    }

    /**
     * @return the standardDeviation
     */
    public float getStandardDeviation() {
        return standardDeviation;
    }

    @Override
    public int pickRandom() {
        do {
            currValue = (int) (random.nextGaussian() * standardDeviation * (max - min))
                    + bias;
        } while (currValue < min || currValue >= max);
        return currValue;
    }

    /**
     * @param bias
     *            the bias to set
     */
    public void setBias(int bias) {
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
        return "BiasedIntegerRange: " + min + " -> " + max + " bias: " + bias
                + " q: " + standardDeviation;
    }
}
