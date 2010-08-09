package toxi.geom.mesh;

import java.util.ArrayList;
import java.util.List;

import toxi.geom.ReadonlyVec3D;
import toxi.geom.Vec3D;

public class WEVertex extends Vec3D {

    public final Vec3D normal = new Vec3D();

    public final int id;

    public List<WingedEdge> edges = new ArrayList<WingedEdge>(4);

    public WEVertex(Vec3D v, int id) {
        super(v);
        this.id = id;
    }

    public void addEdge(WingedEdge e) {
        edges.add(e);
    }

    final void addFaceNormal(Vec3D n) {
        normal.addSelf(n).normalize();
    }

    final void clearNormal() {
        normal.clear();
    }

    final void computeNormal() {
        normal.normalize();
    }

    public WEVertex getNeighborInDirection(ReadonlyVec3D dir, float tolerance) {
        WEVertex closest = null;
        float delta = 1 - tolerance;
        for (WEVertex n : getNeighbors()) {
            float d = n.sub(this).normalize().dot(dir);
            if (d > delta) {
                closest = n;
                delta = d;
            }
        }
        return closest;
    }

    public List<WEVertex> getNeighbors() {
        List<WEVertex> neighbors = new ArrayList<WEVertex>(edges.size());
        for (WingedEdge e : edges) {
            neighbors.add(e.getOtherEndFor(this));
        }
        return neighbors;
    }

    public String toString() {
        return id + " {" + x + "," + y + "," + z + "}";
    }
}