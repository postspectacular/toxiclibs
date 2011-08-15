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
 * Implementation of the circular interpolation function.
 * 
 * i = a-(b-a) * (sqrt(1 - (1 - f) * (1 - f) ))
 */
public class CircularInterpolation implements InterpolateStrategy {

    protected boolean isFlipped;

    public CircularInterpolation() {
        this(false);
    }

    /**
     * The interpolation slope can be flipped to have its steepest ascent
     * towards the end value, rather than at the beginning in the default
     * configuration.
     * 
     * @param isFlipped
     *            true, if slope is inverted
     */
    public CircularInterpolation(boolean isFlipped) {
        this.isFlipped = isFlipped;
    }

    public double interpolate(double a, double b, double f) {
        if (isFlipped) {
            return a - (b - a) * (Math.sqrt(1 - f * f) - 1);
        } else {
            f = 1 - f;
            return a + (b - a) * (Math.sqrt(1 - f * f));
        }
    }

    public float interpolate(float a, float b, float f) {
        if (isFlipped) {
            return a - (b - a) * ((float) Math.sqrt(1 - f * f) - 1);
        } else {
            f = 1 - f;
            return a + (b - a) * ((float) Math.sqrt(1 - f * f));
        }
    }

    public void setFlipped(boolean isFlipped) {
        this.isFlipped = isFlipped;
    }
}
