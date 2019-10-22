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
 * Comprehensive double 3D vector class with additional basic intersection and
 * collision detection features.
 */
public class VecD3D implements Comparable<ReadonlyVecD3D>, ReadonlyVecD3D {

    public static enum AxisD {

        X(VecD3D.X_AXIS),
        Y(VecD3D.Y_AXIS),
        Z(VecD3D.Z_AXIS);

        private final ReadonlyVecD3D vector;

        private AxisD(ReadonlyVecD3D v) {
            this.vector = v;
        }

        public ReadonlyVecD3D getVector() {
            return vector;
        }
    }

    /**
     * Convert from float to double versions of Vec3D
     */
    public VecD3D VecD3D(Vec3D v) {
    	x = (double)v.x;
    	y = (double)v.y;
    	z = (double)v.z;
    	return this;
    }
    
    /** Defines positive X axis. */
    public static final ReadonlyVecD3D X_AXIS = new VecD3D(1, 0, 0);

    /** Defines positive Y axis. */
    public static final ReadonlyVecD3D Y_AXIS = new VecD3D(0, 1, 0);

    /** Defines positive Z axis. */
    public static final ReadonlyVecD3D Z_AXIS = new VecD3D(0, 0, 1);

    /** Defines the zero vector. */
    public static final ReadonlyVecD3D ZERO = new VecD3D();

    /**
     * Defines vector with all coords set to Double.MIN_VALUE. Useful for
     * bounding box operations.
     */
    public static final ReadonlyVecD3D MIN_VALUE = new VecD3D(Double.MIN_VALUE,
            Double.MIN_VALUE, Double.MIN_VALUE);

    /**
     * Defines vector with all coords set to Double.MAX_VALUE. Useful for
     * bounding box operations.
     */
    public static final ReadonlyVecD3D MAX_VALUE = new VecD3D(Double.MAX_VALUE,
            Double.MAX_VALUE, Double.MAX_VALUE);

