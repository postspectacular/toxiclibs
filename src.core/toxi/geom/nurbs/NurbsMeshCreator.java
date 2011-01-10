package toxi.geom.nurbs;

import toxi.geom.Vec3D;
import toxi.geom.mesh.Mesh3D;
import toxi.geom.mesh.TriangleMesh;

public class NurbsMeshCreator {

    public static Mesh3D createMesh(NurbsSurface surf, Mesh3D mesh, int resU,
            int resV, boolean isClosed) {
        double iresU = 1d / resU;
        double iresV = 1d / resV;
        Vec3D[] prev = null;
        Vec3D[] first = null;
        if (mesh == null) {
            mesh = new TriangleMesh();
        }
        for (int u = 0; u <= resU; u++) {
            Vec3D[] curr = new Vec3D[resV + 1];
            for (int v = 0; v <= resV; v++) {
                Vec3D vert = null;
                if (isClosed) {
                    vert =
                            u < resU
                                    ? surf.pointOnSurface(u * iresU, v * iresV)
                                    : first[v];
                } else {
                    vert = surf.pointOnSurface(u * iresU, v * iresV);
                }
                if (v > 0 && u > 0) {
                    mesh.addFace(curr[v - 1], vert, prev[v - 1]);
                    mesh.addFace(vert, prev[v], prev[v - 1]);
                }
                curr[v] = vert;
            }
            prev = curr;
            if (u == 0) {
                first = curr;
            }
        }
        mesh.computeVertexNormals();
        return mesh;
    }
}
