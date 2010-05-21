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

    public String toString() {
        return "isec: " + isIntersection + " at:" + pos + " dist:" + dist;
    }
}