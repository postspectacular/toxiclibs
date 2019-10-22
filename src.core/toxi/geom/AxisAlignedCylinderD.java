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

import toxi.geom.mesh.MeshD3D;

public abstract class AxisAlignedCylinderD implements ShapeD3D {

    protected VecD3D pos;
    protected double radius;
    protected double radiusSquared;
    protected double length;

    protected AxisAlignedCylinderD(ReadonlyVecD3D pos, double radius, double length) {
        this.pos = pos.copy();
        setRadius(radius);
        setLength(length);
    }

    /**
     * Checks if the given point is inside the cylinder.
     * 
     * @param p
     * @return true, if inside
     */
    public abstract boolean containsPoint(ReadonlyVecD3D p);

    /**
     * @return the length
     */
    public double getLength() {
        return length;
    }

    /**
     * @return the cylinder's orientation axis
     */
    public abstract VecD3D.AxisD getMajorAxis();

    /**
     * Returns the cylinder's position (centroid).
     * 
     * @return the pos
     */
    public VecD3D getPosition() {
        return pos.copy();
    }

    /**
     * @return the cylinder radius
     */
    public double getRadius() {
        return radius;
    }

    /**
     * @param length
     *            the length to set
     */
    public void setLength(double length) {
        this.length = length;
    }

    /**
     * @param pos
     *            the pos to set
     */
    public void setPosition(VecD3D pos) {
        this.pos.set(pos);
    }

    /**
     * @param radius
     */
    public void setRadius(double radius) {
        this.radius = radius;
        this.radiusSquared = radius * radius;
    }

    /**
     * Builds a TriangleMesh representation of the cylinder at a default
     * resolution 30 degrees.
     * 
     * @return mesh instance
     */
    public MeshD3D toMeshD() {
        return toMeshD(12, 0);
    }

    /**
     * Builds a TriangleMesh representation of the cylinder using the given
     * number of steps and start angle offset.
     * 
     * @param steps
     * @param thetaOffset
     * @return mesh
     */
    public MeshD3D toMeshD(int steps, double thetaOffset) {
        return toMeshD(null, steps, thetaOffset);
    }

    public MeshD3D toMeshD(MeshD3D mesh, int steps, double thetaOffset) {
        return new ConeD(pos, getMajorAxis().getVector(), radius, radius, length)
                .toMeshD(mesh, steps, thetaOffset, true, true);
    }
}