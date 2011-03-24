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

import toxi.geom.Vec2D;
import toxi.geom.Vec3D;

/**
 * An extensible builder class for {@link TriangleMesh}es based on 3D surface
 * functions using spherical coordinates. In order to create a mesh, you'll need
 * to supply a {@link SurfaceFunction} implementation to the builder.
 */
public class SurfaceMeshBuilder {

    protected SurfaceFunction function;

    public SurfaceMeshBuilder(SurfaceFunction function) {
        this.function = function;
    }

    public Mesh3D createMesh(int res) {
        return createMesh(null, res, 1);
    }

    public Mesh3D createMesh(Mesh3D mesh, int res, float size) {
        return createMesh(mesh, res, size, true);
    }

    public Mesh3D createMesh(Mesh3D mesh, int res, float size, boolean isClosed) {
        if (mesh == null) {
            mesh = new TriangleMesh();
        }
        Vec3D a = new Vec3D();
        Vec3D b = new Vec3D();
        Vec3D pa = new Vec3D(), pb = new Vec3D();
        Vec3D a0 = new Vec3D(), b0 = new Vec3D();
        int phiRes = function.getPhiResolutionLimit(res);
        float phiRange = function.getPhiRange();
        int thetaRes = function.getThetaResolutionLimit(res);
        float thetaRange = function.getThetaRange();
        float pres = 1f / phiRes;
        float tres = 1f / thetaRes;
        float ires = 1f / res;
        Vec2D pauv = new Vec2D();
        Vec2D pbuv = new Vec2D();
        Vec2D auv = new Vec2D();
        Vec2D buv = new Vec2D();
        for (int p = 0; p < phiRes; p++) {
            float phi = p * phiRange * ires;
            float phiNext = (p + 1) * phiRange * ires;
            for (int t = 0; t <= thetaRes; t++) {
                float theta;
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
                    mesh.addFace(pa, pb, a, pauv.copy(), pbuv.copy(),
                            auv.copy());
                    mesh.addFace(pb, b, a, pbuv.copy(), buv.copy(), auv.copy());
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
    public SurfaceFunction getFunction() {
        return function;
    }

    /**
     * @param function
     *            the function to set
     */
    public void setFunction(SurfaceFunction function) {
        this.function = function;
    }
}
