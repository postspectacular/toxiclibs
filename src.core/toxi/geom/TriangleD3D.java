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
import javax.xml.bind.annotation.XmlTransient;
import toxi.geom.VecD3D;

import toxi.math.MathUtils;

@XmlAccessorType(XmlAccessType.FIELD)
public class TriangleD3D implements ShapeD3D {

    public static TriangleD3D createEquilateralFrom(VecD3D a, VecD3D b) {
        VecD3D c = a.interpolateTo(b, 0.5f);
        VecD3D dir = b.sub(a);
        VecD3D n = a.cross(dir.normalize());
        c.addSelf(n.normalizeTo(dir.magnitude() * MathUtils.SQRT3 / 2));
        return new TriangleD3D(a, b, c);
    }

    public static boolean isClockwiseInXY(VecD3D a, VecD3D b, VecD3D c) {
        double determ = (b.x - a.x) * (c.y - a.y) - (c.x - a.x) * (b.y - a.y);
        return (determ < 0.0);
    }

    public static boolean isClockwiseInXZ(VecD3D a, VecD3D b, VecD3D c) {
        double determ = (b.x - a.x) * (c.z - a.z) - (c.x - a.x) * (b.z - a.z);
        return (determ < 0.0);
    }

    public static boolean isClockwiseInYZ(VecD3D a, VecD3D b, VecD3D c) {
        double determ = (b.y - a.y) * (c.z - a.z) - (c.y - a.y) * (b.z - a.z);
        return (determ < 0.0);
    }

    @XmlElement(required = true)
    public VecD3D a, b, c;

    @XmlElement(required = true)
    public VecD3D normal;

    @XmlTransient
    public VecD3D centroid;

    public TriangleD3D() {
    }

