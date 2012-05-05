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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import toxi.geom.Line2D.LineIntersection.Type;
import toxi.math.MathUtils;

@XmlAccessorType(XmlAccessType.FIELD)
public class Triangle2D implements Shape2D {

    public static Triangle2D createEquilateralFrom(ReadonlyVec2D a,
            ReadonlyVec2D b) {
        Vec2D c = a.interpolateTo(b, 0.5f);
        Vec2D dir = a.sub(b);
        Vec2D n = dir.getPerpendicular();
        c.addSelf(n.normalizeTo(dir.magnitude() * MathUtils.SQRT3 / 2));
        return new Triangle2D(a, b, c);
    }

    public static boolean isClockwise(Vec2D a, Vec2D b, Vec2D c) {
        float determ = (b.x - a.x) * (c.y - a.y) - (c.x - a.x) * (b.y - a.y);
        return (determ > 0.0);
    }

    @XmlElement(required = true)
    public Vec2D a, b, c;

    @XmlTransient
    public Vec2D centroid;

    public Triangle2D() {
    }

    public Triangle2D(ReadonlyVec2D a, ReadonlyVec2D b, ReadonlyVec2D c) {
        this.a = a.copy();
        this.b = b.copy();
        this.c = c.copy();
    }

    public Triangle2D adjustTriangleSizeBy(float offset) {
        return adjustTriangleSizeBy(offset, offset, offset);
    }

    public Triangle2D adjustTriangleSizeBy(float offAB, float offBC, float offCA) {
        computeCentroid();
        Line2D ab = new Line2D(a.copy(), b.copy()).offsetAndGrowBy(offAB,
                100000, centroid);
        Line2D bc = new Line2D(b.copy(), c.copy()).offsetAndGrowBy(offBC,
                100000, centroid);
        Line2D ca = new Line2D(c.copy(), a.copy()).offsetAndGrowBy(offCA,
                100000, centroid);
        a = ab.intersectLine(ca).getPos();
        b = ab.intersectLine(bc).getPos();
        c = bc.intersectLine(ca).getPos();
        computeCentroid();
        return this;
    }

    public Vec2D computeCentroid() {
        centroid = a.add(b).addSelf(c).scaleSelf(1f / 3);
        return centroid;
    }

    /**
     * Checks if the given point is inside the triangle created by the points a,
     * b and c. The triangle vertices are inclusive themselves.
     * 
     * @return true, if point is in triangle.
     */
    public boolean containsPoint(ReadonlyVec2D p) {
        Vec2D v1 = p.sub(a);
        Vec2D v2 = p.sub(b);
        Vec2D v3 = p.sub(c);
        if (v1.isZeroVector() || v2.isZeroVector() || v3.isZeroVector()) {
            return true;
        }
        v1.normalize();
        v2.normalize();
        v3.normalize();
        double total_angles = Math.acos(v1.dot(v2));
        total_angles += Math.acos(v2.dot(v3));
        total_angles += Math.acos(v3.dot(v1));
        return (MathUtils.abs(total_angles - MathUtils.TWO_PI) <= 0.001);
    }

    public Triangle2D copy() {
        return new Triangle2D(a.copy(), b.copy(), c.copy());
    }

    public Triangle2D flipVertexOrder() {
        Vec2D t = a;
        a = c;
        c = t;
        return this;
    }

    public Vec2D fromBarycentric(ReadonlyVec3D p) {
        return new Vec2D(a.x * p.x() + b.x * p.y() + c.x * p.z(), a.y * p.x()
                + b.y * p.y() + c.y * p.z());
    }

    public float getArea() {
        return b.sub(a).cross(c.sub(a)) * 0.5f;
    }

    public Circle getBoundingCircle() {
        return Circle.from3Points(a, b, c);
    }

    public Rect getBounds() {
        return new Rect(Vec2D.min(Vec2D.min(a, b), c), Vec2D.max(
                Vec2D.max(a, b), c));
    }

