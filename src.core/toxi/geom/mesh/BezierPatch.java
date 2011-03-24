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

/**
 * 4x4 bezier patch implementation with tesselation support (dynamic resolution)
 * for generating triangle mesh representations.
 */
public class BezierPatch {

    /**
     * Computes a single point on the bezier surface given by the 2d array of
     * control points. The desired point's coordinates have to be specified in
     * UV space (range 0.0 .. 1.0). The implementation does not check or enforce
     * the correct range of these coords and will not return valid points if the
     * range is exceeded.
     * 
     * @param u
     *            positive normalized U coordinate on the bezier surface
     * @param v
     *            positive normalized V coordinate on the bezier surface
     * @param points
     *            4x4 array defining the patch's control points
     * @return point on surface
     */
    public static Vec3D computePointAt(float u, float v, Vec3D[][] points) {
        final float u1 = 1 - u;
        final float u1squared = u1 * u1 * 3 * u;
        final float u1cubed = u1 * u1 * u1;
        final float usquared = u * u;
        final float v1 = 1 - v;
        final float vsquared = v * v * 3;
        final float v1squared = v1 * v1 * 3;
        final float v1cubed = v1 * v1 * v1;
        final float vcubed = v * v * v;

        final float u1usq = u1 * usquared * 3;
        final float usqu = u * usquared;
        final float v1vsq = v1 * vsquared;
        final float v1sqv = v1squared * v;

        final Vec3D[] p0 = points[0];
        final Vec3D[] p1 = points[1];
        final Vec3D[] p2 = points[2];
        final Vec3D[] p3 = points[3];

        final float x = u1cubed
                * (p0[0].x * v1cubed + p0[1].x * v1sqv + p0[2].x * v1vsq + p0[3].x
                        * vcubed)
                + u1squared
                * (p1[0].x * v1cubed + p1[1].x * v1sqv + p1[2].x * v1vsq + p1[3].x
                        * vcubed)
                + u1usq
                * (p2[0].x * v1cubed + p2[1].x * v1sqv + p2[2].x * v1vsq + p2[3].x
                        * vcubed)
                + usqu
                * (p3[0].x * v1cubed + p3[1].x * v1sqv + p3[2].x * v1vsq + p3[3].x
                        * vcubed);

        final float y = u1cubed
                * (p0[0].y * v1cubed + p0[1].y * v1sqv + p0[2].y * v1vsq + p0[3].y
                        * vcubed)
                + u1squared
                * (p1[0].y * v1cubed + p1[1].y * v1sqv + p1[2].y * v1vsq + p1[3].y
                        * vcubed)
                + u1usq
                * (p2[0].y * v1cubed + p2[1].y * v1sqv + p2[2].y * v1vsq + p2[3].y
                        * vcubed)
                + usqu
                * (p3[0].y * v1cubed + p3[1].y * v1sqv + p3[2].y * v1vsq + p3[3].y
                        * vcubed);

        final float z = u1cubed
                * (p0[0].z * v1cubed + p0[1].z * v1sqv + p0[2].z * v1vsq + p0[3].z
                        * vcubed)
                + u1squared
                * (p1[0].z * v1cubed + p1[1].z * v1sqv + p1[2].z * v1vsq + p1[3].z
                        * vcubed)
                + u1usq
                * (p2[0].z * v1cubed + p2[1].z * v1sqv + p2[2].z * v1vsq + p2[3].z
                        * vcubed)
                + usqu
                * (p3[0].z * v1cubed + p3[1].z * v1sqv + p3[2].z * v1vsq + p3[3].z
                        * vcubed);

        return new Vec3D(x, y, z);
    }

    public Vec3D[][] points;

    public BezierPatch() {
        points = new Vec3D[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                points[i][j] = new Vec3D();
            }
        }
    }

    public BezierPatch(Vec3D[][] points) {
        this.points = points;
    }

    public Vec3D computePointAt(float u, float v) {
        return computePointAt(u, v, points);
    }

    public BezierPatch set(int x, int y, Vec3D p) {
        points[y][x].set(p);
        return this;
    }

    public Mesh3D toMesh(int res) {
        return toMesh(null, res);
    }

    public Mesh3D toMesh(Mesh3D mesh, int res) {
        if (mesh == null) {
            mesh = new TriangleMesh();
        }
        Vec3D[] curr = new Vec3D[res + 1];
        Vec3D[] prev = new Vec3D[res + 1];
        float r1 = 1f / res;
        for (int y = 0; y <= res; y++) {
            for (int x = 0; x <= res; x++) {
                Vec3D p = computePointAt(x * r1, y * r1, points);
                if (x > 0 && y > 0) {
                    mesh.addFace(p, curr[x - 1], prev[x - 1]);
                    mesh.addFace(p, prev[x - 1], prev[x]);
                }
                curr[x] = p;
            }
            Vec3D[] tmp = prev;
            prev = curr;
            curr = tmp;
        }
        return mesh;
    }
}
