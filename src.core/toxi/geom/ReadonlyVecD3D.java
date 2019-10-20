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

import toxi.geom.VecD3D.AxisD;
import toxi.math.InterpolateStrategy;
import toxi.math.MathUtils;
import toxi.math.ScaleMap;

/**
 * Readonly, immutable interface wrapper for VecD3D instances. Used throughout
 * the library for safety purposes.
 * @param <AxisD>
 */
public interface ReadonlyVecD3D<AxisD> {

    /**
     * Adds vector {a,b,c} and returns result as new vector.
     * 
     * @param a
     *            X coordinate
     * @param b
     *            Y coordinate
     * @param c
     *            Z coordinate
     * 
     * @return result as new vector
     */
    public VecD3D add(float a, float b, float c); 
    public VecD3D add(double a, double b, double c);

    public VecD3D add(ReadonlyVec3D v);
    public VecD3D add(ReadonlyVecD3D v);

    /**
     * Add vector v and returns result as new vector.
     * 
     * @param v
     *            vector to add
     * 
     * @return result as new vector
     */
    public VecD3D add(Vec3D v);
    public VecD3D add(VecD3D v);

    /**
     * Computes the angle between this vector and vector V. This function
     * assumes both vectors are normalized, if this can't be guaranteed, use the
     * alternative implementation {@link #angleBetween(ReadonlyVec3D, boolean)}
     * 
     * @param v
     *            vector
     * 
     * @return angle in radians, or NaN if vectors are parallel
     */
    public double angleBetween(ReadonlyVec3D v);
    public double angleBetween(ReadonlyVecD3D v);

    /**
     * Computes the angle between this vector and vector V.
     * 
     * @param v
     *            vector
     * @param forceNormalize
     *            true, if normalized versions of the vectors are to be used
     *            (Note: only copies will be used, original vectors will not be
     *            altered by this method)
     * 
     * @return angle in radians, or NaN if vectors are parallel
     */
    public double angleBetween(ReadonlyVec3D v, boolean forceNormalize);
    public double angleBetween(ReadonlyVecD3D v, boolean forceNormalize);

    /**
     * Compares the length of the vector with another one.
     * 
     * @param v
     *            vector to compare with
     * 
     * @return -1 if other vector is longer, 0 if both are equal or else +1
     */
    public int compareTo(ReadonlyVec3D v);
    public int compareTo(ReadonlyVecD3D v);

    /**
     * Copy.
     * 
     * @return a new independent instance/copy of a given vector
     */
    public VecD3D copy();
    
   /**
     * Calculates cross-product with vector v. The resulting vector is
     * perpendicular to both the current and supplied vector.
     * 
     * @param v
     *            vector to cross
     * 
     * @return cross-product as new vector
     */
    public VecD3D cross(ReadonlyVec3D v);
    public VecD3D cross(ReadonlyVecD3D v);

    /**
     * Calculates cross-product with vector v. The resulting vector is
     * perpendicular to both the current and supplied vector and stored in the
     * supplied result vector.
     * 
     * @param v
     *            vector to cross
     * @param result
     *            result vector
     * 
     * @return result vector
     */
    public VecD3D crossInto(ReadonlyVec3D  v, VecD3D result);
    public VecD3D crossInto(ReadonlyVecD3D v, VecD3D result);

    /**
     * Calculates distance to another vector.
     * 
     * @param v
     *            non-null vector
     * 
     * @return distance or Float.NaN if v=null
     */
    public double distanceTo(ReadonlyVec3D v);
    public double distanceTo(ReadonlyVecD3D v);
    

    /**
     * Calculates the squared distance to another vector.
     * 
     * @param v
     *            non-null vector
     * 
     * @return distance or NaN if v=null
     * 
     * @see #magSquared()
     */
    public double distanceToSquared(ReadonlyVec3D v);
    public double distanceToSquared(ReadonlyVecD3D v);