    public Circle getCircumCircle() {
        Vec3D cr = a.bisect(b).cross(b.bisect(c));
        Vec2D circ = new Vec2D(cr.x / cr.z, cr.y / cr.z);
        float sa = a.distanceTo(b);
        float sb = b.distanceTo(c);
        float sc = c.distanceTo(a);
        float radius = sa
                * sb
                * sc
                / (float) Math.sqrt((sa + sb + sc) * (-sa + sb + sc)
                        * (sa - sb + sc) * (sa + sb - sc));
        return new Circle(circ, radius);
    }

    public float getCircumference() {
        return a.distanceTo(b) + b.distanceTo(c) + c.distanceTo(a);
    }

    /**
     * Finds and returns the closest point on any of the triangle edges to the
     * point given.
     * 
     * @param p
     *            point to check
     * @return closest point
     */
    public Vec2D getClosestPointTo(ReadonlyVec2D p) {
        Line2D edge = new Line2D(a, b);
        Vec2D Rab = edge.closestPointTo(p);
        Vec2D Rbc = edge.set(b, c).closestPointTo(p);
        Vec2D Rca = edge.set(c, a).closestPointTo(p);

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

    public List<Line2D> getEdges() {
        return toPolygon2D().getEdges();
    }

    /**
     * Creates a random point within the triangle using barycentric coordinates.
     * 
     * @return Vec2D
     */
    public Vec2D getRandomPoint() {
        List<Float> barycentric = new ArrayList<Float>(3);
        barycentric.add(MathUtils.random(1f));
        barycentric.add(MathUtils.random(1f - barycentric.get(0)));
        barycentric.add(1 - (barycentric.get(0) + barycentric.get(1)));
        Collections.shuffle(barycentric);
        return fromBarycentric(new Vec3D(barycentric.get(0),
                barycentric.get(1), barycentric.get(2)));
    }

    public Vec2D[] getVertexArray() {
        return getVertexArray(null, 0);
    }

    public Vec2D[] getVertexArray(Vec2D[] array, int offset) {
        if (array == null) {
            array = new Vec2D[3];
        }
        array[offset++] = a;
        array[offset++] = b;
        array[offset] = c;
        return array;
    }

    /**
     * Checks if this triangle intersects the given one. The check handles both
     * partial and total containment as well as intersections of all edges.
     * 
     * @param tri
     * @return true, if intersecting
     */
    public boolean intersectsTriangle(Triangle2D tri) {
        if (containsPoint(tri.a) || containsPoint(tri.b)
                || containsPoint(tri.c)) {
            return true;
        }
        if (tri.containsPoint(a) || tri.containsPoint(b)
                || tri.containsPoint(c)) {
            return true;
        }
        Line2D[] ea = new Line2D[] {
                new Line2D(a, b), new Line2D(b, c), new Line2D(c, a)
        };
        Line2D[] eb = new Line2D[] {
                new Line2D(tri.a, tri.b), new Line2D(tri.b, tri.c),
                new Line2D(tri.c, tri.a)
        };
        for (Line2D la : ea) {
            for (Line2D lb : eb) {
                Type type = la.intersectLine(lb).getType();
                if (type != Type.NON_INTERSECTING && type != Type.PARALLEL) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isClockwise() {
        return Triangle2D.isClockwise(a, b, c);
    }

    public void set(Vec2D a2, Vec2D b2, Vec2D c2) {
        a = a2;
        b = b2;
        c = c2;
    }

    /**
     * Produces the barycentric coordinates of the given point within this
     * triangle. These coordinates can then be used to re-project the point into
     * a different triangle using its {@link #fromBarycentric(ReadonlyVec3D)}
     * method.
     * 
     * @param p
     *            point in world space
     * @return barycentric coords as {@link Vec3D}
     */
    public Vec3D toBarycentric(ReadonlyVec2D p) {
        return new Triangle3D(a.to3DXY(), b.to3DXY(), c.to3DXY())
                .toBarycentric(p.to3DXY());
    }

    /**
     * Creates a {@link Polygon2D} instance of the triangle. The vertices of
     * this polygon are disconnected from the ones defining this triangle.
     * 
     * @return triangle as polygon
     */
    public Polygon2D toPolygon2D() {
        Polygon2D poly = new Polygon2D();
        poly.add(a.copy());
        poly.add(b.copy());
        poly.add(c.copy());
        return poly;
    }

    public String toString() {
        return "Triangle2D: " + a + "," + b + "," + c;
    }
}
