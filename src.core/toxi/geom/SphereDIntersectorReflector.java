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

public class SphereDIntersectorReflector implements IntersectorD3D, ReflectorD3D {

    protected SphereD sphere;
    protected IsectDataD3D isectData;

    protected ReadonlyVecD3D reflectedDir, reflectedPos;
    protected double reflectTheta;

    public SphereDIntersectorReflector(SphereD s) {
        sphere = s;
        isectData = new IsectDataD3D();
    }

    public SphereDIntersectorReflector(VecD3D o, double r) {
        this(new SphereD(o, r));
    }

    public IsectDataD3D getIntersectionDataD() {
        return isectData;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.Reflector3D#getReflectedRayPointAtDistance
     */
    public ReadonlyVecD3D getReflectedRayDPointAtDistance(double dist) {
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
    public double getReflectionAngle() {
        return reflectTheta;
    }

    /**
     * @return the sphere
     */
    public SphereD getSphereD() {
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

    public double intersectRayDDistance(RayD3D ray) {
        ReadonlyVecD3D q = sphere.sub(ray);
        double distSquared = q.magSquared();
        double v = q.dot(ray.dir);
        double d = sphere.radius * sphere.radius - (distSquared - v * v);

        // If there was no intersection, return -1
        if (d < 0.0) {
            return -1;
        }

        // Return the distance to the [first] intersecting point
        return v -  Math.sqrt(d);
    }

    public boolean intersectsRayD(RayD3D ray) {
        isectData.dist = intersectRayDDistance(ray);
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
     * @see toxi.geom.Reflector3D#reflectRay(toxi.geom.VecD3D, toxi.geom.VecD3D)
     */
    public RayD3D reflectRayD(RayD3D ray) {
        if (intersectsRayD(ray)) {
            // compute the normal vector of the sphere at the intersection
            // position
            // compute the reflection angle
            reflectTheta = isectData.dir.angleBetween(isectData.normal, true)
                    * 2 + MathUtils.PI;
            // then form a perpendicular vector standing on the plane spanned by
            // isectDir and sphereNormal
            // this vector will be used to mirror the ray around the
            // intersection point
            VecD3D reflectNormal = isectData.dir.getNormalized()
                    .cross(isectData.normal).normalize();
            if (!reflectNormal.isZeroVector()) {
                // compute the reflected ray direction
                reflectedDir = isectData.dir.getNormalized().rotateAroundAxis(
                        reflectNormal, reflectTheta);
            } else {
                reflectedDir = isectData.dir.getInverted();
            }
            return new RayD3D(isectData.pos, reflectedDir);
        } else {
            return null;
        }
    }

    /**
     * @param sphere
     *            the sphere to set
     */
    public void setSphereD(SphereD sphere) {
        this.sphere = sphere;
    }

}
