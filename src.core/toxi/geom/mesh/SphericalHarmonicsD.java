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
 * Spherical harmonics surface evaluator based on code by Paul Bourke:
 * http://local.wasp.uwa.edu.au/~pbourke/geometry/sphericalh/
 */
public class SphericalHarmonicsD implements SurfaceFunctionD {

    double[] m;

    public SphericalHarmonicsD(double[] m) {
        this.m = m;
    }

    // FIXME check where flipped vertex order is coming from sometimes
    public VecD3D computeVertexFor(VecD3D p, double phi, double theta) {
        double r = 0;
        r += Math.pow(MathUtils.sin(m[0] * theta), m[1]);
        r += Math.pow(MathUtils.cos(m[2] * theta), m[3]);
        r += Math.pow(MathUtils.sin(m[4] * phi), m[5]);
        r += Math.pow(MathUtils.cos(m[6] * phi), m[7]);

        double sinTheta = MathUtils.sin(theta);
        p.x = r * sinTheta * MathUtils.cos(phi);
        p.y = r * MathUtils.cos(theta);
        p.z = r * sinTheta * MathUtils.sin(phi);
        return p;
    }

    public double getPhiRange() {
        return MathUtils.TWO_PI;
    }

    public int getPhiResolutionLimit(int res) {
        return res;
    }

    public double getThetaRange() {
        return MathUtils.PI;
    }

    public int getThetaResolutionLimit(int res) {
        return res;
    }
}