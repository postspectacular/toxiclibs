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

import java.util.ArrayList;
import java.util.Random;

import javax.xml.bind.annotation.XmlAttribute;

import toxi.math.InterpolateStrategy;
import toxi.math.MathUtils;

/**
 * Comprehensive 2D vector class with additional basic intersection and
 * collision detection features.
 * 
 * @author Karsten Schmidt
 * 
 */
public class Vec2D implements Comparable<Vec2D>, ReadonlyVec2D {

    public static enum Axis {
        X, Y
    }

    /**
     * Defines positive X axis
     */
    public static final ReadonlyVec2D X_AXIS = new Vec2D(1, 0);

    /**
     * Defines positive Y axis
     */
    public static final ReadonlyVec2D Y_AXIS = new Vec2D(0, 1);;

    /** Defines the zero vector. */
    public static final ReadonlyVec2D ZERO = new Vec2D();

    /**
     * Defines vector with both coords set to Float.MIN_VALUE. Useful for
     * bounding box operations.
     */
    public static final ReadonlyVec2D MIN_VALUE =
            new Vec2D(Float.MIN_VALUE, Float.MIN_VALUE);

    /**
     * Defines vector with both coords set to Float.MAX_VALUE. Useful for
     * bounding box operations.
     */
    public static final ReadonlyVec2D MAX_VALUE =
            new Vec2D(Float.MAX_VALUE, Float.MAX_VALUE);

    /**
     * Creates a new vector from the given angle in the XY plane.
     * 
     * The resulting vector for theta=0 is equal to the positive X axis.
     * 
     * @param theta
     * @return new vector pointing into the direction of the passed in angle
     */
    public static final Vec2D fromTheta(float theta) {
        return new Vec2D((float) Math.cos(theta), (float) Math.sin(theta));
    }

    /**
     * Constructs a new vector consisting of the largest components of both
     * vectors.
     * 
     * @param b
     *            the b
     * @param a
     *            the a
     * 
     * @return result as new vector
     */
    public static final Vec2D max(Vec2D a, Vec2D b) {
        return new Vec2D(MathUtils.max(a.x, b.x), MathUtils.max(a.y, b.y));
    }

    /**
     * Constructs a new vector consisting of the smallest components of both
     * vectors.
     * 
     * @param b
     *            comparing vector
     * @param a
     *            the a
     * 
     * @return result as new vector
     */
    public static final Vec2D min(Vec2D a, Vec2D b) {
        return new Vec2D(MathUtils.min(a.x, b.x), MathUtils.min(a.y, b.y));
    }

    /**
     * Static factory method. Creates a new random unit vector using the default
     * Math.random() Random instance.
     * 
     * @return a new random normalized unit vector.
     */
    public static final Vec2D randomVector() {
        Vec2D rnd =
                new Vec2D((float) Math.random() * 2 - 1,
                        (float) Math.random() * 2 - 1);
        return rnd.normalize();
    }

    /**
     * Static factory method. Creates a new random unit vector using the given
     * Random generator instance. I recommend to have a look at the
     * https://uncommons-maths.dev.java.net library for a good choice of
     * reliable and high quality random number generators.
     * 
     * @return a new random normalized unit vector.
     */
    public static final Vec2D randomVector(Random rnd) {
        Vec2D v = new Vec2D(rnd.nextFloat() * 2 - 1, rnd.nextFloat() * 2 - 1);
        return v.normalize();
    }

    /**
     * X coordinate
     */
    @XmlAttribute(required = true)
    public float x;

    /**
     * Y coordinate
     */
    @XmlAttribute(required = true)
    public float y;

    /**
     * Creates a new zero vector
     */
    public Vec2D() {
        x = y = 0;
    }

    /**
     * Creates a new vector with the given coordinates
     * 
     * @param x
     * @param y
     */
    public Vec2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a new vector with the coordinates of the given vector
     * 
     * @param v
     *            vector to be copied
     */
    public Vec2D(Vec2D v) {
        set(v);
    }

    public final Vec2D abs() {
        x = MathUtils.abs(x);
        y = MathUtils.abs(y);
        return this;
    }

    public final Vec2D add(float a, float b) {
        return new Vec2D(x + a, y + b);
    }

    public Vec2D add(ReadonlyVec2D v) {
        return v.copy().addSelf(this);
    }

    public final Vec2D add(Vec2D v) {
        return new Vec2D(x + v.x, y + v.y);
    }

    /**
     * Adds vector {a,b,c} and overrides coordinates with result.
     * 
     * @param a
     *            X coordinate
     * @param b
     *            Y coordinate
     * @return itself
     */
    public final Vec2D addSelf(float a, float b) {
        x += a;
        y += b;
        return this;
    }

