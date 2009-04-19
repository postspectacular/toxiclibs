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

import toxi.math.InterpolateStrategy;
import toxi.math.MathUtils;

/**
 * Comprehensive 3D vector class with additional basic intersection and
 * collision detection features.
 * 
 * @author Karsten Schmidt
 * 
 */
public class Vec3D implements Comparable, DimensionalVector {

	/**
	 * Defines positive X axis
	 */
	public static final Vec3D X_AXIS = new Vec3D(1, 0, 0);

	/**
	 * Defines positive Y axis
	 */
	public static final Vec3D Y_AXIS = new Vec3D(0, 1, 0);

	/**
	 * Defines positive Z axis
	 */
	public static final Vec3D Z_AXIS = new Vec3D(0, 0, 1);

	/**
	 * Creates a new vector from the given angle in the XY plane. The Z
	 * component of the vector will be zero.
	 * 
	 * The resulting vector for theta=0 is equal to the positive X axis.
	 * 
	 * @param theta
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
	 * @return result as new vector
	 */
	public static final Vec3D max(Vec3D a, Vec3D b) {
		return new Vec3D(MathUtils.max(a.x, b.x), MathUtils.max(a.y, b.y),
				MathUtils.max(a.z, b.z));
	}

	/**
	 * Constructs a new vector consisting of the smallest components of both
	 * vectors.
	 * 
	 * @param b
	 *            comparing vector
	 * @return result as new vector
	 */
	public static final Vec3D min(Vec3D a, Vec3D b) {
		return new Vec3D(MathUtils.min(a.x, b.x), MathUtils.min(a.y, b.y),
				MathUtils.min(a.z, b.z));
	}

