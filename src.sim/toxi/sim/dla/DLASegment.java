package toxi.sim.dla;

import toxi.geom.Line3D;
import toxi.geom.Vec3D;

public class DLASegment extends Line3D {

    protected Vec3D dir, nextDir;

    public DLASegment(Vec3D a, Vec3D b, Vec3D c) {
        super(a, b);
        this.dir = b.sub(a).normalize();
        this.nextDir = c != null ? c.sub(b).normalize() : dir.copy();
    }

    public Vec3D getDirectionAt(float currT) {
        return getDirection().interpolateToSelf(nextDir, currT);
    }

    public Vec3D getNextDirection() {
        return nextDir;
    }

    public String toString() {
        return a.toString() + " -> " + b.toString() + " dir: " + dir + " nd: "
                + nextDir;
    }
}