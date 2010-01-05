/* 
 * Copyright (c) 2007 Karsten Schmidt
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
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
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

    public Ray2D(float x, float y, Vec2D d) {
        super(x, y);
        setDirection(d);
    }

    public Ray2D(Vec2D o, Vec2D d) {
        this(o.x, o.y, d);
    }

    /**
     * Returns a copy of the ray's direction vector.
     * 
     * @return vector
     */
    public Vec2D getDirection() {
        return dir.copy();
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
    public Ray2D setDirection(Vec2D d) {
        dir.set(d).normalize();
        return this;
    }

    public String toString() {
        return "origin: " + super.toString() + " dir: " + dir;
    }
}
