package toxi.geom.mesh;

import toxi.geom.AABBD;
import toxi.geom.IntersectorD3D;
import toxi.geom.IsectDataD3D;
import toxi.geom.RayD3D;
import toxi.geom.VecD3D;

public class MeshIntersectorD implements IntersectorD3D {

    private static final double EPS = 0.00001f;

    private TriangleMeshD mesh;
    private AABBD bounds;

    private final IsectDataD3D isec;

    public MeshIntersectorD(TriangleMeshD mesh) {
        setMesh(mesh);
        this.isec = new IsectDataD3D();
    }

    public IsectDataD3D getIntersectionDataD() {
        return isec;
    }

    public boolean intersectsRayD(RayD3D ray) {
        isec.isIntersection = false;
        if (bounds.intersectsRay(ray, 0, Double.MAX_VALUE) != null) {
            VecD3D dir = ray.getDirection();
            double minD = Double.MAX_VALUE;
            for (FaceD f : mesh.getFaceDs()) {
                double d = intersectTriangleD(f.a, f.b, f.c, ray, dir);
                if (d >= 0.0 && d < minD) {
                    isec.isIntersection = true;
                    isec.normal = f.normal;
                    minD = d;
                }
            }
            if (isec.isIntersection) {
                isec.pos = ray.getPointAtDistance(minD);
                isec.dist = minD;
                isec.dir = dir.getInverted();
            }
        }
        return isec.isIntersection;
    }

    private double intersectTriangleD(VecD3D a, VecD3D b, VecD3D c, VecD3D ro, VecD3D dir) {
        VecD3D e1 = b.sub(a);
        VecD3D e2 = c.sub(a);
        VecD3D pvec = dir.cross(e2);
        double det = e1.dot(pvec);
        if (det > -EPS && det < EPS) {
            return -1;
        }
        double invDet = 1f / det;
        VecD3D tvec = ro.sub(a);
        double u = tvec.dot(pvec) * invDet;
        if (u < 0.0 || u > 1.0) {
            return -1;
        }
        VecD3D qvec = tvec.cross(e1);
        double v = dir.dot(qvec) * invDet;
        if (v < 0.0 || u + v > 1.0) {
            return -1;
        }
        double t = e2.dot(qvec) * invDet;
        return t;
    }

    public void setMesh(TriangleMeshD mesh) {
        this.mesh = mesh;
        this.bounds = mesh.getBoundingBox();
    }


}
