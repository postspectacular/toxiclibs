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

import toxi.geom.VecD2D;
import toxi.geom.VecD3D;

/**
 * An extensible builder class for {@link TriangleMesh}es based on 3D surface
 * functions using spherical coordinates. In order to create a mesh, you'll need
 * to supply a {@link SurfaceFunction} implementation to the builder.
 */
public class SurfaceMeshDBuilder {

    protected SurfaceFunctionD function;

    public SurfaceMeshDBuilder(SurfaceFunctionD function) {
        this.function = function;
    }

    public MeshD3D createMeshD(int res) {
        return createMeshD(null, res, 1);
    }

    public MeshD3D createMeshD(MeshD3D mesh, int res, double size) {
        return createMeshD(mesh, res, size, true);
    }

    public MeshD3D createMeshD(MeshD3D mesh, int res, double size, boolean isClosed) {
        if (mesh == null) {
            mesh = new TriangleMeshD();
        }
        VecD3D a = new VecD3D();
        VecD3D b = new VecD3D();
        VecD3D pa = new VecD3D(), pb = new VecD3D();
        VecD3D a0 = new VecD3D(), b0 = new VecD3D();
        int phiRes = function.getPhiResolutionLimit(res);
        double phiRange = function.getPhiRange();
        int thetaRes = function.getThetaResolutionLimit(res);
        double thetaRange = function.getThetaRange();
        double pres = 1f / phiRes;
        double tres = 1f / thetaRes;
        double ires = 1f / res;
        VecD2D pauv = new VecD2D();
        VecD2D pbuv = new VecD2D();
        VecD2D auv = new VecD2D();
        VecD2D buv = new VecD2D();
        for (int p = 0; p < phiRes; p++) {
            double phi = p * phiRange * ires;
            double phiNext = (p + 1) * phiRange * ires;
            for (int t = 0; t <= thetaRes; t++) {
                double theta;
                theta = t * thetaRange * ires;
                a = function.computeVertexFor(a, phiNext, theta)
                        .scaleSelf(size);
                auv.set(t * tres, 1 - (p + 1) * pres);
                b = function.computeVertexFor(b, phi, theta).scaleSelf(size);
                buv.set(t * tres, 1 - p * pres);
                if (b.equalsWithTolerance(a, 0.0001f)) {
                    b.set(a);
                }
                if (t > 0) {
                    if (t == thetaRes && isClosed) {
                        a.set(a0);
                        b.set(b0);
                    }
                    mesh.addFaceD(pa, pb, a, pauv.copy(), pbuv.copy(),
                            auv.copy());
                    mesh.addFaceD(pb, b, a, pbuv.copy(), buv.copy(), auv.copy());
                } else {
                    a0.set(a);
                    b0.set(b);
                }
                pa.set(a);
                pb.set(b);
                pauv.set(auv);
                pbuv.set(buv);
            }
        }
        return mesh;
    }

    /**
     * @return the function
     */
    public SurfaceFunctionD getFunction() {
        return function;
    }

    /**
     * @param function
     *            the function to set
     */
    public void setFunction(SurfaceFunctionD function) {
        this.function = function;
    }
}
