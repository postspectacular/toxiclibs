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

/**
 * Delivers a number of decimated/stepped values for a given interval. E.g. by
 * using 5 steps the interpolation factor is decimated to: 0, 20, 40, 60, 80 and
 * 100%. By default {@link LinearInterpolation} is used, however any other
 * {@link InterpolateStrategy} can be specified via the constructor.
 */
public class DecimatedInterpolation implements InterpolateStrategy {

    public int numSteps;
    public InterpolateStrategy strategy;

    public DecimatedInterpolation(int steps) {
        this(steps, new LinearInterpolation());
    }

    public DecimatedInterpolation(int steps, InterpolateStrategy strategy) {
        this.numSteps = steps;
        this.strategy = strategy;
    }

    public double interpolate(double a, double b, double f) {
        double fd = (int) (f * numSteps) / (double) numSteps;
        return strategy.interpolate(a, b, fd);
    }

    public float interpolate(float a, float b, float f) {
        float fd = (int) (f * numSteps) / (float) numSteps;
        return strategy.interpolate(a, b, fd);
    }

}
