package toxi.geom;

import toxi.math.MathUtils;

public class PlaneIntersector implements Intersector3D {

    private Plane plane;
    private final IsectData3D isec;

    public PlaneIntersector(Plane p) {
        this.plane = p;
        this.isec = new IsectData3D();
    }

    public IsectData3D getIntersectionData() {
        return isec;
    }

    /**
     * @return the box
     */
    public Plane getPlane() {
        return plane;
    }

    public boolean intersectsRay(Ray3D ray) {
        float d = -plane.normal.dot(plane);
        float numer = plane.normal.dot(ray) + d;
        float denom = plane.normal.dot(ray.dir);

        // normal is orthogonal to vector, can't intersect
        if (isec.isIntersection = (MathUtils.abs(denom) >= MathUtils.EPS)) {
            isec.dist = -(numer / denom);
            isec.pos = ray.getPointAtDistance(isec.dist);
            isec.normal = plane.normal;
        }
        return isec.isIntersection;
    }

    /**
     * @param p
     *            the plane to set
     */
    public void setPlane(Plane p) {
        this.plane = p;
    }
}
