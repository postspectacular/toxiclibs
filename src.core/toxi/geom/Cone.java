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

import toxi.geom.mesh.Mesh3D;
import toxi.geom.mesh.TriangleMesh;
import toxi.math.MathUtils;

/**
 * A geometric definition of a cone (and cylinder as a special case) with
 * support for mesh creation/representation. The class is currently still
 * incomplete in that it doesn't provide any other features than the
 * construction of a cone shaped mesh.
 */
public class Cone extends Vec3D {

    public Vec3D dir;
    public float radiusSouth;
    public float radiusNorth;
    public float length;

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
    public Cone(ReadonlyVec3D pos, ReadonlyVec3D dir, float rNorth,
            float rSouth, float len) {
        super(pos);
        this.dir = dir.getNormalized();
        this.radiusNorth = rNorth;
        this.radiusSouth = rSouth;
        this.length = len;
    }

    public Mesh3D toMesh(int steps) {
        return toMesh(steps, 0);
    }

    public Mesh3D toMesh(int steps, float thetaOffset) {
        return toMesh(null, steps, thetaOffset, true, true);
    }

    public Mesh3D toMesh(Mesh3D mesh, int steps, float thetaOffset,
            boolean topClosed, boolean bottomClosed) {
        ReadonlyVec3D c = this.add(0.01f, 0.01f, 0.01f);
        ReadonlyVec3D n = c.cross(dir.getNormalized()).normalize();
        Vec3D halfAxis = dir.scale(length * 0.5f);
        Vec3D p = sub(halfAxis);
        Vec3D q = add(halfAxis);
        Vec3D[] south = new Vec3D[steps];
        Vec3D[] north = new Vec3D[steps];
        float phi = MathUtils.TWO_PI / steps;
        for (int i = 0; i < steps; i++) {
            float theta = i * phi + thetaOffset;
            ReadonlyVec3D nr = n.getRotatedAroundAxis(dir, theta);
            south[i] = nr.scale(radiusSouth).addSelf(p);
            north[i] = nr.scale(radiusNorth).addSelf(q);
        }
        int numV = steps * 2 + 2;
        int numF = steps * 2 + (topClosed ? steps : 0)
                + (bottomClosed ? steps : 0);
        if (mesh == null) {
            mesh = new TriangleMesh("cone", numV, numF);
        }
        for (int i = 0, j = 1; i < steps; i++, j++) {
            if (j == steps) {
                j = 0;
            }
            mesh.addFace(south[i], north[i], south[j], null, null, null, null);
            mesh.addFace(south[j], north[i], north[j], null, null, null, null);
            if (bottomClosed) {
                mesh.addFace(p, south[i], south[j], null, null, null, null);
            }
            if (topClosed) {
                mesh.addFace(north[i], q, north[j], null, null, null, null);
            }
        }
        return mesh;
    }
}