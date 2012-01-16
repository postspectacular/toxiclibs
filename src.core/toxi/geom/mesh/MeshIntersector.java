package toxi.geom.mesh;

import toxi.geom.AABB;
import toxi.geom.Intersector3D;
import toxi.geom.IsectData3D;
import toxi.geom.Ray3D;
import toxi.geom.Vec3D;

public class MeshIntersector implements Intersector3D {

    private static final float EPS = 0.00001f;

    private TriangleMesh mesh;
    private AABB bounds;

    private final IsectData3D isec;

    public MeshIntersector(TriangleMesh mesh) {
        setMesh(mesh);
        this.isec = new IsectData3D();
    }

    public IsectData3D getIntersectionData() {
        return isec;
    }

    public boolean intersectsRay(Ray3D ray) {
        isec.isIntersection = false;
        if (bounds.intersectsRay(ray, 0, Float.MAX_VALUE) != null) {
            Vec3D dir = ray.getDirection();
            float minD = Float.MAX_VALUE;
            for (Face f : mesh.getFaces()) {
                float d = intersectTriangle(f.a, f.b, f.c, ray, dir);
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

    private float intersectTriangle(Vec3D a, Vec3D b, Vec3D c, Vec3D ro,
            Vec3D dir) {
        Vec3D e1 = b.sub(a);
        Vec3D e2 = c.sub(a);
        Vec3D pvec = dir.cross(e2);
        float det = e1.dot(pvec);
        if (det > -EPS && det < EPS) {
            return -1;
        }
        float invDet = 1f / det;
        Vec3D tvec = ro.sub(a);
        float u = tvec.dot(pvec) * invDet;
        if (u < 0.0 || u > 1.0) {
            return -1;
        }
        Vec3D qvec = tvec.cross(e1);
        float v = dir.dot(qvec) * invDet;
        if (v < 0.0 || u + v > 1.0) {
            return -1;
        }
        float t = e2.dot(qvec) * invDet;
        return t;
    }

    public void setMesh(TriangleMesh mesh) {
        this.mesh = mesh;
        this.bounds = mesh.getBoundingBox();
    }
}