    /**
     * Adds vector v and overrides coordinates with result.
     * 
     * @param v
     *            vector to add
     * @return itself
     */
    public final Vec2D addSelf(Vec2D v) {
        x += v.x;
        y += v.y;
        return this;
    }

    public final float angleBetween(Vec2D v) {
        return (float) Math.acos(dot(v));
    }

    public final float angleBetween(Vec2D v, boolean forceNormalize) {
        float theta;
        if (forceNormalize) {
            theta = getNormalized().dot(v.getNormalized());
        } else {
            theta = dot(v);
        }
        return (float) Math.acos(theta);
    }

    /**
     * Sets all vector components to 0.
     * 
     * @return itself
     */
    public final Vec2D clear() {
        x = y = 0;
        return this;
    }

    public Vec2D closestPointOnLine(Vec2D a, Vec2D b) {
        final Vec2D v = b.sub(a);
        final float t = sub(a).dot(v) / v.magSquared();
        // Check to see if t is beyond the extents of the line segment
        if (t < 0.0f) {
            return a;
        }
        if (t > 1.0f) {
            return b;
        }
        // Return the point between 'a' and 'b'
        return a.add(v.scaleSelf(t));
    }

    public Vec2D closestPointOnTriangle(Vec2D a, Vec2D b, Vec2D c) {
        Vec2D Rab = closestPointOnLine(a, b);
        Vec2D Rbc = closestPointOnLine(b, c);
        Vec2D Rca = closestPointOnLine(c, a);

        float dAB = sub(Rab).magnitude();
        float dBC = sub(Rbc).magnitude();
        float dCA = sub(Rca).magnitude();

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

    public int compareTo(ReadonlyVec2D v) {
        if (x == v.x() && y == v.y()) {
            return 0;
        }
        return (int) (magSquared() - v.magSquared());
    }

    public int compareTo(Vec2D v) {
        if (x == v.x && y == v.y) {
            return 0;
        }
        return (int) (magSquared() - v.magSquared());
    }

    /**
     * Forcefully fits the vector in the given rectangle.
     * 
     * @param r
     * @return itself
     */
    public final Vec2D constrain(Rect r) {
        x = MathUtils.clip(x, r.x, r.x + r.width);
        y = MathUtils.clip(y, r.y, r.y + r.height);
        return this;
    }

    /**
     * Forcefully fits the vector in the given rectangle defined by the points.
     * 
     * @param min
     * @param max
     * @return itself
     */
    public final Vec2D constrain(Vec2D min, Vec2D max) {
        x = MathUtils.clip(x, min.x, max.x);
        y = MathUtils.clip(y, min.y, max.y);
        return this;
    }

    public final Vec2D copy() {
        return new Vec2D(this);
    }

    public float cross(Vec2D v) {
        return (x * v.y) - (y * v.x);
    }

    public final float distanceTo(Vec2D v) {
        if (v != null) {
            float dx = x - v.x;
            float dy = y - v.y;
            return (float) Math.sqrt(dx * dx + dy * dy);
        } else {
            return Float.NaN;
        }
    }

    public final float distanceToSquared(Vec2D v) {
        if (v != null) {
            float dx = x - v.x;
            float dy = y - v.y;
            return dx * dx + dy * dy;
        } else {
            return Float.NaN;
        }
    }

    public final float dot(ReadonlyVec2D v) {
        return x * v.x() + y * v.y();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vec2D) {
            final Vec2D v = (Vec2D) obj;
            return x == v.x && y == v.y;
        }
        return false;
    }

    public boolean equalsWithTolerance(Vec2D v, float tolerance) {
        if (MathUtils.abs(x - v.x) < tolerance) {
            if (MathUtils.abs(y - v.y) < tolerance) {
                return true;
            }
        }
        return false;
    }

    /**
     * Replaces the vector components with integer values of their current
     * values
     * 
     * @return itself
     */
    public final Vec2D floor() {
        x = MathUtils.floor(x);
        y = MathUtils.floor(y);
        return this;
    }

    /**
     * Replaces the vector components with the fractional part of their current
     * values
     * 
     * @return itself
     */
    public final Vec2D frac() {
        x -= MathUtils.floor(x);
        y -= MathUtils.floor(y);
        return this;
    }

    public final Vec2D getAbs() {
        return new Vec2D(this).abs();
    }

    public float getComponent(Axis id) {
        switch (id) {
            case X:
                return x;
            case Y:
                return y;
        }
        return 0;
    }

