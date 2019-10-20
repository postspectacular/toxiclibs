package toxi.geom;

public class BoxDIntersector implements IntersectorD3D {

    private AABBD box;
    private final IsectDataD3D isec;

    public BoxDIntersector(AABBD box) {
        this.box = box;
        this.isec = new IsectDataD3D();
    }

    /**
     * @return the box
     */
    public AABBD getBoxD() {
        return box;
    }

    public IsectDataD3D getIntersectionDataD() {
        return isec;
    }

    public boolean intersectsRayD(RayD3D ray) {
        final VecD3D pos = box.intersectsRay(ray, 0, Float.MAX_VALUE);
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
    public void setBoxD(AABBD box) {
        this.box = box;
    }
}