    public static final ReadonlyVecD3D NEG_MAX_VALUE = new VecD3D(
            -Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);

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
    public static final VecD3D fromXYTheta(float theta) {
        return new VecD3D(Math.cos(theta), Math.sin(theta), 0.d);
    }
    public static final VecD3D fromXYTheta(double theta) {
        return new VecD3D(Math.cos(theta), Math.sin(theta), 0.d);
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
    public static final VecD3D fromXZTheta(float theta) {
        return new VecD3D(Math.cos(theta), 0.d, Math.sin(theta));
    }
    public static final VecD3D fromXZTheta(double theta) {
        return new VecD3D(Math.cos(theta), 0.d, Math.sin(theta));
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
    public static final VecD3D fromYZTheta(float theta) {
        return new VecD3D(0.d, Math.cos(theta),  Math.sin(theta));
    }
    public static final VecD3D fromYZTheta(double theta) {
        return new VecD3D(0.d, Math.cos(theta),  Math.sin(theta));
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
    public static final VecD3D max(ReadonlyVec3D a, ReadonlyVec3D b) {
        return new VecD3D(MathUtils.max(a.x(), b.x()), MathUtils.max(a.y(),
                b.y()), MathUtils.max(a.z(), b.z()));
    }
    public static final VecD3D max(ReadonlyVecD3D a, ReadonlyVec3D b) {
        return new VecD3D(MathUtils.max(a.x(), b.x()), MathUtils.max(a.y(),
                b.y()), MathUtils.max(a.z(), b.z()));
    }
    public static final VecD3D max(ReadonlyVec3D a, ReadonlyVecD3D b) {
        return new VecD3D(MathUtils.max(a.x(), b.x()), MathUtils.max(a.y(),
                b.y()), MathUtils.max(a.z(), b.z()));
    }
    public static final VecD3D max(ReadonlyVecD3D a, ReadonlyVecD3D b) {
        return new VecD3D(MathUtils.max(a.x(), b.x()), MathUtils.max(a.y(),
                b.y()), MathUtils.max(a.z(), b.z()));
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
    public static final VecD3D min(ReadonlyVec3D a, ReadonlyVec3D b) {
        return new VecD3D(MathUtils.min(a.x(), b.x()), MathUtils.min(a.y(),
                b.y()), MathUtils.min(a.z(), b.z()));
    }
    public static final VecD3D min(ReadonlyVecD3D a, ReadonlyVec3D b) {
        return new VecD3D(MathUtils.min(a.x(), b.x()), MathUtils.min(a.y(),
                b.y()), MathUtils.min(a.z(), b.z()));
    }
    public static final VecD3D min(ReadonlyVec3D a, ReadonlyVecD3D b) {
        return new VecD3D(MathUtils.min(a.x(), b.x()), MathUtils.min(a.y(),
                b.y()), MathUtils.min(a.z(), b.z()));
    }
    public static final VecD3D min(ReadonlyVecD3D a, ReadonlyVecD3D b) {
        return new VecD3D(MathUtils.min(a.x(), b.x()), MathUtils.min(a.y(),
                b.y()), MathUtils.min(a.z(), b.z()));
    }

    /**
     * Static factory method. Creates a new random unit vector using the Random
     * implementation set as default for the {@link MathUtils} class.
     * 
     * @return a new random normalized unit vector.
     */
    public static final VecD3D randomVector() {
        return randomVector(MathUtils.RND);
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
    public static final VecD3D randomVector(Random rnd) {
        VecD3D v = new VecD3D(rnd.nextDouble() * 2 - 1, rnd.nextDouble() * 2 - 1,
                rnd.nextDouble() * 2 - 1);
        return v.normalize();
    }

    /** X coordinate. */
    @XmlAttribute(required = true)
    public double x;

    /** Y coordinate. */
    @XmlAttribute(required = true)
    public double y;

    /** Z coordinate. */
    @XmlAttribute(required = true)
    public double z;

    /**
     * Creates a new zero vector.
     */
    public VecD3D() {
    }

    public VecD3D(Vec3D v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
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
    public VecD3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public VecD3D(double x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public VecD3D(float x, double y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public VecD3D(float x, float y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public VecD3D(double x, double y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public VecD3D(double x, float y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public VecD3D(float x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public VecD3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public VecD3D(float[] v) {
        this.x = v[0];
        this.y = v[1];
        this.z = v[2];
    }
    public VecD3D(double[] v) {
        this.x = v[0];
        this.y = v[1];
        this.z = v[2];
    }

    /**
     * Creates a new vector with the coordinates of the given vector.
     * 
     * @param v
     *            vector to be copied
     */
    public VecD3D(ReadonlyVec3D v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
    }
    public VecD3D(ReadonlyVecD3D v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
    }

    /**
     * Abs.
     * 
     * @return the vec3 d
     */
    public final VecD3D abs() {
        x = MathUtils.abs(x);
        y = MathUtils.abs(y);
        z = MathUtils.abs(z);
        return this;
    }

    public final VecD3D add(float a, float b, float c) {
        return new VecD3D(x + a, y + b, z + c);
    }
    public final VecD3D add(double a, float b, float c) {
        return new VecD3D(x + a, y + b, z + c);
    }
    public final VecD3D add(float a, double b, float c) {
        return new VecD3D(x + a, y + b, z + c);
    }
    public final VecD3D add(float a, float b, double c) {
        return new VecD3D(x + a, y + b, z + c);
    }
    public final VecD3D add(double a, double b, float c) {
        return new VecD3D(x + a, y + b, z + c);
    }
    public final VecD3D add(double a, float b, double c) {
        return new VecD3D(x + a, y + b, z + c);
    }
    public final VecD3D add(float a, double b, double c) {
        return new VecD3D(x + a, y + b, z + c);
    }
    public final VecD3D add(double a, double b, double c) {
        return new VecD3D(x + a, y + b, z + c);
    }

    public VecD3D add(ReadonlyVec3D v) {
        return new VecD3D(x + v.x(), y + v.y(), z + v.z());
    }
    public VecD3D add(ReadonlyVecD3D v) {
        return new VecD3D(x + v.x(), y + v.y(), z + v.z());
    }


    public final VecD3D add(Vec3D v) {
        return new VecD3D(x + v.x, y + v.y, z + v.z);
    }
    public final VecD3D add(VecD3D v) {
        return new VecD3D(x + v.x, y + v.y, z + v.z);
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
    public final VecD3D addSelf(float a, float b, float c) {
        x += a;
        y += b;
        z += c;
        return this;
    }
    public final VecD3D addSelf(double a, float b, float c) {
        x += a;
        y += b;
        z += c;
        return this;
    }
    public final VecD3D addSelf(float a, double b, float c) {
        x += a;
        y += b;
        z += c;
        return this;
    }
    public final VecD3D addSelf(float a, float b, double c) {
        x += a;
        y += b;
        z += c;
        return this;
    }
    public final VecD3D addSelf(double a, double b, float c) {
        x += a;
        y += b;
        z += c;
        return this;
    }
    public final VecD3D addSelf(double a, float b, double c) {
        x += a;
        y += b;
        z += c;
        return this;
    }
    public final VecD3D addSelf(float a, double b, double c) {
        x += a;
        y += b;
        z += c;
        return this;
    }
    public final VecD3D addSelf(double a, double b, double c) {
        x += a;
        y += b;
        z += c;
        return this;
    }

    public final VecD3D addSelf(ReadonlyVec3D v) {
        x += v.x();
        y += v.y();
        z += v.z();
        return this;
    }
    public final VecD3D addSelf(ReadonlyVecD3D v) {
        x += v.x();
        y += v.y();
        z += v.z();
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
    public final VecD3D addSelf(Vec3D v) {
        x += v.x;
        y += v.y;
        z += v.z;
        return this;
    }
    public final VecD3D addSelf(VecD3D v) {
        x += v.x;
        y += v.y;
        z += v.z;
        return this;
    }

    public final double angleBetween(ReadonlyVec3D v) {
        return  Math.acos(dot(v));
    }
    public final double angleBetween(ReadonlyVecD3D v) {
        return  Math.acos(dot(v));
    }

    public final double angleBetween(ReadonlyVec3D v, boolean forceNormalize) {
        double theta;
        if (forceNormalize) {
            theta = getNormalized().dot(v.getNormalized());
        } else {
            theta = dot(v);
        }
        return  Math.acos(theta);
    }
    public final double angleBetween(ReadonlyVecD3D v, boolean forceNormalize) {
        double theta;
        if (forceNormalize) {
            theta = getNormalized().dot(v.getNormalized());
        } else {
            theta = dot(v);
        }
        return  Math.acos(theta);
    }

    /**
     * Sets all vector components to 0.
     * 
     * @return itself
     */
    public final ReadonlyVecD3D clear() {
        x = y = z = 0;
        return this;
    }

    public int compareTo(ReadonlyVec3D v) {
        if (x == (double)v.x() && y == (double)v.y() && z == (double)v.z()) {
            return 0;
        }
        double a = magSquared();
        double b = v.magSquared();
        if (a < b) {
            return -1;
        }
        return +1;
    }
    public int compareTo(ReadonlyVecD3D v) {
        if (x == v.x() && y == v.y() && z == v.z()) {
            return 0;
        }
        double a = magSquared();
        double b = v.magSquared();
        if (a < b) {
            return -1;
        }
        return +1;
    }


    /**
     * Forcefully fits the vector in the given AABB.
     * 
     * @param box
     *            the box
     * 
     * @return itself
     */
    public VecD3D constrain(AABB box) {
        return constrain(box.getMin(), box.getMax());
    }
    public VecD3D constrain(AABBD box) {
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
    public VecD3D constrain(Vec3D min, Vec3D max) {
        x = MathUtils.clip(x, min.x, max.x);
        y = MathUtils.clip(y, min.y, max.y);
        z = MathUtils.clip(z, min.z, max.z);
        return this;
    }
    public VecD3D constrain(VecD3D min, Vec3D max) {
        x = MathUtils.clip(x, min.x, max.x);
        y = MathUtils.clip(y, min.y, max.y);
        z = MathUtils.clip(z, min.z, max.z);
        return this;
    }
    public VecD3D constrain(Vec3D min, VecD3D max) {
        x = MathUtils.clip(x, min.x, max.x);
        y = MathUtils.clip(y, min.y, max.y);
        z = MathUtils.clip(z, min.z, max.z);
        return this;
    }
    public VecD3D constrain(VecD3D min, VecD3D max) {
        x = MathUtils.clip(x, min.x, max.x);
        y = MathUtils.clip(y, min.y, max.y);
        z = MathUtils.clip(z, min.z, max.z);
        return this;
    }

    public VecD3D copy() {
        return new VecD3D(this);
    }

    public final VecD3D cross(ReadonlyVec3D v) {
        return new VecD3D(y * v.z() - v.y() * z, z * v.x() - v.z() * x, x * v.y() - v.x() * y);
    }
    public final VecD3D cross(ReadonlyVecD3D v) {
        return new VecD3D(y * v.z() - v.y() * z, z * v.x() - v.z() * x, x * v.y() - v.x() * y);
    }

    public final VecD3D cross(Vec3D v) {
        return new VecD3D(y * v.z - v.y * z, z * v.x - v.z * x, x * v.y - v.x * y);
    }
    public final VecD3D cross(VecD3D v) {
        return new VecD3D(y * v.z - v.y * z, z * v.x - v.z * x, x * v.y - v.x * y);
    }


    public final VecD3D crossInto(ReadonlyVec3D v, VecD3D result) {
        final double vx = v.x();
        final double vy = v.y();
        final double vz = v.z();
        result.x = y * vz - vy * z;
        result.y = z * vx - vz * x;
        result.z = x * vy - vx * y;
        return result;
    }
    public final VecD3D crossInto(ReadonlyVecD3D v, VecD3D result) {
        final double vx = v.x();
        final double vy = v.y();
        final double vz = v.z();
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
    public final VecD3D crossSelf(Vec3D v) {
        final double cx = y * v.z - v.y * z;
        final double cy = z * v.x - v.z * x;
        z = x * v.y - v.x * y;
        y = cy;
        x = cx;
        return this;
    }
    public final VecD3D crossSelf(VecD3D v) {
        final double cx = y * v.z - v.y * z;
        final double cy = z * v.x - v.z * x;
        z = x * v.y - v.x * y;
        y = cy;
        x = cx;
        return this;
    }

    public final double distanceTo(ReadonlyVec3D v) {
        if (v != null) {
            final double dx = x - v.x();
            final double dy = y - v.y();
            final double dz = z - v.z();
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        } else {
            return Double.NaN;
        }
    }
    public final double distanceTo(ReadonlyVecD3D v) {
        if (v != null) {
            final double dx = x - v.x();
            final double dy = y - v.y();
            final double dz = z - v.z();
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        } else {
            return Double.NaN;
        }
    }

    public final double distanceToSquared(ReadonlyVec3D v) {
        if (v != null) {
            final double dx = x - v.x();
            final double dy = y - v.y();
            final double dz = z - v.z();
            return dx * dx + dy * dy + dz * dz;
        } else {
            return Double.NaN;
        }
    }
    public final double distanceToSquared(ReadonlyVecD3D v) {
        if (v != null) {
            final double dx = x - v.x();
            final double dy = y - v.y();
            final double dz = z - v.z();
            return dx * dx + dy * dy + dz * dz;
        } else {
            return Double.NaN;
        }
    }

    public final double dot(ReadonlyVec3D v) {
        return x * v.x() + y * v.y() + z * v.z();
    }
    public final double dot(ReadonlyVecD3D v) {
        return x * v.x() + y * v.y() + z * v.z();
    }

    public final double dot(Vec3D v) {
        return x * v.x + y * v.y + z * v.z;
    }
    public final double dot(VecD3D v) {
        return x * v.x + y * v.y + z * v.z;
    }

    /**
     * Returns true if the Object v is of type ReadonlyVec3D and all of the data
     * members of v are equal to the corresponding data members in this vector.
     * 
     * @param v
     *            the Object with which the comparison is made
     * @return true or false
     */
    public boolean equals(Object v) {
        try {
            ReadonlyVecD3D vv = (ReadonlyVecD3D) v;
            return (x == vv.x() && y == vv.y() && z == vv.z());
        } catch (NullPointerException e) {
            return false;
        } catch (ClassCastException e) {
            return false;
        }
    }

    /**
     * Returns true if the Object v is of type ReadonlyVec3D and all of the data
     * members of v are equal to the corresponding data members in this vector.
     * 
     * @param v
     *            the vector with which the comparison is made
     * @return true or false
     */
    public boolean equals(ReadonlyVec3D v) {
        try {
            return (x == (double)v.x() && y == (double) v.y() && z ==(double) v.z());
        } catch (NullPointerException e) {
            return false;
        }
    }
    public boolean equals(ReadonlyVecD3D v) {
        try {
            return (x == v.x() && y == v.y() && z == v.z());
        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean equalsWithTolerance(ReadonlyVec3D v, float tolerance) {
    	return equalsWithTolerance((ReadonlyVecD3D) v, (double) tolerance);
    }
    public boolean equalsWithTolerance(ReadonlyVecD3D v, float tolerance) {
    	return equalsWithTolerance(v, (double) tolerance);
    }
    public boolean equalsWithTolerance(ReadonlyVec3D v, double tolerance) {
    	return equalsWithTolerance((ReadonlyVecD3D) v,  tolerance);
    }
    public boolean equalsWithTolerance(ReadonlyVecD3D v, double tolerance) {
        try {
            double diff = x - v.x();
            if (Double.isNaN(diff)) {
                return false;
            }
            if ((diff < 0 ? -diff : diff) > tolerance) {
                return false;
            }
            diff = y - v.y();
            if (Double.isNaN(diff)) {
                return false;
            }
            if ((diff < 0 ? -diff : diff) > tolerance) {
                return false;
            }
            diff = z - v.z();
            if (Double.isNaN(diff)) {
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
     * values.
     * 
     * @return itself
     */
    public final VecD3D floor() {
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
    public final VecD3D frac() {
        x -= MathUtils.floor(x);
        y -= MathUtils.floor(y);
        z -= MathUtils.floor(z);
        return this;
    }

    public final VecD3D getAbs() {
        return new VecD3D(this).abs();
    }

    public VecD3D getCartesian() {
        return copy().toCartesian();
    }

    /**
     * Identifies the closest cartesian axis to this vector. If at leat two
     * vector components are equal, no unique decision can be made and the
     * method returns null.
     * 
     * @return Axis enum or null
     */
    public final AxisD getClosestAxis() {
        double ax = MathUtils.abs(x);
        double ay = MathUtils.abs(y);
        double az = MathUtils.abs(z);
        if (ax > ay && ax > az) {
            return AxisD.X;
        }
        if (ay > ax && ay > az) {
            return AxisD.Y;
        }
        if (az > ax && az > ay) {
            return AxisD.Z;
        }
        return null;
    }

    public final double getComponent(AxisD id) {
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

    public final double getComponent(int id) {
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
	public final double getComponent(Object id) {
       try {
          int iid = Integer.parseInt(id.toString());
          switch (iid) {
	        case 0:
	            return x;
	        case 1:
	            return y;
	        case 2:
	            return z;
          }
		} catch (NumberFormatException e) {
	   }
       throw new IllegalArgumentException("index must be 0, 1 or 2");
	}
    
    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#getConstrained(toxi.geom.AABB)
     */
    public final VecD3D getConstrained(AABB box) {
        return new VecD3D(this).constrain(box);
    }
    public final VecD3D getConstrained(AABBD box) {
        return new VecD3D(this).constrain(box);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#getFloored()
     */
    public final VecD3D getFloored() {
        return new VecD3D(this).floor();
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#getFrac()
     */
    public final VecD3D getFrac() {
        return new VecD3D(this).frac();
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#getInverted()
     */
    public final VecD3D getInverted() {
        return new VecD3D(-x, -y, -z);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#getLimited(float)
     */
    public final VecD3D getLimited(float lim) {
        if (magSquared() > lim * lim) {
            return getNormalizedTo(lim);
        }
        return new VecD3D(this);
    }
    public final VecD3D getLimited(double lim) {
        if (magSquared() > lim * lim) {
            return getNormalizedTo(lim);
        }
        return new VecD3D(this);
    }

    public VecD3D getMapped(ScaleMap map) {
        return new VecD3D(map.getClippedValueFor(x),
                          map.getClippedValueFor(y),
                          map.getClippedValueFor(z));
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#getNormalized()
     */
    public final VecD3D getNormalized() {
        return new VecD3D(this).normalize();
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#getNormalizedTo(float)
     */
    public final VecD3D getNormalizedTo(float len) {
        return new VecD3D(this).normalizeTo(len);
    }
    public final VecD3D getNormalizedTo(double len) {
        return new VecD3D(this).normalizeTo(len);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#getReciprocal()
     */
    public final VecD3D getReciprocal() {
        return copy().reciprocal();
    }

    public final VecD3D getReflected(ReadonlyVec3D normal) {
        return copy().reflect(normal);
    }
    public final VecD3D getReflected(ReadonlyVecD3D normal) {
        return copy().reflect(normal);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#getRotatedAroundAxis(toxi.geom.Vec3D, float)
     */
    public final VecD3D getRotatedAroundAxis(ReadonlyVec3D axis, float theta) {
        return new VecD3D(this).rotateAroundAxis(axis, theta);
    }
    public final VecD3D getRotatedAroundAxis(ReadonlyVecD3D axis, float theta) {
        return new VecD3D(this).rotateAroundAxis(axis, theta);
    }
    public final VecD3D getRotatedAroundAxis(ReadonlyVec3D axis, double theta) {
        return new VecD3D(this).rotateAroundAxis(axis, theta);
    }
    public final VecD3D getRotatedAroundAxis(ReadonlyVecD3D axis, double theta) {
        return new VecD3D(this).rotateAroundAxis(axis, theta);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#getRotatedX(float)
     */
    public final VecD3D getRotatedX(float theta) {
        return new VecD3D(this).rotateX(theta);
    }
    public final VecD3D getRotatedX(double theta) {
        return new VecD3D(this).rotateX(theta);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#getRotatedY(float)
     */
    public final VecD3D getRotatedY(float theta) {
        return new VecD3D(this).rotateY(theta);
    }
    public final VecD3D getRotatedY(double theta) {
        return new VecD3D(this).rotateY(theta);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#getRotatedZ(float)
     */
    public final VecD3D getRotatedZ(float theta) {
        return new VecD3D(this).rotateZ(theta);
    }
    public final VecD3D getRotatedZ(double theta) {
        return new VecD3D(this).rotateZ(theta);
    }

    public VecD3D getRoundedTo(float prec) {
        return copy().roundTo(prec);
    }
    public VecD3D getRoundedTo(double prec) {
        return copy().roundTo(prec);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#getSignum()
     */
    public final VecD3D getSignum() {
        return new VecD3D(this).signum();
    }

    public VecD3D getSpherical() {
        return copy().toSpherical();
    }

    /**
     * Returns a hash code value based on the data values in this object. Two
     * different Vec3D objects with identical data values (i.e., Vec3D.equals
     * returns true) will return the same hash code value. Two objects with
     * different data members may return the same hash value, although this is
     * not likely.
     * 
     * @return the integer hash code value
     */
    public int hashCode() {
    	return ((Double)x).hashCode()+((Double)y).hashCode()+((Double)z).hashCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#headingXY()
     */
    public final double headingXY() {
        return  Math.atan2(y, x);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#headingXZ()
     */
    public final double headingXZ() {
        return Math.atan2(z, x);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#headingYZ()
     */
    public final double headingYZ() {
        return  Math.atan2(y, z);
    }

    public ReadonlyVecD3D immutable() {
        return this;
    }

    public final VecD3D interpolateTo(ReadonlyVec3D v, float f) {
        return new VecD3D(x + (v.x() - x) * f, y + (v.y() - y) * f, z + (v.z() - z) * f);
    }
    public final VecD3D interpolateTo(ReadonlyVecD3D v, float f) {
        return new VecD3D(x + (v.x() - x) * f, y + (v.y() - y) * f, z + (v.z() - z) * f);
    }
    public final VecD3D interpolateTo(ReadonlyVec3D v, double f) {
        return new VecD3D(x + (v.x() - x) * f, y + (v.y() - y) * f, z + (v.z() - z) * f);
    }
    public final VecD3D interpolateTo(ReadonlyVecD3D v, double f) {
        return new VecD3D(x + (v.x() - x) * f, y + (v.y() - y) * f, z + (v.z() - z) * f);
    }

    public final VecD3D interpolateTo(ReadonlyVec3D v, float f, InterpolateStrategy s) {
        return new VecD3D(s.interpolate(x, v.x(), f), s.interpolate(y, v.y(), f), s.interpolate(z, v.z(), f));
    }
    public final VecD3D interpolateTo(ReadonlyVecD3D v, float f, InterpolateStrategy s) {
        return new VecD3D(s.interpolate(x, v.x(), f), s.interpolate(y, v.y(), f), s.interpolate(z, v.z(), f));
    }
    public final VecD3D interpolateTo(ReadonlyVec3D v, double f, InterpolateStrategy s) {
        return new VecD3D(s.interpolate(x, v.x(), f), s.interpolate(y, v.y(), f), s.interpolate(z, v.z(), f));
    }
    public final VecD3D interpolateTo(ReadonlyVecD3D v, double f, InterpolateStrategy s) {
        return new VecD3D(s.interpolate(x, v.x(), f), s.interpolate(y, v.y(), f), s.interpolate(z, v.z(), f));
    }
    

    public final VecD3D interpolateTo(Vec3D v, float f) {
        return new VecD3D(x + (v.x - x) * f, y + (v.y - y) * f, z + (v.z - z) * f);
    }
    public final VecD3D interpolateTo(VecD3D v, float f) {
        return new VecD3D(x + (v.x - x) * f, y + (v.y - y) * f, z + (v.z - z) * f);
    }
    public final VecD3D interpolateTo(Vec3D v, double f) {
        return new VecD3D(x + (v.x - x) * f, y + (v.y - y) * f, z + (v.z - z) * f);
    }
    public final VecD3D interpolateTo(VecD3D v, double f) {
        return new VecD3D(x + (v.x - x) * f, y + (v.y - y) * f, z + (v.z - z) * f);
    }

    public final VecD3D interpolateTo(Vec3D v, float f, InterpolateStrategy s) {
        return new VecD3D(s.interpolate(x, v.x, f), s.interpolate(y, v.y, f), s.interpolate(z, v.z, f));
    }
    public final VecD3D interpolateTo(VecD3D v, float f, InterpolateStrategy s) {
        return new VecD3D(s.interpolate(x, v.x, f), s.interpolate(y, v.y, f), s.interpolate(z, v.z, f));
    }
    public final VecD3D interpolateTo(Vec3D v, double f, InterpolateStrategy s) {
        return new VecD3D(s.interpolate(x, v.x, f), s.interpolate(y, v.y, f), s.interpolate(z, v.z, f));
    }
    public final VecD3D interpolateTo(VecD3D v, double f, InterpolateStrategy s) {
        return new VecD3D(s.interpolate(x, v.x, f), s.interpolate(y, v.y, f), s.interpolate(z, v.z, f));
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
    public final VecD3D interpolateToSelf(ReadonlyVec3D v, float f) {
        x += (v.x() - x) * f;
        y += (v.y() - y) * f;
        z += (v.z() - z) * f;
        return this;
    }
    public final VecD3D interpolateToSelf(ReadonlyVecD3D v, float f) {
        x += (v.x() - x) * f;
        y += (v.y() - y) * f;
        z += (v.z() - z) * f;
        return this;
    }
    public final VecD3D interpolateToSelf(ReadonlyVec3D v, double f) {
        x += (v.x() - x) * f;
        y += (v.y() - y) * f;
        z += (v.z() - z) * f;
        return this;
    }
    public final VecD3D interpolateToSelf(ReadonlyVecD3D v, double f) {
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
    public final VecD3D interpolateToSelf(ReadonlyVec3D v, float f, InterpolateStrategy s) {
        x = s.interpolate(x, v.x(), f);
        y = s.interpolate(y, v.y(), f);
        z = s.interpolate(z, v.z(), f);
        return this;
    }
    public final VecD3D interpolateToSelf(ReadonlyVecD3D v, float f, InterpolateStrategy s) {
        x = s.interpolate(x, v.x(), f);
        y = s.interpolate(y, v.y(), f);
        z = s.interpolate(z, v.z(), f);
        return this;
    }
    public final VecD3D interpolateToSelf(ReadonlyVec3D v, double f, InterpolateStrategy s) {
        x = s.interpolate(x, v.x(), f);
        y = s.interpolate(y, v.y(), f);
        z = s.interpolate(z, v.z(), f);
        return this;
    }
    public final VecD3D interpolateToSelf(ReadonlyVecD3D v, double f, InterpolateStrategy s) {
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
    public final VecD3D invert() {
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
    public boolean isInAABBD(AABB box) {
        final VecD3D min = VecD3D(box.getMin());
        final VecD3D max = VecD3D(box.getMax());
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
    public boolean isInAABBD(AABBD box) {
        final VecD3D min = box.getMin();
        final VecD3D max = box.getMax();
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

    public boolean isInAABBD(Vec3D boxOrigin, Vec3D boxExtent) {
    	return isInAABBD(VecD3D(boxOrigin), VecD3D(boxExtent));
    }	
    public boolean isInAABBD(VecD3D boxOrigin, Vec3D boxExtent) {
    	return isInAABBD(boxOrigin, VecD3D(boxExtent));
    }
    public boolean isInAABBD(Vec3D boxOrigin, VecD3D boxExtent) {
    	return isInAABBD(VecD3D(boxOrigin), boxExtent);
	}
    public boolean isInAABBD(VecD3D boxOrigin, VecD3D boxExtent) {
        double w = boxExtent.x;
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

    public final boolean isMajorAxisD(float tol) {
    	return isMajorAxisD((double)tol); 
    }
    public final boolean isMajorAxisD(double tol) {
        double ax = MathUtils.abs(x);
        double ay = MathUtils.abs(y);
        double az = MathUtils.abs(z);
        double itol = 1 - tol;
        if (ax > itol) {
            if (ay < tol) {
                return (az < tol);
            }
        } else if (ay > itol) {
            if (ax < tol) {
                return (az < tol);
            }
        } else if (az > itol) {
            if (ax < tol) {
                return (ay < tol);
            }
        }
        return false;
    }

    public final boolean isZeroVector() {
        return MathUtils.abs(x) < MathUtils.EPS
                && MathUtils.abs(y) < MathUtils.EPS
                && MathUtils.abs(z) < MathUtils.EPS;
    }

    /**
     * Add random jitter to the vector in the range -j ... +j using the default
     * {@link Random} generator of {@link MathUtils}.
     * 
     * @param j
     *            the j
     * 
     * @return the vec3 d
     */
    public final VecD3D jitter(float j) {
        return jitter(j, j, j);
    }

    /**
     * Adds random jitter to the vector in the range -j ... +j using the default
     * {@link Random} generator of {@link MathUtils}.
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
    public final VecD3D jitter(float  jx, float  jy, float  jz) {
    	return jitter((double) jx, (double) jy, (double) jz);
    }
    public final VecD3D jitter(double jx, float  jy, float  jz) {
    	return jitter(jx, (double) jy, (double) jz);
    }
    public final VecD3D jitter(float  jx, double jy, float  jz) {
    	return jitter((double) jx, jy, (double) jz);
    }
    public final VecD3D jitter(float  jx, float  jy, double jz) {
    	return jitter((double) jx, (double) jy, jz);
    }
    public final VecD3D jitter(double jx, double jy, float  jz) {
    	return jitter(jx,  jy, (double) jz);
    }
    public final VecD3D jitter(double jx, float  jy, double jz) {
    	return jitter(jx, (double) jy, jz);
    }
    public final VecD3D jitter(float  jx, double jy, double jz) {
    	return jitter((double) jx, jy, jz);
    }
    public final VecD3D jitter(double jx, double jy, double jz) {
        x += MathUtils.normalizedRandom() * jx;
        y += MathUtils.normalizedRandom() * jy;
        z += MathUtils.normalizedRandom() * jz;
        return this;
    }

    public final VecD3D jitter(Random rnd, float j) {
        return jitter(rnd, j, j, j);
    }
    public final VecD3D jitter(Random rnd, double j) {
        return jitter(rnd, j, j, j);
    }
    public final VecD3D jitter(Random rnd, float  jx, float  jy, float  jz) {
    	return jitter(rnd, (double) jx, (double) jy, (double) jz);
    }
    public final VecD3D jitter(Random rnd, double jx, float  jy, float  jz) {
    	return jitter(rnd, jx, (double) jy, (double) jz);
    }
    public final VecD3D jitter(Random rnd, float  jx, double jy, float  jz) {
    	return jitter(rnd, (double) jx,  jy, (double) jz);
    }
    public final VecD3D jitter(Random rnd, float  jx, float  jy, double jz) {
    	return jitter(rnd, (double) jx, (double) jy,  jz);
    }
    public final VecD3D jitter(Random rnd, double jx, double jy, float  jz) {
    	return jitter(rnd,  jx, jy, (double) jz);
    }
    public final VecD3D jitter(Random rnd, double jx, float  jy, double jz) {
    	return jitter(rnd, jx, (double) jy, jz);
    }
    public final VecD3D jitter(Random rnd, float  jx, double jy, double jz) {
    	return jitter(rnd, (double) jx, jy, jz);
    }
    public final VecD3D jitter(Random rnd, double jx, double jy, double jz) {
        x += MathUtils.normalizedRandom(rnd) * jx;
        y += MathUtils.normalizedRandom(rnd) * jy;
        z += MathUtils.normalizedRandom(rnd) * jz;
        return this;
    }

    public final VecD3D jitter(Random rnd, Vec3D jitterVec) {
        return jitter(rnd, jitterVec.x, jitterVec.y, jitterVec.z);
    }
    public final VecD3D jitter(Random rnd, VecD3D jitterVec) {
        return jitter(rnd, jitterVec.x, jitterVec.y, jitterVec.z);
    }

    /**
     * Adds random jitter to the vector in the range defined by the given vector
     * components and using the default {@link Random} generator of
     * {@link MathUtils}.
     * 
     * @param jitterVec
     *            the jitter vec
     * 
     * @return itself
     */
    public final VecD3D jitter(Vec3D jitterVec) {
        return jitter(jitterVec.x, jitterVec.y, jitterVec.z);
    }
    public final VecD3D jitter(VecD3D jitterVec) {
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
    public final VecD3D limit(float lim) {
        if (magSquared() > lim * lim) {
            return normalize().scaleSelf(lim);
        }
        return this;
    }
    public final VecD3D limit(double lim) {
        if (magSquared() > lim * lim) {
            return normalize().scaleSelf(lim);
        }
        return this;
    }

    public final double magnitude() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public final double magSquared() {
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
    public final VecD3D maxSelf(ReadonlyVec3D b) {
        x = MathUtils.max(x, b.x());
        y = MathUtils.max(y, b.y());
        z = MathUtils.max(z, b.z());
        return this;
    }
    public final VecD3D maxSelf(ReadonlyVecD3D b) {
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
    public final VecD3D minSelf(ReadonlyVec3D b) {
        x = MathUtils.min(x, b.x());
        y = MathUtils.min(y, b.y());
        z = MathUtils.min(z, b.z());
        return this;
    }
    public final VecD3D minSelf(ReadonlyVecD3D b) {
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
    public final VecD3D modSelf(float base) {
        x %= base;
        y %= base;
        z %= base;
        return this;
    }
    public final VecD3D modSelf(double base) {
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

    public final VecD3D modSelf(float bx, float by, float bz) {
        x %= bx;
        y %= by;
        z %= bz;
        return this;
    }
    public final VecD3D modSelf(double bx, float by, float bz) {
        x %= bx;
        y %= by;
        z %= bz;
        return this;
    }
    public final VecD3D modSelf(float bx, double by, float bz) {
        x %= bx;
        y %= by;
        z %= bz;
        return this;
    }
    public final VecD3D modSelf(float bx, float by, double bz) {
        x %= bx;
        y %= by;
        z %= bz;
        return this;
    }
    public final VecD3D modSelf(double bx, double by, float bz) {
        x %= bx;
        y %= by;
        z %= bz;
        return this;
    }
    public final VecD3D modSelf(double bx, float by, double bz) {
        x %= bx;
        y %= by;
        z %= bz;
        return this;
    }
    public final VecD3D modSelf(float bx, double by, double bz) {
        x %= bx;
        y %= by;
        z %= bz;
        return this;
    }
    public final VecD3D modSelf(double bx, double by, double bz) {
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
    public final VecD3D normalize() {
        double mag = Math.sqrt(x * x + y * y + z * z);
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
    public final VecD3D normalizeTo(float len) {
        double mag =  Math.sqrt(x * x + y * y + z * z);
        if (mag > 0) {
            mag = len / mag;
            x *= mag;
            y *= mag;
            z *= mag;
        }
        return this;
    }
    public final VecD3D normalizeTo(double len) {
        double mag =  Math.sqrt(x * x + y * y + z * z);
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
    public final VecD3D reciprocal() {
        x = 1f / x;
        y = 1f / y;
        z = 1f / z;
        return this;
    }

    public final VecD3D reflect(ReadonlyVec3D normal) {
        return reflect((ReadonlyVecD3D) normal);
    }
    public final VecD3D reflect(ReadonlyVecD3D normal) {
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
    public final VecD3D rotateAroundAxis(ReadonlyVec3D axis, float theta) {
    	return rotateAroundAxis((ReadonlyVecD3D) axis, (double) theta);
    }
    public final VecD3D rotateAroundAxis(ReadonlyVecD3D axis, float theta) {
    	return rotateAroundAxis(axis, (double) theta);
    }
    public final VecD3D rotateAroundAxis(ReadonlyVec3D axis, double theta) {
    	return rotateAroundAxis((ReadonlyVecD3D) axis,theta);
    }
    public final VecD3D rotateAroundAxis(ReadonlyVecD3D axis, double theta) {
        final double ax = axis.x();
        final double ay = axis.y();
        final double az = axis.z();
        final double ux = ax * x;
        final double uy = ax * y;
        final double uz = ax * z;
        final double vx = ay * x;
        final double vy = ay * y;
        final double vz = ay * z;
        final double wx = az * x;
        final double wy = az * y;
        final double wz = az * z;
        final double si = Math.sin(theta);
        final double co = Math.cos(theta);
        double xx = (ax * (ux + vy + wz)
                + (x * (ay * ay + az * az) - ax * (vy + wz)) * co + (-wy + vz)
                * si);
        double yy = (ay * (ux + vy + wz)
                + (y * (ax * ax + az * az) - ay * (ux + wz)) * co + (wx - uz)
                * si);
        double zz = (az * (ux + vy + wz)
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
    public final VecD3D rotateX(float theta) {
        final double co = Math.cos(theta);
        final double si = Math.sin(theta);
        final double zz = co * z - si * y;
        y = si * z + co * y;
        z = zz;
        return this;
    }
    public final VecD3D rotateX(double theta) {
        final double co = Math.cos(theta);
        final double si = Math.sin(theta);
        final double zz = co * z - si * y;
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
    public final VecD3D rotateY(float theta) {
        final double co = Math.cos(theta);
        final double si = Math.sin(theta);
        final double xx = co * x - si * z;
        z = si * x + co * z;
        x = xx;
        return this;
    }
    public final VecD3D rotateY(double theta) {
        final double co = Math.cos(theta);
        final double si = Math.sin(theta);
        final double xx = co * x - si * z;
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
    public final VecD3D rotateZ(float theta) {
        final double co = Math.cos(theta);
        final double si = Math.sin(theta);
        final double xx = co * x - si * y;
        y = si * x + co * y;
        x = xx;
        return this;
    }
    public final VecD3D rotateZ(double theta) {
        final double co = Math.cos(theta);
        final double si = Math.sin(theta);
        final double xx = co * x - si * y;
        y = si * x + co * y;
        x = xx;
        return this;
    }

    public VecD3D roundTo(float prec) {
        x = MathUtils.roundTo(x, prec);
        y = MathUtils.roundTo(y, prec);
        z = MathUtils.roundTo(z, prec);
        return this;
    }
    public VecD3D roundTo(double prec) {
        x = MathUtils.roundTo(x, prec);
        y = MathUtils.roundTo(y, prec);
        z = MathUtils.roundTo(z, prec);
        return this;
    }

    public VecD3D scale(float s) {
        return new VecD3D(x * s, y * s, z * s);
    }
    public VecD3D scale(double s) {
        return new VecD3D(x * s, y * s, z * s);
    }

    public VecD3D scale(float a, float b, float c) {
        return new VecD3D(x * a, y * b, z * c);
    }
    public VecD3D scale(double a, float b, float c) {
       return new VecD3D(x * a, y * b, z * c);
    }
    public VecD3D scale(float a, double b, float c) {
        return new VecD3D(x * a, y * b, z * c);
    }
    public VecD3D scale(float a, float b, double c) {
        return new VecD3D(x * a, y * b, z * c);
    }
    public VecD3D scale(double a, double b, float c) {
        return new VecD3D(x * a, y * b, z * c);
    }
    public VecD3D scale(double a, float b, double c) {
        return new VecD3D(x * a, y * b, z * c);
    }
    public VecD3D scale(float a, double b, double c) {
        return new VecD3D(x * a, y * b, z * c);
    }
    public VecD3D scale(double a, double b, double c) {
        return new VecD3D(x * a, y * b, z * c);
    }

    public VecD3D scale(ReadonlyVec3D s) {
        return new VecD3D(x * s.x(), y * s.y(), z * s.z());
    }
    public VecD3D scale(ReadonlyVecD3D s) {
        return new VecD3D(x * s.x(), y * s.y(), z * s.z());
    }

    public VecD3D scale(Vec3D s) {
        return new VecD3D(x * s.x, y * s.y, z * s.z);
    }
    public VecD3D scale(VecD3D s) {
        return new VecD3D(x * s.x, y * s.y, z * s.z);
    }

    /**
     * Scales vector uniformly and overrides coordinates with result.
     * 
     * @param s
     *            scale factor
     * 
     * @return itself
     */
    public VecD3D scaleSelf(float s) {
        x *= s;
        y *= s;
        z *= s;
        return this;
    }
    public VecD3D scaleSelf(double s) {
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
    public VecD3D scaleSelf(float a, float b, float c) {
        x *= a;
        y *= b;
        z *= c;
        return this;
    }
    public VecD3D scaleSelf(double a, float b, float c) {
        x *= a;
        y *= b;
        z *= c;
        return this;
    }
    public VecD3D scaleSelf(float a, double b, float c) {
        x *= a;
        y *= b;
        z *= c;
        return this;
    }
    public VecD3D scaleSelf(float a, float b, double c) {
        x *= a;
        y *= b;
        z *= c;
        return this;
    }
    public VecD3D scaleSelf(double a, double b, float c) {
        x *= a;
        y *= b;
        z *= c;
        return this;
    }
    public VecD3D scaleSelf(double a, float b, double c) {
        x *= a;
        y *= b;
        z *= c;
        return this;
    }
    public VecD3D scaleSelf(float a, double b, double c) {
        x *= a;
        y *= b;
        z *= c;
        return this;
    }
    public VecD3D scaleSelf(double a, double b, double c) {
        x *= a;
        y *= b;
        z *= c;
        return this;
    }

    public VecD3D scaleSelf(ReadonlyVec3D s) {
        x *= s.x();
        y *= s.y();
        z *= s.z();
        return this;
    }
    public VecD3D scaleSelf(ReadonlyVecD3D s) {
        x *= s.x();
        y *= s.y();
        z *= s.z();
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

    public VecD3D scaleSelf(Vec3D s) {
        x *= s.x;
        y *= s.y;
        z *= s.z;
        return this;
    }
    public VecD3D scaleSelf(VecD3D s) {
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
    public VecD3D set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }
    public VecD3D set(double x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }
    public VecD3D set(float x, double y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }
    public VecD3D set(float x, float y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }
    public VecD3D set(double x, double y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }
    public VecD3D set(double x, float y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }
    public VecD3D set(float x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }
    public VecD3D set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public VecD3D set(ReadonlyVec3D v) {
        x = v.x();
        y = v.y();
        z = v.z();
        return this;
    }
    public VecD3D set(ReadonlyVecD3D v) {
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
    public VecD3D set(Vec3D v) {
        x = v.x;
        y = v.y;
        z = v.z;
        return this;
    }
    public VecD3D set(VecD3D v) {
        x = v.x;
        y = v.y;
        z = v.z;
        return this;
    }
    
    public final VecD3D setComponent(AxisD id, float val) {
    	return setComponent(id, (double) val);
    }
    public final VecD3D setComponent(AxisD id, double val) {
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
    
    public final VecD3D setComponent(int id, float val) {
    	return setComponent(id, (double) val);
    }
    public final VecD3D setComponent(int id, double val) {
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

    public VecD3D setX(float x) {
        this.x = x;
        return this;
    }
    public VecD3D setX(double x) {
        this.x = x;
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
    public VecD3D setXY(Vec2D v) {
        x = v.x;
        y = v.y;
        return this;
    }
    public VecD3D setXY(VecD2D v) {
        x = v.x;
        y = v.y;
        return this;
    }

    public VecD3D setY(float y) {
        this.y = y;
        return this;
    }
    public VecD3D setY(double y) {
        this.y = y;
        return this;
    }

    public VecD3D setZ(float z) {
        this.z = z;
        return this;
    }
    public VecD3D setZ(double z) {
        this.z = z;
        return this;
    }

    public VecD3D shuffle(int iterations) {
        double t;
        for (int i = 0; i < iterations; i++) {
            switch (MathUtils.random(3)) {
                case 0:
                    t = x;
                    x = y;
                    y = t;
                    break;
                case 1:
                    t = x;
                    x = z;
                    z = t;
                    break;
                case 2:
                    t = y;
                    y = z;
                    z = t;
                    break;
            }
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
    public VecD3D signum() {
        x = (x < 0 ? -1 : x == 0 ? 0 : 1);
        y = (y < 0 ? -1 : y == 0 ? 0 : 1);
        z = (z < 0 ? -1 : z == 0 ? 0 : 1);
        return this;
    }

    /**
     * Rounds the vector to the closest major axis. Assumes the vector is
     * normalized.
     * 
     * @return itself
     */
    public final VecD3D snapToAxis() {
        if (MathUtils.abs(x) < 0.5f) {
            x = 0;
        } else {
            x = x < 0 ? -1 : 1;
            y = z = 0;
        }
        if (MathUtils.abs(y) < 0.5f) {
            y = 0;
        } else {
            y = y < 0 ? -1 : 1;
            x = z = 0;
        }
        if (MathUtils.abs(z) < 0.5f) {
            z = 0;
        } else {
            z = z < 0 ? -1 : 1;
            x = y = 0;
        }
        return this;
    }

    public final VecD3D sub(float a, float b, float c) {
        return new VecD3D(x - a, y - b, z - c);
    }
    public final VecD3D sub(double a, float b, float c) {
        return new VecD3D(x - a, y - b, z - c);
    }
    public final VecD3D sub(float a, double b, float c) {
        return new VecD3D(x - a, y - b, z - c);
    }
    public final VecD3D sub(float a, float b, double c) {
        return new VecD3D(x - a, y - b, z - c);
    }
    public final VecD3D sub(double a, double b, float c) {
        return new VecD3D(x - a, y - b, z - c);
    }
    public final VecD3D sub(double a, float b, double c) {
        return new VecD3D(x - a, y - b, z - c);
    }
    public final VecD3D sub(float a, double b, double c) {
        return new VecD3D(x - a, y - b, z - c);
    }
    public final VecD3D sub(double a, double b, double c) {
        return new VecD3D(x - a, y - b, z - c);
    }
    

    public final VecD3D sub(ReadonlyVec3D v) {
        return new VecD3D(x - v.x(), y - v.y(), z - v.z());
    }
    public final VecD3D sub(ReadonlyVecD3D v) {
        return new VecD3D(x - v.x(), y - v.y(), z - v.z());
    }

    public final VecD3D sub(Vec3D v) {
        return new VecD3D(x - v.x, y - v.y, z - v.z);
    }
    public final VecD3D sub(VecD3D v) {
        return new VecD3D(x - v.x, y - v.y, z - v.z);
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
    public final VecD3D subSelf(float a, float b, float c) {
        x -= a;
        y -= b;
        z -= c;
        return this;
    }
    public final VecD3D subSelf(double a, float b, float c) {
        x -= a;
        y -= b;
        z -= c;
        return this;
    }
    public final VecD3D subSelf(float a, double b, float c) {
        x -= a;
        y -= b;
        z -= c;
        return this;
    }
    public final VecD3D subSelf(float a, float b, double c) {
        x -= a;
        y -= b;
        z -= c;
        return this;
    }
    public final VecD3D subSelf(double a, double b, float c) {
        x -= a;
        y -= b;
        z -= c;
        return this;
    }
    public final VecD3D subSelf(double a, float b, double c) {
        x -= a;
        y -= b;
        z -= c;
        return this;
    }
    public final VecD3D subSelf(float a, double b, double c) {
        x -= a;
        y -= b;
        z -= c;
        return this;
    }
    public final VecD3D subSelf(double a, double b, double c) {
        x -= a;
        y -= b;
        z -= c;
        return this;
    }
    

    public final VecD3D subSelf(ReadonlyVec3D v) {
        x -= v.x();
        y -= v.y();
        z -= v.z();
        return this;
    }
    public final VecD3D subSelf(ReadonlyVecD3D v) {
        x -= v.x();
        y -= v.y();
        z -= v.z();
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
    public final VecD3D subSelf(Vec3D v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        return this;
    }
    public final VecD3D subSelf(VecD3D v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        return this;
    }

    public final VecD2D toD2DXY() {
        return new VecD2D(x, y);
    }

    public final VecD2D toD2DXZ() {
        return new VecD2D(x, z);
    }

    public final VecD2D toD2DYZ() {
        return new VecD2D(y, z);
    }

    public VecD4D toD4D() {
        return new VecD4D(x, y, z, 1);
    }

    public VecD4D toD4D(float w) {
        return new VecD4D(x, y, z, w);
    }
    public VecD4D toD4D(double w) {
        return new VecD4D(x, y, z, w);
    }

    public double[] toArray() {
        return new double[] {
                x, y, z
        };
    }

    public double[] toArray4(float w) {
        return new double[] {
                x, y, z,(double) w
        };
    }
    public double[] toArray4(double w) {
        return new double[] {
                x, y, z, w
        };
    }

    public final VecD3D toCartesian() {
        final double a =  x * Math.cos(z);
        final double xx = a * Math.cos(y);
        final double yy = x * Math.sin(z);
        final double zz = a * Math.sin(y);
        x = xx;
        y = yy;
        z = zz;
        return this;
    }

    public final VecD3D toSpherical() {
        final double xx = Math.abs(x) <= MathUtils.EPS ? MathUtils.EPS : x;
        final double zz = z;

        final double radius = Math.sqrt((xx * xx) + (y * y) + (zz * zz));
        z = Math.asin(y / radius);
        y = Math.atan(zz / xx) + (xx < 0.0 ? MathUtils.PI : 0);
        x = radius;
        return this;
    }

    public String toString() {
        final StringBuffer sb = new StringBuffer(48);
        sb.append("{x:").append(x).append(", y:").append(y).append(", z:")
                .append(z).append("}");
        return sb.toString();
    }

    public final double x() {
        return x;
    }

    public final double y() {
        return y;
    }

    public final double z() {
        return z;
    }

}