    /**
     * Computes the scalar product (dot product) with the given vector.
     * 
     * @param v
     *            the v
     * 
     * @return dot product
     * 
     * @see <a href="http://en.wikipedia.org/wiki/Dot_product">Wikipedia
     *      entry</a>
     */
    public double dot(ReadonlyVec3D v);
    public double dot(ReadonlyVecD3D v);

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj);

    /**
     * Compares this vector with the one given. The vectors are deemed equal if
     * the individual differences of all component values are within the given
     * tolerance.
     * 
     * @param v
     *            the v
     * @param tolerance
     *            the tolerance
     * 
     * @return true, if equal
     */
    public boolean equalsWithTolerance(ReadonlyVec3D  v, float  tolerance);
    public boolean equalsWithTolerance(ReadonlyVecD3D v, float  tolerance);
    public boolean equalsWithTolerance(ReadonlyVec3D  v, double tolerance);
    public boolean equalsWithTolerance(ReadonlyVecD3D v, double tolerance);

    /**
     * Gets the abs.
     * 
     * @return the abs
     */
    public VecD3D getAbs();

    /**
     * Converts the spherical vector back into cartesian coordinates.
     * 
     * @return new vector
     */
    public VecD3D getCartesian();

    public AxisD getClosestAxis();

    public double getComponent(AxisD id);

    public double getComponent(int id);

    /**
     * Creates a copy of the vector which forcefully fits in the given AABB.
     * 
     * @param box
     *            the box
     * 
     * @return fitted vector
     */
    public VecD3D getConstrained(AABB box);
    public VecD3D getConstrained(AABBD box);

    /**
     * Creates a new vector whose components are the integer value of their
     * current values.
     * 
     * @return result as new vector
     */
    public VecD3D getFloored();
    
    /**
     * Creates a new vector whose components are the fractional part of their
     * current values.
     * 
     * @return result as new vector
     */
    public VecD3D getFrac();

    /**
     * Scales vector uniformly by factor -1 ( v = -v ).
     * 
     * @return result as new vector
     */
    public VecD3D getInverted();

    /**
     * Creates a copy of the vector with its magnitude limited to the length
     * given.
     * 
     * @param lim
     *            new maximum magnitude
     * 
     * @return result as new vector
     */
    public VecD3D getLimited(float lim);
    public VecD3D getLimited(double lim);

    /**
     * Produces a new vector with its coordinates passed through the given
     * {@link ScaleMap}.
     * 
     * @param map
     * @return mapped vector
     */
    public VecD3D getMapped(ScaleMap map);

    /**
     * Produces the normalized version as a new vector.
     * 
     * @return new vector
     */
    public VecD3D getNormalized();

    /**
     * Produces a new vector normalized to the given length.
     * 
     * @param len
     *            new desired length
     * 
     * @return new vector
     */
    public VecD3D getNormalizedTo(float len);
    public VecD3D getNormalizedTo(double len);

    /**
     * Returns a multiplicative inverse copy of the vector.
     * 
     * @return new vector
     */
    public VecD3D getReciprocal();

    public VecD3D getReflected(ReadonlyVec3D normal);
    public VecD3D getReflected(ReadonlyVecD3D normal);

    /**
     * Gets the rotated around axis.
     * 
     * @param axis
     *            the axis
     * @param theta
     *            the theta
     * 
     * @return new result vector
     */
    public VecD3D getRotatedAroundAxis(ReadonlyVec3D  axis, float  theta);
    public VecD3D getRotatedAroundAxis(ReadonlyVecD3D axis, float theta);
    public VecD3D getRotatedAroundAxis(ReadonlyVec3D  axis, double  theta);
    public VecD3D getRotatedAroundAxis(ReadonlyVecD3D axis, double theta);

    /**
     * Creates a new vector rotated by the given angle around the X axis.
     * 
     * @param theta
     *            the theta
     * 
     * @return rotated vector
     */
    public VecD3D getRotatedX(float theta);
    public VecD3D getRotatedX(double theta);

    /**
     * Creates a new vector rotated by the given angle around the Y axis.
     * 
     * @param theta
     *            the theta
     * 
     * @return rotated vector
     */
    public VecD3D getRotatedY(float theta);
    public VecD3D getRotatedY(double theta);

    /**
     * Creates a new vector rotated by the given angle around the Z axis.
     * 
     * @param theta
     *            the theta
     * 
     * @return rotated vector
     */
    public VecD3D getRotatedZ(float theta);
    public VecD3D getRotatedZ(double theta);

    /**
     * Creates a new vector with its coordinates rounded to the given precision
     * (grid alignment).
     * 
     * @param prec
     * @return grid aligned vector
     */
    public VecD3D getRoundedTo(float prec);
    public VecD3D getRoundedTo(double prec);

    /**
     * Creates a new vector in which all components are replaced with the signum
     * of their original values. In other words if a components value was
     * negative its new value will be -1, if zero => 0, if positive => +1
     * 
     * @return result vector
     */
    public VecD3D getSignum();

    /**
     * Converts the vector into spherical coordinates. After the conversion the
     * vector components are to be interpreted as:
     * <ul>
     * <li>x = radius</li>
     * <li>y = azimuth</li>
     * <li>z = theta</li>
     * </ul>
     * 
     * @return new vector
     */
    public VecD3D getSpherical();

    /**
     * Computes the vector's direction in the XY plane (for example for 2D
     * points). The positive X axis equals 0 degrees.
     * 
     * @return rotation angle
     */
    public double headingXY();

    /**
     * Computes the vector's direction in the XZ plane. The positive X axis
     * equals 0 degrees.
     * 
     * @return rotation angle
     */
    public double headingXZ();

    /**
     * Computes the vector's direction in the YZ plane. The positive Z axis
     * equals 0 degrees.
     * 
     * @return rotation angle
     */
    public double headingYZ();

    /**
     * Interpolates the vector towards the given target vector, using linear
     * interpolation.
     * 
     * @param v
     *            target vector
     * @param f
     *            interpolation factor (should be in the range 0..1)
     * 
     * @return result as new vector
     */
    public VecD3D interpolateTo(ReadonlyVec3D  v, float f);
    public VecD3D interpolateTo(ReadonlyVecD3D v, float f);
    public VecD3D interpolateTo(ReadonlyVec3D  v, double f);
    public VecD3D interpolateTo(ReadonlyVecD3D v, double f);

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
     * @return result as new vector
     */
    public VecD3D interpolateTo(ReadonlyVec3D  v, float f, InterpolateStrategy s);
    public VecD3D interpolateTo(ReadonlyVecD3D v, float f, InterpolateStrategy s);
    public VecD3D interpolateTo(ReadonlyVec3D  v, double f, InterpolateStrategy s);
    public VecD3D interpolateTo(ReadonlyVecD3D v, double f, InterpolateStrategy s);

    /**
     * Checks if the point is inside the given AABB.
     * 
     * @param box
     *            bounding box to check
     * 
     * @return true, if point is inside
     */
    public boolean isInAABBD(AABB box);
    public boolean isInAABBD(AABBD box);

    /**
     * Checks if the point is inside the given axis-aligned bounding box.
     * 
     * @param boxOrigin
     *            bounding box origin/center
     * @param boxExtent
     *            bounding box extends (half measure)
     * 
     * @return true, if point is inside the box
     */

    public boolean isInAABBD(Vec3D  boxOrigin, Vec3D  boxExtent);
    public boolean isInAABBD(VecD3D boxOrigin, Vec3D  boxExtent);
    public boolean isInAABBD(Vec3D  boxOrigin, VecD3D boxExtent);
    public boolean isInAABBD(VecD3D boxOrigin, VecD3D boxExtent);

    /**
     * Checks if the vector is parallel with either the X or Y axis (any
     * direction).
     * 
     * @param tolerance
     * @return true, if parallel within the given tolerance
     */
    public boolean isMajorAxisD(float tolerance);
    public boolean isMajorAxisD(double tolerance);

    /**
     * Checks if vector has a magnitude equals or close to zero (tolerance used
     * is {@link MathUtils#EPS}).
     * 
     * @return true, if zero vector
     */
    public boolean isZeroVector();

    /**
     * Calculates the magnitude/eucledian length of the vector.
     * 
     * @return vector length
     */
    public double magnitude();

    /**
     * Calculates only the squared magnitude/length of the vector. Useful for
     * inverse square law applications and/or for speed reasons or if the real
     * eucledian distance is not required (e.g. sorting).
     * 
     * @return squared magnitude (x^2 + y^2 + z^2)
     */
    public double magSquared();

    /**
     * Scales vector uniformly and returns result as new vector.
     * 
     * @param s
     *            scale factor
     * 
     * @return new vector
     */
    public VecD3D scale(float s);
    public VecD3D scale(double s);

    /**
     * Scales vector non-uniformly and returns result as new vector.
     * 
     * @param a
     *            scale factor for X coordinate
     * @param b
     *            scale factor for Y coordinate
     * @param c
     *            scale factor for Z coordinate
     * 
     * @return new vector
     */
    public VecD3D scale(float  a, float  b, float  c);
    public VecD3D scale(double a, float  b, float  c);
    public VecD3D scale(float  a, double b, float  c);
    public VecD3D scale(float  a, float  b, double c);
    public VecD3D scale(double a, double b, float  c);
    public VecD3D scale(double a, float  b, double c);
    public VecD3D scale(float  a, double b, double c);
    public VecD3D scale(double a, double b, double c);

    /**
     * Scales vector non-uniformly by vector v and returns result as new vector.
     * 
     * @param s
     *            scale vector
     * 
     * @return new vector
     */
    public VecD3D scale(ReadonlyVec3D s);
    public VecD3D scale(ReadonlyVecD3D s);

    /**
     * Subtracts vector {a,b,c} and returns result as new vector.
     * 
     * @param a
     *            X coordinate
     * @param b
     *            Y coordinate
     * @param c
     *            Z coordinate
     * 
     * @return result as new vector
     */
    public VecD3D sub(float  a, float  b, float  c);
    public VecD3D sub(double a, float  b, float  c);
    public VecD3D sub(float  a, double b, float  c);
    public VecD3D sub(float  a, float  b, double c);
    public VecD3D sub(double a, double b, float  c);
    public VecD3D sub(double a, float  b, double c);
    public VecD3D sub(float  a, double b, double c);
    public VecD3D sub(double a, double b, double c);

    /**
     * Convert from float to double versions of Vec3Dr
     */
    public VecD3D VecD3D(Vec3D v);
    
    /**
     * Subtracts vector v and returns result as new vector.
     * 
     * @param v
     *            vector to be subtracted
     * 
     * @return result as new vector
     */
    public VecD3D sub(ReadonlyVec3D v);
    public VecD3D sub(ReadonlyVecD3D v);

    /**
     * Creates a new 2D vector of the XY components.
     * 
     * @return new vector
     */
    public VecD2D toD2DXY();

    /**
     * Creates a new 2D vector of the XZ components.
     * 
     * @return new vector
     */
    public VecD2D toD2DXZ();

    /**
     * Creates a new 2D vector of the YZ components.
     * 
     * @return new vector
     */
    public VecD2D toD2DYZ();

    /**
     * Creates a Vec4D instance of this vector with the w component set to 1.0
     * 
     * @return 4d vector
     */
    public VecD4D toD4D();

    /**
     * Creates a Vec4D instance of this vector with it w component set to the
     * given value.
     * 
     * @param w
     * @return weighted 4d vector
     */
    public VecD4D toD4D(float w);
    public VecD4D toD4D(double w);

    public double[] toArray();

    public double[] toArray4(float w);
    public double[] toArray4(double w);

    public double x();

    public double y();

    public double z();
}