    public final float getComponent(int id) {
        switch (id) {
            case 0:
                return x;
            case 1:
                return y;
        }
        throw new IllegalArgumentException("index must be 0 or 1");
    }

    public final Vec2D getConstrained(Rect r) {
        return new Vec2D(this).constrain(r);
    }

    public final Vec2D getFloored() {
        return new Vec2D(this).floor();
    }

    public final Vec2D getFrac() {
        return new Vec2D(this).frac();
    }

    public final Vec2D getInverted() {
        return new Vec2D(-x, -y);
    }

    public final Vec2D getLimited(float lim) {
        if (magSquared() > lim * lim) {
            return getNormalized().scaleSelf(lim);
        }
        return new Vec2D(this);
    }

    public final Vec2D getNormalized() {
        return new Vec2D(this).normalize();
    }

    public final Vec2D getNormalizedTo(float len) {
        return getNormalized().scaleSelf(len);
    }

    public final Vec2D getPerpendicular() {
        return new Vec2D(this).perpendicular();
    }

    public final Vec2D getReciprocal() {
        return copy().reciprocal();
    }

    public final Vec2D getReflected(Vec2D normal) {
        return copy().reflect(normal);
    }

    public final Vec2D getRotated(float theta) {
        return new Vec2D(this).rotate(theta);
    }

    public Vec2D getSignum() {
        return new Vec2D(this).signum();
    }

    /**
     * Returns a unique code for this vector object based on it's values. If two
     * vectors are logically equivalent, they will return the same hash code
     * value.
     * 
     * @return the hash code value of this vector.
     */
    public int hashCode() {
        return 37 * Float.floatToIntBits(x) + Float.floatToIntBits(y);
    }

    public final float heading() {
        return (float) Math.atan2(y, x);
    }

    public Vec2D interpolateTo(ReadonlyVec2D v, float f) {
        return new Vec2D(x + (v.x() - x) * f, y + (v.y() - y) * f);
    }

    public Vec2D interpolateTo(ReadonlyVec2D v, float f, InterpolateStrategy s) {
        return new Vec2D(s.interpolate(x, v.x(), f), s.interpolate(y, v.y(), f));
    }

    public final Vec2D interpolateTo(Vec2D v, float f) {
        return new Vec2D(x + (v.x - x) * f, y + (v.y - y) * f);
    }

    public Vec2D interpolateTo(Vec2D v, float f, InterpolateStrategy s) {
        return new Vec2D(s.interpolate(x, v.x, f), s.interpolate(y, v.y, f));
    }

    /**
     * Interpolates the vector towards the given target vector, using linear
     * interpolation
     * 
     * @param v
     *            target vector
     * @param f
     *            interpolation factor (should be in the range 0..1)
     * @return itself, result overrides current vector
     */
    public final Vec2D interpolateToSelf(Vec2D v, float f) {
        x += (v.x - x) * f;
        y += (v.y - y) * f;
        return this;
    }

    /**
     * Interpolates the vector towards the given target vector, using the given
     * {@link InterpolateStrategy}
     * 
     * @param v
     *            target vector
     * @param f
     *            interpolation factor (should be in the range 0..1)
     * @param s
     *            InterpolateStrategy instance
     * @return itself, result overrides current vector
     */
    public Vec2D interpolateToSelf(Vec2D v, float f, InterpolateStrategy s) {
        x = s.interpolate(x, v.x, f);
        y = s.interpolate(y, v.y, f);
        return this;
    }

    public float intersectRayCircle(ReadonlyVec2D rayDir,
            ReadonlyVec2D circleOrigin, float circleRadius) {
        ReadonlyVec2D q = circleOrigin.sub(this);
        float distSquared = q.magSquared();
        float v = q.dot(rayDir);
        float d = circleRadius * circleRadius - (distSquared - v * v);

        // If there was no intersection, return -1
        if (d < 0.0) {
            return -1;
        }

        // Return the distance to the [first] intersecting point
        return v - (float) Math.sqrt(d);
    }

    /**
     * Scales vector uniformly by factor -1 ( v = -v ), overrides coordinates
     * with result
     * 
     * @return itself
     */
    public final Vec2D invert() {
        x *= -1;
        y *= -1;
        return this;
    }

    public boolean isInCircle(ReadonlyVec2D sO, float sR) {
        float d = sub(sO).magSquared();
        return (d <= sR * sR);
    }

    public boolean isInRectangle(Rect r) {
        if (x < r.x || x > r.x + r.width) {
            return false;
        }
        if (y < r.y || y > r.y + r.height) {
            return false;
        }
        return true;
    }

