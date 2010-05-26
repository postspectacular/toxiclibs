package toxi.geom;

import toxi.geom.Vec3D.Axis;
import toxi.math.InterpolateStrategy;
import toxi.math.MathUtils;

public interface ReadonlyVec3D {

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
    public Vec3D add(float a, float b, float c);

    public Vec3D add(ReadonlyVec3D v);

    /**
     * Add vector v and returns result as new vector.
     * 
     * @param v
     *            vector to add
     * 
     * @return result as new vector
     */
    public Vec3D add(Vec3D v);

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
    public float angleBetween(ReadonlyVec3D v);

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
    public float angleBetween(ReadonlyVec3D v, boolean forceNormalize);

    /**
     * Compares the length of the vector with another one.
     * 
     * @param v
     *            vector to compare with
     * 
     * @return -1 if other vector is longer, 0 if both are equal or else +1
     */
    public int compareTo(ReadonlyVec3D v);

    /**
     * Copy.
     * 
     * @return a new independent instance/copy of a given vector
     */
    public Vec3D copy();

    /**
     * Calculates cross-product with vector v. The resulting vector is
     * perpendicular to both the current and supplied vector.
     * 
     * @param v
     *            vector to cross
     * 
     * @return cross-product as new vector
     */
    public Vec3D cross(ReadonlyVec3D v);

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
    public Vec3D crossInto(ReadonlyVec3D v, Vec3D result);

    /**
     * Calculates distance to another vector.
     * 
     * @param v
     *            non-null vector
     * 
     * @return distance or Float.NaN if v=null
     */
    public float distanceTo(ReadonlyVec3D v);

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
    public float distanceToSquared(ReadonlyVec3D v);

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
    public float dot(ReadonlyVec3D v);

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
    public boolean equalsWithTolerance(ReadonlyVec3D v, float tolerance);

    /**
     * Gets the abs.
     * 
     * @return the abs
     */
    public Vec3D getAbs();

    public float getComponent(Axis id);

    public float getComponent(int id);

    /**
     * Creates a copy of the vector which forcefully fits in the given AABB.
     * 
     * @param box
     *            the box
     * 
     * @return fitted vector
     */
    public Vec3D getConstrained(AABB box);

    /**
     * Creates a new vector whose components are the integer value of their
     * current values.
     * 
     * @return result as new vector
     */
    public Vec3D getFloored();

    /**
     * Creates a new vector whose components are the fractional part of their
     * current values.
     * 
     * @return result as new vector
     */
    public Vec3D getFrac();

    /**
     * Scales vector uniformly by factor -1 ( v = -v ).
     * 
     * @return result as new vector
     */
    public Vec3D getInverted();

    /**
     * Creates a copy of the vector with its magnitude limited to the length
     * given.
     * 
     * @param lim
     *            new maximum magnitude
     * 
     * @return result as new vector
     */
    public Vec3D getLimited(float lim);

    /**
     * Produces the normalized version as a new vector.
     * 
     * @return new vector
     */
    public Vec3D getNormalized();

    /**
     * Produces a new vector normalized to the given length.
     * 
     * @param len
     *            new desired length
     * 
     * @return new vector
     */
    public Vec3D getNormalizedTo(float len);

    /**
     * Returns a multiplicative inverse copy of the vector.
     * 
     * @return new vector
     */
    public Vec3D getReciprocal();

    public Vec3D getReflected(ReadonlyVec3D normal);

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
    public Vec3D getRotatedAroundAxis(ReadonlyVec3D axis, float theta);

    /**
     * Creates a new vector rotated by the given angle around the X axis.
     * 
     * @param theta
     *            the theta
     * 
     * @return rotated vector
     */
    public Vec3D getRotatedX(float theta);

    /**
     * Creates a new vector rotated by the given angle around the Y axis.
     * 
     * @param theta
     *            the theta
     * 
     * @return rotated vector
     */
    public Vec3D getRotatedY(float theta);

    /**
     * Creates a new vector rotated by the given angle around the Z axis.
     * 
     * @param theta
     *            the theta
     * 
     * @return rotated vector
     */
    public Vec3D getRotatedZ(float theta);

    /**
     * Creates a new vector in which all components are replaced with the signum
     * of their original values. In other words if a components value was
     * negative its new value will be -1, if zero => 0, if positive => +1
     * 
     * @return result vector
     */
    public Vec3D getSignum();

    /**
     * Computes the vector's direction in the XY plane (for example for 2D
     * points). The positive X axis equals 0 degrees.
     * 
     * @return rotation angle
     */
    public float headingXY();

    /**
     * Computes the vector's direction in the XZ plane. The positive X axis
     * equals 0 degrees.
     * 
     * @return rotation angle
     */
    public float headingXZ();

    /**
     * Computes the vector's direction in the YZ plane. The positive Z axis
     * equals 0 degrees.
     * 
     * @return rotation angle
     */
    public float headingYZ();

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
    public Vec3D interpolateTo(ReadonlyVec3D v, float f);

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
    public Vec3D interpolateTo(ReadonlyVec3D v, float f, InterpolateStrategy s);

    /**
     * Checks if the point is inside the given AABB.
     * 
     * @param box
     *            bounding box to check
     * 
     * @return true, if point is inside
     */
    public boolean isInAABB(AABB box);

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

    public boolean isInAABB(Vec3D boxOrigin, Vec3D boxExtent);

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
    public float magnitude();

    /**
     * Calculates only the squared magnitude/length of the vector. Useful for
     * inverse square law applications and/or for speed reasons or if the real
     * eucledian distance is not required (e.g. sorting).
     * 
     * @return squared magnitude (x^2 + y^2 + z^2)
     */
    public float magSquared();

    /**
     * Scales vector uniformly and returns result as new vector.
     * 
     * @param s
     *            scale factor
     * 
     * @return new vector
     */
    public Vec3D scale(float s);

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
    public Vec3D scale(float a, float b, float c);

    /**
     * Scales vector non-uniformly by vector v and returns result as new vector.
     * 
     * @param s
     *            scale vector
     * 
     * @return new vector
     */
    public Vec3D scale(ReadonlyVec3D s);

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
    public Vec3D sub(float a, float b, float c);

    /**
     * Subtracts vector v and returns result as new vector.
     * 
     * @param v
     *            vector to be subtracted
     * 
     * @return result as new vector
     */
    public Vec3D sub(ReadonlyVec3D v);

    /**
     * Creates a new 2D vector of the XY components.
     * 
     * @return new vector
     */
    public Vec2D to2DXY();

    /**
     * Creates a new 2D vector of the XZ components.
     * 
     * @return new vector
     */
    public Vec2D to2DXZ();

    /**
     * Creates a new 2D vector of the YZ components.
     * 
     * @return new vector
     */
    public Vec2D to2DYZ();

    public float[] toArray();

    /**
     * Converts the spherical vector back into cartesian coordinates.
     * 
     * @return itself
     */
    public Vec3D toCartesian();

    /**
     * Converts the vector into spherical coordinates. After the conversion the
     * vector components are to be interpreted as:
     * <ul>
     * <li>x = radius</li>
     * <li>y = azimuth</li>
     * <li>z = theta</li>
     * </ul>
     * 
     * @return itself
     */
    public Vec3D toSpherical();

    public float x();

    public float y();

    public float z();

}