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

package toxi.math;

import toxi.util.datatypes.DoubleRange;

/**
 * This class maps values from one interval into another. By default the mapping
 * is using linear projection, but can be changed by using alternative
 * {@link toxi.math.InterpolateStrategy} implementations to achieve a
 * non-regular mapping.
 */
public class ScaleMap {

    /**
     * The actual mapping behaviour function.
     * 
     * @see toxi.math.InterpolateStrategy
     */
    protected InterpolateStrategy mapFunction = new LinearInterpolation();

    protected double interval;
    protected double mapRange;

    protected DoubleRange in, out;

    /**
     * Creates a new instance to map values between the 2 number ranges
     * specified. By default linear projection is used.
     * 
     * @param minIn
     * @param maxIn
     * @param minOut
     * @param maxOut
     */
    public ScaleMap(double minIn, double maxIn, double minOut, double maxOut) {
        setInputRange(minIn, maxIn);
        setOutputRange(minOut, maxOut);
    }

    /**
     * Computes mapped value in the target interval and ensures the input value
     * is clipped to source interval.
     * 
     * @param val
     * @return mapped value
     */
    public double getClippedValueFor(double val) {
        double t = MathUtils.clipNormalized((val - in.min) / interval);
        if (Double.isNaN(t)) {
            t = 0;
        }
        return mapFunction.interpolate(out.min, out.max, t);
    }

    /**
     * @return the middle value of the input range.
     */
    public double getInputMedian() {
        return (in.min + in.max) * 0.5;
    }

    /**
     * @return the in
     */
    public DoubleRange getInputRange() {
        return in;
    }

    /**
     * @return the mapped middle value of the output range. Depending on the
     *         mapping function used, this value might be different to the one
     *         returned by {@link #getOutputMedian()}.
     */
    public double getMappedMedian() {
        return getMappedValueFor(0.5);
    }

    /**
     * Computes mapped value in the target interval. Does check if input value
     * is outside the input range.
     * 
     * @param val
     * @return mapped value
     */
    public double getMappedValueFor(double val) {
        double t = ((val - in.min) / interval);
        if (Double.isNaN(t)) {
            t = 0;
        }
        return mapFunction.interpolate(out.min, out.max, t);
    }

    /**
     * @return the middle value of the output range
     */
    public double getOutputMedian() {
        return (out.min + out.max) * 0.5;
    }

    /**
     * @return the output range
     */
    public DoubleRange getOutputRange() {
        return out;
    }

    /**
     * Sets new minimum & maximum values for the input range
     * 
     * @param min
     * @param max
     */
    public void setInputRange(double min, double max) {
        in = new DoubleRange(min, max);
        interval = max - min;
    }

    /**
     * Overrides the mapping function used for the scale conversion. By default
     * a linear mapping is used: {@link LinearInterpolation}.
     * 
     * @param func
     *            interpolate strategy implementation
     */
    public void setMapFunction(InterpolateStrategy func) {
        mapFunction = func;
    }

    /**
     * Sets new minimum & maximum values for the output/target range
     * 
     * @param min
     *            new min output value
     * @param max
     *            new max output value
     */
    public void setOutputRange(double min, double max) {
        out = new DoubleRange(min, max);
        mapRange = max - min;
    }
}
