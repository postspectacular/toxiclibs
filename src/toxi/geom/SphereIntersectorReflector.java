package toxi.geom;

import toxi.math.FastMath;

public class SphereIntersectorReflector implements Intersector, Reflector {
	protected Vec3D sOrigin, sRadius;

	protected Vec3D isectPos, isectDir;

	protected float isectDist;

	protected Vec3D sphereNormal;

	protected float reflectTheta;

	protected Vec3D reflectedDir, reflectedPos;

	public SphereIntersectorReflector(Vec3D o, float r) {
		sOrigin = new Vec3D(o);
		sRadius = new Vec3D(r, r, r);
	}

	/* (non-Javadoc)
	 * @see toxi.geom.Intersector#intersectsRay(toxi.geom.Vec3D, toxi.geom.Vec3D)
	 */
	public boolean intersectsRay(Ray3D ray) {
		isectDist = ray.intersectRaySphere(ray.getDirection(), sOrigin, sRadius.x);
		if (isectDist >= 0) {
			// get the intersection point
			isectPos = ray.add(ray.getDirection().scale(isectDist));
			// calculate the direction from our point to the intersection pos
			isectDir = isectPos.sub(ray);
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see toxi.geom.Reflector#reflectRay(toxi.geom.Vec3D, toxi.geom.Vec3D)
	 */
	public Ray3D reflectRay(Ray3D ray) {
		if (intersectsRay(ray)) {
			// compute the normal vector of the sphere at the intersection
			// position
			sphereNormal = isectPos.tangentPlaneNormalOfEllipsoid(sOrigin,
					sRadius);
			// compute the reflection angle
			reflectTheta = isectDir.angleBetween(sphereNormal, true) * 2
					+ FastMath.PI;
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
			} else {
				reflectedDir = isectDir.getInverted();
			}
			return new Ray3D(isectPos,reflectedDir);
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see toxi.geom.Reflector#getReflectionAngle()
	 */
	public float getReflectionAngle() {
		return reflectTheta;
	}

	/* (non-Javadoc)
	 * @see toxi.geom.Reflector#getReflectedRayPointAtDistance(float)
	 */
	public Vec3D getReflectedRayPointAtDistance(float dist) {
		if (reflectedDir != null) {
			return isectPos.add(reflectedDir.scale(dist));
		} else
			return null;
	}

	/* (non-Javadoc)
	 * @see toxi.geom.Intersector#getIntersectionPoint()
	 */
	public Vec3D getIntersectionPoint() {
		return isectPos;
	}

	/* (non-Javadoc)
	 * @see toxi.geom.Intersector#getIntersectionDistance()
	 */
	public float getIntersectionDistance() {
		return isectDist;
	}

	/* (non-Javadoc)
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

	/* (non-Javadoc)
	 * @see toxi.geom.Intersector#getNormalAtIntersection()
	 */
	public Vec3D getNormalAtIntersection() {
		return sphereNormal;
	}
}
