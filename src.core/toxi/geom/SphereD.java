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
import javax.xml.bind.annotation.XmlAttribute;

import toxi.geom.mesh.MeshD3D;
import toxi.geom.mesh.SphereDFunction;
import toxi.geom.mesh.SurfaceMeshDBuilder;

@XmlAccessorType(XmlAccessType.FIELD)
public class SphereD extends VecD3D implements ShapeD3D {

    /**
     * Earth's mean radius in km
     * (http://en.wikipedia.org/wiki/Earth_radius#Mean_radii)
     */
    public static final double EARTH_RADIUS = ((2 * 6378.1370 + 6356.752314245) / 3.0);

    @XmlAttribute(required = true)
    public double radius;

    public SphereD() {
        this(new VecD3D(), 1);
    }

    public SphereD(double radius) {
        this(new VecD3D(), radius);
    }

    public SphereD(ReadonlyVecD3D v, double r) {
        super(v);
        radius = r;
    }

    public SphereD(SphereD s) {
        this(s, s.radius);
    }
    public SphereD(Sphere s) {
        this(new VecD3D(s.x,s.y,s.z), s.radius);
    }

    public boolean containsPoint(ReadonlyVecD3D p) {
        double d = this.sub(p).magSquared();
        return (d <= radius * radius);
    }

    /**
     * Alternative to {@link SphereDIntersectorReflector}. Computes primary &
     * secondary intersection points of this sphere with the given ray. If no
     * intersection is found the method returns null. In all other cases, the
     * returned array will contain the distance to the primary intersection
     * point (i.e. the closest in the direction of the ray) as its first index
     * and the other one as its second. If any of distance values is negative,
     * the intersection point lies in the opposite ray direction (might be
     * useful to know). To get the actual intersection point coordinates, simply
     * pass the returned values to {@link Ray3D#getPointAtDistance(double)}.
     * 
     * @param ray
     * @return 2-element double array of intersection points or null if ray
     *         doesn't intersect sphere at all.
     */
    public double[] intersectRay(RayD3D ray) {
        double[] result = null;
        ReadonlyVecD3D q = ray.sub(this);
        double distSquared = q.magSquared();
        double v = -q.dot(ray.getDirection());
        double d = radius * radius - (distSquared - v * v);
        if (d >= 0.0) {
            d = Math.sqrt(d);
            double a = v + d;
            double b = v - d;
            if (!(a < 0 && b < 0)) {
                if (a > 0 && b > 0) {
                    if (a > b) {
                        double t = a;
                        a = b;
                        b = t;
                    }
                } else {
                    if (b > 0) {
                        double t = a;
                        a = b;
                        b = t;
                    }
                }
            }
            result = new double[] {
                    a, b
            };
        }
        return result;
    }

    /**
     * Considers the current vector as centre of a collision sphere with radius
     * r and checks if the triangle abc intersects with this sphere. The VecD3D p
     * The point on abc closest to the sphere center is returned via the
     * supplied result vector argument.
     * 
     * @param t
     *            triangle to check for intersection
     * @param result
     *            a non-null vector for storing the result
     * @return true, if sphere intersects triangle ABC
     */
    public boolean intersectSphereDTriangleD(TriangleD3D t, VecD3D result) {
        // Find VecD3D P on triangle ABC closest to sphere center
        result.set(t.closestPointOnSurface(this));

        // SphereD and TriangleD intersect if the (squared) distance from sphere
        // center to VecD3D p is less than the (squared) sphere radius
        ReadonlyVecD3D v = result.sub(this);
        return v.magSquared() <= radius * radius;
    }

    /**
     * Computes the surface distance on this sphere between two points given as
     * lon/lat coordinates. The x component of each vector needs to contain the
     * longitude and the y component the latitude (both in radians).
     * 
     * Algorithm from: http://www.csgnetwork.com/gpsdistcalc.html
     * 
     * @param p
     * @param q
     * @return distance on the sphere surface
     */
    public double surfaceDistanceBetween(VecD2D p, VecD2D q) {
        double t1 = Math.sin(p.y) * Math.sin(q.y);
        double t2 = Math.cos(p.y) * Math.cos(q.y);
        double t3 = Math.cos(p.x - q.x);
        double t4 = t2 * t3;
        double t5 = t1 + t4;
        double dist = Math.atan(-t5 / Math.sqrt(-t5 * t5 + 1)) + 2
                * Math.atan(1);
        if (Double.isNaN(dist)) {
            dist = 0;
        } else {
            dist *= radius;
        }
        return dist;
    }

    /**
     * Calculates the normal vector on the sphere in the direction of the
     * current point.
     * 
     * @param q
     * @return a unit normal vector to the tangent plane of the ellipsoid in the
     *         point.
     */
    public VecD3D tangentPlaneNormalAt(ReadonlyVecD3D q) {
        return q.sub(this).normalize();
    }

    public MeshD3D toMesh(int res) {
        return toMesh(null, res);
    }

    public MeshD3D toMesh(MeshD3D mesh, int res) {
        SurfaceMeshDBuilder builder = new SurfaceMeshDBuilder(new SphereDFunction(
                this));
        return builder.createMeshD(mesh, res, 1);
    }
}
