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

package toxi.geom;

/**
 * An immutable origin + axis in 3D-Space.
 */
public class AxisD3D {

    /**
     * Creates a new x-Axis3D object from the world origin.
     */
    public static final AxisD3D xAxis() {
        return new AxisD3D(VecD3D.X_AXIS);
    }

    /**
     * Creates a new y-Axis3D object from the world origin.
     */
    public static final AxisD3D yAxis() {
        return new AxisD3D(VecD3D.Y_AXIS);
    }

    /**
     * Creates a new z-Axis3D object from the world origin.
     */
    public static final AxisD3D zAxis() {
        return new AxisD3D(VecD3D.Z_AXIS);
    }

    public final ReadonlyVecD3D origin;
    public final ReadonlyVecD3D dir;

    /**
     * Creates a new z-Axis3D object from the world origin.
     */
    public AxisD3D() {
        this(VecD3D.Z_AXIS);
    }

    public AxisD3D(double x, double y, double z) {
        this(new VecD3D(x, y, z));
    }

    public AxisD3D(RayD3D ray) {
        this(ray, ray.getDirection());
    }

    /**
     * Creates a new Axis3D from the world origin in the given direction.
     * 
     * @param dir
     *            direction vector
     */
    public AxisD3D(ReadonlyVecD3D dir) {
        this(new VecD3D(), dir);
    }

    /**
     * Creates a new Axis3D from the given origin and direction.
     * 
     * @param o
     *            origin
     * @param dir
     *            direction
     */
    public AxisD3D(ReadonlyVecD3D o, ReadonlyVecD3D dir) {
        this.origin = o;
        this.dir = dir.getNormalized();
    }
}
