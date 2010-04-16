package toxi.geom;

import toxi.math.MathUtils;

public class SphereIntersectorReflector implements Intersector, Reflector {

    protected Sphere sphere;
    protected IsectData isectData = new IsectData();

    protected Vec3D reflectedDir, reflectedPos;
    protected float reflectTheta;

    public SphereIntersectorReflector(Sphere s) {
        sphere = s;
    }

    public SphereIntersectorReflector(Vec3D o, float r) {
        sphere = new Sphere(o, r);
    }

    public IsectData getIntersectionData() {
        return isectData;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.Reflector#getReflectedRayPointAtDistance(float)
     */
    public Vec3D getReflectedRayPointAtDistance(float dist) {
        if (reflectedDir != null) {
            return isectData.pos.add(reflectedDir.scale(dist));
        } else {
            return null;
        }
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
     * @return the sphere
     */
    public Sphere getSphere() {
        return sphere;
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
        isectData.dist = intersectRayDistance(ray);
        isectData.isIntersection = isectData.dist >= 0;
        if (isectData.isIntersection) {
            // get the intersection point
            isectData.pos = ray.add(ray.getDirection().scale(isectData.dist));
            // calculate the direction from our point to the intersection pos
            isectData.dir = isectData.pos.sub(ray);
            isectData.normal = sphere.tangentPlaneNormalAt(isectData.pos);
        }
        return isectData.isIntersection;
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
            // compute the reflection angle
            reflectTheta =
                    isectData.dir.angleBetween(isectData.normal, true) * 2
                            + MathUtils.PI;
            // then form a perpendicular vector standing on the plane spanned by
            // isectDir and sphereNormal
            // this vector will be used to mirror the ray around the
            // intersection point
            Vec3D reflectNormal =
                    isectData.dir.getNormalized().cross(isectData.normal)
                            .normalize();
            if (!reflectNormal.isZeroVector()) {
                // compute the reflected ray direction
                reflectedDir =
                        isectData.dir.getNormalized().rotateAroundAxis(
                                reflectNormal, reflectTheta);
            } else {
                reflectedDir = isectData.dir.getInverted();
            }
            return new Ray3D(isectData.pos, reflectedDir);
        } else {
            return null;
        }
    }

    /**
     * @param sphere
     *            the sphere to set
     */
    public void setSphere(Sphere sphere) {
        this.sphere = sphere;
    }
}
