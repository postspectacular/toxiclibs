package toxi.geom;

import toxi.math.MathUtils;

public class SphereIntersectorReflector implements Intersector, Reflector {
	protected Sphere sphere;

	protected Vec3D isectPos, isectDir;

	protected float isectDist;

	protected Vec3D sphereNormal;

	protected float reflectTheta;

	protected Vec3D reflectedDir, reflectedPos;

	public SphereIntersectorReflector(Sphere s) {
		sphere = s;
	}

	public SphereIntersectorReflector(Vec3D o, float r) {
		sphere = new Sphere(o, r);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.geom.Intersector#getIntersectionDir(boolean)
	 */
	public Vec3D getIntersectionDir(boolean normalized) {
		if (isectDir != null) {
			if (normalized) {
				return isectDir.getNormalized();
			}
			return isectDir;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.geom.Intersector#getIntersectionDistance()
	 */
	public float getIntersectionDistance() {
		return isectDist;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.geom.Intersector#getIntersectionPoint()
	 */
	public Vec3D getIntersectionPoint() {
		return isectPos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.geom.Intersector#getNormalAtIntersection()
	 */
	public Vec3D getNormalAtIntersection() {
		return sphereNormal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.geom.Reflector#getReflectedRayPointAtDistance(float)
	 */
	public Vec3D getReflectedRayPointAtDistance(float dist) {
		if (reflectedDir != null) {
			return isectPos.add(reflectedDir.scale(dist));
		}
		else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.geom.Reflector#getReflectionAngle()
	 */
	public float getReflectionAngle() {
		return reflectTheta;
	}

	/**
	 * Calculates the distance of the vector to the given sphere in the
	 * specified direction. A sphere is defined by a 3D point and a radius.
	 * Normalized directional vectors expected.
	 * 
	 * @param ray
	 *            intersection ray
	 * @return distance to sphere in world units, -1 if no intersection.
	 */

	public float intersectRayDistance(Ray3D ray) {
		Vec3D q = sphere.sub(ray);
		float distSquared = q.magSquared();
		float v = q.dot(ray.dir);
		float d = sphere.radius * sphere.radius - (distSquared - v * v);

		// If there was no intersection, return -1
		if (d < 0.0) {
			return -1;
		}

		// Return the distance to the [first] intersecting point
		return v - (float) Math.sqrt(d);
	}

	public boolean intersectsRay(Ray3D ray) {
		isectDist = intersectRayDistance(ray);
		if (isectDist >= 0) {
			// get the intersection point
			isectPos = ray.add(ray.getDirection().scale(isectDist));
			// calculate the direction from our point to the intersection pos
			isectDir = isectPos.sub(ray);
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.geom.Reflector#reflectRay(toxi.geom.Vec3D, toxi.geom.Vec3D)
	 */
	public Ray3D reflectRay(Ray3D ray) {
		if (intersectsRay(ray)) {
			// compute the normal vector of the sphere at the intersection
			// position
			sphereNormal = sphere.tangentPlaneNormalAt(isectPos);
			// compute the reflection angle
			reflectTheta = isectDir.angleBetween(sphereNormal, true) * 2
					+ MathUtils.PI;
			// then form a perpendicular vector standing on the plane spanned by
			// isectDir and sphereNormal
			// this vector will be used to mirror the ray around the
			// intersection point
			Vec3D reflectNormal = isectDir.getNormalized().cross(sphereNormal)
					.normalize();
			if (!reflectNormal.isZeroVector()) {
				// compute the reflected ray direction
				reflectedDir = isectDir.getNormalized().rotateAroundAxis(
						reflectNormal, reflectTheta);
			}
			else {
				reflectedDir = isectDir.getInverted();
			}
			return new Ray3D(isectPos, reflectedDir);
		}
		else {
			return null;
		}
	}
}