    public boolean isInTriangle(Vec2D a, Vec2D b, Vec2D c) {
        Vec2D v1 = sub(a).normalize();
        Vec2D v2 = sub(b).normalize();
        Vec2D v3 = sub(c).normalize();

        double total_angles = Math.acos(v1.dot(v2));
        total_angles += Math.acos(v2.dot(v3));
        total_angles += Math.acos(v3.dot(v1));

        return (MathUtils.abs((float) total_angles - MathUtils.TWO_PI) <= 0.005f);
    }

    public final boolean isZeroVector() {
        return MathUtils.abs(x) < MathUtils.EPS
                && MathUtils.abs(y) < MathUtils.EPS;
    }

    public final ReadonlyVec2D jitter(float j) {
        return jitter(j, j);
    }

    /**
     * Adds random jitter to the vector.
     * 
     * @param jx
     *            maximum x jitter
     * @param jy
     *            maximum y jitter
     * @return itself
     */
    public final Vec2D jitter(float jx, float jy) {
        x += MathUtils.normalizedRandom() * jx;
        y += MathUtils.normalizedRandom() * jy;
        return this;
    }

    public final Vec2D jitter(Random rnd, float j) {
        return jitter(rnd, j, j);
    }

    public final Vec2D jitter(Random rnd, float jx, float jy) {
        x += MathUtils.normalizedRandom(rnd) * jx;
        y += MathUtils.normalizedRandom(rnd) * jy;
        return this;
    }

    public final Vec2D jitter(Random rnd, Vec2D jv) {
        return jitter(rnd, jv.x, jv.y);
    }

    public final Vec2D jitter(Vec2D jv) {
        return jitter(jv.x, jv.y);
    }

    /**
     * Limits the vector's magnitude to the length given
     * 
     * @param lim
     *            new maximum magnitude
     * @return itself
     */
    public final Vec2D limit(float lim) {
        if (magSquared() > lim * lim) {
            return normalize().scaleSelf(lim);
        }
        return this;
    }

    public final float magnitude() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public final float magSquared() {
        return x * x + y * y;
    }

    public final Vec2D max(Vec2D v) {
        return new Vec2D(MathUtils.max(x, v.x), MathUtils.max(y, v.y));
    }

    /**
     * Adjusts the vector components to the maximum values of both vectors
     * 
     * @param v
     * @return itself
     */
    public final Vec2D maxSelf(Vec2D v) {
        x = MathUtils.max(x, v.x);
        y = MathUtils.max(y, v.y);
        return this;
    }

    public final Vec2D min(Vec2D v) {
        return new Vec2D(MathUtils.min(x, v.x), MathUtils.min(y, v.y));
    }

    /**
     * Adjusts the vector components to the minimum values of both vectors
     * 
     * @param v
     * @return itself
     */
    public final Vec2D minSelf(Vec2D v) {
        x = MathUtils.min(x, v.x);
        y = MathUtils.min(y, v.y);
        return this;
    }

    /**
     * Normalizes the vector so that its magnitude = 1
     * 
     * @return itself
     */
    public final Vec2D normalize() {
        float mag = x * x + y * y;
        if (mag > 0) {
            mag = 1f / (float) Math.sqrt(mag);
            x *= mag;
            y *= mag;
        }
        return this;
    }

    /**
     * Normalizes the vector to the given length.
     * 
     * @param len
     *            desired length
     * @return itself
     */
    public final Vec2D normalizeTo(float len) {
        return normalize().scaleSelf(len);
    }

    public final Vec2D perpendicular() {
        float t = x;
        x = -y;
        y = t;
        return this;
    }

    /**
     * Checks if the point is within the convex polygon defined by the points in
     * the given list
     * 
     * @param vertices
     * @return true, if inside polygon
     * 
     * @deprecated use {@link Polygon2D#containsPoint(Vec2D)} instead
     */
    @Deprecated
    public boolean pointInPolygon(ArrayList<Vec2D> vertices) {
        return new Polygon2D(vertices).containsPoint(this);
    }

    public final Vec2D reciprocal() {
        x = 1f / x;
        y = 1f / y;
        return this;
    }

    public final Vec2D reflect(Vec2D normal) {
        return set(normal.scale(this.dot(normal) * 2).subSelf(this));
    }

    /**
     * Rotates the vector by the given angle around the Z axis.
     * 
     * @param theta
     * @return itself
     */
    public final Vec2D rotate(float theta) {
        float co = (float) Math.cos(theta);
        float si = (float) Math.sin(theta);
        float xx = co * x - si * y;
        y = si * x + co * y;
        x = xx;
        return this;
    }

