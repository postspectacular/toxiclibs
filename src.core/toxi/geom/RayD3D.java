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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * A simple 3D ray datatype
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class RayD3D extends VecD3D {

    @XmlElement(required = true)
    protected VecD3D dir;

    public RayD3D() {
        super();
        dir = VecD3D.Y_AXIS.copy();
    }

    public RayD3D(double x, double y, double z, ReadonlyVecD3D d) {
        super(x, y, z);
        dir = d.getNormalized();
    }

    public RayD3D(ReadonlyVecD3D o, ReadonlyVecD3D d) {
        this(o.x(), o.y(), o.z(), d);
    }

    /**
     * Returns a copy of the ray's direction vector.
     * 
     * @return vector
     */
    public VecD3D getDirection() {
        return dir.copy();
    }

    /**
     * Calculates the distance between the given point and the infinite line
     * coinciding with this ray.
     * 
     * @param p
     * @return distance
     */
    public double getDistanceToPoint(VecD3D p) {
        VecD3D sp = p.sub(this);
        return sp.distanceTo(dir.scale(sp.dot(dir)));
    }

    /**
     * Returns the point at the given distance on the ray. The distance can be
     * any real number.
     * 
     * @param dist
     * @return vector
     */
    public VecD3D getPointAtDistance(double dist) {
        return add(dir.scale(dist));
    }

    /**
     * Uses a normalized copy of the given vector as the ray direction.
     * 
     * @param d
     *            new direction
     * @return itself
     */
    public RayD3D setDirection(ReadonlyVecD3D d) {
        dir.set(d).normalize();
        return this;
    }

    public RayD3D setNormalizedDirection(ReadonlyVecD3D d) {
        dir.set(d);
        return this;
    }

    /**
     * Converts the ray into a 3D Line segment with its start point coinciding
     * with the ray origin and its other end point at the given distance along
     * the ray.
     * 
     * @param dist
     *            end point distance
     * @return line segment
     */
    public LineD3D toLineD3DWithPointAtDistance(double dist) {
        return new LineD3D(this, getPointAtDistance(dist));
    }

    public String toString() {
        return "origin: " + super.toString() + " dir: " + dir;
    }
}
