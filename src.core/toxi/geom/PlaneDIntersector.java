package toxi.geom;

import toxi.math.MathUtils;

public class PlaneDIntersector implements IntersectorD3D {

    private PlaneD plane;
    private final IsectDataD3D isec;

    public PlaneDIntersector(PlaneD p) {
        this.plane = p;
        this.isec = new IsectDataD3D();
    }

    public IsectDataD3D getIntersectionDataD() {
        return isec;
    }

    /**
     * @return the box
     */
    public PlaneD getPlane() {
        return plane;
    }

    public boolean intersectsRayD(RayD3D ray) {
        double d = -plane.normal.dot(plane);
        double numer = plane.normal.dot(ray) + d;
        double denom = plane.normal.dot(ray.dir);

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
    public void setPlaneD(PlaneD p) {
        this.plane = p;
    }
}
