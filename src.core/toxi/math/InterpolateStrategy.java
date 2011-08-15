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
 * Defines a generic function to interpolate 2 float values.
 */
public interface InterpolateStrategy {

    /**
     * Implements an interpolation equation using double precision values.
     * 
     * @param a
     *            current value
     * @param b
     *            target value
     * @param f
     *            normalized interpolation factor (0.0 .. 1.0)
     * @return interpolated value
     */
    public double interpolate(double a, double b, double f);

    /**
     * Implements an interpolation equation using float values.
     * 
     * @param a
     *            current value
     * @param b
     *            target value
     * @param f
     *            normalized interpolation factor (0.0 .. 1.0)
     * @return interpolated value
     */
    public float interpolate(float a, float b, float f);
}
