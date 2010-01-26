/*
 * Copyright (c) 2006-2008 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */

package toxi.geom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import toxi.math.MathUtils;

@XmlAccessorType(XmlAccessType.FIELD)
public class Triangle2D {

    public static Triangle2D createEquilateralFrom(Vec2D a, Vec2D b) {
        Vec2D c = a.interpolateTo(b, 0.5f);
        Vec2D dir = a.sub(b);
        Vec2D n = dir.getPerpendicular();
        c.addSelf(n.normalizeTo(dir.magnitude() * MathUtils.SQRT3 / 2));
        return new Triangle2D(a, b, c);
    }

    public static boolean isClockwise(Vec2D a, Vec2D b, Vec2D c) {
        float determ = (b.x - a.x) * (c.y - a.y) - (c.x - a.x) * (b.y - a.y);
        return (determ < 0.0);
    }

    @XmlElement(required = true)
    public Vec2D a, b, c;

    @XmlTransient
    public Vec2D centroid;

    public Triangle2D() {
    }

    public Triangle2D(Vec2D a, Vec2D b, Vec2D c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Vec2D computeCentroid() {
        centroid = a.add(b).addSelf(c).scaleSelf(1f / 3);
        return centroid;
    }

    /**
     * Checks if point vector is inside the triangle created by the points a, b
     * and c. These points will create a plane and the point checked will have
     * to be on this plane in the region between a,b,c.
     * 
     * Note: The triangle must be defined in clockwise order a,b,c
     * 
     * @return true, if point is in triangle.
     */
    public boolean containsPoint(Vec2D p) {
        Vec2D v1 = p.sub(a).normalize();
        Vec2D v2 = p.sub(b).normalize();
        Vec2D v3 = p.sub(c).normalize();
        double total_angles = Math.acos(v1.dot(v2));
        total_angles += Math.acos(v2.dot(v3));
        total_angles += Math.acos(v3.dot(v1));
        return (MathUtils.abs((float) total_angles - MathUtils.TWO_PI) <= 0.01f);
    }

    /**
     * Finds and returns the closest point on any of the triangle edges to the
     * point given.
     * 
     * @param p
     *            point to check
     * @return closest point
     */

    public Vec2D getClosestVertexTo(Vec2D p) {
        Vec2D Rab = p.closestPointOnLine(a, b);
        Vec2D Rbc = p.closestPointOnLine(b, c);
        Vec2D Rca = p.closestPointOnLine(c, a);

        float dAB = p.sub(Rab).magSquared();
        float dBC = p.sub(Rbc).magSquared();
        float dCA = p.sub(Rca).magSquared();

        float min = dAB;
        Vec2D result = Rab;

        if (dBC < min) {
            min = dBC;
            result = Rbc;
        }
        if (dCA < min) {
            result = Rca;
        }

        return result;
    }

    public boolean isClockwise() {
        return Triangle2D.isClockwise(a, b, c);
    }

    public void set(Vec2D a2, Vec2D b2, Vec2D c2) {
        a = a2;
        b = b2;
        c = c2;
    }

    public String toString() {
        return "Triangle: " + a + "," + b + "," + c;
    }
}
