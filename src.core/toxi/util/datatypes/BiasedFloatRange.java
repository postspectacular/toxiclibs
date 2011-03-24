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

public class BiasedFloatRange extends FloatRange {

    @XmlAttribute
    protected float bias = 0.5f;

    @XmlAttribute
    protected float standardDeviation = bias * 0.5f;

    public BiasedFloatRange() {
        this(0, 1, 0.5f, 1);
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
        setBias(bias);
        setStandardDeviation(sd);
    }

    public BiasedFloatRange copy() {
        BiasedFloatRange r = new BiasedFloatRange(min, max, bias,
                standardDeviation * 2);
        r.currValue = currValue;
        return r;
    }

    /**
     * @return the bias
     */
    public float getBias() {
        return bias;
    }

    /**
     * @return the standardDeviation
     */
    public float getStandardDeviation() {
        return standardDeviation;
    }

    @Override
    public float pickRandom() {
        do {
            currValue = (float) (random.nextGaussian() * standardDeviation * (max - min))
                    + bias;
        } while (currValue < min || currValue >= max);
        return currValue;
    }

    /**
     * @param bias
     *            the bias to set
     */
    public void setBias(float bias) {
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
        return "BiasedFloatRange: " + min + " -> " + max + " bias: " + bias
                + " q: " + standardDeviation;
    }
}
