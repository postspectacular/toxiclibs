package toxi.geom.mesh;

import toxi.geom.Vec3D;

/**
 * An extensible builder class for {@link TriangleMesh}es based on 3D surface
 * functions using spherical coordinates. In order to create mesh, you'll need
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
        if (mesh == null) {
            mesh = new TriangleMesh();
        }
        Vec3D p, q, pp = null, pq = null;
        int phiRes = function.getPhiResolutionLimit(res);
        float phiRange = function.getPhiRange();
        int thetaRes = function.getThetaResolutionLimit(res);
        float thetaRange = function.getThetaRange();
        for (int j = 0; j < phiRes; j++) {
            float phi = j * phiRange / res;
            float phiNext = (j + 1) * phiRange / res;
            for (int i = 0; i <= thetaRes; i++) {
                float theta;
                if (i == 0 || i == thetaRes) {
                    theta = 0;
                } else {
                    theta = i * thetaRange / res;
                }
                p = function.computeVertexFor(phiNext, theta).scaleSelf(size);
                q = function.computeVertexFor(phi, theta).scaleSelf(size);
                if (i > 0) {
                    mesh.addFace(pp, pq, p);
                    mesh.addFace(pq, q, p);
                }
                pp = p;
                pq = q;
            }
        }
        mesh.faceOutwards();
        mesh.computeVertexNormals();
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
