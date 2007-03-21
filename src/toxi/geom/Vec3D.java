/* 
 * Copyright (c) 2006, 2007 Karsten Schmidt
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

import toxi.math.FastMath;
import toxi.math.InterpolateStrategy;

/**
 * Comprehensive 3D vector class with additional basic intersection and
 * collision detection features.
 * 
 * @author Karsten Schmidt
 * 
 */
public class Vec3D {

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
	 * Classifier constant for {@link #classifyPoint(Vec3D, Vec3D)}
	 */
	public static final int PLANE_FRONT = -1;

	/**
	 * Classifier constant for {@link #classifyPoint(Vec3D, Vec3D)}
	 */
	public static final int PLANE_BACK = 1;

	/**
	 * Classifier constant for {@link #classifyPoint(Vec3D, Vec3D)}
	 */
	public static final int ON_PLANE = 0;

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
	 * Creates a new zero vect	`or
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
	 * Checks if vector has a magnitude of 0
	 * 
	 * @return true, if vector = {0,0,0}
	 */
	public boolean isZeroVector() {
		return x == 0 && y == 0 && z == 0;
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
	 * Normalizes the vector so that its magnitude = 1
	 * 
	 * @return itself
	 */
	public Vec3D normalize() {
		float mag = FastMath.sqrt(x * x + y * y + z * z);
		if (mag > 0) {
			mag = 1f / mag;
			x *= mag;
			y *= mag;
			z *= mag;
		}
		return this;
	}

	/**
	 * Creates a copy of the vector with its magnitude limited to the length given
	 * 
	 * @param lim
	 *            new maximum magnitude
	 * @return result as new vector
	 */
	public Vec3D getLimited(float lim) {
		if (magSquared() > lim * lim) {
			return getNormalized().scaleSelf(lim);
		}
		return new Vec3D(this);
	}

	/**
	 * Limits the vector's magnitude to the length given
	 * 
	 * @param lim
	 *            new maximum magnitude
	 * @return itself
	 */
	public Vec3D limit(float lim) {
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
		return FastMath.sqrt(x * x + y * y + z * z);
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

	/**
	 * Calculates distance to another vector
	 * 
	 * @param v
	 *            non-null vector
	 * @return distance or NaN if v=null
	 */
	public float distanceTo(Vec3D v) {
		if (v != null) {
			float dx = x - v.x;
			float dy = y - v.y;
			float dz = z - v.z;
			return FastMath.sqrt(dx * dx + dy * dy + dz * dz);
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
	public float distanceToSquared(Vec3D v) {
		if (v != null) {
			float dx = x - v.x;
			float dy = y - v.y;
			float dz = z - v.z;
			return dx * dx + dy * dy + dz * dz;
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
	public Vec3D sub(Vec3D v) {
		return new Vec3D(x - v.x, y - v.y, z - v.z);
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
	public Vec3D sub(float a, float b, float c) {
		return new Vec3D(x - a, y - b, z - c);
	}

	/**
	 * Subtracts vector v and overrides coordinates with result.
	 * 
	 * @param v
	 *            vector to be subtracted
	 * @return itself
	 */
	public Vec3D subSelf(Vec3D v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
		return this;
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
	public Vec3D subSelf(float a, float b, float c) {
		x -= a;
		y -= b;
		z -= c;
		return this;
	}

	/**
	 * Add vector v and returns result as new vector.
	 * 
	 * @param v
	 *            vector to add
	 * @return result as new vector
	 */
	public Vec3D add(Vec3D v) {
		return new Vec3D(x + v.x, y + v.y, z + v.z);
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
	public Vec3D add(float a, float b, float c) {
		return new Vec3D(x + a, y + b, z + c);
	}

	/**
	 * Adds vector v and overrides coordinates with result.
	 * 
	 * @param v
	 *            vector to add
	 * @return itself
	 */
	public Vec3D addSelf(Vec3D v) {
		x += v.x;
		y += v.y;
		z += v.z;
		return this;
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
	public Vec3D addSelf(float a, float b, float c) {
		x += a;
		y += b;
		z += c;
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
	 * Scales vector uniformly by factor -1 ( v = -v ), overrides coordinates
	 * with result
	 * 
	 * @return itself
	 */
	public Vec3D invert() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}

	/**
	 * Scales vector uniformly by factor -1 ( v = -v )
	 * 
	 * @return result as new vector
	 */
	public Vec3D getInverted() {
		return new Vec3D(-x, -y, -z);
	}

	/**
	 * Calculates cross-product with vector v. The resulting vector is
	 * perpendicular to both the current and supplied vector.
	 * 
	 * @param v
	 *            vector to cross
	 * @return cross-product as new vector
	 */
	public Vec3D cross(Vec3D v) {
		return new Vec3D(y * v.z - v.y * z, z * v.x - v.z * x, x * v.y - v.x
				* y);
	}

	/**
	 * Calculates cross-product with vector v. The resulting vector is
	 * perpendicular to both the current and supplied vector and overrides the
	 * current.
	 * 
	 * @param v
	 * @return itself
	 */
	public Vec3D crossSelf(Vec3D v) {
		float cx = y * v.z - v.y * z;
		float cy = z * v.x - v.z * x;
		z = x * v.y - v.x * y;
		y = cy;
		x = cx;
		return this;
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
	public Vec3D crossInto(Vec3D v, Vec3D result) {
		float rx = y * v.z - v.y * z;
		float ry = z * v.x - v.z * x;
		float rz = x * v.y - v.x * y;
		result.set(rx, ry, rz);
		return result;
	}

	/**
	 * Computes the scalar product (dot product) with the given vector.
	 * 
	 * @see <a href="http://en.wikipedia.org/wiki/Dot_product">Wikipedia entry</a>
	 * 
	 * @param v
	 * @return dot product
	 */
	public float dot(Vec3D v) {
		return x * v.x + y * v.y + z * v.z;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer(32);
		sb.append("{x:");
		sb.append(x);
		sb.append(", y:");
		sb.append(y);
		sb.append(", z:");
		sb.append(z);
		sb.append("}");
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
	public Vec3D interpolateTo(Vec3D v, float f) {
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
	public Vec3D interpolateToSelf(Vec3D v, float f) {
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
	 * Computes the angle between this vector and vector V. This function
	 * assumes both vectors are normalized, if this can't be guaranteed, use the
	 * alternative implementation {@link #angleBetween(Vec3D, boolean)}
	 * 
	 * @param v
	 *            vector
	 * @return angle in radians, or NaN if vectors are parallel
	 */
	public float angleBetween(Vec3D v) {
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
	public float angleBetween(Vec3D v, boolean forceNormalize) {
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
	public float headingXY() {
		return (float) Math.atan2(y, x);
	}

	/**
	 * Computes the vector's direction in the XZ plane. The positive X axis
	 * equals 0 degrees.
	 * 
	 * @return rotation angle
	 */
	public float headingXZ() {
		return (float) Math.atan2(z, x);
	}

	/**
	 * Computes the vector's direction in the YZ plane. The positive Z axis
	 * equals 0 degrees.
	 * 
	 * @return rotation angle
	 */
	public float headingYZ() {
		return (float) Math.atan2(y, z);
	}

	// intersection code below is adapted from C version at
	// http://www.peroxide.dk/

	/**
	 * Calculates the distance of the vector to the given plane in the specified
	 * direction. A plane is specified by a 3D point and a normal vector
	 * perpendicular to the plane. Normalized directional vectors expected (for
	 * rayDir and planeNormal).
	 * 
	 * @param rayDir
	 *            intersection direction
	 * @param planeOrigin
	 * @param planeNormal
	 * @return distance to plane in world units, -1 if no intersection.
	 */
	public float intersectRayPlane(Vec3D rayDir, Vec3D planeOrigin,
			Vec3D planeNormal) {
		float d = -planeNormal.dot(planeOrigin);
		float numer = planeNormal.dot(this) + d;
		float denom = planeNormal.dot(rayDir);

		// normal is orthogonal to vector, cant intersect
		if (FastMath.abs(denom) < FastMath.EPS)
			return -1;

		return -(numer / denom);
	}

	/**
	 * Calculates the distance of the vector to the given sphere in the
	 * specified direction. A sphere is defined by a 3D point and a radius.
	 * Normalized directional vectors expected.
	 * 
	 * @param rayDir
	 *            intersection direction
	 * @param sphereOrigin
	 * @param sphereRadius
	 * @return distance to sphere in world units, -1 if no intersection.
	 */

	public float intersectRaySphere(Vec3D rayDir, Vec3D sphereOrigin,
			float sphereRadius) {
		Vec3D q = sphereOrigin.sub(this);
		float c = q.magnitude();
		float v = q.dot(rayDir);
		float d = sphereRadius * sphereRadius - (c * c - v * v);

		// If there was no intersection, return -1
		if (d < 0.0)
			return -1;

		// Return the distance to the [first] intersecting point
		return v - FastMath.sqrt(d);
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

	public boolean isInTriangle(Vec3D a, Vec3D b, Vec3D c) {
		Vec3D v1 = sub(a).normalize();
		Vec3D v2 = sub(b).normalize();
		Vec3D v3 = sub(c).normalize();

		float total_angles = (float) Math.acos(v1.dot(v2));
		total_angles += (float) Math.acos(v2.dot(v3));
		total_angles += (float) Math.acos(v3.dot(v1));

		return (FastMath.abs(total_angles - FastMath.TWO_PI) <= 0.005f);
	}

	/**
	 * 
	 * Helper function for {@link #closestPointOnTriangle(Vec3D, Vec3D, Vec3D)}
	 * 
	 * @param a
	 *            start point of line segment
	 * @param b
	 *            end point of line segment
	 * @return closest point on the line segment a -> b
	 */

	public Vec3D closestPointOnLine(Vec3D a, Vec3D b) {
		// Determine t (the length of the vector from ‘a’ to 'this')
		Vec3D c = sub(a);
		Vec3D v = b.sub(a);

		float d = v.magnitude();
		v.normalize();

		float t = v.dot(c);

		// Check to see if ‘t’ is beyond the extents of the line segment
		if (t < 0.0f)
			return a;
		if (t > d)
			return b;

		// Return the point between 'a' and 'b'
		// set length of V to t. V is normalized so this is easy
		v.scale(t);

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

	public Vec3D closestPointOnTriangle(Vec3D a, Vec3D b, Vec3D c) {
		Vec3D Rab = closestPointOnLine(a, b);
		Vec3D Rbc = closestPointOnLine(b, c);
		Vec3D Rca = closestPointOnLine(c, a);

		float dAB = sub(Rab).magnitude();
		float dBC = sub(Rbc).magnitude();
		float dCA = sub(Rca).magnitude();

		float min = dAB;
		Vec3D result = Rab;

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
	 *            sphere origin/centre
	 * @param sR
	 *            sphere radius
	 * @return true, if point is in sphere
	 */

	public boolean isInSphere(Vec3D sO, float sR) {
		float d = this.sub(sO).magnitude();
		return (d <= sR);
	}

	/**
	 * Calculates the normal vector on the given ellipsoid in the direction of
	 * the current point.
	 * 
	 * @param eO
	 *            ellipsoid origin/centre
	 * @param eR
	 *            ellipsoid radius
	 * @return a unit normal vector to the tangent plane of the ellipsoid in the
	 *         point.
	 */

	public Vec3D tangentPlaneNormalOfEllipsoid(Vec3D eO, Vec3D eR) {
		Vec3D p = this.sub(eO);

		float a2 = eR.x * eR.x;
		float b2 = eR.y * eR.y;
		float c2 = eR.z * eR.z;

		return new Vec3D(p.x / a2, p.y / b2, p.z / c2).normalize();
	}

	/**
	 * Checks and classifies the relative position of the point to the given
	 * plane.
	 * 
	 * @param pO
	 *            plane origin
	 * @param pN
	 *            plane normal vector
	 * @return One of the 3 classification codes: PLANE_FRONT, PLANE_BACK,
	 *         ON_PLANE
	 */
	public int classifyPoint(Vec3D pO, Vec3D pN) {
		Vec3D dir = pO.sub(this);
		float d = dir.dot(pN);
		if (d < -FastMath.EPS)
			return PLANE_FRONT;
		else if (d > FastMath.EPS)
			return PLANE_BACK;

		return ON_PLANE;
	}

	/**
	 * Computes the the point closest to the current vector on the surface of
	 * triangle abc.
	 * 
	 * From Real-Time Collision Detection by Christer Ericson, published by
	 * Morgan Kaufmann Publishers, Copyright 2005 Elsevier Inc
	 * 
	 * @param a
	 *            triangle vertex
	 * @param b
	 *            triangle vertex
	 * @param c
	 *            triangle vertex
	 * @return closest point on triangle (result may also be one of a, b or c)
	 */

	public Vec3D closestPointTriangle(Vec3D a, Vec3D b, Vec3D c) {
		Vec3D ab = b.sub(a);
		Vec3D ac = c.sub(a);
		Vec3D bc = c.sub(b);

		Vec3D pa = this.sub(a);
		Vec3D pb = this.sub(b);
		Vec3D pc = this.sub(c);

		// Compute parametric position s for projection P' of P on AB,
		// P' = A + s*AB, s = snom/(snom+sdenom)
		float snom = pa.dot(ab);
		float sdenom = pb.dot(a.sub(b));

		// Compute parametric position t for projection P' of P on AC,
		// P' = A + t*AC, s = tnom/(tnom+tdenom)
		float tnom = pa.dot(ac);
		float tdenom = pc.dot(a.sub(c));

		if (snom <= 0.0f && tnom <= 0.0f)
			return a; // Vertex region early out

		// Compute parametric position u for projection P' of P on BC,
		// P' = B + u*BC, u = unom/(unom+udenom)
		float unom = pb.dot(bc);
		float udenom = pc.dot(b.sub(c));

		if (sdenom <= 0.0f && unom <= 0.0f)
			return b; // Vertex region early out
		if (tdenom <= 0.0f && udenom <= 0.0f)
			return c; // Vertex region early out

		// P is outside (or on) AB if the triple scalar product [N PA PB] <= 0
		Vec3D n = ab.cross(ac);
		float vc = n.dot(a.sub(this).crossSelf(b.sub(this)));

		// If P outside AB and within feature region of AB,
		// return projection of P onto AB
		if (vc <= 0.0f && snom >= 0.0f && sdenom >= 0.0f) {
			// return a + snom / (snom + sdenom) * ab;
			return a.add(ab.scaleSelf(snom / (snom + sdenom)));
		}

		// P is outside (or on) BC if the triple scalar product [N PB PC] <= 0
		float va = n.dot(b.sub(this).crossSelf(c.sub(this)));
		// If P outside BC and within feature region of BC,
		// return projection of P onto BC
		if (va <= 0.0f && unom >= 0.0f && udenom >= 0.0f) {
			// return b + unom / (unom + udenom) * bc;
			return b.add(bc.scaleSelf(unom / (unom + udenom)));
		}

		// P is outside (or on) CA if the triple scalar product [N PC PA] <= 0
		float vb = n.dot(c.sub(this).crossSelf(a.sub(this)));
		// If P outside CA and within feature region of CA,
		// return projection of P onto CA
		if (vb <= 0.0f && tnom >= 0.0f && tdenom >= 0.0f) {
			// return a + tnom / (tnom + tdenom) * ac;
			return a.add(ac.scaleSelf(tnom / (tnom + tdenom)));
		}

		// P must project inside face region. Compute Q using barycentric
		// coordinates
		float u = va / (va + vb + vc);
		float v = vb / (va + vb + vc);
		float w = 1.0f - u - v; // = vc / (va + vb + vc)
		// return u * a + v * b + w * c;
		return a.scale(u).addSelf(b.scale(v)).addSelf(c.scale(w));
	}

	/**
	 * Considers the current vector as centre of a collision sphere with radius
	 * r and checks if the triangle abc intersects with this sphere. The Vec3D p
	 * The point on abc closest to the sphere center is returned via the
	 * supplied result vector argument.
	 * 
	 * @param r
	 *            collision sphere radius
	 * @param a
	 *            triangle vertex
	 * @param b
	 *            triangle vertex
	 * @param c
	 *            triangle vertex
	 * @param result
	 *            a non-null vector for storing the result
	 * @return true, if sphere intersects triangle ABC
	 */
	public boolean intersectSphereTriangle(float r, Vec3D a, Vec3D b, Vec3D c,
			Vec3D result) {
		// Find Vec3D P on triangle ABC closest to sphere center
		result.set(this.closestPointTriangle(a, b, c));

		// Sphere and triangle intersect if the (squared) distance from sphere
		// center to Vec3D p is less than the (squared) sphere radius
		Vec3D v = result.sub(this);
		return v.x * v.x + v.y * v.y + v.z * v.z <= r * r;
	}

	/**
	 * Factory method.
	 * 
	 * @return a new random normalized vector.
	 */
	public static final Vec3D randomVector() {
		Vec3D rnd = new Vec3D((float) Math.random() * 2 - 1, (float) Math
				.random() * 2 - 1, (float) Math.random() * 2 - 1);
		return rnd.normalize();
	}
}