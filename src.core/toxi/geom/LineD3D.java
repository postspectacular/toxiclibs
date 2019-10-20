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
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import toxi.geom.LineD3D.LineIntersection.Type;
import toxi.math.MathUtils;

public class LineD3D {

    public static class LineIntersection {

        public static enum Type {
            NON_INTERSECTING,
            INTERSECTING
        }

        private final Type type;
        private final LineD3D line;
        private final double[] coeff;

        private LineIntersection(Type type) {
            this(type, null, 0, 0);
        }

        private LineIntersection(Type type, LineD3D line, double mua, double mub) {
            this.type = type;
            this.line = line;
            this.coeff = new double[] {
                    mua, mub
            };
        }

        public double[] getCoefficients() {
            return coeff;
        }

        public double getLength() {
            return line.getLength();
        }

        /**
         * @return the pos
         */
        public LineD3D getLine() {
            return line.copy();
        }

        /**
         * @return the type
         */
        public Type getType() {
            return type;
        }

        public boolean isIntersectionInside() {
            return type == Type.INTERSECTING && coeff[0] >= 0 && coeff[0] <= 1
                    && coeff[1] >= 0 && coeff[1] <= 1;
        }

        public String toString() {
            return "type: " + type + " line: " + line;
        }
    }

    /**
     * Splits the line between A and B into segments of the given length,
     * starting at point A. The tweened points are added to the given result
     * list. The last point added is B itself and hence it is likely that the
     * last segment has a shorter length than the step length requested. The
     * first point (A) can be omitted and not be added to the list if so
     * desired.
     * 
     * @param a
     *            start point
     * @param b
     *            end point (always added to results)
     * @param stepLength
     *            desired distance between points
     * @param segments
     *            existing array list for results (or a new list, if null)
     * @param addFirst
     *            false, if A is NOT to be added to results
     * @return list of result vectors
     */
    public static final List<VecD3D> splitIntoSegments(VecD3D a, VecD3D b,
            double stepLength, List<VecD3D> segments, boolean addFirst) {
        if (segments == null) {
            segments = new ArrayList<VecD3D>();
        }
        if (addFirst) {
            segments.add(a.copy());
        }
        double dist = a.distanceTo(b);
        if (dist > stepLength) {
            VecD3D pos = a.copy();
            VecD3D step = b.sub(a).limit(stepLength);
            while (dist > stepLength) {
                pos.addSelf(step);
                segments.add(pos.copy());
                dist -= stepLength;
            }
        }
        segments.add(b.copy());
        return segments;
    }

    @XmlElement
    public VecD3D a, b;

    public LineD3D(double x1, double y1, double z1, double x2, double y2, double z2) {
        this.a = new VecD3D(x1, y1, z1);
        this.b = new VecD3D(x2, y2, z2);
    }

    public LineD3D(ReadonlyVecD3D a, ReadonlyVecD3D b) {
        this.a = a.copy();
        this.b = b.copy();
    }

    public LineD3D(VecD3D a, VecD3D b) {
        this.a = a;
        this.b = b;
    }

    /**
     * Calculates the line segment that is the shortest route between this line
     * and the given one. Also calculates the coefficients where the end points
     * of this new line lie on the existing ones. If these coefficients are
     * within the 0.0 .. 1.0 interval the endpoints of the intersection line are
     * within the given line segments, if not then the intersection line is
     * outside.
     * 
     * <p>
     * Code based on original by Paul Bourke:<br/>
     * http://local.wasp.uwa.edu.au/~pbourke/geometry/lineline3d/
     * </p>
     */
    public LineIntersection closestLineTo(LineD3D l) {
        VecD3D p43 = l.a.sub(l.b);
        if (p43.isZeroVector()) {
            return new LineIntersection(Type.NON_INTERSECTING);
        }
        VecD3D p21 = b.sub(a);
        if (p21.isZeroVector()) {
            return new LineIntersection(Type.NON_INTERSECTING);
        }
        VecD3D p13 = a.sub(l.a);

        double d1343 = p13.x * p43.x + p13.y * p43.y + p13.z * p43.z;
        double d4321 = p43.x * p21.x + p43.y * p21.y + p43.z * p21.z;
        double d1321 = p13.x * p21.x + p13.y * p21.y + p13.z * p21.z;
        double d4343 = p43.x * p43.x + p43.y * p43.y + p43.z * p43.z;
        double d2121 = p21.x * p21.x + p21.y * p21.y + p21.z * p21.z;

        double denom = d2121 * d4343 - d4321 * d4321;
        if (MathUtils.abs(denom) < MathUtils.EPS) {
            return new LineIntersection(Type.NON_INTERSECTING);
        }
        double numer = d1343 * d4321 - d1321 * d4343;
        double mua =  (numer / denom);
        double mub =  ((d1343 + d4321 * mua) / d4343);

        VecD3D pa = a.add(p21.scaleSelf(mua));
        VecD3D pb = l.a.add(p43.scaleSelf(mub));
        return new LineIntersection(Type.INTERSECTING, new LineD3D(pa, pb), mua,
                mub);
    }

