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

import toxi.math.InterpolateStrategy;
import toxi.math.MathUtils;
import toxi.math.ScaleMap;

/**
 * Readonly, immutable interface wrapper for VecD3D instances. Used throughout
 * the library for safety purposes.
 */
public interface ReadonlyVecD4D {

    /**
     * Add vector v and returns result as new vector.
     * 
     * @param v
     *            vector to add
     * 
     * @return result as new vector
     */
    public VecD4D add(ReadonlyVecD4D v);

    public VecD4D addScaled(ReadonlyVecD4D t, double s);

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
    public VecD4D addXYZ(double a, double b, double c);

    /**
     * Returns the (4-space) angle in radians between this vector and the vector
     * parameter; the return value is constrained to the range [0,PI].
     * 
     * @param v
     *            the other vector
     * @return the angle in radians in the range [0,PI]
     */
    public double angleBetween(ReadonlyVecD4D v);

    /**
     * Compares the length of the vector with another one.
     * 
     * @param v
     *            vector to compare with
     * 
     * @return -1 if other vector is longer, 0 if both are equal or else +1
     */
    public int compareTo(ReadonlyVecD4D v);

    /**
     * Copy.
     * 
     * @return a new independent instance/copy of a given vector
     */
    public VecD4D copy();

    /**
     * Calculates distance to another vector.
     * 
     * @param v
     *            non-null vector
     * 
     * @return distance or Float.NaN if v=null
     */
    public double distanceTo(ReadonlyVecD4D v);

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
    public double distanceToSquared(ReadonlyVecD4D v);

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
    public double dot(ReadonlyVecD4D v);

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
    public boolean equalsWithTolerance(ReadonlyVecD4D v, double tolerance);

    /**
     * Gets the abs.
     * 
     * @return the abs
     */
    public VecD4D getAbs();

    /**
     * Scales vector uniformly by factor -1 ( v = -v ).
     * 
     * @return result as new vector
     */
    public VecD4D getInvertedXYZ();

    /**
     * Produces a new vector with all of its coordinates passed through the
     * given {@link ScaleMap}. This version also maps the w coordinate. For
     * mapping only XYZ use the {@link #getMappedXYZ(ScaleMap)} version.
     * 
     * @param map
     * @return mapped vector
     */
    public VecD4D getMapped(ScaleMap map);

    /**
     * Produces a new vector with only its XYZ coordinates passed through the
     * given {@link ScaleMap}. This version keeps the original w coordinate. For
     * mapping all XYZW, use the {@link #getMapped(ScaleMap)} version.
     * 
     * @param map
     * @return mapped vector
     */
    public VecD4D getMappedXYZ(ScaleMap map);

    /**
     * Produces the normalized version as a new vector.
     * 
     * @return new vector
     */
    public VecD4D getNormalized();

    /**
     * Produces a new vector normalized to the given length.
     * 
     * @param len
     *            new desired length
     * 
     * @return new vector
     */
    public VecD4D getNormalizedTo(double len);

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
    public VecD4D getRotatedAroundAxis(ReadonlyVecD3D axis, double theta);

    /**
     * Creates a new vector rotated by the given angle around the X axis.
     * 
     * @param theta
     *            the theta
     * 
     * @return rotated vector
     */
    public VecD4D getRotatedX(double theta);

    /**
     * Creates a new vector rotated by the given angle around the Y axis.
     * 
     * @param theta
     *            the theta
     * 
     * @return rotated vector
     */
    public VecD4D getRotatedY(double theta);

    /**
     * Creates a new vector rotated by the given angle around the Z axis.
     * 
     * @param theta
     *            the theta
     * 
     * @return rotated vector
     */
    public VecD4D getRotatedZ(double theta);

    /**
     * Creates a new vector with its XYZ coordinates rounded to the given
     * precision (grid alignment). The weight component remains unmodified.
     * 
     * @param prec
     * @return grid aligned vector
     */
    public VecD4D getRoundedXYZTo(double prec);

    public VecD4D getUnweighted();

    public VecD4D getWeighted();

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
    public VecD4D interpolateTo(ReadonlyVecD4D v, double f);

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
    public VecD4D interpolateTo(ReadonlyVecD4D v, double f, InterpolateStrategy s);

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
    public VecD4D scale(double s);

    /**
     * Scales vector non-uniformly and returns result as new vector.
     * 
     * @param x
     *            scale factor for X coordinate
     * @param y
     *            scale factor for Y coordinate
     * @param z
     *            scale factor for Z coordinate
     * 
     * @return new vector
     */
    public VecD4D scale(double x, double y, double z, double w);

    /**
     * Scales vector non-uniformly by vector v and returns result as new vector.
     * 
     * @param s
     *            scale vector
     * 
     * @return new vector
     */
    public VecD4D scale(ReadonlyVecD4D s);

    /**
     * Subtracts vector v and returns result as new vector.
     * 
     * @param v
     *            vector to be subtracted
     * 
     * @return result as new vector
     */
    public VecD4D sub(ReadonlyVecD4D v);

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
    public VecD4D subXYZ(double a, double b, double c);

    public double[] toArray();

    public VecD3D unweightInto(VecD3D out);

    public double w();

    public VecD3D weightInto(VecD3D out);

    public double x();

    public double y();

    public double z();
}