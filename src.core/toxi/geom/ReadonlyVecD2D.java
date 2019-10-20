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

import toxi.geom.VecD2D.AxisD;
import toxi.math.InterpolateStrategy;
import toxi.math.MathUtils;
import toxi.math.ScaleMap;

/**
 * Readonly, immutable interface wrapper for Vec2D instances. Used throughout
 * the library for safety purposes.
 */
public interface ReadonlyVecD2D {

    /**
     * Adds vector {a,b,c} and returns result as new vector.
     * 
     * @param a
     *            X coordinate
     * @param b
     *            Y coordinate
     * @return result as new vector
     */
    public VecD2D add(float  a, float  b);
    public VecD2D add(double a, float  b);
    public VecD2D add(float  a, double b);
    public VecD2D add(double a, double b);

    public VecD2D VecD2D(Vec2D v);
    public VecD2D VecD2D(ReadonlyVec2D v);
    
    /**
     * Add vector v and returns result as new vector.
     * 
     * @param v
     *            vector to add
     * @return result as new vector
     */
    public VecD2D add(ReadonlyVec2D v);
    public VecD2D add(ReadonlyVecD2D v);


    /**
     * Computes the angle between this vector and vector V. This function
     * assumes both vectors are normalized, if this can't be guaranteed, use the
     * alternative implementation {@link #angleBetween(ReadonlyVec2D, boolean)}
     * 
     * @param v
     *            vector
     * @return angle in radians, or NaN if vectors are parallel
     */
    public double angleBetween(ReadonlyVec2D v);
    public double angleBetween(ReadonlyVecD2D v);

    /**
     * Computes the angle between this vector and vector V
     * 
     * @param v
     *            vector
     * @param forceNormalize
     *            true, if normalized versions of the vectors are to be used
     *            (Note: only copies will be used, original vectors will not be
     *            altered by this method)
     * @return angle in radians, or NaN if vectors are parallel
     */
    public double angleBetween(ReadonlyVec2D v, boolean forceNormalize);
    public double angleBetween(ReadonlyVecD2D v, boolean forceNormalize);

    /**
     * Computes the perpendicular bisector of two points.
     * 
     * @param p
     *            other point
     * @return bisector coefficients as {@link Vec3D}
     */
    public VecD3D bisect(Vec2D p);
    public VecD3D bisect(VecD2D p);

    /**
     * Compares the length of the vector with another one.
     * 
     * @param v
     *            vector to compare with
     * @return -1 if other vector is longer, 0 if both are equal or else +1
     */
    public int compareTo(ReadonlyVec2D v);
    public int compareTo(ReadonlyVecD2D v);

    /**
     * @return a new independent instance/copy of a given vector
     */
    public VecD2D copy();

    /**
     * Calculates the cross-product with the given vector.
     * 
     * @param v
     *            vector
     * @return the magnitude of the vector that would result from a regular 3D
     *         cross product of the input vectors, taking their Z values
     *         implicitly as 0 (i.e. treating the 2D space as a plane in the 3D
     *         space). The 3D cross product will be perpendicular to that plane,
     *         and thus have 0 X & Y components (thus the scalar returned is the
     *         Z value of the 3D cross product vector).
     * @see <a href="http://stackoverflow.com/questions/243945/">Stackoverflow
     *      entry</a>
     */
    public double cross(ReadonlyVec2D v);
    public double cross(ReadonlyVecD2D v);

    /**
     * Calculates distance to another vector
     * 
     * @param v
     *            non-null vector
     * @return distance or Double.NaN if v=null
     */
    public double distanceTo(ReadonlyVec2D v);
    public double distanceTo(ReadonlyVecD2D v);

    /**
     * Calculates the squared distance to another vector
     * 
     * @see #magSquared()
     * @param v
     *            non-null vector
     * @return distance or NaN if v=null
     */
    public double distanceToSquared(ReadonlyVec2D v);
    public double distanceToSquared(ReadonlyVecD2D v);

    /**
     * Computes the scalar product (dot product) with the given vector.
     * 
     * @see <a href="http://en.wikipedia.org/wiki/Dot_product">Wikipedia entry<
     *      /a>
     * 
     * @param v
     * @return dot product
     */
    public double dot(ReadonlyVec2D v);
    public double dot(ReadonlyVecD2D v);

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
    public boolean equalsWithTolerance(ReadonlyVec2D  v, float  tolerance);
    public boolean equalsWithTolerance(ReadonlyVecD2D v, float  tolerance);
    public boolean equalsWithTolerance(ReadonlyVec2D  v, double tolerance);
    public boolean equalsWithTolerance(ReadonlyVecD2D v, double tolerance);

    public VecD2D getAbs();

    /**
     * Converts the vector from polar to Cartesian space. Assumes this order:
     * x=radius, y=theta
     * 
     * @return new vector
     */
    public VecD2D getCartesian();

    public double getComponent(AxisD id);
    public double getComponent(int id);

    /**
     * Creates a copy of the vector which forcefully fits in the given
     * rectangle.
     * 
     * @param r
     * @return fitted vector
     */
    public VecD2D getConstrained(Rect r);