    /**
     * Computes the closest point on this line to the given one.
     * 
     * @param p
     *            point to check against
     * @return closest point on the line
     */
    public VecD3D closestPointTo(ReadonlyVecD3D p) {
        final VecD3D v = b.sub(a);
        final double t = p.sub(a).dot(v) / v.magSquared();
        // Check to see if t is beyond the extents of the line segment
        if (t < 0.0f) {
            return a.copy();
        } else if (t > 1.0f) {
            return b.copy();
        }
        // Return the point between 'a' and 'b'
        return a.add(v.scaleSelf(t));
    }

    public LineD3D copy() {
        return new LineD3D(a.copy(), b.copy());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LineD3D)) {
            return false;
        }
        LineD3D l = (LineD3D) obj;
        return (a.equals(l.a) || a.equals(l.b))
                && (b.equals(l.b) || b.equals(l.a));
    }

    /**
     * Returns the line's axis-aligned bounding box.
     * 
     * @return aabb
     * @see toxi.geom.AABB
     */
    public AABBD getBounds() {
        return AABBD.fromMinMax(a, b);
    }

    public VecD3D getDirection() {
        return b.sub(a).normalize();
    }

    public double getLength() {
        return a.distanceTo(b);
    }

    public double getLengthSquared() {
        return a.distanceToSquared(b);
    }

    public VecD3D getMidPoint() {
        return a.add(b).scaleSelf(0.5f);
    }

    public VecD3D getNormal() {
        return b.cross(a);
    }

    public boolean hasEndPoint(VecD3D p) {
        return a.equals(p) || b.equals(p);
    }

    /**
     * Computes a hash code ignoring the directionality of the line.
     * 
     * @return hash code
     * 
     * @see java.lang.Object#hashCode()
     * @see #hashCodeWithDirection()
     */
    public int hashCode() {
        return a.hashCode() + b.hashCode();
    }

    /**
     * Computes the hash code for this instance taking directionality into
     * account. A->B will produce a different hash code than B->A. If
     * directionality is not required or desired use the default
     * {@link #hashCode()} method.
     * 
     * @return hash code
     * 
     * @see #hashCode()
     */
    public int hashCodeWithDirection() {
        long bits = 1L;
        bits = 31L * bits + a.hashCode();
        bits = 31L * bits + b.hashCode();
        return (int) (bits ^ (bits >> 32));
    }

    public LineD3D offsetAndGrowBy(double offset, double scale, VecD3D ref) {
        VecD3D m = getMidPoint();
        VecD3D d = getDirection();
        VecD3D n = a.cross(d).normalize();
        if (ref != null && m.sub(ref).dot(n) < 0) {
            n.invert();
        }
        n.normalizeTo(offset);
        a.addSelf(n);
        b.addSelf(n);
        d.scaleSelf(scale);
        a.subSelf(d);
        b.addSelf(d);
        return this;
    }

    public LineD3D scaleLength(double scale) {
        double delta = (1 - scale) * 0.5f;
        VecD3D newA = a.interpolateTo(b, delta);
        b.interpolateToSelf(a, delta);
        a.set(newA);
        return this;
    }

    public LineD3D set(ReadonlyVecD3D a, ReadonlyVecD3D b) {
        this.a = a.copy();
        this.b = b.copy();
        return this;
    }

    public LineD3D set(VecD3D a, VecD3D b) {
        this.a = a;
        this.b = b;
        return this;
    }

    public List<VecD3D> splitIntoSegments(List<VecD3D> segments,
            double stepLength, boolean addFirst) {
        return splitIntoSegments(a, b, stepLength, segments, addFirst);
    }

    public RayD3D toRayD3D() {
        return new RayD3D(a.copy(), getDirection());
    }

    public String toString() {
        return a.toString() + " -> " + b.toString();
    }
}
