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
 * This class defines an origin and set of axis vectors for a 3D cartesian
 * coordinate system.
 */
public class Origin3D {

    public final ReadonlyVec3D origin;
    public final ReadonlyVec3D xAxis, yAxis, zAxis;

    /**
     * Creates a new origin at the world origin using the standard XYZ axes
     */
    public Origin3D() {
        this(new Vec3D());
    }

    public Origin3D(float x, float y, float z) {
        this(new Vec3D(x, y, z));
    }

    public Origin3D(Matrix4x4 mat) {
        this.origin = mat.applyToSelf(new Vec3D());
        this.xAxis = mat.applyTo(Vec3D.X_AXIS).subSelf(origin).normalize();
        this.yAxis = mat.applyTo(Vec3D.Y_AXIS).subSelf(origin).normalize();
        zAxis = xAxis.crossInto(yAxis, new Vec3D());
    }

    /**
     * Creates a new origin at the given origin using the standard XYZ axes
     * 
     * @param o
     *            origin
     */
    public Origin3D(Vec3D o) {
        origin = o;
        xAxis = Vec3D.X_AXIS;
        yAxis = Vec3D.Y_AXIS;
        zAxis = Vec3D.Z_AXIS;
    }

    /**
     * @param o
     *            origin of the soordinate system
     * @param x
     *            x-direction of the coordinate system
     * @param y
     *            y-direction of the coordinate system
     * @throws IllegalArgumentException
     *             if x and y vectors are not orthogonal
     */
    public Origin3D(Vec3D o, Vec3D x, Vec3D y) throws IllegalArgumentException {
        origin = o;
        xAxis = x;
        yAxis = y;
        xAxis.getNormalized();
        yAxis.getNormalized();
        if (Math.abs(xAxis.dot(yAxis)) > 0.0001) {
            throw new IllegalArgumentException("Axis vectors aren't orthogonal");
        }
        zAxis = xAxis.crossInto(yAxis, new Vec3D());
    }
}