    /**
     * Creates a new vector whose components are the integer value of their
     * current values
     * 
     * @return result as new vector
     */
    public VecD2D getFloored();

    /**
     * Creates a new vector whose components are the fractional part of their
     * current values
     * 
     * @return result as new vector
     */
    public VecD2D getFrac();

    /**
     * Scales vector uniformly by factor -1 ( v = -v )
     * 
     * @return result as new vector
     */
    public VecD2D getInverted();

    /**
     * Creates a copy of the vector with its magnitude limited to the length
     * given
     * 
     * @param lim
     *            new maximum magnitude
     * @return result as new vector
     */
    public VecD2D getLimited(float lim);
    public VecD2D getLimited(double lim);

    /**
     * Produces a new vector with its coordinates passed through the given
     * {@link ScaleMap}.
     * 
     * @param map
     * @return mapped vector
     */
    public VecD2D getMapped(ScaleMap map);
    
    /**
     * Produces the normalized version as a new vector
     * 
     * @return new vector
     */
    public VecD2D getNormalized();

    /**
     * Produces a new vector normalized to the given length.
     * 
     * @param len
     *            new desired length
     * 
     * @return new vector
     */
    public VecD2D getNormalizedTo(float len);
    public VecD2D getNormalizedTo(double len);

    public VecD2D getPerpendicular();

    /**
     * Converts the current vector into polar coordinates. After the conversion
     * the x component of the vector contains the radius (magnitude) and y the
     * rotation angle.
     * 
     * @return new vector
     */
    public VecD2D getPolar();

    public VecD2D getReciprocal();

    public VecD2D getReflected(ReadonlyVec2D normal);
    public VecD2D getReflected(ReadonlyVecD2D normal);

    /**
     * Creates a new vector rotated by the given angle around the Z axis.
     * 
     * @param theta
     * @return rotated vector
     */
    public VecD2D getRotated(float theta);
    public VecD2D getRotated(double theta);

    /**
     * Creates a new vector with its coordinates rounded to the given precision
     * (grid alignment).
     * 
     * @param prec
     * @return grid aligned vector
     */
    public VecD2D getRoundedTo(float prec);
    public VecD2D getRoundedTo(double prec);

    /**
     * Creates a new vector in which all components are replaced with the signum
     * of their original values. In other words if a components value was
     * negative its new value will be -1, if zero => 0, if positive => +1
     * 
     * @return result vector
     */
    public VecD2D getSignum();

    /**
     * Computes the vector's direction in the XY plane (for example for 2D
     * points). The positive X axis equals 0 degrees.
     * 
     * @return rotation angle
     */
    public double heading();

    /**
     * Interpolates the vector towards the given target vector, using linear
     * interpolation
     * 
     * @param v
     *            target vector
     * @param f
     *            interpolation factor (should be in the range 0..1)
     * @return result as new vector
     */
    public VecD2D interpolateTo(ReadonlyVec2D  v, float  f);
    public VecD2D interpolateTo(ReadonlyVecD2D v, float  f);
    public VecD2D interpolateTo(ReadonlyVec2D  v, double f);
    public VecD2D interpolateTo(ReadonlyVecD2D v, double f);

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
     * @return result as new vector
     */
    public VecD2D interpolateTo(ReadonlyVec2D  v, float  f, InterpolateStrategy s);
    public VecD2D interpolateTo(ReadonlyVecD2D v, float  f, InterpolateStrategy s);
    public VecD2D interpolateTo(ReadonlyVec2D  v, double f, InterpolateStrategy s);
    public VecD2D interpolateTo(ReadonlyVecD2D v, double f, InterpolateStrategy s);
    

    /**
     * Checks if the point is inside the given sphere.
     * 
     * @param sO
     *            circle origin/centre
     * @param sR
     *            circle radius
     * @return true, if point is in sphere
     */

    public boolean isInCircleD(ReadonlyVec2D  sO, float  sR);
    public boolean isInCircleD(ReadonlyVecD2D sO, float  sR);
    public boolean isInCircleD(ReadonlyVec2D  sO, double sR);
    public boolean isInCircleD(ReadonlyVecD2D sO, double sR);

    /**
     * Checks if the point is inside the given rectangle.
     * 
     * @param r
     *            bounding rectangle
     * @return true, if point is inside
     */
    public boolean isInRectangle(Rect r);

    /**
     * Checks if point vector is inside the triangle created by the points a, b
     * and c. These points will create a plane and the point checked will have
     * to be on this plane in the region between a,b,c.
     * 
     * Note: The triangle must be defined in clockwise order a,b,c
     * 
     * @return true, if point is in triangle.
     */
    public boolean isInTriangleD(Vec2D  a, Vec2D  b, Vec2D  c);
    public boolean isInTriangleD(VecD2D a, Vec2D  b, Vec2D  c);
    public boolean isInTriangleD(Vec2D  a, VecD2D b, Vec2D  c);
    public boolean isInTriangleD(Vec2D  a, Vec2D  b, VecD2D c);
    public boolean isInTriangleD(VecD2D a, VecD2D b, Vec2D  c);
    public boolean isInTriangleD(VecD2D a, Vec2D  b, VecD2D c);
    public boolean isInTriangleD(Vec2D  a, VecD2D b, VecD2D c);
    public boolean isInTriangleD(VecD2D a, VecD2D b, VecD2D c);

