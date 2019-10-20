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

import toxi.geom.mesh.MeshD3D;
import toxi.geom.mesh.TriangleMeshD;
import toxi.math.MathUtils;

/**
 * A geometric definition of a cone (and cylinder as a special case) with
 * support for mesh creation/representation. The class is currently still
 * incomplete in that it doesn't provide any other features than the
 * construction of a cone shaped mesh.
 */
public class ConeD extends VecD3D {

    public VecD3D dir;
    public double radiusSouth;
    public double radiusNorth;
    public double length;

    /**
     * Constructs a new cone instance.
     * 
     * @param pos
     *            centre position
     * @param dir
     *            direction
     * @param rNorth
     *            radius on the side in the forward direction
     * @param rSouth
     *            radius on the side in the opposite direction
     * @param len
     *            length of the cone
     */
    public ConeD(ReadonlyVecD3D pos, ReadonlyVecD3D dir, double rNorth,
            double rSouth, double len) {
        super(pos);
        this.dir = dir.getNormalized();
        this.radiusNorth = rNorth;
        this.radiusSouth = rSouth;
        this.length = len;
    }

    public MeshD3D toMeshD(int steps) {
        return toMeshD(steps, 0);
    }

    public MeshD3D toMeshD(int steps, double thetaOffset) {
        return toMeshD(null, steps, thetaOffset, true, true);
    }

    public MeshD3D toMeshD(MeshD3D mesh, int steps, double thetaOffset,
            boolean topClosed, boolean bottomClosed) {
        ReadonlyVecD3D c = this.add(0.01f, 0.01f, 0.01f);
        ReadonlyVecD3D n = c.cross(dir.getNormalized()).normalize();
        VecD3D halfAxis = dir.scale(length * 0.5f);
        VecD3D p = sub(halfAxis);
        VecD3D q = add(halfAxis);
        VecD3D[] south = new VecD3D[steps];
        VecD3D[] north = new VecD3D[steps];
        double phi = MathUtils.TWO_PI / steps;
        for (int i = 0; i < steps; i++) {
            double theta = i * phi + thetaOffset;
            ReadonlyVecD3D nr = n.getRotatedAroundAxis(dir, theta);
            south[i] = nr.scale(radiusSouth).addSelf(p);
            north[i] = nr.scale(radiusNorth).addSelf(q);
        }
        int numV = steps * 2 + 2;
        int numF = steps * 2 + (topClosed ? steps : 0)
                + (bottomClosed ? steps : 0);
        if (mesh == null) {
            mesh = new TriangleMeshD("cone", numV, numF);
        }
        for (int i = 0, j = 1; i < steps; i++, j++) {
            if (j == steps) {
                j = 0;
            }
            mesh.addFaceD(south[i], north[i], south[j], null, null, null, null);
            mesh.addFaceD(south[j], north[i], north[j], null, null, null, null);
            if (bottomClosed) {
                mesh.addFaceD(p, south[i], south[j], null, null, null, null);
            }
            if (topClosed) {
                mesh.addFaceD(north[i], q, north[j], null, null, null, null);
            }
        }
        return mesh;
    }
}