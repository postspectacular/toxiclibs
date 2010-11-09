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
        float pres = 1f / (1 == res % 2 ? res - 0 : res);
        for (int p = 0; p < phiRes; p++) {
            float phi = p * phiRange * pres;
            float phiNext = (p + 1) * phiRange * pres;
            for (int t = 0; t <= thetaRes; t++) {
                float theta;
                theta = t * thetaRange / res;
                a =
                        function.computeVertexFor(a, phiNext, theta).scaleSelf(
                                size);
                b = function.computeVertexFor(b, phi, theta).scaleSelf(size);
                if (b.distanceTo(a) < 0.0001) {
                    b.set(a);
                }
                if (t > 0) {
                    if (t == thetaRes && isClosed) {
                        a.set(a0);
                        b.set(b0);
                    }
                    mesh.addFace(pa, pb, a);
                    mesh.addFace(pb, b, a);
                } else {
                    a0.set(a);
                    b0.set(b);
                }
                pa.set(a);
                pb.set(b);
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
