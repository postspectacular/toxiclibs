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

import toxi.geom.LineD2D.LineIntersection.Type;
import toxi.math.MathUtils;

@XmlAccessorType(XmlAccessType.FIELD)
public class TriangleD2D implements ShapeD2D {

    public static TriangleD2D createEquilateralFrom(ReadonlyVecD2D a,
            ReadonlyVecD2D b) {
        VecD2D c = a.interpolateTo(b, 0.5f);
        VecD2D dir = a.sub(b);
        VecD2D n = dir.getPerpendicular();
        c.addSelf(n.normalizeTo(dir.magnitude() * MathUtils.SQRT3 / 2));
        return new TriangleD2D(a, b, c);
    }

    public static boolean isClockwise(VecD2D a, VecD2D b, VecD2D c) {
        double determ = (b.x - a.x) * (c.y - a.y) - (c.x - a.x) * (b.y - a.y);
        return (determ > 0.0);
    }

    @XmlElement(required = true)
    public VecD2D a, b, c;

    @XmlTransient
    public VecD2D centroid;

    public TriangleD2D() {
    }

    public TriangleD2D(ReadonlyVecD2D a, ReadonlyVecD2D b, ReadonlyVecD2D c) {
        this.a = a.copy();
        this.b = b.copy();
        this.c = c.copy();
    }

    public TriangleD2D adjustTriangleSizeBy(double offset) {
        return adjustTriangleSizeBy(offset, offset, offset);
    }

    public TriangleD2D adjustTriangleSizeBy(double offAB, double offBC, double offCA) {
        computeCentroid();
        LineD2D ab = new LineD2D(a.copy(), b.copy()).offsetAndGrowBy(offAB,
                100000, centroid);
        LineD2D bc = new LineD2D(b.copy(), c.copy()).offsetAndGrowBy(offBC,
                100000, centroid);
        LineD2D ca = new LineD2D(c.copy(), a.copy()).offsetAndGrowBy(offCA,
                100000, centroid);
        a = ab.intersectLine(ca).getPos();
        b = ab.intersectLine(bc).getPos();
        c = bc.intersectLine(ca).getPos();
        computeCentroid();
        return this;
    }

    public VecD2D computeCentroid() {
        centroid = a.add(b).addSelf(c).scaleSelf(1f / 3);
        return centroid;
    }

