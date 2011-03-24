package toxi.geom.nurbs;

import toxi.geom.Vec3D;
import toxi.geom.mesh.Mesh3D;
import toxi.geom.mesh.TriangleMesh;

public class NurbsMeshCreator {

    public static Mesh3D createControlMesh(NurbsSurface surf, Mesh3D mesh) {
        Vec3D[] prev = null;
        if (mesh == null) {
            mesh = new TriangleMesh();
        }
        int resU = surf.getControlNet().uLength();
        int resV = surf.getControlNet().vLength();
        for (int u = 0; u < resU; u++) {
            Vec3D[] curr = new Vec3D[resV + 1];
            for (int v = 0; v < resV; v++) {
                Vec3D vert = surf.getControlNet().get(u, v).to3D();
                if (v > 0 && u > 0) {
                    mesh.addFace(curr[v - 1], vert, prev[v - 1]);
                    mesh.addFace(vert, prev[v], prev[v - 1]);
                }
                curr[v] = vert;
            }
            prev = curr;
        }
        mesh.computeVertexNormals();
        return mesh;
    }

    public static Mesh3D createMesh(NurbsSurface surf, Mesh3D mesh, int resU,
            int resV, boolean isClosed) {
        final KnotVector knotU = surf.getUKnotVector();
        final KnotVector knotV = surf.getVKnotVector();
        double iresU = knotU.get(knotU.length() - 1) / resU;
        double iresV = knotV.get(knotV.length() - 1) / resV;
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
                    vert = u < resU ? surf.pointOnSurface(u * iresU, v * iresV)
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
