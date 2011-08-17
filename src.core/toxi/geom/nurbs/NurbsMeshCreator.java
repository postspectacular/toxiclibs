package toxi.geom.nurbs;

import toxi.geom.Vec2D;
import toxi.geom.Vec3D;
import toxi.geom.mesh.Mesh3D;
import toxi.geom.mesh.TriangleMesh;

public class NurbsMeshCreator {

    private NurbsSurface surf;
    private Vec2D maxUV;

    public NurbsMeshCreator(NurbsSurface surf) {
        this(surf, new Vec2D(1, 1));
    }

    public NurbsMeshCreator(NurbsSurface surf, Vec2D maxUV) {
        this.surf = surf;
        this.maxUV = maxUV;
    }

    public Mesh3D createControlMesh(Mesh3D mesh) {
        Vec3D[] prev = null;
        if (mesh == null) {
            mesh = new TriangleMesh();
        }
        int resU = surf.getControlNet().uLength();
        int resV = surf.getControlNet().vLength();
        Vec2D dUV = maxUV.scale(1f / resU, 1f / resV);
        for (int u = 0; u < resU; u++) {
            Vec3D[] curr = new Vec3D[resV + 1];
            for (int v = 0; v < resV; v++) {
                Vec3D vert = surf.getControlNet().get(u, v).to3D();
                if (v > 0 && u > 0) {
                    mesh.addFace(curr[v - 1], vert, prev[v - 1],
                            dUV.scale(u, v - 1), dUV.scale(u, v),
                            dUV.scale(u - 1, v - 1));
                    mesh.addFace(vert, prev[v], prev[v - 1], dUV.scale(u, v),
                            dUV.scale(u - 1, v), dUV.scale(u - 1, v - 1));
                }
                curr[v] = vert;
            }
            prev = curr;
        }
        mesh.computeVertexNormals();
        return mesh;
    }

    public Mesh3D createMesh(Mesh3D mesh, int resU, int resV, boolean isClosed) {
        final KnotVector knotU = surf.getUKnotVector();
        final KnotVector knotV = surf.getVKnotVector();
        double iresU = knotU.get(knotU.length() - 1) / resU;
        double iresV = knotV.get(knotV.length() - 1) / resV;
        Vec3D[] prev = null;
        Vec3D[] first = null;
        if (mesh == null) {
            mesh = new TriangleMesh();
        }
        Vec2D dUV = maxUV.scale(1f / resU, 1f / resV);
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
                    mesh.addFace(curr[v - 1], vert, prev[v - 1],
                            dUV.scale(u, v - 1), dUV.scale(u, v),
                            dUV.scale(u - 1, v - 1));
                    mesh.addFace(vert, prev[v], prev[v - 1], dUV.scale(u, v),
                            dUV.scale(u - 1, v), dUV.scale(u - 1, v - 1));
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

    public NurbsSurface getSurface() {
        return surf;
    }

    public Vec2D getUVScale() {
        return maxUV;
    }

    public void setSurface(NurbsSurface surf) {
        this.surf = surf;
    }

    public void setUVScale(Vec2D maxUV) {
        this.maxUV = maxUV;
    }
}
