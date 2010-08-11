package toxi.geom.mesh;

import java.util.ArrayList;
import java.util.List;

import toxi.geom.ReadonlyVec3D;
import toxi.geom.Vec3D;

public class WEVertex extends Vertex {

    public List<WingedEdge> edges = new ArrayList<WingedEdge>(4);

    public WEVertex(Vec3D v, int id) {
        super(v, id);
    }

    public void addEdge(WingedEdge e) {
        edges.add(e);
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