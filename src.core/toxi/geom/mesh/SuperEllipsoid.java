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

import toxi.geom.Vec3D;
import toxi.math.MathUtils;

/**
 * Super ellipsoid surface evaluator based on code by Paul Bourke:
 * http://local.wasp.uwa.edu.au/~pbourke/geometry/superellipse/
 */
public class SuperEllipsoid implements SurfaceFunction {

    private float p1;
    private float p2;

    public SuperEllipsoid(float n1, float n2) {
        this.p1 = n1;
        this.p2 = n2;
    }

    public Vec3D computeVertexFor(Vec3D p, float phi, float theta) {
        phi -= MathUtils.HALF_PI;
        float cosPhi = MathUtils.cos(phi);
        float cosTheta = MathUtils.cos(theta);
        float sinPhi = MathUtils.sin(phi);
        float sinTheta = MathUtils.sin(theta);

        float t = MathUtils.sign(cosPhi)
                * (float) Math.pow(MathUtils.abs(cosPhi), p1);
        p.x = t * MathUtils.sign(cosTheta)
                * (float) Math.pow(MathUtils.abs(cosTheta), p2);
        p.y = MathUtils.sign(sinPhi)
                * (float) Math.pow(MathUtils.abs(sinPhi), p1);
        p.z = t * MathUtils.sign(sinTheta)
                * (float) Math.pow(MathUtils.abs(sinTheta), p2);
        return p;
    }

    public float getPhiRange() {
        return MathUtils.TWO_PI;
    }

    public int getPhiResolutionLimit(int res) {
        return res / 2;
    }

    public float getThetaRange() {
        return MathUtils.TWO_PI;
    }

    public int getThetaResolutionLimit(int res) {
        return res;
    }
}
