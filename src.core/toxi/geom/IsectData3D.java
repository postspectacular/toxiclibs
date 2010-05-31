package toxi.geom;

public class IsectData3D {

    public boolean isIntersection;
    public float dist;
    public ReadonlyVec3D pos;
    public ReadonlyVec3D dir;
    public ReadonlyVec3D normal;

    public IsectData3D() {

    }

    public IsectData3D(IsectData3D isec) {
        isIntersection = isec.isIntersection;
        dist = isec.dist;
        pos = isec.pos.copy();
        dir = isec.dir.copy();
        normal = isec.normal.copy();
    }

    public void clear() {
        isIntersection = false;
        dist = 0;
        pos = new Vec3D();
        dir = new Vec3D();
        normal = new Vec3D();
    }

    public String toString() {
        String s = "isec: " + isIntersection;
        if (isIntersection) {
            s += " at:" + pos + " dist:" + dist + "normal:" + normal;
        }
        return s;
    }
}