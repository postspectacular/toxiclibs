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

package toxi.sim.dla;

import java.util.Comparator;

import toxi.geom.Line3D;
import toxi.geom.Vec3D;

/**
 * This comparator sorts segments based on their mid-point's distance to a given
 * origin point. This creates a circular growth. The order can be reversed via a
 * constructor flag and then causes the DLA system to grow from the outside
 * towards the given origin point.
 */
public class RadialDistanceOrder implements Comparator<Line3D> {

    public Vec3D origin;
    public boolean isFlipped;

    public RadialDistanceOrder() {
        this(new Vec3D(), false);
    }

    public RadialDistanceOrder(Vec3D origin, boolean isFlipped) {
        this.origin = origin;
        this.isFlipped = isFlipped;
    }

    public int compare(Line3D a, Line3D b) {
        float da = origin.sub(a.getMidPoint()).magSquared();
        float db = origin.sub(b.getMidPoint()).magSquared();
        if (isFlipped) {
            da *= -1;
            db *= -1;
        }
        if (da < db) {
            return -1;
        }
        if (da > db) {
            return 1;
        }
        return 0;
    }
}