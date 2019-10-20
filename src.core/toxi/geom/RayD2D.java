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
 * A simple 2D ray datatype
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class RayD2D extends VecD2D {

    @XmlElement(required = true)
    protected VecD2D dir;

    public RayD2D() {
        super();
        dir = VecD2D.Y_AXIS.copy();
    }

    public RayD2D(double x, double y, ReadonlyVecD2D d) {
        super(x, y);
        dir = d.getNormalized();
    }

    public RayD2D(ReadonlyVecD2D o, ReadonlyVecD2D d) {
        this(o.x(), o.y(), d);
    }

    /**
     * Returns a copy of the ray's direction vector.
     * 
     * @return vector
     */
    public VecD2D getDirection() {
        return dir.copy();
    }

    /**
     * Calculates the distance between the given point and the infinite line
     * coinciding with this ray.
     * 
     * @param p
     * @return distance
     */
    public double getDistanceToPoint(VecD2D p) {
        VecD2D sp = p.sub(this);
        return sp.distanceTo(dir.scale(sp.dot(dir)));
    }

    public VecD2D getPointAtDistance(double dist) {
        return add(dir.scale(dist));
    }

    /**
     * Uses a normalized copy of the given vector as the ray direction.
     * 
     * @param d
     *            new direction
     * @return itself
     */
    public RayD2D setDirection(ReadonlyVecD2D d) {
        dir.set(d).normalize();
        return this;
    }

    public RayD2D setNormalizedDirection(ReadonlyVecD2D d) {
        dir.set(d);
        return this;
    }

    /**
     * Converts the ray into a 2D Line segment with its start point coinciding
     * with the ray origin and its other end point at the given distance along
     * the ray.
     * 
     * @param dist
     *            end point distance
     * @return line segment
     */
    public LineD2D toLineD2DWithPointAtDistance(double dist) {
        return new LineD2D(this, getPointAtDistance(dist));
    }

    public String toString() {
        return "origin: " + super.toString() + " dir: " + dir;
    }
}
