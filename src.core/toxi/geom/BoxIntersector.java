package toxi.geom;

public class BoxIntersector implements Intersector3D {

    private AABB box;
    private final IsectData3D isec;

    public BoxIntersector(AABB box) {
        this.box = box;
        this.isec = new IsectData3D();
    }

    /**
     * @return the box
     */
    public AABB getBox() {
        return box;
    }

    public IsectData3D getIntersectionData() {
        return isec;
    }

    public boolean intersectsRay(Ray3D ray) {
        final Vec3D pos = box.intersectsRay(ray, 0, Float.MAX_VALUE);
        isec.pos = pos;
        isec.isIntersection = pos != null;
        if (isec.isIntersection) {
            isec.normal = box.getNormalForPoint(pos);
            isec.dist = ray.distanceTo(pos);
        }
        return isec.isIntersection;
    }

    /**
     * @param box
     *            the box to set
     */
    public void setBox(AABB box) {
        this.box = box;
    }
}
