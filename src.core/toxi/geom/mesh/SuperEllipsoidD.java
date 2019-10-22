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

import toxi.geom.VecD3D;
import toxi.math.MathUtils;

/**
 * Super ellipsoid surface evaluator based on code by Paul Bourke:
 * http://local.wasp.uwa.edu.au/~pbourke/geometry/superellipse/
 */
public class SuperEllipsoidD implements SurfaceFunctionD {

    private double p1;
    private double p2;

    public SuperEllipsoidD(double n1, double n2) {
        this.p1 = n1;
        this.p2 = n2;
    }

    public VecD3D computeVertexFor(VecD3D p, double phi, double theta) {
        phi -= MathUtils.HALF_PI;
        double cosPhi = MathUtils.cos(phi);
        double cosTheta = MathUtils.cos(theta);
        double sinPhi = MathUtils.sin(phi);
        double sinTheta = MathUtils.sin(theta);

        double t = MathUtils.sign(cosPhi)
                * (double) Math.pow(MathUtils.abs(cosPhi), p1);
        p.x = t * MathUtils.sign(cosTheta)
                * (double) Math.pow(MathUtils.abs(cosTheta), p2);
        p.y = MathUtils.sign(sinPhi)
                * (double) Math.pow(MathUtils.abs(sinPhi), p1);
        p.z = t * MathUtils.sign(sinTheta)
                * (double) Math.pow(MathUtils.abs(sinTheta), p2);
        return p;
    }

    public double getPhiRange() {
        return MathUtils.TWO_PI;
    }

    public int getPhiResolutionLimit(int res) {
        return res / 2;
    }

    public double getThetaRange() {
        return MathUtils.TWO_PI;
    }

    public int getThetaResolutionLimit(int res) {
        return res;
    }
}
