/*
 * Copyright (c) 2007 Karsten Schmidt
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
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Sphere extends Vec3D {

    @XmlAttribute(required = true)
    public float radius;

    public Sphere() {
        this(new Vec3D(), 1);
    }

    public Sphere(Sphere s) {
        this(s, s.radius);
    }

    public Sphere(Vec3D v, float r) {
        super(v);
        radius = r;
    }

    public boolean containsPoint(Vec3D p) {
        float d = this.sub(p).magSquared();
        return (d <= radius * radius);
    }

    /**
     * Considers the current vector as centre of a collision sphere with radius
     * r and checks if the triangle abc intersects with this sphere. The Vec3D p
     * The point on abc closest to the sphere center is returned via the
     * supplied result vector argument.
     * 
     * @param t
     *            triangle to check for intersection
     * @param result
     *            a non-null vector for storing the result
     * @return true, if sphere intersects triangle ABC
     */
    public boolean intersectSphereTriangle(Triangle t, Vec3D result) {
        // Find Vec3D P on triangle ABC closest to sphere center
        result.set(t.closestPointOnSurface(this));

        // Sphere and triangle intersect if the (squared) distance from sphere
        // center to Vec3D p is less than the (squared) sphere radius
        Vec3D v = result.sub(this);
        return v.magSquared() <= radius * radius;
    }

    /**
     * Calculates the normal vector on the sphere in the direction of the
     * current point.
     * 
     * @param q
     * @return a unit normal vector to the tangent plane of the ellipsoid in the
     *         point.
     */
    // FIXME this method is totally broken for ages
    public Vec3D tangentPlaneNormalAt(Vec3D q) {
        // Vec3D p = this.sub(q);
        // float xr2 = eR.x * eR.x;
        // float yr2 = eR.y * eR.y;
        // float zr2 = eR.z * eR.z;
        // float r2 = 1f / (radius * radius);
        // return p.scaleSelf(r2).normalize();
        return sub(q).normalize();
    }
}