    /**
     * Checks if the given point is inside the triangle created by the points a,
     * b and c. The triangle vertices are inclusive themselves.
     * 
     * @return true, if point is in triangle.
     */
    public boolean containsPoint(ReadonlyVecD2D p) {
        VecD2D v1 = p.sub(a);
        VecD2D v2 = p.sub(b);
        VecD2D v3 = p.sub(c);
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

    public TriangleD2D copy() {
        return new TriangleD2D(a.copy(), b.copy(), c.copy());
    }

    public TriangleD2D flipVertexOrder() {
        VecD2D t = a;
        a = c;
        c = t;
        return this;
    }

    public VecD2D fromBarycentric(ReadonlyVecD3D p) {
        return new VecD2D(a.x * p.x() + b.x * p.y() + c.x * p.z(), a.y * p.x()
                + b.y * p.y() + c.y * p.z());
    }

    public double getArea() {
        return b.sub(a).cross(c.sub(a)) * 0.5f;
    }

    public CircleD getBoundingCircleD() {
        return CircleD.from3Points(a, b, c);
    }

    public RectD getBounds() {
        return new RectD(VecD2D.min(VecD2D.min(a, b), c), VecD2D.max(VecD2D.max(a, b), c));
    }

    public CircleD getCircumCircleD() {
        VecD3D cr = a.bisect(b).cross(b.bisect(c));
        VecD2D circ = new VecD2D(cr.x / cr.z, cr.y / cr.z);
        double sa = a.distanceTo(b);
        double sb = b.distanceTo(c);
        double sc = c.distanceTo(a);
        double radius = sa
                * sb
                * sc
                / Math.sqrt((sa + sb + sc) * (-sa + sb + sc)
                        * (sa - sb + sc) * (sa + sb - sc));
        return new CircleD(circ, radius);
    }

    public double getCircumference() {
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
    public VecD2D getClosestPointTo(ReadonlyVecD2D p) {
        LineD2D edge = new LineD2D(a, b);
        VecD2D Rab = edge.closestPointTo(p);
        VecD2D Rbc = edge.set(b, c).closestPointTo(p);
        VecD2D Rca = edge.set(c, a).closestPointTo(p);

        double dAB = p.sub(Rab).magSquared();
        double dBC = p.sub(Rbc).magSquared();
        double dCA = p.sub(Rca).magSquared();

        double min = dAB;
        VecD2D result = Rab;

        if (dBC < min) {
            min = dBC;
            result = Rbc;
        }
        if (dCA < min) {
            result = Rca;
        }

        return result;
    }

    public List<LineD2D> getEdges() {
        return toPolygonD2D().getEdges();
    }

    /**
     * Creates a random point within the triangle using barycentric coordinates.
     * 
     * @return VecD2D
     */
    public VecD2D getRandomPoint() {
        List<Double> barycentric = new ArrayList<Double>(3);
        barycentric.add(MathUtils.random(1d));
        barycentric.add(MathUtils.random(1d - barycentric.get(0)));
        barycentric.add(1 - (barycentric.get(0) + barycentric.get(1)));
        Collections.shuffle(barycentric);
        return fromBarycentric(new VecD3D(barycentric.get(0),
                barycentric.get(1), barycentric.get(2)));
    }

    public VecD2D[] getVertexArray() {
        return getVertexArray(null, 0);
    }

    public VecD2D[] getVertexArray(VecD2D[] array, int offset) {
        if (array == null) {
            array = new VecD2D[3];
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
    public boolean intersectsTriangle(TriangleD2D tri) {
        if (containsPoint(tri.a) || containsPoint(tri.b)
                || containsPoint(tri.c)) {
            return true;
        }
        if (tri.containsPoint(a) || tri.containsPoint(b)
                || tri.containsPoint(c)) {
            return true;
        }
        LineD2D[] ea = new LineD2D[] {
                new LineD2D(a, b), new LineD2D(b, c), new LineD2D(c, a)
        };
        LineD2D[] eb = new LineD2D[] {
                new LineD2D(tri.a, tri.b), new LineD2D(tri.b, tri.c),
                new LineD2D(tri.c, tri.a)
        };
        for (LineD2D la : ea) {
            for (LineD2D lb : eb) {
                Type type = la.intersectLine(lb).getType();
                if (type != Type.NON_INTERSECTING && type != Type.PARALLEL) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isClockwise() {
        return TriangleD2D.isClockwise(a, b, c);
    }

    public void set(VecD2D a2, VecD2D b2, VecD2D c2) {
        a = a2;
        b = b2;
        c = c2;
    }

    /**
     * Produces the barycentric coordinates of the given point within this
     * triangle. These coordinates can then be used to re-project the point into
     * a different triangle using its {@link #fromBarycentric(ReadonlyVecD3D)}
     * method.
     * 
     * @param p
     *            point in world space
     * @return barycentric coords as {@link VecD3D}
     */
    public VecD3D toBarycentric(ReadonlyVecD2D p) {
        return new TriangleD3D(a.toD3DXY(), b.toD3DXY(), c.toD3DXY())
                .toBarycentric(p.toD3DXY());
    }

    /**
     * Creates a {@link Polygon2D} instance of the triangle. The vertices of
     * this polygon are disconnected from the ones defining this triangle.
     * 
     * @return triangle as polygon
     */
    public PolygonD2D toPolygonD2D() {
        PolygonD2D poly = new PolygonD2D();
        poly.add(a.copy());
        poly.add(b.copy());
        poly.add(c.copy());
        return poly;
    }

    public String toString() {
        return "Triangle2D: " + a + "," + b + "," + c;
    }
}
