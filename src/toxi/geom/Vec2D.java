/* 
 * Copyright (c) 2006-2008 Karsten Schmidt
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
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package toxi.geom;

import java.awt.Rectangle;
import java.util.Random;

import toxi.math.MathUtils;
import toxi.math.InterpolateStrategy;

/**
 * Comprehensive 2D vector class with additional basic intersection and
 * collision detection features.
 * 
 * @author Karsten Schmidt
 * 
 */
public class Vec2D {

	/**
	 * Defines positive X axis
	 */
	public static final Vec2D X_AXIS = new Vec2D(1, 0);

	/**
	 * Defines positive Y axis
	 */
	public static final Vec2D Y_AXIS = new Vec2D(0, 1);

	/**
	 * X coordinate
	 */
	public float x;

	/**
	 * Y coordinate
	 */
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

	/**
	 * @return a new independent instance/copy of a given vector
	 */
	public final Vec2D copy() {
		return new Vec2D(this);
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

	/**
	 * Checks if vector has a magnitude of 0
	 * 
	 * @return true, if vector = {0,0,0}
	 */
	public final boolean isZeroVector() {
		return x == 0 && y == 0;
		// return magnitude()<FastMath.EPS;
	}

	/**
	 * Produces the normalized version as a new vector
	 * 
	 * @return new vector
	 */
	public final Vec2D getNormalized() {
		return new Vec2D(this).normalize();
	}

	/**
	 * Normalizes the vector so that its magnitude = 1
	 * 
	 * @return itself
	 */
	public final Vec2D normalize() {
		float mag = MathUtils.sqrt(x * x + y * y);
		if (mag > 0) {
			mag = 1f / mag;
			x *= mag;
			y *= mag;
		}
		return this;
	}

	/**
	 * Creates a copy of the vector with its magnitude limited to the length
	 * given
	 * 
	 * @param lim
	 *            new maximum magnitude
	 * @return result as new vector
	 */
	public final Vec2D getLimited(float lim) {
		if (magSquared() > lim * lim) {
			return getNormalized().scaleSelf(lim);
		}
		return new Vec2D(this);
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

	/**
	 * Forcefully fits the vector in the given rectangle.
	 * 
	 * @param r
	 * @return itself
	 */

	public final Vec2D constrain(Rectangle r) {
		x = MathUtils.max(MathUtils.min(x, r.x + r.width), r.x);
		y = MathUtils.max(MathUtils.min(y, r.y + r.height), r.y);
		return this;
	}

	/**
	 * Creates a copy of the vector which forcefully fits in the given
	 * rectangle.
	 * 
	 * @param r
	 * @return fitted vector
	 */
	public final Vec2D getConstrained(Rectangle r) {
		return new Vec2D(MathUtils.max(MathUtils.min(x, r.x + r.width), r.x),
				MathUtils.max(MathUtils.min(y, r.y + r.height), r.y));
	}

	/**
	 * Calculates the magnitude/eucledian length of the vector
	 * 
	 * @return vector length
	 */
	public final float magnitude() {
		return MathUtils.sqrt(x * x + y * y);
	}

	/**
	 * Calculates only the squared magnitude/length of the vector. Useful for
	 * inverse square law applications and/or for speed reasons or if the real
	 * eucledian distance is not required (e.g. sorting).
	 * 
	 * @return squared magnitude (x^2 + y^2)
	 */
	public final float magSquared() {
		return x * x + y * y;
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
			return MathUtils.sqrt(dx * dx + dy * dy);
		} else {
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
			return dx * dx + dy * dy;
		} else {
			return Float.NaN;
		}
	}

	/**
	 * Subtracts vector v and returns result as new vector.
	 * 
	 * @param v
	 *            vector to be subtracted
	 * @return result as new vector
	 */
	public final Vec2D sub(Vec2D v) {
		return new Vec2D(x - v.x, y - v.y);
	}

	/**
	 * Subtracts vector {a,b,c} and returns result as new vector.
	 * 
	 * @param a
	 *            X coordinate
	 * @param b
	 *            Y coordinate
	 * @return result as new vector
	 */
	public final Vec2D sub(float a, float b) {
		return new Vec2D(x - a, y - b);
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
	 * Add vector v and returns result as new vector.
	 * 
	 * @param v
	 *            vector to add
	 * @return result as new vector
	 */
	public final Vec2D add(Vec2D v) {
		return new Vec2D(x + v.x, y + v.y);
	}

	/**
	 * Adds vector {a,b,c} and returns result as new vector.
	 * 
	 * @param a
	 *            X coordinate
	 * @param b
	 *            Y coordinate
	 * @return result as new vector
	 */
	public final Vec2D add(float a, float b) {
		return new Vec2D(x + a, y + b);
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
	 * Scales vector uniformly and returns result as new vector.
	 * 
	 * @param s
	 *            scale factor
	 * @return new vector
	 */
	public final Vec2D scale(float s) {
		return new Vec2D(x * s, y * s);
	}

	/**
	 * Scales vector non-uniformly and returns result as new vector.
	 * 
	 * @param a
	 *            scale factor for X coordinate
	 * @param b
	 *            scale factor for Y coordinate
	 * @return new vector
	 */
	public final Vec2D scale(float a, float b) {
		return new Vec2D(x * a, y * b);
	}

	/**
	 * Scales vector non-uniformly by vector v and returns result as new vector
	 * 
	 * @param s
	 *            scale vector
	 * @return new vector
	 */
	public final Vec2D scale(Vec2D s) {
		return new Vec2D(x * s.x, y * s.y);
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

	/**
	 * Scales vector uniformly by factor -1 ( v = -v )
	 * 
	 * @return result as new vector
	 */
	public final Vec2D getInverted() {
		return new Vec2D(-x, -y);
	}

	/**
	 * Creates a new vector whose components are the integer value of their
	 * current values
	 * 
	 * @return result as new vector
	 */
	public final Vec2D getFloored() {
		return new Vec2D(MathUtils.fastFloor(x), MathUtils.fastFloor(y));
	}

	/**
	 * Replaces the vector components with integer values of their current
	 * values
	 * 
	 * @return itself
	 */
	public final Vec2D floor() {
		x = MathUtils.fastFloor(x);
		y = MathUtils.fastFloor(y);
		return this;
	}

	/**
	 * Creates a new vector whose components are the fractional part of their
	 * current values
	 * 
	 * @return result as new vector
	 */
	public final Vec2D getFrac() {
		return new Vec2D(x - MathUtils.fastFloor(x), y - MathUtils.fastFloor(y));
	}

	/**
	 * Replaces the vector components with the fractional part of their current
	 * values
	 * 
	 * @return itself
	 */
	public final Vec2D frac() {
		x -= MathUtils.fastFloor(x);
		y -= MathUtils.fastFloor(y);
		return this;
	}

	/**
	 * Constructs a new vector consisting of the smallest components of both
	 * vectors.
	 * 
	 * @param v
	 *            comparing vector
	 * @return result as new vector
	 */
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
	 * Constructs a new vector consisting of the largest components of both
	 * vectors.
	 * 
	 * @param v
	 * @return result as new vector
	 */
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

	/**
	 * Computes the scalar product (dot product) with the given vector.
	 * 
	 * @see <a href="http://en.wikipedia.org/wiki/Dot_product">Wikipedia entry</a>
	 * 
	 * @param v
	 * @return dot product
	 */
	public final float dot(Vec2D v) {
		return x * v.x + y * v.y;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer(32);
		sb.append("{x:").append(x).append(", y:").append(y).append("}");
		return sb.toString();
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
	public final Vec2D interpolateTo(Vec2D v, float f) {
		return new Vec2D(x + (v.x - x) * f, y + (v.y - y) * f);
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

	/**
	 * Computes the angle between this vector and vector V. This function
	 * assumes both vectors are normalized, if this can't be guaranteed, use the
	 * alternative implementation {@link #angleBetween(Vec2D, boolean)}
	 * 
	 * @param v
	 *            vector
	 * @return angle in radians, or NaN if vectors are parallel
	 */
	public final float angleBetween(Vec2D v) {
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
	 * Computes the vector's direction in the XY plane (for example for 2D
	 * points). The positive X axis equals 0 degrees.
	 * 
	 * @return rotation angle
	 */
	public final float heading() {
		return (float) Math.atan2(y, x);
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

	/**
	 * Creates a new vector rotated by the given angle around the Z axis.
	 * 
	 * @param theta
	 * @return rotated vector
	 */
	public final Vec2D getRotated(float theta) {
		return new Vec2D(this).rotate(theta);
	}

	/**
	 * Calculates the distance of the vector to the given sphere in the
	 * specified direction. A sphere is defined by a 3D point and a radius.
	 * Normalized directional vectors expected.
	 * 
	 * @param rayDir
	 *            intersection direction
	 * @param circleOrigin
	 * @param circleRadius
	 * @return distance to sphere in world units, -1 if no intersection.
	 */

	public float intersectRayCircle(Vec2D rayDir, Vec2D circleOrigin,
			float circleRadius) {
		Vec2D q = circleOrigin.sub(this);
		float c = q.magnitude();
		float v = q.dot(rayDir);
		float d = circleRadius * circleRadius - (c * c - v * v);

		// If there was no intersection, return -1
		if (d < 0.0)
			return -1;

		// Return the distance to the [first] intersecting point
		return v - (float) Math.sqrt(d);
	}

	/**
	 * Checks if point vector is inside the triangle created by the points a, b
	 * and c. These points will create a plane and the point checked will have
	 * to be on this plane in the region between a,b,c.
	 * 
	 * Note: The triangle must be defined in clockwise order a,b,c
	 * 
	 * @return true, if point is in triangle.
	 */

	public boolean isInTriangle(Vec2D a, Vec2D b, Vec2D c) {
		Vec2D v1 = sub(a).normalize();
		Vec2D v2 = sub(b).normalize();
		Vec2D v3 = sub(c).normalize();

		float total_angles = (float) Math.acos(v1.dot(v2));
		total_angles += (float) Math.acos(v2.dot(v3));
		total_angles += (float) Math.acos(v3.dot(v1));

		return (MathUtils.abs(total_angles - MathUtils.TWO_PI) <= 0.005f);
	}

	/**
	 * 
	 * Helper function for {@link #closestPointOnTriangle(Vec2D, Vec2D, Vec2D)}
	 * 
	 * @param a
	 *            start point of line segment
	 * @param b
	 *            end point of line segment
	 * @return closest point on the line segment a -> b
	 */

	public Vec2D closestPointOnLine(Vec2D a, Vec2D b) {
		// Determine t (the length of the vector from �a� to 'this')
		Vec2D c = sub(a);
		Vec2D v = b.sub(a);

		float d = v.magnitude();
		v.normalize();

		float t = v.dot(c);

		// Check to see if point is beyond the extents of the line segment
		if (t < 0.0f)
			return a;
		if (t > d)
			return b;

		// Return the point between 'a' and 'b'
		// set length of V to t. V is normalized so this is easy
		v.scaleSelf(t);

		return a.add(v);
	}

	/**
	 * Finds and returns the closest point on any of the edges of the given
	 * triangle.
	 * 
	 * @param a
	 *            triangle vertex
	 * @param b
	 *            triangle vertex
	 * @param c
	 *            triangle vertex
	 * @return closest point
	 */

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
		if (dCA < min)
			result = Rca;

		return result;
	}

	/**
	 * Checks if the point is inside the given sphere.
	 * 
	 * @param sO
	 *            circle origin/centre
	 * @param sR
	 *            circle radius
	 * @return true, if point is in sphere
	 */

	public boolean isInCircle(Vec2D sO, float sR) {
		float d = this.sub(sO).magSquared();
		return (d <= sR * sR);
	}

	/**
	 * Checks if the point is inside the given rectangle.
	 * 
	 * @param r
	 *            bounding rectangle
	 * @return true, if point is inside
	 */
	public boolean isInRecangle(Rectangle r) {
		if (x < r.x || x > r.x + r.width)
			return false;
		if (y < r.y || y > r.y + r.height)
			return false;
		return true;
	}

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

	public Vec2D tangentNormalOfEllipse(Vec2D eO, Vec2D eR) {
		Vec2D p = this.sub(eO);

		float xr2 = eR.x * eR.x;
		float yr2 = eR.y * eR.y;

		return new Vec2D(p.x / xr2, p.y / yr2).normalize();
	}

	/**
	 * Static factory method. Creates a new random unit vector using the default
	 * Math.random() Random instance.
	 * 
	 * @return a new random normalized unit vector.
	 */
	public static final Vec2D randomVector() {
		Vec2D rnd = new Vec2D((float) Math.random() * 2 - 1, (float) Math
				.random() * 2 - 1);
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
	 * Creates a new vector from the given angle in the XY plane. The Z
	 * component of the vector will be zero.
	 * 
	 * The resulting vector for theta=0 is equal to the positive X axis.
	 * 
	 * @param theta
	 * @return
	 */
	public static final Vec2D fromTheta(float theta) {
		return new Vec2D((float) Math.cos(theta), (float) Math.sin(theta));
	}
}