    /**
     * Checks if the vector is parallel with either the X or Y axis (any
     * direction).
     * 
     * @param tolerance
     * @return true, if parallel within the given tolerance
     */
    public boolean isMajorAxis(float tolerance);
    public boolean isMajorAxis(double tolerance);

    /**
     * Checks if vector has a magnitude equals or close to zero (tolerance used
     * is {@link MathUtils#EPS}).
     * 
     * @return true, if zero vector
     */
    public boolean isZeroVector();

    /**
     * Calculates the magnitude/eucledian length of the vector
     * 
     * @return vector length
     */
    public double magnitude();

    /**
     * Calculates only the squared magnitude/length of the vector. Useful for
     * inverse square law applications and/or for speed reasons or if the real
     * eucledian distance is not required (e.g. sorting).
     * 
     * Please note the vector should contain cartesian (not polar) coordinates
     * in order for this function to work. The magnitude of polar vectors is
     * stored in the x component.
     * 
     * @return squared magnitude (x^2 + y^2)
     */
    public double magSquared();

    /**
     * Constructs a new vector consisting of the largest components of both
     * vectors.
     * 
     * @param v
     * @return result as new vector
     */
    public VecD2D max(ReadonlyVec2D v);
    public VecD2D max(ReadonlyVecD2D v);

    /**
     * Constructs a new vector consisting of the smallest components of both
     * vectors.
     * 
     * @param v
     *            comparing vector
     * @return result as new vector
     */
    public VecD2D min(ReadonlyVec2D v);
    public VecD2D min(ReadonlyVecD2D v);

    /**
     * Scales vector uniformly and returns result as new vector.
     * 
     * @param s
     *            scale factor
     * @return new vector
     */
    public VecD2D scale(float s);
    public VecD2D scale(double s);

    /**
     * Scales vector non-uniformly and returns result as new vector.
     * 
     * @param a
     *            scale factor for X coordinate
     * @param b
     *            scale factor for Y coordinate
     * @return new vector
     */
    public VecD2D scale(float  a, float  b);
    public VecD2D scale(double a, float  b);
    public VecD2D scale(float  a, double b);
    public VecD2D scale(double a, double b);

    public VecD2D scale(ReadonlyVec2D s);
    public VecD2D scale(ReadonlyVecD2D s);

    /**
     * Scales vector non-uniformly by vector v and returns result as new vector
     * 
     * @param s
     *            scale vector
     * @return new vector
     */
    public VecD2D scale(Vec2D s);
    public VecD2D scale(VecD2D s);

    /**
     * Subtracts vector {a,b,c} and returns result as new vector.
     * 
     * @param a
     *            X coordinate
     * @param b
     *            Y coordinate
     * @return result as new vector
     */
    public VecD2D sub(float  a, float  b);
    public VecD2D sub(double a, float  b);
    public VecD2D sub(float  a, double b);
    public VecD2D sub(double a, double b);

    public VecD2D sub(ReadonlyVec2D v);
    public VecD2D sub(ReadonlyVecD2D v);

    /**
     * Subtracts vector v and returns result as new vector.
     * 
     * @param v
     *            vector to be subtracted
     * @return result as new vector
     */
    public VecD2D sub(Vec2D v);
    public VecD2D sub(VecD2D v);

    /**
     * Calculates the normal vector on the given ellipse in the direction of the
     * current point.
     * 
     * @param eO
     *            ellipse origin/centre
     * @param eR
     *            ellipse radii
     * @return a unit normal vector to the tangent plane of the ellipsoid in the
     *         point.
     */

    public VecD2D tangentNormalOfEllipse(Vec2D  eO, Vec2D  eR);
    public VecD2D tangentNormalOfEllipse(VecD2D eO, Vec2D  eR);
    public VecD2D tangentNormalOfEllipse(Vec2D  eO, VecD2D eR);
    public VecD2D tangentNormalOfEllipse(VecD2D eO, VecD2D eR);

    /**
     * Creates a 3D version of this vector in the XY plane.
     * 
     * @return D3D vector
     */
    public VecD3D toD3DXY();

    /**
     * Creates a 3D version of this vector in the XZ plane. (The 2D Y coordinate
     * interpreted as Z)
     * 
     * @return D3D vector
     */
    public VecD3D toD3DXZ();

    /**
     * Creates a 3D version of this vector in the YZ plane. (The 2D X coordinate
     * interpreted as Y &amp; 2D Y as Z)
     * 
     * @return 3D vector
     */
    public VecD3D toD3DYZ();

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.DimensionalVector#toArray()
     */
    public double[] toArray();

    public double x();

    public double y();

}