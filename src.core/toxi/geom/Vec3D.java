/*
 * Copyright (c) 2006, 2007 Karsten Schmidt
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

import java.util.Random;

import javax.xml.bind.annotation.XmlAttribute;

import toxi.math.InterpolateStrategy;
import toxi.math.MathUtils;

/**
 * Comprehensive 3D vector class with additional basic intersection and
 * collision detection features.
 * 
 * @author Karsten Schmidt
 */
public class Vec3D implements Comparable<ReadonlyVec3D>, ReadonlyVec3D {

    public static enum Axis {

        X(Vec3D.X_AXIS), Y(Vec3D.Y_AXIS), Z(Vec3D.Z_AXIS);

        private final ReadonlyVec3D vector;

        private Axis(ReadonlyVec3D v) {
            this.vector = v;
        }

        public ReadonlyVec3D getVector() {
            return vector;
        }
    }

    /** Defines positive X axis. */
    public static final ReadonlyVec3D X_AXIS = new Vec3D(1, 0, 0);

    /** Defines positive Y axis. */
    public static final ReadonlyVec3D Y_AXIS = new Vec3D(0, 1, 0);

    /** Defines positive Z axis. */
    public static final ReadonlyVec3D Z_AXIS = new Vec3D(0, 0, 1);

    /** Defines the zero vector. */
    public static final ReadonlyVec3D ZERO = new Vec3D();;

    /**
     * Defines vector with all coords set to Float.MIN_VALUE. Useful for
     * bounding box operations.
     */
    public static final ReadonlyVec3D MIN_VALUE =
            new Vec3D(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);

    /**
     * Defines vector with all coords set to Float.MAX_VALUE. Useful for
     * bounding box operations.
     */
    public static final ReadonlyVec3D MAX_VALUE =
            new Vec3D(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);

    /**
     * Creates a new vector from the given angle in the XY plane. The Z
     * component of the vector will be zero.
     * 
     * The resulting vector for theta=0 is equal to the positive X axis.
     * 
     * @param theta
     *            the theta
     * 
     * @return new vector in the XY plane
     */
    public static final Vec3D fromXYTheta(float theta) {
        return new Vec3D((float) Math.cos(theta), (float) Math.sin(theta), 0);
    }

    /**
     * Creates a new vector from the given angle in the XZ plane. The Y
     * component of the vector will be zero.
     * 
     * The resulting vector for theta=0 is equal to the positive X axis.
     * 
     * @param theta
     *            the theta
     * 
     * @return new vector in the XZ plane
     */
    public static final Vec3D fromXZTheta(float theta) {
        return new Vec3D((float) Math.cos(theta), 0, (float) Math.sin(theta));
    }

