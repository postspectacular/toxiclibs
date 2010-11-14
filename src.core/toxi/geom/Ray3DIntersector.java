package toxi.geom;

import toxi.math.MathUtils;

public class Ray3DIntersector implements Intersector3D {

    public Ray3D ray;
    private IsectData3D isec;

    public Ray3DIntersector(Ray3D ray) {
        this.ray = ray;
        isec = new IsectData3D();
    }

    public IsectData3D getIntersectionData() {
        return isec;
    }

    public boolean intersectsRay(Ray3D other) {
        Vec3D n = ray.dir.cross(other.dir);
        Vec3D sr = ray.sub(other);
        float absX = MathUtils.abs(n.x);
        float absY = MathUtils.abs(n.y);
        float absZ = MathUtils.abs(n.z);
        float t;
        if (absZ > absX && absZ > absY) {
            t = (sr.x * other.dir.y - sr.y * other.dir.x) / n.z;
        } else if (absX > absY) {
            t = (sr.y * other.dir.z - sr.z * other.dir.y) / n.x;
        } else {
            t = (sr.z * other.dir.x - sr.x * other.dir.z) / n.y;
        }
        isec.isIntersection = (t <= MathUtils.EPS && !Float.isInfinite(t));
        isec.pos = ray.getPointAtDistance(-t);
        return isec.isIntersection;
    }
}