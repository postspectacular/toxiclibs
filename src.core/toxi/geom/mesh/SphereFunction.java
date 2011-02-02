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

package toxi.geom.mesh;

import toxi.geom.Sphere;
import toxi.geom.Vec3D;
import toxi.math.MathUtils;

/**
 * This implementation of a {@link SurfaceFunction} samples a given
 * {@link Sphere} instance when called by the {@link SurfaceMeshBuilder}.
 */
public class SphereFunction implements SurfaceFunction {

    public Sphere sphere;

    protected float phiRange = MathUtils.PI;
    protected float thetaRange = MathUtils.TWO_PI;

    public SphereFunction() {
        this(1);
    }

    /**
     * Creates a new instance using a sphere of the given radius, located at the
     * world origin.
     * 
     * @param radius
     */
    public SphereFunction(float radius) {
        this(new Sphere(new Vec3D(), radius));
    }

    /**
     * Creates a new instance using the given sphere
     * 
     * @param s
     *            sphere
     */
    public SphereFunction(Sphere s) {
        this.sphere = s;
    }

    public Vec3D computeVertexFor(Vec3D p, float phi, float theta) {
        phi -= MathUtils.HALF_PI;
        float cosPhi = MathUtils.cos(phi);
        float cosTheta = MathUtils.cos(theta);
        float sinPhi = MathUtils.sin(phi);
        float sinTheta = MathUtils.sin(theta);
        float t = MathUtils.sign(cosPhi) * MathUtils.abs(cosPhi);
        p.x = t * MathUtils.sign(cosTheta) * MathUtils.abs(cosTheta);
        p.y = MathUtils.sign(sinPhi) * MathUtils.abs(sinPhi);
        p.z = t * MathUtils.sign(sinTheta) * MathUtils.abs(sinTheta);
        return p.scaleSelf(sphere.radius).addSelf(sphere);
    }

    public float getPhiRange() {
        return phiRange;
    }

    public int getPhiResolutionLimit(int res) {
        return res;
    }

    public float getThetaRange() {
        return thetaRange;
    }

    public int getThetaResolutionLimit(int res) {
        return res;
    }

    public void setMaxPhi(float max) {
        phiRange = MathUtils.min(max / 2, MathUtils.PI);
    }

    public void setMaxTheta(float max) {
        thetaRange = MathUtils.min(max, MathUtils.TWO_PI);
    }
}
