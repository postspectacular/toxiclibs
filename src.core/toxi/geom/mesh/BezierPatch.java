package toxi.geom.mesh;

import toxi.geom.Vec3D;

public class BezierPatch {

    public static Vec3D computePointAt(float u, float v, Vec3D[][] points) {
        float u1 = 1 - u;
        float v1 = 1 - v;
        float u1squared = u1 * u1;
        float u1cubed = u1squared * u1;
        float usquared = u * u;
        float v1squared = v1 * v1;
        float v1cubed = v1 * v1squared;
        float vsquared = v * v;
        float vcubed = v * vsquared;

        Vec3D[] p0 = points[0];
        Vec3D[] p1 = points[1];
        Vec3D[] p2 = points[2];
        Vec3D[] p3 = points[3];

        float x =
                u1cubed
                        * (p0[0].x * v1cubed + 3 * p0[1].x * v1squared * v + 3
                                * p0[2].x * v1 * vsquared + p0[3].x * vcubed)
                        + 3
                        * u1squared
                        * u
                        * (p1[0].x * v1cubed + 3 * p1[1].x * v1squared * v + 3
                                * p1[2].x * v1 * vsquared + p1[3].x * vcubed)
                        + 3
                        * u1
                        * usquared
                        * (p2[0].x * v1cubed + 3 * p2[1].x * v1squared * v + 3
                                * p2[2].x * v1 * vsquared + p2[3].x * vcubed)
                        + u
                        * usquared
                        * (p3[0].x * v1cubed + 3 * p3[1].x * v1squared * v + 3
                                * p3[2].x * v1 * vsquared + p3[3].x * vcubed);

        float y =
                u1cubed
                        * (p0[0].y * v1cubed + 3 * p0[1].y * v1squared * v + 3
                                * p0[2].y * v1 * vsquared + p0[3].y * vcubed)
                        + 3
                        * u1squared
                        * u
                        * (p1[0].y * v1cubed + 3 * p1[1].y * v1squared * v + 3
                                * p1[2].y * v1 * vsquared + p1[3].y * vcubed)
                        + 3
                        * u1
                        * usquared
                        * (p2[0].y * v1cubed + 3 * p2[1].y * v1squared * v + 3
                                * p2[2].y * v1 * vsquared + p2[3].y * vcubed)
                        + u
                        * usquared
                        * (p3[0].y * v1cubed + 3 * p3[1].y * v1squared * v + 3
                                * p3[2].y * v1 * vsquared + p3[3].y * vcubed);

        float z =
                u1cubed
                        * (p0[0].z * v1cubed + 3 * p0[1].z * v1squared * v + 3
                                * p0[2].z * v1 * vsquared + p0[3].z * vcubed)
                        + 3
                        * u1squared
                        * u
                        * (p1[0].z * v1cubed + 3 * p1[1].z * v1squared * v + 3
                                * p1[2].z * v1 * vsquared + p1[3].z * vcubed)
                        + 3
                        * u1
                        * usquared
                        * (p2[0].z * v1cubed + 3 * p2[1].z * v1squared * v + 3
                                * p2[2].z * v1 * vsquared + p2[3].z * vcubed)
                        + u
                        * usquared
                        * (p3[0].z * v1cubed + 3 * p3[1].z * v1squared * v + 3
                                * p3[2].z * v1 * vsquared + p3[3].z * vcubed);

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
        Mesh3D mesh = new TriangleMesh();
        Vec3D[] curr = new Vec3D[res + 1];
        Vec3D[] prev = new Vec3D[res + 1];
        float r1 = 1f / res;
        for (int y = 0; y <= res; y++) {
            for (int x = 0; x <= res; x++) {
                Vec3D p = computePointAt(x * r1, y * r1, points);
                if (y > 0 && x > 0) {
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