    /**
     * Creates a new vector from the given angle in the YZ plane. The X
     * component of the vector will be zero.
     * 
     * The resulting vector for theta=0 is equal to the positive Y axis.
     * 
     * @param theta
     *            the theta
     * 
     * @return new vector in the YZ plane
     */
    public static final Vec3D fromYZTheta(float theta) {
        return new Vec3D(0, (float) Math.cos(theta), (float) Math.sin(theta));
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
    public static final Vec3D max(ReadonlyVec3D a, ReadonlyVec3D b) {
        return new Vec3D(MathUtils.max(a.x(), b.x()), MathUtils.max(a.y(), b
                .y()), MathUtils.max(a.z(), b.z()));
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
    public static final Vec3D min(ReadonlyVec3D a, ReadonlyVec3D b) {
        return new Vec3D(MathUtils.min(a.x(), b.x()), MathUtils.min(a.y(), b
                .y()), MathUtils.min(a.z(), b.z()));
    }

    /**
     * Static factory method. Creates a new random unit vector using the default
     * Math.random() Random instance.
     * 
     * @return a new random normalized unit vector.
     */
    public static final Vec3D randomVector() {
        Vec3D rnd =
                new Vec3D(MathUtils.normalizedRandom(), MathUtils
                        .normalizedRandom(), MathUtils.normalizedRandom());
        return rnd.normalize();
    }

    /**
     * Static factory method. Creates a new random unit vector using the given
     * Random generator instance. I recommend to have a look at the
     * https://uncommons-maths.dev.java.net library for a good choice of
     * reliable and high quality random number generators.
     * 
     * @param rnd
     *            the rnd
     * 
     * @return a new random normalized unit vector.
     */
    public static final Vec3D randomVector(Random rnd) {
        Vec3D v =
                new Vec3D(MathUtils.normalizedRandom(rnd), MathUtils
                        .normalizedRandom(rnd), MathUtils.normalizedRandom(rnd));
        return v.normalize();
    }

    /** X coordinate. */
    @XmlAttribute(required = true)
    public float x;

    /** Y coordinate. */
    @XmlAttribute(required = true)
    public float y;

    /** Z coordinate. */
    @XmlAttribute(required = true)
    public float z;

    /**
     * Creates a new zero vector.
     */
    public Vec3D() {
    }

    /**
     * Creates a new vector with the given coordinates.
     * 
     * @param x
     *            the x
     * @param y
     *            the y
     * @param z
     *            the z
     */
    public Vec3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Creates a new vector with the coordinates of the given vector.
     * 
     * @param v
     *            vector to be copied
     */
    public Vec3D(ReadonlyVec3D v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
    }

    /**
     * Abs.
     * 
     * @return the vec3 d
     */
    public final Vec3D abs() {
        x = MathUtils.abs(x);
        y = MathUtils.abs(y);
        z = MathUtils.abs(z);
        return this;
    }

    public final Vec3D add(float a, float b, float c) {
        return new Vec3D(x + a, y + b, z + c);
    }

    public Vec3D add(ReadonlyVec3D v) {
        return new Vec3D(x + v.x(), y + v.y(), z + v.z());
    }

    public final Vec3D add(Vec3D v) {
        return new Vec3D(x + v.x, y + v.y, z + v.z);
    }

    /**
     * Adds vector {a,b,c} and overrides coordinates with result.
     * 
     * @param a
     *            X coordinate
     * @param b
     *            Y coordinate
     * @param c
     *            Z coordinate
     * 
     * @return itself
     */
    public final ReadonlyVec3D addSelf(float a, float b, float c) {
        x += a;
        y += b;
        z += c;
        return this;
    }

    /**
     * Adds vector v and overrides coordinates with result.
     * 
     * @param v
     *            vector to add
     * 
     * @return itself
     */
    public final Vec3D addSelf(Vec3D v) {
        x += v.x;
        y += v.y;
        z += v.z;
        return this;
    }

    public final float angleBetween(ReadonlyVec3D v) {
        return (float) Math.acos(dot(v));
    }

    public final float angleBetween(ReadonlyVec3D v, boolean forceNormalize) {
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
    public final ReadonlyVec3D clear() {
        x = y = z = 0;
        return this;
    }

    public int compareTo(ReadonlyVec3D v) {
        if (x == v.x() && y == v.y() && z == v.z()) {
            return 0;
        }
        return (int) (magSquared() - v.magSquared());
    }

    /**
     * Forcefully fits the vector in the given AABB.
     * 
     * @param box
     *            the box
     * 
     * @return itself
     */
    public final Vec3D constrain(AABB box) {
        return constrain(box.getMin(), box.getMax());
    }

    /**
     * Forcefully fits the vector in the given AABB specified by the 2 given
     * points.
     * 
     * @param min
     * @param max
     * @return itself
     */
    public final Vec3D constrain(Vec3D min, Vec3D max) {
        x = MathUtils.clip(x, min.x, max.x);
        y = MathUtils.clip(y, min.y, max.y);
        z = MathUtils.clip(z, min.z, max.z);
        return this;
    }

    public Vec3D copy() {
        return new Vec3D(this);
    }

    public final Vec3D cross(ReadonlyVec3D v) {
        return new Vec3D(y * v.z() - v.y() * z, z * v.x() - v.z() * x, x
                * v.y() - v.x() * y);
    }

    public final Vec3D cross(Vec3D v) {
        return new Vec3D(y * v.z - v.y * z, z * v.x - v.z * x, x * v.y - v.x
                * y);
    }

    public final Vec3D crossInto(ReadonlyVec3D v, Vec3D result) {
        final float vx = v.x();
        final float vy = v.y();
        final float vz = v.z();
        result.x = y * vz - vy * z;
        result.y = z * vx - vz * x;
        result.z = x * vy - vx * y;
        return result;
    }

    /**
     * Calculates cross-product with vector v. The resulting vector is
     * perpendicular to both the current and supplied vector and overrides the
     * current.
     * 
     * @param v
     *            the v
     * 
     * @return itself
     */
    public final Vec3D crossSelf(Vec3D v) {
        final float cx = y * v.z - v.y * z;
        final float cy = z * v.x - v.z * x;
        z = x * v.y - v.x * y;
        y = cy;
        x = cx;
        return this;
    }

    public final float distanceTo(ReadonlyVec3D v) {
        if (v != null) {
            final float dx = x - v.x();
            final float dy = y - v.y();
            final float dz = z - v.z();
            return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
        } else {
            return Float.NaN;
        }
    }

    public final float distanceToSquared(ReadonlyVec3D v) {
        if (v != null) {
            final float dx = x - v.x();
            final float dy = y - v.y();
            final float dz = z - v.z();
            return dx * dx + dy * dy + dz * dz;
        } else {
            return Float.NaN;
        }
    }

    public final float dot(ReadonlyVec3D v) {
        return x * v.x() + y * v.y() + z * v.z();
    }

    public final float dot(Vec3D v) {
        return x * v.x + y * v.y + z * v.z;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ReadonlyVec3D) {
            final ReadonlyVec3D v = (ReadonlyVec3D) obj;
            return x == v.x() && y == v.y() && z == v.z();
        }
        return false;
    }

    public boolean equalsWithTolerance(ReadonlyVec3D v, float tolerance) {
        if (MathUtils.abs(x - v.x()) < tolerance) {
            if (MathUtils.abs(y - v.y()) < tolerance) {
                if (MathUtils.abs(z - v.z()) < tolerance) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Replaces the vector components with integer values of their current
     * values.
     * 
     * @return itself
     */
    public final Vec3D floor() {
        x = MathUtils.floor(x);
        y = MathUtils.floor(y);
        z = MathUtils.floor(z);
        return this;
    }

    /**
     * Replaces the vector components with the fractional part of their current
     * values.
     * 
     * @return itself
     */
    public final Vec3D frac() {
        x -= MathUtils.floor(x);
        y -= MathUtils.floor(y);
        z -= MathUtils.floor(z);
        return this;
    }

    public final Vec3D getAbs() {
        return new Vec3D(this).abs();
    }

    public final float getComponent(Axis id) {
        switch (id) {
            case X:
                return x;
            case Y:
                return y;
            case Z:
                return z;
        }
        throw new IllegalArgumentException();
    }

    public final float getComponent(int id) {
        switch (id) {
            case 0:
                return x;
            case 1:
                return y;
            case 2:
                return z;
        }
        throw new IllegalArgumentException("index must be 0, 1 or 2");
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#getConstrained(toxi.geom.AABB)
     */
    public final Vec3D getConstrained(AABB box) {
        return new Vec3D(this).constrain(box);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#getFloored()
     */
    public final Vec3D getFloored() {
        return new Vec3D(this).floor();
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#getFrac()
     */
    public final Vec3D getFrac() {
        return new Vec3D(this).frac();
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#getInverted()
     */
    public final Vec3D getInverted() {
        return new Vec3D(-x, -y, -z);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#getLimited(float)
     */
    public final Vec3D getLimited(float lim) {
        if (magSquared() > lim * lim) {
            return getNormalizedTo(lim);
        }
        return new Vec3D(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#getNormalized()
     */
    public final Vec3D getNormalized() {
        return new Vec3D(this).normalize();
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#getNormalizedTo(float)
     */
    public final Vec3D getNormalizedTo(float len) {
        return new Vec3D(this).normalizeTo(len);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#getReciprocal()
     */
    public final Vec3D getReciprocal() {
        return copy().reciprocal();
    }

    public final Vec3D getReflected(ReadonlyVec3D normal) {
        return copy().reflect(normal);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#getRotatedAroundAxis(toxi.geom.Vec3D, float)
     */
    public final Vec3D getRotatedAroundAxis(ReadonlyVec3D axis, float theta) {
        return new Vec3D(this).rotateAroundAxis(axis, theta);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#getRotatedX(float)
     */
    public final Vec3D getRotatedX(float theta) {
        return new Vec3D(this).rotateX(theta);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#getRotatedY(float)
     */
    public final Vec3D getRotatedY(float theta) {
        return new Vec3D(this).rotateY(theta);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#getRotatedZ(float)
     */
    public final Vec3D getRotatedZ(float theta) {
        return new Vec3D(this).rotateZ(theta);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#getSignum()
     */
    public final Vec3D getSignum() {
        return new Vec3D(this).signum();
    }

    /**
     * Returns a unique code for this vector object based on it's values. If two
     * vectors are logically equivalent, they will return the same hash code
     * value.
     * 
     * @return the hash code value of this vector.
     */
    public int hashCode() {
        int hash = Float.floatToIntBits(x);
        hash += 37 * hash + Float.floatToIntBits(y);
        hash += 37 * hash + Float.floatToIntBits(z);
        return hash;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#headingXY()
     */
    public final float headingXY() {
        return (float) Math.atan2(y, x);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#headingXZ()
     */
    public final float headingXZ() {
        return (float) Math.atan2(z, x);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#headingYZ()
     */
    public final float headingYZ() {
        return (float) Math.atan2(y, z);
    }

    public ReadonlyVec3D immutable() {
        return this;
    }

    public final Vec3D interpolateTo(ReadonlyVec3D v, float f) {
        return new Vec3D(x + (v.x() - x) * f, y + (v.y() - y) * f, z
                + (v.z() - z) * f);
    }

    public final Vec3D interpolateTo(ReadonlyVec3D v, float f,
            InterpolateStrategy s) {
        return new Vec3D(s.interpolate(x, v.x(), f),
                s.interpolate(y, v.y(), f), s.interpolate(z, v.z(), f));
    }

    public final Vec3D interpolateTo(Vec3D v, float f) {
        return new Vec3D(x + (v.x - x) * f, y + (v.y - y) * f, z + (v.z - z)
                * f);
    }

    public final Vec3D interpolateTo(Vec3D v, float f, InterpolateStrategy s) {
        return new Vec3D(s.interpolate(x, v.x, f), s.interpolate(y, v.y, f), s
                .interpolate(z, v.z, f));
    }

    /**
     * Interpolates the vector towards the given target vector, using linear
     * interpolation.
     * 
     * @param v
     *            target vector
     * @param f
     *            interpolation factor (should be in the range 0..1)
     * 
     * @return itself, result overrides current vector
     */
    public final Vec3D interpolateToSelf(ReadonlyVec3D v, float f) {
        x += (v.x() - x) * f;
        y += (v.y() - y) * f;
        z += (v.z() - z) * f;
        return this;
    }

    /**
     * Interpolates the vector towards the given target vector, using the given
     * {@link InterpolateStrategy}.
     * 
     * @param v
     *            target vector
     * @param f
     *            interpolation factor (should be in the range 0..1)
     * @param s
     *            InterpolateStrategy instance
     * 
     * @return itself, result overrides current vector
     */
    public final Vec3D interpolateToSelf(ReadonlyVec3D v, float f,
            InterpolateStrategy s) {
        x = s.interpolate(x, v.x(), f);
        y = s.interpolate(y, v.y(), f);
        z = s.interpolate(z, v.z(), f);
        return this;
    }

    /**
     * Scales vector uniformly by factor -1 ( v = -v ), overrides coordinates
     * with result.
     * 
     * @return itself
     */
    public final Vec3D invert() {
        x *= -1;
        y *= -1;
        z *= -1;
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#isInAABB(toxi.geom.AABB)
     */
    public boolean isInAABB(AABB box) {
        final Vec3D min = box.getMin();
        final Vec3D max = box.getMax();
        if (x < min.x || x > max.x) {
            return false;
        }
        if (y < min.y || y > max.y) {
            return false;
        }
        if (z < min.z || z > max.z) {
            return false;
        }
        return true;
    }

    public boolean isInAABB(Vec3D boxOrigin, Vec3D boxExtent) {
        float w = boxExtent.x;
        if (x < boxOrigin.x - w || x > boxOrigin.x + w) {
            return false;
        }
        w = boxExtent.y;
        if (y < boxOrigin.y - w || y > boxOrigin.y + w) {
            return false;
        }
        w = boxExtent.z;
        if (z < boxOrigin.z - w || z > boxOrigin.z + w) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#isZeroVector()
     */
    public final boolean isZeroVector() {
        return MathUtils.abs(x) < MathUtils.EPS
                && MathUtils.abs(y) < MathUtils.EPS
                && MathUtils.abs(z) < MathUtils.EPS;
    }

    /**
     * Jitter.
     * 
     * @param j
     *            the j
     * 
     * @return the vec3 d
     */
    public final Vec3D jitter(float j) {
        return jitter(j, j, j);
    }

    /**
     * Adds random jitter to the vector.
     * 
     * @param jx
     *            maximum x jitter
     * @param jy
     *            maximum y jitter
     * @param jz
     *            maximum z jitter
     * 
     * @return itself
     */
    public final Vec3D jitter(float jx, float jy, float jz) {
        x += MathUtils.normalizedRandom() * jx;
        y += MathUtils.normalizedRandom() * jy;
        z += MathUtils.normalizedRandom() * jz;
        return this;
    }

    public final Vec3D jitter(Random rnd, float j) {
        return jitter(rnd, j, j, j);
    }

    public final Vec3D jitter(Random rnd, float jx, float jy, float jz) {
        x += MathUtils.normalizedRandom(rnd) * jx;
        y += MathUtils.normalizedRandom(rnd) * jy;
        z += MathUtils.normalizedRandom(rnd) * jz;
        return this;
    }

    public final Vec3D jitter(Random rnd, Vec3D jitterVec) {
        return jitter(rnd, jitterVec.x, jitterVec.y, jitterVec.z);
    }

    /**
     * Jitter.
     * 
     * @param jitterVec
     *            the jitter vec
     * 
     * @return the vec3 d
     */
    public final Vec3D jitter(Vec3D jitterVec) {
        return jitter(jitterVec.x, jitterVec.y, jitterVec.z);
    }

    /**
     * Limits the vector's magnitude to the length given.
     * 
     * @param lim
     *            new maximum magnitude
     * 
     * @return itself
     */
    public final Vec3D limit(float lim) {
        if (magSquared() > lim * lim) {
            return normalize().scaleSelf(lim);
        }
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#magnitude()
     */
    public final float magnitude() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#magSquared()
     */
    public final float magSquared() {
        return x * x + y * y + z * z;
    }

    /**
     * Max self.
     * 
     * @param b
     *            the b
     * 
     * @return the vec3 d
     */
    public final Vec3D maxSelf(ReadonlyVec3D b) {
        x = MathUtils.max(x, b.x());
        y = MathUtils.max(y, b.y());
        z = MathUtils.max(z, b.z());
        return this;
    }

    /**
     * Min self.
     * 
     * @param b
     *            the b
     * 
     * @return the vec3 d
     */
    public final Vec3D minSelf(ReadonlyVec3D b) {
        x = MathUtils.min(x, b.x());
        y = MathUtils.min(y, b.y());
        z = MathUtils.min(z, b.z());
        return this;
    }

    /**
     * Applies a uniform modulo operation to the vector, using the same base for
     * all components.
     * 
     * @param base
     *            the base
     * 
     * @return itself
     */
    public final Vec3D modSelf(float base) {
        x %= base;
        y %= base;
        z %= base;
        return this;
    }

    /**
     * Calculates modulo operation for each vector component separately.
     * 
     * @param bx
     *            the bx
     * @param by
     *            the by
     * @param bz
     *            the bz
     * 
     * @return itself
     */

    public final Vec3D modSelf(float bx, float by, float bz) {
        x %= bx;
        y %= by;
        z %= bz;
        return this;
    }

    /**
     * Normalizes the vector so that its magnitude = 1.
     * 
     * @return itself
     */
    public final Vec3D normalize() {
        float mag = (float) Math.sqrt(x * x + y * y + z * z);
        if (mag > 0) {
            mag = 1f / mag;
            x *= mag;
            y *= mag;
            z *= mag;
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
    public final Vec3D normalizeTo(float len) {
        float mag = (float) Math.sqrt(x * x + y * y + z * z);
        if (mag > 0) {
            mag = len / mag;
            x *= mag;
            y *= mag;
            z *= mag;
        }
        return this;
    }

    /**
     * Replaces the vector components with their multiplicative inverse.
     * 
     * @return itself
     */
    public final Vec3D reciprocal() {
        x = 1f / x;
        y = 1f / y;
        z = 1f / z;
        return this;
    }

    public final Vec3D reflect(ReadonlyVec3D normal) {
        return set(normal.scale(this.dot(normal) * 2).subSelf(this));
    }

    /**
     * Rotates the vector around the giving axis.
     * 
     * @param axis
     *            rotation axis vector
     * @param theta
     *            rotation angle (in radians)
     * 
     * @return itself
     */
    public final Vec3D rotateAroundAxis(ReadonlyVec3D axis, float theta) {
        final float ax = axis.x();
        final float ay = axis.y();
        final float az = axis.z();
        final float ux = ax * x;
        final float uy = ax * y;
        final float uz = ax * z;
        final float vx = ay * x;
        final float vy = ay * y;
        final float vz = ay * z;
        final float wx = az * x;
        final float wy = az * y;
        final float wz = az * z;
        final double si = Math.sin(theta);
        final double co = Math.cos(theta);
        float xx =
                (float) (ax * (ux + vy + wz)
                        + (x * (ay * ay + az * az) - ax * (vy + wz)) * co + (-wy + vz)
                        * si);
        float yy =
                (float) (ay * (ux + vy + wz)
                        + (y * (ax * ax + az * az) - ay * (ux + wz)) * co + (wx - uz)
                        * si);
        float zz =
                (float) (az * (ux + vy + wz)
                        + (z * (ax * ax + ay * ay) - az * (ux + vy)) * co + (-vx + uy)
                        * si);
        x = xx;
        y = yy;
        z = zz;
        return this;
    }

    /**
     * Rotates the vector by the given angle around the X axis.
     * 
     * @param theta
     *            the theta
     * 
     * @return itself
     */
    public final Vec3D rotateX(float theta) {
        final float co = (float) Math.cos(theta);
        final float si = (float) Math.sin(theta);
        final float zz = co * z - si * y;
        y = si * z + co * y;
        z = zz;
        return this;
    }

    /**
     * Rotates the vector by the given angle around the Y axis.
     * 
     * @param theta
     *            the theta
     * 
     * @return itself
     */
    public final Vec3D rotateY(float theta) {
        final float co = (float) Math.cos(theta);
        final float si = (float) Math.sin(theta);
        final float xx = co * x - si * z;
        z = si * x + co * z;
        x = xx;
        return this;
    }

    /**
     * Rotates the vector by the given angle around the Z axis.
     * 
     * @param theta
     *            the theta
     * 
     * @return itself
     */
    public final Vec3D rotateZ(float theta) {
        final float co = (float) Math.cos(theta);
        final float si = (float) Math.sin(theta);
        final float xx = co * x - si * y;
        y = si * x + co * y;
        x = xx;
        return this;
    }

    public Vec3D scale(float s) {
        return new Vec3D(x * s, y * s, z * s);
    }

    public Vec3D scale(float a, float b, float c) {
        return new Vec3D(x * a, y * b, z * c);
    }

    public Vec3D scale(ReadonlyVec3D s) {
        return new Vec3D(x * s.x(), y * s.y(), z * s.z());
    }

    public Vec3D scale(Vec3D s) {
        return new Vec3D(x * s.x, y * s.y, z * s.z);
    }

    /**
     * Scales vector uniformly and overrides coordinates with result.
     * 
     * @param s
     *            scale factor
     * 
     * @return itself
     */
    public Vec3D scaleSelf(float s) {
        x *= s;
        y *= s;
        z *= s;
        return this;
    }

    /**
     * Scales vector non-uniformly by vector {a,b,c} and overrides coordinates
     * with result.
     * 
     * @param a
     *            scale factor for X coordinate
     * @param b
     *            scale factor for Y coordinate
     * @param c
     *            scale factor for Z coordinate
     * 
     * @return itself
     */
    public Vec3D scaleSelf(float a, float b, float c) {
        x *= a;
        y *= b;
        z *= c;
        return this;
    }

    /**
     * Scales vector non-uniformly by vector v and overrides coordinates with
     * result.
     * 
     * @param s
     *            scale vector
     * 
     * @return itself
     */

    public Vec3D scaleSelf(Vec3D s) {
        x *= s.x;
        y *= s.y;
        z *= s.z;
        return this;
    }

    /**
     * Overrides coordinates with the given values.
     * 
     * @param x
     *            the x
     * @param y
     *            the y
     * @param z
     *            the z
     * 
     * @return itself
     */
    public Vec3D set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vec3D set(ReadonlyVec3D v) {
        x = v.x();
        y = v.y();
        z = v.z();
        return this;
    }

    /**
     * Overrides coordinates with the ones of the given vector.
     * 
     * @param v
     *            vector to be copied
     * 
     * @return itself
     */
    public Vec3D set(Vec3D v) {
        x = v.x;
        y = v.y;
        z = v.z;
        return this;
    }

    public final Vec3D setComponent(Axis id, float val) {
        switch (id) {
            case X:
                x = val;
                break;
            case Y:
                y = val;
                break;
            case Z:
                z = val;
                break;
        }
        return this;
    }

    public final Vec3D setComponent(int id, float val) {
        switch (id) {
            case 0:
                x = val;
                break;
            case 1:
                y = val;
                break;
            case 2:
                z = val;
                break;
        }
        return this;
    }

    /**
     * Overrides XY coordinates with the ones of the given 2D vector.
     * 
     * @param v
     *            2D vector
     * 
     * @return itself
     */
    public Vec3D setXY(Vec2D v) {
        x = v.x;
        y = v.y;
        return this;
    }

    /**
     * Replaces all vector components with the signum of their original values.
     * In other words if a components value was negative its new value will be
     * -1, if zero => 0, if positive => +1
     * 
     * @return itself
     */
    public Vec3D signum() {
        x = (x < 0 ? -1 : x == 0 ? 0 : 1);
        y = (y < 0 ? -1 : y == 0 ? 0 : 1);
        z = (z < 0 ? -1 : z == 0 ? 0 : 1);
        return this;
    }

    public final Vec3D sub(float a, float b, float c) {
        return new Vec3D(x - a, y - b, z - c);
    }

    public final Vec3D sub(ReadonlyVec3D v) {
        return new Vec3D(x - v.x(), y - v.y(), z - v.z());
    }

    public final Vec3D sub(Vec3D v) {
        return new Vec3D(x - v.x, y - v.y, z - v.z);
    }

    /**
     * Subtracts vector {a,b,c} and overrides coordinates with result.
     * 
     * @param a
     *            X coordinate
     * @param b
     *            Y coordinate
     * @param c
     *            Z coordinate
     * 
     * @return itself
     */
    public final Vec3D subSelf(float a, float b, float c) {
        x -= a;
        y -= b;
        z -= c;
        return this;
    }

    /**
     * Subtracts vector v and overrides coordinates with result.
     * 
     * @param v
     *            vector to be subtracted
     * 
     * @return itself
     */
    public final Vec3D subSelf(Vec3D v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        return this;
    }

    public final Vec2D to2DXY() {
        return new Vec2D(x, y);
    }

    public final Vec2D to2DXZ() {
        return new Vec2D(x, z);
    }

    public final Vec2D to2DYZ() {
        return new Vec2D(y, z);
    }

    public float[] toArray() {
        return new float[] { x, y, z };
    }

    public final Vec3D toCartesian() {
        final float a = (float) (x * Math.cos(z));
        final float xx = (float) (a * Math.cos(y));
        final float yy = (float) (x * Math.sin(z));
        final float zz = (float) (a * Math.sin(y));
        x = xx;
        y = yy;
        z = zz;
        return this;
    }

    public final Vec3D toSpherical() {
        final float xx = Math.abs(x) <= MathUtils.EPS ? MathUtils.EPS : x;
        final float zz = z;

        final float radius = (float) Math.sqrt((xx * xx) + (y * y) + (zz * zz));
        z = (float) Math.asin(y / radius);
        y = (float) Math.atan(zz / xx) + (xx < 0.0 ? MathUtils.PI : 0);
        x = radius;
        return this;
    }

    public String toString() {
        final StringBuffer sb = new StringBuffer(48);
        sb.append("{x:").append(x).append(", y:").append(y).append(", z:")
                .append(z).append("}");
        return sb.toString();
    }

    public final float x() {
        return x;
    }

    public final float y() {
        return y;
    }

    public final float z() {
        return z;
    }
}
