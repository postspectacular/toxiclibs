package toxi.geom;

import java.util.ArrayList;
import java.util.List;

import toxi.geom.Line3D.LineIntersection.Type;
import toxi.math.MathUtils;

public class Line3D {

    public static class LineIntersection {

        public static enum Type {
            NON_INTERSECTING, INTERSECTING
        }

        private final Type type;
        private final Line3D line;
        private float[] coeff;

        private LineIntersection(Type type) {
            this(type, null, 0, 0);
        }

        private LineIntersection(Type type, Line3D line, float mua, float mub) {
            this.type = type;
            this.line = line;
            this.coeff = new float[] { mua, mub };
        }

        public float[] getCoefficients() {
            return coeff;
        }

        public float getLength() {
            return line.getLength();
        }

        /**
         * @return the pos
         */
        public Line3D getLine() {
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
    public static final List<Vec3D> splitIntoSegments(Vec3D a, Vec3D b,
            float stepLength, List<Vec3D> segments, boolean addFirst) {
        if (segments == null) {
            segments = new ArrayList<Vec3D>();
        }
        if (addFirst) {
            segments.add(a.copy());
        }
        float dist = a.distanceTo(b);
        if (dist > stepLength) {
            Vec3D pos = a.copy();
            Vec3D step = b.sub(a).limit(stepLength);
            while (dist > stepLength) {
                pos.addSelf(step);
                segments.add(pos.copy());
                dist -= stepLength;
            }
        }
        segments.add(b.copy());
        return segments;
    }

    public Vec3D a, b;

    public Line3D(ReadonlyVec3D a, ReadonlyVec3D b) {
        this.a = a.copy();
        this.b = b.copy();
    }

    public Line3D(Vec3D a, Vec3D b) {
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
    public LineIntersection closestLineTo(Line3D l) {

        Vec3D p43 = l.a.sub(l.b);
        if (p43.isZeroVector()) {
            return new LineIntersection(Type.NON_INTERSECTING);
        }
        Vec3D p21 = b.sub(a);
        if (p21.isZeroVector()) {
            return new LineIntersection(Type.NON_INTERSECTING);
        }
        Vec3D p13 = a.sub(l.a);

        float d1343 = p13.x * p43.x + p13.y * p43.y + p13.z * p43.z;
        float d4321 = p43.x * p21.x + p43.y * p21.y + p43.z * p21.z;
        float d1321 = p13.x * p21.x + p13.y * p21.y + p13.z * p21.z;
        float d4343 = p43.x * p43.x + p43.y * p43.y + p43.z * p43.z;
        float d2121 = p21.x * p21.x + p21.y * p21.y + p21.z * p21.z;

        float denom = d2121 * d4343 - d4321 * d4321;
        if (MathUtils.abs(denom) < MathUtils.EPS) {
            return new LineIntersection(Type.NON_INTERSECTING);
        }
        float numer = d1343 * d4321 - d1321 * d4343;
        float mua = numer / denom;
        float mub = (d1343 + d4321 * mua) / d4343;

        Vec3D pa = a.add(p21.scaleSelf(mua));
        Vec3D pb = l.a.add(p43.scaleSelf(mub));
        return new LineIntersection(Type.INTERSECTING, new Line3D(pa, pb), mua,
                mub);
    }

    /**
     * Computes the closest point on this line to the given one.
     * 
     * @param p
     *            point to check against
     * @return closest point on the line
     */
    public Vec3D closestPointTo(ReadonlyVec3D p) {
        final Vec3D v = b.sub(a);
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

    public Line3D copy() {
        return new Line3D(a.copy(), b.copy());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Line3D)) {
            return false;
        }
        Line3D l = (Line3D) obj;
        return (a.equals(l.a) || a.equals(l.b))
                && (b.equals(l.b) || b.equals(l.a));
    }

    public Vec3D getDirection() {
        return b.sub(a).normalize();
    }

    public float getLength() {
        return a.distanceTo(b);
    }

    public float getLengthSquared() {
        return a.distanceToSquared(b);
    }

    public Vec3D getMidPoint() {
        return a.add(b).scaleSelf(0.5f);
    }

    public Vec3D getNormal() {
        return b.cross(a);
    }

    public boolean hasEndPoint(Vec3D p) {
        return a.equals(p) || b.equals(p);
    }

    @Override
    public int hashCode() {
        return a.hashCode() + b.hashCode();
    }

    public Line3D set(Vec3D a, Vec3D b) {
        this.a = a;
        this.b = b;
        return this;
    }

    public List<Vec3D> splitIntoSegments(List<Vec3D> segments,
            float stepLength, boolean addFirst) {
        return splitIntoSegments(a, b, stepLength, segments, addFirst);
    }

    public Ray3D toRay3D() {
        return new Ray3D(a.copy(), b.sub(a).normalize());
    }

    public String toString() {
        return a.toString() + " -> " + b.toString();
    }
}