	/**
	 * Static factory method. Creates a new random unit vector using the default
	 * Math.random() Random instance.
	 * 
	 * @return a new random normalized unit vector.
	 */
	public static final Vec3D randomVector() {
		Vec3D rnd = new Vec3D(MathUtils.normalizedRandom(), MathUtils
				.normalizedRandom(), MathUtils.normalizedRandom());
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
	public static final Vec3D randomVector(Random rnd) {
		Vec3D v = new Vec3D(MathUtils.normalizedRandom(rnd), MathUtils
				.normalizedRandom(rnd), MathUtils.normalizedRandom(rnd));
		return v.normalize();
	}

	/**
	 * X coordinate
	 */
	public float x;

	/**
	 * Y coordinate
	 */
	public float y;

	/**
	 * Z coordinate
	 */
	public float z;

	/**
	 * Creates a new zero vector
	 */
	public Vec3D() {
		x = y = z = 0;
	}

	/**
	 * Creates a new vector with the given coordinates
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vec3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Creates a new vector with the coordinates of the given vector
	 * 
	 * @param v
	 *            vector to be copied
	 */
	public Vec3D(Vec3D v) {
		set(v);
	}

	public final Vec3D abs() {
		x = MathUtils.abs(x);
		y = MathUtils.abs(y);
		z = MathUtils.abs(z);
		return this;
	}

	/**
	 * Adds vector {a,b,c} and returns result as new vector.
	 * 
	 * @param a
	 *            X coordinate
	 * @param b
	 *            Y coordinate
	 * @param c
	 *            Z coordinate
	 * @return result as new vector
	 */
	public final Vec3D add(float a, float b, float c) {
		return new Vec3D(x + a, y + b, z + c);
	}

	/**
	 * Add vector v and returns result as new vector.
	 * 
	 * @param v
	 *            vector to add
	 * @return result as new vector
	 */
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
	 * @return itself
	 */
	public final Vec3D addSelf(float a, float b, float c) {
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
	 * @return itself
	 */
	public final Vec3D addSelf(Vec3D v) {
		x += v.x;
		y += v.y;
		z += v.z;
		return this;
	}

	/**
	 * Computes the angle between this vector and vector V. This function
	 * assumes both vectors are normalized, if this can't be guaranteed, use the
	 * alternative implementation {@link #angleBetween(Vec3D, boolean)}
	 * 
	 * @param v
	 *            vector
	 * @return angle in radians, or NaN if vectors are parallel
	 */
	public final float angleBetween(Vec3D v) {
		return (float) Math.acos(dot(v));
	}

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
	public final float angleBetween(Vec3D v, boolean forceNormalize) {
		float theta;
		if (forceNormalize) {
			theta = getNormalized().dot(v.getNormalized());
		}
		else {
			theta = dot(v);
		}
		return (float) Math.acos(theta);
	}

	/**
	 * Sets all vector components to 0.
	 * 
	 * @return itself
	 */
	public final Vec3D clear() {
		x = y = z = 0;
		return this;
	}

	/**
	 * 
	 * Helper function for {@link toxi.geom.Triangle#closedPoint(Vec3D)}
	 * 
	 * @param a
	 *            start point of line segment
	 * @param b
	 *            end point of line segment
	 * @return closest point on the line segment a -> b
	 */

	public Vec3D closestPointOnLine(Vec3D a, Vec3D b) {
		Vec3D c = sub(a);
		Vec3D v = b.sub(a);

		float d = v.magnitude();
		v.normalize();

		float t = v.dot(c);

		// Check to see if t is beyond the extents of the line segment
		if (t < 0.0f) {
			return a;
		}
		if (t > d) {
			return b;
		}

		// Return the point between 'a' and 'b'
		// set length of V to t. V is normalized so this is easy
		v.scaleSelf(t);

		return a.add(v);
	}

	/**
	 * Compares the length of the vector with another one.
	 * 
	 * @param vec
	 *            vector to compare with
	 * @return -1 if other vector is longer, 0 if both are equal or else +1
	 */
	public int compareTo(Object vec) {
		Vec3D v = (Vec3D) vec;
		if (Float.compare(x, v.x) == 0 && Float.compare(y, v.y) == 0
				&& Float.compare(z, v.z) == 0) {
			return 0;
		}
		if (magSquared() < v.magSquared()) {
			return -1;
		}
		return 1;
	}

	/**
	 * Forcefully fits the vector in the given AABB.
	 * 
	 * @param box
	 * @return itself
	 */
	public final Vec3D constrain(AABB box) {
		x = MathUtils.clip(x, box.minX(), box.maxX());
		y = MathUtils.clip(y, box.minY(), box.maxY());
		z = MathUtils.clip(z, box.minZ(), box.maxZ());
		return this;
	}

	/**
	 * @return a new independent instance/copy of a given vector
	 */
	public final Vec3D copy() {
		return new Vec3D(this);
	}

	/**
	 * Calculates cross-product with vector v. The resulting vector is
	 * perpendicular to both the current and supplied vector.
	 * 
	 * @param v
	 *            vector to cross
	 * @return cross-product as new vector
	 */
	public final Vec3D cross(Vec3D v) {
		return new Vec3D(y * v.z - v.y * z, z * v.x - v.z * x, x * v.y - v.x
				* y);
	}

	/**
	 * Calculates cross-product with vector v. The resulting vector is
	 * perpendicular to both the current and supplied vector and stored in the
	 * supplied result vector.
	 * 
	 * @param v
	 *            vector to cross
	 * @param result
	 *            result vector
	 * @return result vector
	 */
	public final Vec3D crossInto(Vec3D v, Vec3D result) {
		float rx = y * v.z - v.y * z;
		float ry = z * v.x - v.z * x;
		float rz = x * v.y - v.x * y;
		result.set(rx, ry, rz);
		return result;
	}

	/**
	 * Calculates cross-product with vector v. The resulting vector is
	 * perpendicular to both the current and supplied vector and overrides the
	 * current.
	 * 
	 * @param v
	 * @return itself
	 */
	public final Vec3D crossSelf(Vec3D v) {
		float cx = y * v.z - v.y * z;
		float cy = z * v.x - v.z * x;
		z = x * v.y - v.x * y;
		y = cy;
		x = cx;
		return this;
	}

	/**
	 * Calculates distance to another vector
	 * 
	 * @param v
	 *            non-null vector
	 * @return distance or Float.NaN if v=null
	 */
	public final float distanceTo(Vec3D v) {
		if (v != null) {
			float dx = x - v.x;
			float dy = y - v.y;
			float dz = z - v.z;
			return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
		}
		else {
			return Float.NaN;
		}
	}

	/**
	 * Calculates the squared distance to another vector
	 * 
	 * @see #magSquared()
	 * @param v
	 *            non-null vector
	 * @return distance or NaN if v=null
	 */
	public final float distanceToSquared(Vec3D v) {
		if (v != null) {
			float dx = x - v.x;
			float dy = y - v.y;
			float dz = z - v.z;
			return dx * dx + dy * dy + dz * dz;
		}
		else {
			return Float.NaN;
		}
	}

	/**
	 * Computes the scalar product (dot product) with the given vector.
	 * 
	 * @see <a href="http://en.wikipedia.org/wiki/Dot_product">Wikipedia entry<
	 *      /a>
	 * 
	 * @param v
	 * @return dot product
	 */
	public final float dot(Vec3D v) {
		return x * v.x + y * v.y + z * v.z;
	}

	@Override
	public boolean equals(Object obj) {
		Vec3D v = (Vec3D) obj;
		return (Float.compare(x, v.x) == 0 && Float.compare(y, v.y) == 0 && Float
				.compare(z, v.z) == 0);
	}

	/**
	 * Replaces the vector components with integer values of their current
	 * values
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
	 * values
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

	/**
	 * Creates a copy of the vector which forcefully fits in the given AABB.
	 * 
	 * @param box
	 * @return fitted vector
	 */
	public final Vec3D getConstrained(AABB box) {
		return new Vec3D(this).constrain(box);
	}

	public int getDimensions() {
		return 3;
	}

	/**
	 * Creates a new vector whose components are the integer value of their
	 * current values
	 * 
	 * @return result as new vector
	 */
	public final Vec3D getFloored() {
		return new Vec3D(this).floor();
	}

	/**
	 * Creates a new vector whose components are the fractional part of their
	 * current values
	 * 
	 * @return result as new vector
	 */
	public final Vec3D getFrac() {
		return new Vec3D(this).frac();
	}

	/**
	 * Scales vector uniformly by factor -1 ( v = -v )
	 * 
	 * @return result as new vector
	 */
	public final Vec3D getInverted() {
		return new Vec3D(-x, -y, -z);
	}

	/**
	 * Creates a copy of the vector with its magnitude limited to the length
	 * given
	 * 
	 * @param lim
	 *            new maximum magnitude
	 * @return result as new vector
	 */
	public final Vec3D getLimited(float lim) {
		if (magSquared() > lim * lim) {
			return getNormalized().scaleSelf(lim);
		}
		return new Vec3D(this);
	}

	/**
	 * Produces the normalized version as a new vector
	 * 
	 * @return new vector
	 */
	public Vec3D getNormalized() {
		return new Vec3D(this).normalize();
	}

	/**
	 * @see #rotateAroundAxis(Vec3D, float)
	 * @return new result vector
	 */
	public final Vec3D getRotatedAroundAxis(Vec3D axis, float theta) {
		return new Vec3D(this).rotateAroundAxis(axis, theta);
	}

	/**
	 * Creates a new vector rotated by the given angle around the X axis.
	 * 
	 * @param theta
	 * @return rotated vector
	 */
	public final Vec3D getRotatedX(float theta) {
		return new Vec3D(this).rotateX(theta);
	}

	/**
	 * Creates a new vector rotated by the given angle around the Y axis.
	 * 
	 * @param theta
	 * @return rotated vector
	 */
	public final Vec3D getRotatedY(float theta) {
		return new Vec3D(this).rotateY(theta);
	}

	/**
	 * Creates a new vector rotated by the given angle around the Z axis.
	 * 
	 * @param theta
	 * @return rotated vector
	 */
	public final Vec3D getRotatedZ(float theta) {
		return new Vec3D(this).rotateZ(theta);
	}

	/**
	 * Creates a new vector in which all components are replaced with the signum
	 * of their original values. In other words if a components value was
	 * negative its new value will be -1, if zero => 0, if positive => +1
	 * 
	 * @return result vector
	 */
	public Vec3D getSignum() {
		return new Vec3D(this).signum();
	}

	/**
	 * Computes the vector's direction in the XY plane (for example for 2D
	 * points). The positive X axis equals 0 degrees.
	 * 
	 * @return rotation angle
	 */
	public final float headingXY() {
		return (float) Math.atan2(y, x);
	}

	/**
	 * Computes the vector's direction in the XZ plane. The positive X axis
	 * equals 0 degrees.
	 * 
	 * @return rotation angle
	 */
	public final float headingXZ() {
		return (float) Math.atan2(z, x);
	}

	/**
	 * Computes the vector's direction in the YZ plane. The positive Z axis
	 * equals 0 degrees.
	 * 
	 * @return rotation angle
	 */
	public final float headingYZ() {
		return (float) Math.atan2(y, z);
	}

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
	public final Vec3D interpolateTo(Vec3D v, float f) {
		return new Vec3D(x + (v.x - x) * f, y + (v.y - y) * f, z + (v.z - z)
				* f);
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
	 * @return result as new vector
	 */
	public Vec3D interpolateTo(Vec3D v, float f, InterpolateStrategy s) {
		return new Vec3D(s.interpolate(x, v.x, f), s.interpolate(y, v.y, f), s
				.interpolate(z, v.z, f));
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
	public final Vec3D interpolateToSelf(Vec3D v, float f) {
		x += (v.x - x) * f;
		y += (v.y - y) * f;
		z += (v.z - z) * f;
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
	public Vec3D interpolateToSelf(Vec3D v, float f, InterpolateStrategy s) {
		x = s.interpolate(x, v.x, f);
		y = s.interpolate(y, v.y, f);
		z = s.interpolate(z, v.z, f);
		return this;
	}

	/**
	 * Scales vector uniformly by factor -1 ( v = -v ), overrides coordinates
	 * with result
	 * 
	 * @return itself
	 */
	public final Vec3D invert() {
		x *= -1;
		y *= -1;
		z *= -1;
		return this;
	}

	/**
	 * Checks if the point is inside the given AABB.
	 * 
	 * @param box
	 *            bounding box to check
	 * @return true, if point is inside
	 */
	public boolean isInAABB(AABB box) {
		Vec3D min = box.getMin();
		Vec3D max = box.getMax();
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

	/**
	 * Checks if the point is inside the given axis-aligned bounding box.
	 * 
	 * @param boxOrigin
	 *            bounding box origin/center
	 * @param boxExtent
	 *            bounding box extends (half measure)
	 * @return true, if point is inside the box
	 */

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

	/**
	 * Checks if vector has a magnitude of 0
	 * 
	 * @return true, if vector = {0,0,0}
	 */
	public final boolean isZeroVector() {
		return x == 0 && y == 0 && z == 0;
		// return magnitude()<FastMath.EPS;
	}

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
	 * @return itself
	 */
	public final Vec3D jitter(float jx, float jy, float jz) {
		x += MathUtils.normalizedRandom() * jx;
		y += MathUtils.normalizedRandom() * jy;
		z += MathUtils.normalizedRandom() * jz;
		return this;
	}

	public final Vec3D jitter(Vec3D jitterVec) {
		return jitter(jitterVec.x, jitterVec.y, jitterVec.z);
	}

	/**
	 * Limits the vector's magnitude to the length given
	 * 
	 * @param lim
	 *            new maximum magnitude
	 * @return itself
	 */
	public final Vec3D limit(float lim) {
		if (magSquared() > lim * lim) {
			return normalize().scaleSelf(lim);
		}
		return this;
	}

	/**
	 * Calculates the magnitude/eucledian length of the vector
	 * 
	 * @return vector length
	 */
	public float magnitude() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Calculates only the squared magnitude/length of the vector. Useful for
	 * inverse square law applications and/or for speed reasons or if the real
	 * eucledian distance is not required (e.g. sorting).
	 * 
	 * @return squared magnitude (x^2 + y^2 + z^2)
	 */
	public float magSquared() {
		return x * x + y * y + z * z;
	}

	public final Vec3D maxSelf(Vec3D b) {
		x = MathUtils.max(x, b.x);
		y = MathUtils.max(y, b.y);
		z = MathUtils.max(z, b.z);
		return this;
	}

	public final Vec3D minSelf(Vec3D b) {
		x = MathUtils.min(x, b.x);
		y = MathUtils.min(y, b.y);
		z = MathUtils.min(z, b.z);
		return this;
	}

	/**
	 * Applies a uniform modulo operation to the vector, using the same base for
	 * all components.
	 * 
	 * @param base
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
	 * @param by
	 * @param bz
	 * @return itself
	 */

	public final Vec3D modSelf(float bx, float by, float bz) {
		x %= bx;
		y %= by;
		z %= bz;
		return this;
	}

	/**
	 * Normalizes the vector so that its magnitude = 1
	 * 
	 * @return itself
	 */
	public Vec3D normalize() {
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
	 * Rotates the vector around the giving axis
	 * 
	 * @param axis
	 *            rotation axis vector
	 * @param theta
	 *            rotation angle (in radians)
	 * @return itself
	 */
	public final Vec3D rotateAroundAxis(Vec3D axis, float theta) {
		float ux = axis.x * x;
		float uy = axis.x * y;
		float uz = axis.x * z;
		float vx = axis.y * x;
		float vy = axis.y * y;
		float vz = axis.y * z;
		float wx = axis.z * x;
		float wy = axis.z * y;
		float wz = axis.z * z;
		double si = Math.sin(theta);
		double co = Math.cos(theta);
		float xx = (float) (axis.x
				* (ux + vy + wz)
				+ (x * (axis.y * axis.y + axis.z * axis.z) - axis.x * (vy + wz))
				* co + (-wy + vz) * si);
		float yy = (float) (axis.y
				* (ux + vy + wz)
				+ (y * (axis.x * axis.x + axis.z * axis.z) - axis.y * (ux + wz))
				* co + (wx - uz) * si);
		float zz = (float) (axis.z
				* (ux + vy + wz)
				+ (z * (axis.x * axis.x + axis.y * axis.y) - axis.z * (ux + vy))
				* co + (-vx + uy) * si);
		x = xx;
		y = yy;
		z = zz;
		return this;
	}

	/**
	 * Rotates the vector by the given angle around the X axis.
	 * 
	 * @param theta
	 * @return itself
	 */
	public final Vec3D rotateX(float theta) {
		float co = (float) Math.cos(theta);
		float si = (float) Math.sin(theta);
		float zz = co * z - si * y;
		y = si * z + co * y;
		z = zz;
		return this;
	}

	/**
	 * Rotates the vector by the given angle around the Y axis.
	 * 
	 * @param theta
	 * @return itself
	 */
	public final Vec3D rotateY(float theta) {
		float co = (float) Math.cos(theta);
		float si = (float) Math.sin(theta);
		float xx = co * x - si * z;
		z = si * x + co * z;
		x = xx;
		return this;
	}

	/**
	 * Rotates the vector by the given angle around the Z axis.
	 * 
	 * @param theta
	 * @return itself
	 */
	public final Vec3D rotateZ(float theta) {
		float co = (float) Math.cos(theta);
		float si = (float) Math.sin(theta);
		float xx = co * x - si * y;
		y = si * x + co * y;
		x = xx;
		return this;
	}

	/**
	 * Scales vector uniformly and returns result as new vector.
	 * 
	 * @param s
	 *            scale factor
	 * @return new vector
	 */
	public Vec3D scale(float s) {
		return new Vec3D(x * s, y * s, z * s);
	}

	/**
	 * Scales vector non-uniformly and returns result as new vector.
	 * 
	 * @param a
	 *            scale factor for X coordinate
	 * @param b
	 *            scale factor for Y coordinate
	 * @param c
	 *            scale factor for Z coordinate
	 * @return new vector
	 */
	public Vec3D scale(float a, float b, float c) {
		return new Vec3D(x * a, y * b, z * c);
	}

	/**
	 * Scales vector non-uniformly by vector v and returns result as new vector
	 * 
	 * @param s
	 *            scale vector
	 * @return new vector
	 */
	public Vec3D scale(Vec3D s) {
		return new Vec3D(x * s.x, y * s.y, z * s.z);
	}

	/**
	 * Scales vector uniformly and overrides coordinates with result
	 * 
	 * @param s
	 *            scale factor
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
	 * with result
	 * 
	 * @param a
	 *            scale factor for X coordinate
	 * @param b
	 *            scale factor for Y coordinate
	 * @param c
	 *            scale factor for Z coordinate
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
	 * result
	 * 
	 * @param s
	 *            scale vector
	 * @return itself
	 */

	public Vec3D scaleSelf(Vec3D s) {
		x *= s.x;
		y *= s.y;
		z *= s.z;
		return this;
	}

	/**
	 * Overrides coordinates with the given values
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return itself
	 */
	public Vec3D set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	/**
	 * Overrides coordinates with the ones of the given vector
	 * 
	 * @param v
	 *            vector to be copied
	 * @return itself
	 */
	public Vec3D set(Vec3D v) {
		x = v.x;
		y = v.y;
		z = v.z;
		return this;
	}

	/**
	 * Overrides XY coordinates with the ones of the given 2D vector
	 * 
	 * @param v
	 *            2D vector
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

	/**
	 * Subtracts vector {a,b,c} and returns result as new vector.
	 * 
	 * @param a
	 *            X coordinate
	 * @param b
	 *            Y coordinate
	 * @param c
	 *            Z coordinate
	 * @return result as new vector
	 */
	public final Vec3D sub(float a, float b, float c) {
		return new Vec3D(x - a, y - b, z - c);
	}

	/**
	 * Subtracts vector v and returns result as new vector.
	 * 
	 * @param v
	 *            vector to be subtracted
	 * @return result as new vector
	 */
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
	 * @return itself
	 */
	public final Vec3D subSelf(Vec3D v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
		return this;
	}

	public float[] toArray() {
		return new float[] { x, y, z };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer(48);
		sb.append("{x:").append(x).append(", y:").append(y).append(", z:")
				.append(z).append("}");
		return sb.toString();
	}
}
