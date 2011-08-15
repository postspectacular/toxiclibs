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
 * Bezier curve interpolation with configurable coefficients. The curve
 * parameters need to be normalized offsets relative to the start and end values
 * passed to the {@link #interpolate(float, float, float)} method, but can
 * exceed the normal 0 .. 1.0 interval. Use symmetrical offsets to create a
 * symmetrical curve, e.g. this will create a curve with 2 dips reaching the
 * minimum and maximum values at 25% and 75% of the interval...
 * 
 * <p>
 * <code>BezierInterpolation b=new BezierInterpolation(3,-3);</code>
 * </p>
 * 
 * The curve will be a straight line with this configuration:
 * 
 * <p>
 * <code>BezierInterpolation b=new BezierInterpolation(1f/3,-1f/3);</code>
 * </p>
 */
public class BezierInterpolation implements InterpolateStrategy {

    public float c1;
    public float c2;

    public BezierInterpolation(float h1, float h2) {
        this.c1 = h1;
        this.c2 = h2;
    }

    public double interpolate(double a, double b, double t) {
        double tSquared = t * t;
        double invT = 1.0f - t;
        double invTSquared = invT * invT;
        return (a * invTSquared * invT)
                + (3 * (c1 * (b - a) + a) * t * invTSquared)
                + (3 * (c2 * (b - a) + b) * tSquared * invT)
                + (b * tSquared * t);
    }

    public float interpolate(float a, float b, float t) {
        float tSquared = t * t;
        float invT = 1.0f - t;
        float invTSquared = invT * invT;
        return (a * invTSquared * invT)
                + (3 * (c1 * (b - a) + a) * t * invTSquared)
                + (3 * (c2 * (b - a) + b) * tSquared * invT)
                + (b * tSquared * t);
    }

    public void setCoefficients(float a, float b) {
        c1 = a;
        c2 = b;
    }

}