    public final Vec2D scale(float s) {
        return new Vec2D(x * s, y * s);
    }

    public final Vec2D scale(float a, float b) {
        return new Vec2D(x * a, y * b);
    }

    public Vec2D scale(ReadonlyVec2D s) {
        return s.copy().scaleSelf(this);
    }

    public final Vec2D scale(Vec2D s) {
        return new Vec2D(x * s.x, y * s.y);
    }

    /**
     * Scales vector uniformly and overrides coordinates with result
     * 
     * @param s
     *            scale factor
     * @return itself
     */
    public final Vec2D scaleSelf(float s) {
        x *= s;
        y *= s;
        return this;
    }

    /**
     * Scales vector non-uniformly by vector {a,b,c} and overrides coordinates
     * with result
     * 
     * @param a
     *            scale factor for X coordinate
     * @param b
     *            scale factor for Y coordinate
     * @return itself
     */
    public final Vec2D scaleSelf(float a, float b) {
        x *= a;
        y *= b;
        return this;
    }

    /**
     * Scales vector non-uniformly by vector v and overrides coordinates with
     * result
     * 
     * @param s
     *            scale vector
     * @return itself
     */

    public final Vec2D scaleSelf(Vec2D s) {
        x *= s.x;
        y *= s.y;
        return this;
    }

    /**
     * Overrides coordinates with the given values
     * 
     * @param x
     * @param y
     * @return itself
     */
    public final Vec2D set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vec2D set(ReadonlyVec2D v) {
        x = v.x();
        y = v.y();
        return this;
    }

    /**
     * Overrides coordinates with the ones of the given vector
     * 
     * @param v
     *            vector to be copied
     * @return itself
     */
    public final Vec2D set(Vec2D v) {
        x = v.x;
        y = v.y;
        return this;
    }

    public final Vec2D setComponent(Axis id, float val) {
        switch (id) {
            case X:
                x = val;
                break;
            case Y:
                y = val;
                break;
        }
        return this;
    }

    public final Vec2D setComponent(int id, float val) {
        switch (id) {
            case 0:
                x = val;
                break;
            case 1:
                y = val;
                break;
        }
        return this;
    }

    /**
     * Replaces all vector components with the signum of their original values.
     * In other words if a components value was negative its new value will be
     * -1, if zero => 0, if positive => +1
     * 
     * @return itself
     */
    public final Vec2D signum() {
        x = (x < 0 ? -1 : x == 0 ? 0 : 1);
        y = (y < 0 ? -1 : y == 0 ? 0 : 1);
        return this;
    }

    public final Vec2D sub(float a, float b) {
        return new Vec2D(x - a, y - b);
    }

    public final Vec2D sub(ReadonlyVec2D v) {
        return new Vec2D(x - v.x(), y - v.y());
    }

    public final Vec2D sub(Vec2D v) {
        return new Vec2D(x - v.x, y - v.y);
    }

    /**
     * Subtracts vector {a,b,c} and overrides coordinates with result.
     * 
     * @param a
     *            X coordinate
     * @param b
     *            Y coordinate
     * @return itself
     */
    public final Vec2D subSelf(float a, float b) {
        x -= a;
        y -= b;
        return this;
    }

    /**
     * Subtracts vector v and overrides coordinates with result.
     * 
     * @param v
     *            vector to be subtracted
     * @return itself
     */
    public final Vec2D subSelf(Vec2D v) {
        x -= v.x;
        y -= v.y;
        return this;
    }

    public Vec2D tangentNormalOfEllipse(Vec2D eO, Vec2D eR) {
        Vec2D p = this.sub(eO);

        float xr2 = eR.x * eR.x;
        float yr2 = eR.y * eR.y;

        return new Vec2D(p.x / xr2, p.y / yr2).normalize();
    }

    public Vec3D to3DXY() {
        return new Vec3D(x, y, 0);
    }

    public Vec3D to3DXZ() {
        return new Vec3D(x, 0, y);
    }

    public Vec3D to3DYZ() {
        return new Vec3D(0, x, y);
    }

    public float[] toArray() {
        return new float[] { x, y };
    }

    public Vec2D toCartesian() {
        float xx = (float) (x * Math.cos(y));
        y = (float) (x * Math.sin(y));
        x = xx;
        return this;
    }

    public Vec2D toPolar() {
        float r = (float) Math.sqrt(x * x + y * y);
        y = (float) Math.atan2(y, x);
        x = r;
        return this;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(32);
        sb.append("{x:").append(x).append(", y:").append(y).append("}");
        return sb.toString();
    }

    public final float x() {
        return x;
    }

    public final float y() {
        return y;
    }
}