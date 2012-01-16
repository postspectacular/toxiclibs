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
public class Ray2D extends Vec2D {

    @XmlElement(required = true)
    protected Vec2D dir;

    public Ray2D() {
        super();
        dir = Vec2D.Y_AXIS.copy();
    }

    public Ray2D(float x, float y, ReadonlyVec2D d) {
        super(x, y);
        dir = d.getNormalized();
    }

    public Ray2D(ReadonlyVec2D o, ReadonlyVec2D d) {
        this(o.x(), o.y(), d);
    }

    /**
     * Returns a copy of the ray's direction vector.
     * 
     * @return vector
     */
    public Vec2D getDirection() {
        return dir.copy();
    }

    /**
     * Calculates the distance between the given point and the infinite line
     * coinciding with this ray.
     * 
     * @param p
     * @return distance
     */
    public float getDistanceToPoint(Vec2D p) {
        Vec2D sp = p.sub(this);
        return sp.distanceTo(dir.scale(sp.dot(dir)));
    }

    public Vec2D getPointAtDistance(float dist) {
        return add(dir.scale(dist));
    }

    /**
     * Uses a normalized copy of the given vector as the ray direction.
     * 
     * @param d
     *            new direction
     * @return itself
     */
    public Ray2D setDirection(ReadonlyVec2D d) {
        dir.set(d).normalize();
        return this;
    }

    public Ray2D setNormalizedDirection(ReadonlyVec2D d) {
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
    public Line2D toLine2DWithPointAtDistance(float dist) {
        return new Line2D(this, getPointAtDistance(dist));
    }

    public String toString() {
        return "origin: " + super.toString() + " dir: " + dir;
    }
}
