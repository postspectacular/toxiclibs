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

import toxi.math.MathUtils;

public class SphereIntersectorReflector implements Intersector3D, Reflector3D {

    protected Sphere sphere;
    protected IsectData3D isectData;

    protected ReadonlyVec3D reflectedDir, reflectedPos;
    protected float reflectTheta;

    public SphereIntersectorReflector(Sphere s) {
        sphere = s;
        isectData = new IsectData3D();
    }

    public SphereIntersectorReflector(Vec3D o, float r) {
        this(new Sphere(o, r));
    }

    public IsectData3D getIntersectionData() {
        return isectData;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.Reflector3D#getReflectedRayPointAtDistance(float)
     */
    public ReadonlyVec3D getReflectedRayPointAtDistance(float dist) {
        if (reflectedDir != null) {
            return isectData.pos.add(reflectedDir.scale(dist));
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.Reflector3D#getReflectionAngle()
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
        ReadonlyVec3D q = sphere.sub(ray);
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
     * @see toxi.geom.Reflector3D#reflectRay(toxi.geom.Vec3D, toxi.geom.Vec3D)
     */
    public Ray3D reflectRay(Ray3D ray) {
        if (intersectsRay(ray)) {
            // compute the normal vector of the sphere at the intersection
            // position
            // compute the reflection angle
            reflectTheta = isectData.dir.angleBetween(isectData.normal, true)
                    * 2 + MathUtils.PI;
            // then form a perpendicular vector standing on the plane spanned by
            // isectDir and sphereNormal
            // this vector will be used to mirror the ray around the
            // intersection point
            Vec3D reflectNormal = isectData.dir.getNormalized()
                    .cross(isectData.normal).normalize();
            if (!reflectNormal.isZeroVector()) {
                // compute the reflected ray direction
                reflectedDir = isectData.dir.getNormalized().rotateAroundAxis(
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
