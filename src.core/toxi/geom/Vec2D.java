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

import java.util.Random;

import javax.xml.bind.annotation.XmlAttribute;

import toxi.math.InterpolateStrategy;
import toxi.math.MathUtils;
import toxi.math.ScaleMap;

/**
 * Comprehensive 2D vector class with additional basic intersection and
 * collision detection features.
 */
public class Vec2D implements Comparable<ReadonlyVec2D>, ReadonlyVec2D {

    public static enum Axis {

        X(Vec2D.X_AXIS),
        Y(Vec2D.Y_AXIS);

        private final ReadonlyVec2D vector;

        private Axis(ReadonlyVec2D v) {
            this.vector = v;
        }

        public ReadonlyVec2D getVector() {
            return vector;
        }
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
    public static final ReadonlyVec2D MIN_VALUE = new Vec2D(Float.MIN_VALUE,
            Float.MIN_VALUE);

    /**
     * Defines vector with both coords set to Float.MAX_VALUE. Useful for
     * bounding box operations.
     */
    public static final ReadonlyVec2D MAX_VALUE = new Vec2D(Float.MAX_VALUE,
            Float.MAX_VALUE);

    public static final ReadonlyVec2D NEG_MAX_VALUE = new Vec2D(
            -Float.MAX_VALUE, -Float.MAX_VALUE);

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
    public static final Vec2D max(ReadonlyVec2D a, ReadonlyVec2D b) {
        return new Vec2D(MathUtils.max(a.x(), b.x()), MathUtils.max(a.y(),
                b.y()));
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
    public static final Vec2D min(ReadonlyVec2D a, ReadonlyVec2D b) {
        return new Vec2D(MathUtils.min(a.x(), b.x()), MathUtils.min(a.y(),
                b.y()));
    }

    /**
     * Static factory method. Creates a new random unit vector using the Random
     * implementation set as default for the {@link MathUtils} class.
     * 
     * @return a new random normalized unit vector.
     */
    public static final Vec2D randomVector() {
        return randomVector(MathUtils.RND);
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

    public Vec2D(float[] v) {
        this.x = v[0];
        this.y = v[1];
    }

    /**
     * Creates a new vector with the coordinates of the given vector
     * 
     * @param v
     *            vector to be copied
     */
    public Vec2D(ReadonlyVec2D v) {
        this.x = v.x();
        this.y = v.y();
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
        return new Vec2D(x + v.x(), y + v.y());
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

    public final float angleBetween(ReadonlyVec2D v) {
        return (float) Math.acos(dot(v));
    }

    public final float angleBetween(ReadonlyVec2D v, boolean forceNormalize) {
        float theta;
        if (forceNormalize) {
            theta = getNormalized().dot(v.getNormalized());
        } else {
            theta = dot(v);
        }
        return (float) Math.acos(MathUtils.clipNormalized(theta));
    }

    public Vec3D bisect(Vec2D b) {
        Vec2D diff = this.sub(b);
        Vec2D sum = this.add(b);
        float dot = diff.dot(sum);
        return new Vec3D(diff.x, diff.y, -dot / 2);
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

    public int compareTo(ReadonlyVec2D v) {
        if (x == v.x() && y == v.y()) {
            return 0;
        }
        float a = magSquared();
        float b = v.magSquared();
        if (a < b) {
            return -1;
        }
        return +1;
    }

    /**
     * Constraints this vector to the perimeter of the given polygon. Unlike the
     * {@link #constrain(Rect)} version of this method, this version DOES NOT
     * check containment automatically. If you want to only constrain a point if
     * its (for example) outside the polygon, then check containment with
     * {@link Polygon2D#containsPoint(ReadonlyVec2D)} first before calling this
     * method.
     * 
     * @param poly
     * @return itself
     */
    public Vec2D constrain(Polygon2D poly) {
        float minD = Float.MAX_VALUE;
        Vec2D q = null;
        for (Line2D l : poly.getEdges()) {
            Vec2D c = l.closestPointTo(this);
            float d = c.distanceToSquared(this);
            if (d < minD) {
                q = c;
                minD = d;
            }
        }
        x = q.x;
        y = q.y;
        return this;
    }

    /**
     * Forcefully fits the vector in the given rectangle.
     * 
     * @param r
     * @return itself
     */
    public Vec2D constrain(Rect r) {
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
    public Vec2D constrain(Vec2D min, Vec2D max) {
        x = MathUtils.clip(x, min.x, max.x);
        y = MathUtils.clip(y, min.y, max.y);
        return this;
    }

    public final Vec2D copy() {
        return new Vec2D(this);
    }

    public float cross(ReadonlyVec2D v) {
        return (x * v.y()) - (y * v.x());
    }

    public final float distanceTo(ReadonlyVec2D v) {
        if (v != null) {
            float dx = x - v.x();
            float dy = y - v.y();
            return (float) Math.sqrt(dx * dx + dy * dy);
        } else {
            return Float.NaN;
        }
    }

    public final float distanceToSquared(ReadonlyVec2D v) {
        if (v != null) {
            float dx = x - v.x();
            float dy = y - v.y();
            return dx * dx + dy * dy;
        } else {
            return Float.NaN;
        }
    }

    public final float dot(ReadonlyVec2D v) {
        return x * v.x() + y * v.y();
    }

    /**
     * Returns true if the Object v is of type ReadonlyVec2D and all of the data
     * members of v are equal to the corresponding data members in this vector.
     * 
     * @param v
     *            the object with which the comparison is made
     * @return true or false
     */
    public boolean equals(Object v) {
        try {
            ReadonlyVec2D vv = (ReadonlyVec2D) v;
            return (x == vv.x() && y == vv.y());
        } catch (NullPointerException e) {
            return false;
        } catch (ClassCastException e) {
            return false;
        }

    }

    /**
     * Returns true if all of the data members of ReadonlyVec2D v are equal to
     * the corresponding data members in this vector.
     * 
     * @param v
     *            the vector with which the comparison is made
     * @return true or false
     */
    public boolean equals(ReadonlyVec2D v) {
        try {
            return (x == v.x() && y == v.y());
        } catch (NullPointerException e) {
            return false;
        }

    }

    public boolean equalsWithTolerance(ReadonlyVec2D v, float tolerance) {
        try {
            float diff = x - v.x();
            if (Float.isNaN(diff)) {
                return false;
            }
            if ((diff < 0 ? -diff : diff) > tolerance) {
                return false;
            }
            diff = y - v.y();
            if (Float.isNaN(diff)) {
                return false;
            }
            if ((diff < 0 ? -diff : diff) > tolerance) {
                return false;
            }
            return true;
        } catch (NullPointerException e) {
            return false;
        }
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

    public Vec2D getCartesian() {
        return copy().toCartesian();
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

    public final Vec2D getConstrained(Polygon2D poly) {
        return new Vec2D(this).constrain(poly);
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
            return getNormalizedTo(lim);
        }
        return new Vec2D(this);
    }

    public Vec2D getMapped(ScaleMap map) {
        return new Vec2D((float) map.getClippedValueFor(x),
                (float) map.getClippedValueFor(y));
    }

    public final Vec2D getNormalized() {
        return new Vec2D(this).normalize();
    }

    public final Vec2D getNormalizedTo(float len) {
        return new Vec2D(this).normalizeTo(len);
    }

    public final Vec2D getPerpendicular() {
        return new Vec2D(this).perpendicular();
    }

    public Vec2D getPolar() {
        return copy().toPolar();
    }

    public final Vec2D getReciprocal() {
        return copy().reciprocal();
    }

    public final Vec2D getReflected(ReadonlyVec2D normal) {
        return copy().reflect(normal);
    }

    public final Vec2D getRotated(float theta) {
        return new Vec2D(this).rotate(theta);
    }

    public Vec2D getRoundedTo(float prec) {
        return copy().roundTo(prec);
    }

    public Vec2D getSignum() {
        return new Vec2D(this).signum();
    }

    /**
     * Returns a hash code value based on the data values in this object. Two
     * different Vec2D objects with identical data values (i.e., Vec2D.equals
     * returns true) will return the same hash code value. Two objects with
     * different data members may return the same hash value, although this is
     * not likely.
     * 
     * @return the hash code value of this vector.
     */
    public int hashCode() {
        long bits = 1L;
        bits = 31L * bits + VecMathUtil.floatToIntBits(x);
        bits = 31L * bits + VecMathUtil.floatToIntBits(y);
        return (int) (bits ^ (bits >> 32));
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
    public final Vec2D interpolateToSelf(ReadonlyVec2D v, float f) {
        x += (v.x() - x) * f;
        y += (v.y() - y) * f;
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
    public Vec2D interpolateToSelf(ReadonlyVec2D v, float f,
            InterpolateStrategy s) {
        x = s.interpolate(x, v.x(), f);
        y = s.interpolate(y, v.y(), f);
        return this;
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

    public final boolean isMajorAxis(float tol) {
        float ax = MathUtils.abs(x);
        float ay = MathUtils.abs(y);
        float itol = 1 - tol;
        if (ax > itol) {
            return (ay < tol);
        } else if (ay > itol) {
            return (ax < tol);
        }
        return false;
    }

    public final boolean isZeroVector() {
        return MathUtils.abs(x) < MathUtils.EPS
                && MathUtils.abs(y) < MathUtils.EPS;
    }

    public final Vec2D jitter(float j) {
        return jitter(j, j);
    }

    /**
     * Adds random jitter to the vector in the range -j ... +j using the default
     * {@link Random} generator of {@link MathUtils}.
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

    public final Vec2D max(ReadonlyVec2D v) {
        return new Vec2D(MathUtils.max(x, v.x()), MathUtils.max(y, v.y()));
    }

    /**
     * Adjusts the vector components to the maximum values of both vectors
     * 
     * @param v
     * @return itself
     */
    public final Vec2D maxSelf(ReadonlyVec2D v) {
        x = MathUtils.max(x, v.x());
        y = MathUtils.max(y, v.y());
        return this;
    }

    public final Vec2D min(ReadonlyVec2D v) {
        return new Vec2D(MathUtils.min(x, v.x()), MathUtils.min(y, v.y()));
    }

    /**
     * Adjusts the vector components to the minimum values of both vectors
     * 
     * @param v
     * @return itself
     */
    public final Vec2D minSelf(ReadonlyVec2D v) {
        x = MathUtils.min(x, v.x());
        y = MathUtils.min(y, v.y());
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
        float mag = (float) Math.sqrt(x * x + y * y);
        if (mag > 0) {
            mag = len / mag;
            x *= mag;
            y *= mag;
        }
        return this;
    }

    public final Vec2D perpendicular() {
        float t = x;
        x = -y;
        y = t;
        return this;
    }

    public final float positiveHeading() {
        double dist = Math.sqrt(x * x + y * y);
        if (y >= 0) {
            return (float) Math.acos(x / dist);
        } else {
            return (float) (Math.acos(-x / dist) + MathUtils.PI);
        }
    }

    public final Vec2D reciprocal() {
        x = 1f / x;
        y = 1f / y;
        return this;
    }

    public final Vec2D reflect(ReadonlyVec2D normal) {
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

    public Vec2D roundTo(float prec) {
        x = MathUtils.roundTo(x, prec);
        y = MathUtils.roundTo(y, prec);
        return this;
    }

    public final Vec2D scale(float s) {
        return new Vec2D(x * s, y * s);
    }

    public final Vec2D scale(float a, float b) {
        return new Vec2D(x * a, y * b);
    }

    public final Vec2D scale(ReadonlyVec2D s) {
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

    public final Vec2D set(ReadonlyVec2D v) {
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
            default:
                throw new IllegalArgumentException(
                        "component id needs to be 0 or 1");
        }
        return this;
    }

    public Vec2D setX(float x) {
        this.x = x;
        return this;
    }

    public Vec2D setY(float y) {
        this.y = y;
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

    /**
     * Rounds the vector to the closest major axis. Assumes the vector is
     * normalized.
     * 
     * @return itself
     */
    public final Vec2D snapToAxis() {
        if (MathUtils.abs(x) < 0.5f) {
            x = 0;
        } else {
            x = x < 0 ? -1 : 1;
            y = 0;
        }
        if (MathUtils.abs(y) < 0.5f) {
            y = 0;
        } else {
            y = y < 0 ? -1 : 1;
            x = 0;
        }
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

    public final Vec2D tangentNormalOfEllipse(Vec2D eO, Vec2D eR) {
        Vec2D p = this.sub(eO);

        float xr2 = eR.x * eR.x;
        float yr2 = eR.y * eR.y;

        return new Vec2D(p.x / xr2, p.y / yr2).normalize();
    }

    public final Vec3D to3DXY() {
        return new Vec3D(x, y, 0);
    }

    public final Vec3D to3DXZ() {
        return new Vec3D(x, 0, y);
    }

    public final Vec3D to3DYZ() {
        return new Vec3D(0, x, y);
    }

    public float[] toArray() {
        return new float[] {
                x, y
        };
    }

    public final Vec2D toCartesian() {
        float xx = (float) (x * Math.cos(y));
        y = (float) (x * Math.sin(y));
        x = xx;
        return this;
    }

    public final Vec2D toPolar() {
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