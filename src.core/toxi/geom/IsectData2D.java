package toxi.geom;

public class IsectData2D {

    public boolean isIntersection;
    public float dist;
    public ReadonlyVec2D pos;
    public ReadonlyVec2D dir;
    public ReadonlyVec2D normal;

    public IsectData2D() {

    }

    public IsectData2D(IsectData2D isec) {
        isIntersection = isec.isIntersection;
        dist = isec.dist;
        pos = isec.pos.copy();
        dir = isec.dir.copy();
        normal = isec.normal.copy();
    }

    public void clear() {
        isIntersection = false;
        dist = 0;
        pos = new Vec2D();
        dir = new Vec2D();
        normal = new Vec2D();
    }

    public String toString() {
        String s = "isec: " + isIntersection;
        if (isIntersection) {
            s += " at:" + pos + " dist:" + dist + "normal:" + normal;
        }
        return s;
    }
}