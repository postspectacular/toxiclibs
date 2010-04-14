package toxi.geom;

public class IsectData {

    public boolean isIntersection = false;
    public float dist;
    public Vec3D pos;
    public Vec3D dir;
    public Vec3D normal;

    public IsectData() {
    }

    public IsectData(IsectData isec) {
        isIntersection = isec.isIntersection;
        dist = isec.dist;
        pos = isec.pos.copy();
        dir = isec.dir.copy();
        normal = isec.normal.copy();
    }

    public String toString() {
        return "isec: " + isIntersection + " at:" + pos + " dist:" + dist;
    }
}