    public TriangleD3D(VecD3D a, VecD3D b, VecD3D c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    public TriangleD3D(Triangle3D t) {
        this.a = new VecD3D(t.a);
        this.b = new VecD3D(t.a);
        this.c = new VecD3D(t.a);
    }

    /**
     * Computes the the point closest to the current vector on the surface of
     * triangle abc.
     * 
     * From Real-Time Collision Detection by Christer Ericson, published by
     * Morgan Kaufmann Publishers, Copyright 2005 Elsevier Inc
     * 
     * @return closest point on triangle (result may also be one of a, b or c)
     */
    public VecD3D closestPointOnSurface(VecD3D p) {
        VecD3D ab = b.sub(a);
        VecD3D ac = c.sub(a);
        VecD3D bc = c.sub(b);

        ReadonlyVecD3D pa = p.sub(a);
        ReadonlyVecD3D pb = p.sub(b);
        ReadonlyVecD3D pc = p.sub(c);

        VecD3D ap = a.sub(p);
        VecD3D bp = b.sub(p);
        VecD3D cp = c.sub(p);

        // Compute parametric position s for projection P' of P on AB,
        // P' = A + s*AB, s = snom/(snom+sdenom)
        double snom = pa.dot(ab);

        // Compute parametric position t for projection P' of P on AC,
        // P' = A + t*AC, s = tnom/(tnom+tdenom)
        double tnom = pa.dot(ac);

        if (snom <= 0.0f && tnom <= 0.0f) {
            return a; // Vertex region early out
        }

        double sdenom = pb.dot(a.sub(b));
        double tdenom = pc.dot(a.sub(c));

        // Compute parametric position u for projection P' of P on BC,
        // P' = B + u*BC, u = unom/(unom+udenom)
        double unom = pb.dot(bc);
        double udenom = pc.dot(b.sub(c));

        if (sdenom <= 0.0f && unom <= 0.0f) {
            return b; // Vertex region early out
        }
        if (tdenom <= 0.0f && udenom <= 0.0f) {
            return c; // Vertex region early out
        }

        // P is outside (or on) AB if the triple scalar product [N PA PB] <= 0
        ReadonlyVecD3D n = ab.cross(ac);
        double vc = n.dot(ap.crossSelf(bp));

        // If P outside AB and within feature region of AB,
        // return projection of P onto AB
        if (vc <= 0.0f && snom >= 0.0f && sdenom >= 0.0f) {
            // return a + snom / (snom + sdenom) * ab;
            return a.add(ab.scaleSelf(snom / (snom + sdenom)));
        }

        // P is outside (or on) BC if the triple scalar product [N PB PC] <= 0
        double va = n.dot(bp.crossSelf(cp));
        // If P outside BC and within feature region of BC,
        // return projection of P onto BC
        if (va <= 0.0f && unom >= 0.0f && udenom >= 0.0f) {
            // return b + unom / (unom + udenom) * bc;
            return b.add(bc.scaleSelf(unom / (unom + udenom)));
        }

        // P is outside (or on) CA if the triple scalar product [N PC PA] <= 0
        double vb = n.dot(cp.crossSelf(ap));
        // If P outside CA and within feature region of CA,
        // return projection of P onto CA
        if (vb <= 0.0f && tnom >= 0.0f && tdenom >= 0.0f) {
            // return a + tnom / (tnom + tdenom) * ac;
            return a.add(ac.scaleSelf(tnom / (tnom + tdenom)));
        }

        // P must project inside face region. Compute Q using barycentric
        // coordinates
        double u = va / (va + vb + vc);
        double v = vb / (va + vb + vc);
        double w = 1.0f - u - v; // = vc / (va + vb + vc)
        // return u * a + v * b + w * c;
        return a.scale(u).addSelf(b.scale(v)).addSelf(c.scale(w));
    }

    public VecD3D computeCentroid() {
        centroid = a.add(b).addSelf(c).scaleSelf(1f / 3);
        return centroid;
    }

    public VecD3D computeNormal() {
        normal = a.sub(c).crossSelf(a.sub(b)).normalize();
        return normal;
    }

    /**
     * Checks if point vector is inside the triangle created by the points a, b
     * and c. These points will create a plane and the point checked will have
     * to be on this plane in the region between a,b,c (triangle vertices
     * inclusive).
     * 
     * @return true, if point is in triangle.
     */
    public boolean containsPoint(ReadonlyVecD3D p) {
        VecD3D v0 = c.sub(a);
        VecD3D v1 = b.sub(a);
        VecD3D v2 = p.sub(a);

        // Compute dot products
        double dot00 = v0.dot(v0);
        double dot01 = v0.dot(v1);
        double dot02 = v0.dot(v2);
        double dot11 = v1.dot(v1);
        double dot12 = v1.dot(v2);

        // Compute barycentric coordinates
        double invDenom = 1.0f / (dot00 * dot11 - dot01 * dot01);
        double u = (dot11 * dot02 - dot01 * dot12) * invDenom;
        double v = (dot00 * dot12 - dot01 * dot02) * invDenom;

        // Check if point is in triangle
        return (u >= 0.0) && (v >= 0.0) && (u + v <= 1.0);
    }

    public TriangleD3D flipVertexOrder() {
        VecD3D t = a;
        a = c;
        c = t;
        return this;
    }

    public VecD3D fromBarycentric(ReadonlyVecD3D p) {
        return new VecD3D(a.x * p.x() + b.x * p.y() + c.x * p.z(), a.y * p.x()
                + b.y * p.y() + c.y * p.z(), a.z * p.x() + b.z * p.y() + c.z
                * p.z());
    }

    public AABBD getBoundingBox() {
        VecD3D min = VecD3D.min(VecD3D.min(a, b), c);
        VecD3D max = VecD3D.max(VecD3D.max(a, b), c);
        return AABBD.fromMinMax(min, max);
    }

    /**
     * Finds and returns the closest point on any of the triangle edges to the
     * point given.
     * 
     * @param p
     *            point to check
     * @return closest point
     */

    public VecD3D getClosestPointTo(ReadonlyVecD3D p) {
        LineD3D edge = new LineD3D(a, b);
        final VecD3D Rab = edge.closestPointTo(p);
        final VecD3D Rbc = edge.set(b, c).closestPointTo(p);
        final VecD3D Rca = edge.set(c, a).closestPointTo(p);

        final double dAB = p.sub(Rab).magSquared();
        final double dBC = p.sub(Rbc).magSquared();
        final double dCA = p.sub(Rca).magSquared();

        double min = dAB;
        VecD3D result = Rab;

        if (dBC < min) {
            min = dBC;
            result = Rbc;
        }
        if (dCA < min) {
            result = Rca;
        }

        return result;
    }

    public VecD3D[] getVertexArray() {
        return getVertexArray(null, 0);
    }

    public VecD3D[] getVertexArray(VecD3D[] array, int offset) {
        if (array == null) {
            array = new VecD3D[3];
        }
        array[offset++] = a;
        array[offset++] = b;
        array[offset] = c;
        return array;
    }

    public boolean isClockwiseInXY() {
        return TriangleD3D.isClockwiseInXY(a, b, c);
    }

    public boolean isClockwiseInXZ() {
        return TriangleD3D.isClockwiseInXY(a, b, c);
    }

    public boolean isClockwiseInYZ() {
        return TriangleD3D.isClockwiseInXY(a, b, c);
    }

    private boolean isSameClockDir(VecD3D a, VecD3D b, ReadonlyVecD3D p, VecD3D norm) {
        double bax = b.x - a.x;
        double bay = b.y - a.y;
        double baz = b.z - a.z;
        double pax = p.x() - a.x;
        double pay = p.y() - a.y;
        double paz = p.z() - a.z;
        double nx = bay * paz - pay * baz;
        double ny = baz * pax - paz * bax;
        double nz = bax * pay - pax * bay;
        double dotprod = nx * norm.x + ny * norm.y + nz * norm.z;
        return dotprod < 0;
    }

    public void set(VecD3D a2, VecD3D b2, VecD3D c2) {
        a = a2;
        b = b2;
        c = c2;
    }

    public VecD3D toBarycentric(ReadonlyVecD3D p) {
        VecD3D e = b.sub(a).cross(c.sub(a));
        VecD3D n = e.getNormalized();

        // Compute twice area of triangle ABC
        double areaABC = n.dot(e);
        // Compute lambda1
        double areaPBC = n.dot(b.sub(p).cross(c.sub(p)));
        double l1 = areaPBC / areaABC;

        // Compute lambda2
        double areaPCA = n.dot(c.sub(p).cross(a.sub(p)));
        double l2 = areaPCA / areaABC;

        // Compute lambda3
        double l3 = 1.0f - l1 - l2;

        return new VecD3D(l1, l2, l3);
        // return new VecD3D(a.x * l1 + b.x * l2 + c.x * l3, a.y * l1 + b.y * l2
        // + c.y * l3, a.z * l1 + b.z * l2 + c.z * l3);
    }

    public String toString() {
        return "Triangle3D: " + a + "," + b + "," + c;
    }
}
