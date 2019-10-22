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

import toxi.geom.SphereD;
import toxi.geom.VecD3D;
import toxi.math.MathUtils;

/**
 * This implementation of a {@link SurfaceFunction} samples a given
 * {@link Sphere} instance when called by the {@link SurfaceMeshBuilder}.
 */
public class SphereDFunction implements SurfaceFunctionD {

    public SphereD sphere;

    protected double phiRange = MathUtils.PI;
    protected double thetaRange = MathUtils.TWO_PI;

    public SphereDFunction() {
        this(1);
    }

    /**
     * Creates a new instance using a sphere of the given radius, located at the
     * world origin.
     * 
     * @param radius
     */
    public SphereDFunction(double radius) {
        this(new SphereD(new VecD3D(), radius));
    }

    /**
     * Creates a new instance using the given sphere
     * 
     * @param s
     *            sphere
     */
    public SphereDFunction(SphereD s) {
        this.sphere = s;
    }

    public VecD3D computeVertexFor(VecD3D p, double phi, double theta) {
        phi -= MathUtils.HALF_PI;
        double cosPhi = MathUtils.cos(phi);
        double cosTheta = MathUtils.cos(theta);
        double sinPhi = MathUtils.sin(phi);
        double sinTheta = MathUtils.sin(theta);
        double t = MathUtils.sign(cosPhi) * MathUtils.abs(cosPhi);
        p.x = t * MathUtils.sign(cosTheta) * MathUtils.abs(cosTheta);
        p.y = MathUtils.sign(sinPhi) * MathUtils.abs(sinPhi);
        p.z = t * MathUtils.sign(sinTheta) * MathUtils.abs(sinTheta);
        return p.scaleSelf(sphere.radius).addSelf(sphere);
    }

    public double getPhiRange() {
        return phiRange;
    }

    public int getPhiResolutionLimit(int res) {
        return res;
    }

    public double getThetaRange() {
        return thetaRange;
    }

    public int getThetaResolutionLimit(int res) {
        return res;
    }

    public void setMaxPhi(double max) {
        phiRange = MathUtils.min(max / 2, MathUtils.PI);
    }

    public void setMaxTheta(double max) {
        thetaRange = MathUtils.min(max, MathUtils.TWO_PI);
    }
}
