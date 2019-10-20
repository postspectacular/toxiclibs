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

import toxi.geom.VecD3D.AxisD;

/**
 * This class defines an origin and set of axis vectors for a 3D cartesian
 * coordinate system.
 */
public class OriginD3D {

    public ReadonlyVecD3D origin;
    public ReadonlyVecD3D xAxis, yAxis, zAxis;

    /**
     * Creates a new origin at the world origin using the standard XYZ axes
     */
    public OriginD3D() {
        this(new VecD3D());
    }

    public OriginD3D(double x, double y, double z) {
        this(new VecD3D(x, y, z));
    }

    public OriginD3D(Matrix4x4 mat) {
        this.origin = mat.applyToSelf(new VecD3D());
        this.xAxis = mat.applyTo(VecD3D.X_AXIS).subSelf(origin).normalize();
        this.yAxis = mat.applyTo(VecD3D.Y_AXIS).subSelf(origin).normalize();
        zAxis = xAxis.crossInto(yAxis, new VecD3D());
    }

    /**
     * Creates a new origin at the given origin using the standard XYZ axes
     * 
     * @param o
     *            origin
     */
    public OriginD3D(VecD3D o) {
        origin = o;
        xAxis = VecD3D.X_AXIS;
        yAxis = VecD3D.Y_AXIS;
        zAxis = VecD3D.Z_AXIS;
    }

    /**
     * Attempts to create a cartesian coordinate system with the given point as
     * its origin and the direction as its Z-axis. In cases when two of the
     * direction vector components are equal, the constructor will throw an
     * {@link IllegalArgumentException}.
     * 
     * @param o
     *            origin of the coordinate system
     * @param dir
     *            z-axis
     */
    public OriginD3D(VecD3D o, VecD3D dir) {
        this.origin = o;
        this.zAxis = dir.getNormalized();
        VecD3D av = null;
        AxisD a = (AxisD) zAxis.getClosestAxis();
        if (a == VecD3D.AxisD.X) {
            av = VecD3D.AxisD.Z.getVector().getInverted();
        } else if (a == VecD3D.AxisD.Y) {
            av = VecD3D.AxisD.Z.getVector().getInverted();
        } else if (a == VecD3D.AxisD.Z) {
            av = VecD3D.AxisD.X.getVector().getInverted();
        }
        if (av == null) {
            throw new IllegalArgumentException(
                    "can't create a coordinate system for direction: " + dir);
        }
        xAxis = av.cross(dir).normalize();
        yAxis = xAxis.cross(zAxis).normalize();

    }

    /**
     * @param o
     *            origin of the coordinate system
     * @param x
     *            x-direction of the coordinate system
     * @param y
     *            y-direction of the coordinate system
     * @throws IllegalArgumentException
     *             if x and y vectors are not orthogonal
     */
    public OriginD3D(VecD3D o, VecD3D x, VecD3D y) throws IllegalArgumentException {
        origin = o;
        xAxis = x;
        yAxis = y;
        xAxis.getNormalized();
        yAxis.getNormalized();
        if (Math.abs(xAxis.dot(yAxis)) > 0.0001) {
            throw new IllegalArgumentException("Axis vectors aren't orthogonal");
        }
        zAxis = xAxis.crossInto(yAxis, new VecD3D());
    }
}
