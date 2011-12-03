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

import toxi.geom.Line2D.LineIntersection.Type;

public class Line2D {

    public static class LineIntersection {

        public static enum Type {
            COINCIDENT,
            COINCIDENT_NO_INTERSECT,
            PARALLEL,
            NON_INTERSECTING,
            INTERSECTING
        }

        private final Type type;
        private final ReadonlyVec2D pos;
        private final float[] coeff;

        public LineIntersection(Type type, ReadonlyVec2D pos) {
            this(type, pos, 0, 0);
        }

        public LineIntersection(Type type, ReadonlyVec2D pos, float ua, float ub) {
            this.type = type;
            this.pos = pos;
            this.coeff = new float[] {
                    ua, ub
            };
        }

        public float[] getCoefficients() {
            return coeff;
        }

        /**
         * Returns copy of intersection point.
         * 
         * @return point
         */
        public Vec2D getPos() {
            return pos != null ? pos.copy() : null;
        }

        /**
         * Returns intersection type enum.
         * 
         * @return type
         */
        public Type getType() {
            return type;
        }

        public String toString() {
            return "type: " + type + " pos: " + pos;
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
    public static final List<Vec2D> splitIntoSegments(Vec2D a, Vec2D b,
            float stepLength, List<Vec2D> segments, boolean addFirst) {
        if (segments == null) {
            segments = new ArrayList<Vec2D>();
        }
        if (addFirst) {
            segments.add(a.copy());
        }
        float dist = a.distanceTo(b);
        if (dist > stepLength) {
            Vec2D pos = a.copy();
            Vec2D step = b.sub(a).limit(stepLength);
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
    public Vec2D a, b;

    public Line2D(float x1, float y1, float x2, float y2) {
        this.a = new Vec2D(x1, y1);
        this.b = new Vec2D(x2, y2);
    }

    public Line2D(ReadonlyVec2D a, ReadonlyVec2D b) {
        this.a = a.copy();
        this.b = b.copy();
    }

    public Line2D(Vec2D a, Vec2D b) {
        this.a = a;
        this.b = b;
    }

    /**
     * Computes the dot product of these 2 vectors: line start -> point and the
     * perpendicular line direction. If the result is negative
     * 
     * @param p
     * @return classifier float
     */
    public float classifyPoint(ReadonlyVec2D p) {
        Vec2D normal = b.sub(a).perpendicular();
        float d = p.sub(a).dot(normal);
        return Math.signum(d);
    }

    /**
     * Computes the closest point on this line to the point given.
     * 
     * @param p
     *            point to check against
     * @return closest point on the line
     */
    public Vec2D closestPointTo(ReadonlyVec2D p) {
        final Vec2D v = b.sub(a);
        final float t = p.sub(a).dot(v) / v.magSquared();
        // Check to see if t is beyond the extents of the line segment
        if (t < 0.0f) {
            return a.copy();
        } else if (t > 1.0f) {
            return b.copy();
        }
        // Return the point between 'a' and 'b'
        return a.add(v.scaleSelf(t));
    }

    public Line2D copy() {
        return new Line2D(a.copy(), b.copy());
    }

    public float distanceToPoint(ReadonlyVec2D p) {
        return closestPointTo(p).distanceTo(p);
    }

    public float distanceToPointSquared(ReadonlyVec2D p) {
        return closestPointTo(p).distanceToSquared(p);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Line2D)) {
            return false;
        }
        Line2D l = (Line2D) obj;
        return (a.equals(l.a) || a.equals(l.b))
                && (b.equals(l.b) || b.equals(l.a));
    }

    public Circle getBoundingCircle() {
        return Circle.from2Points(a, b);
    }

    /**
     * Returns the line's bounding rect.
     * 
     * @return bounding rect
     */
    public Rect getBounds() {
        return new Rect(a, b);
    }

    public Vec2D getDirection() {
        return b.sub(a).normalize();
    }

    public float getHeading() {
        return b.sub(a).heading();
    }

    public float getLength() {
        return a.distanceTo(b);
    }

    public float getLengthSquared() {
        return a.distanceToSquared(b);
    }

    public Vec2D getMidPoint() {
        return a.add(b).scaleSelf(0.5f);
    }

    public Vec2D getNormal() {
        return b.sub(a).perpendicular();
    }

    public boolean hasEndPoint(Vec2D p) {
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

    /**
     * Computes intersection between this and the given line. The returned value
     * is a {@link LineIntersection} instance and contains both the type of
     * intersection as well as the intersection points (if existing). The
     * intersection points are ALWAYS computed for the types INTERSECTING and
     * NON_INTERSECTING. In the latter case the points will lie outside the two
     * given line segments and constitute the intersections of the infinite
     * versions of each line.
     * 
     * Based on: http://local.wasp.uwa.edu.au/~pbourke/geometry/lineline2d/
     * 
     * @param l
     *            line to intersect with
     * @return intersection result
     */
    public LineIntersection intersectLine(Line2D l) {
        LineIntersection isec = null;
        float denom = (l.b.y - l.a.y) * (b.x - a.x) - (l.b.x - l.a.x)
                * (b.y - a.y);

        float na = (l.b.x - l.a.x) * (a.y - l.a.y) - (l.b.y - l.a.y)
                * (a.x - l.a.x);
        float nb = (b.x - a.x) * (a.y - l.a.y) - (b.y - a.y) * (a.x - l.a.x);

        if (denom != 0.0) {
            float ua = na / denom;
            float ub = nb / denom;
            final Vec2D i = a.interpolateTo(b, ua);
            if (ua >= 0.0f && ua <= 1.0 && ub >= 0.0 && ub <= 1.0) {
                isec = new LineIntersection(Type.INTERSECTING, i, ua, ub);
            } else {
                isec = new LineIntersection(Type.NON_INTERSECTING, i, ua, ub);
            }
        } else {
            if (na == 0.0 && nb == 0.0) {
                if (distanceToPoint(l.a) == 0.0) {
                    isec = new LineIntersection(Type.COINCIDENT, null);
                } else {
                    isec = new LineIntersection(Type.COINCIDENT_NO_INTERSECT,
                            null);
                }
            } else {
                isec = new LineIntersection(Type.PARALLEL, null);
            }
        }
        return isec;
    }

    public Line2D offsetAndGrowBy(float offset, float scale, Vec2D ref) {
        Vec2D m = getMidPoint();
        Vec2D d = getDirection();
        Vec2D n = d.getPerpendicular();
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

    public Line2D scaleLength(float scale) {
        float delta = (1 - scale) * 0.5f;
        Vec2D newA = a.interpolateTo(b, delta);
        b.interpolateToSelf(a, delta);
        a.set(newA);
        return this;
    }

    public Line2D set(Vec2D a, Vec2D b) {
        this.a = a;
        this.b = b;
        return this;
    }

    public List<Vec2D> splitIntoSegments(List<Vec2D> segments,
            float stepLength, boolean addFirst) {
        return splitIntoSegments(a, b, stepLength, segments, addFirst);
    }

    public Ray2D toRay2D() {
        return new Ray2D(a.copy(), getDirection());
    }

    public String toString() {
        return a.toString() + " -> " + b.toString();
    }
}
