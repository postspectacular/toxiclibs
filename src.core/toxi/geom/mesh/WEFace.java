package toxi.geom.mesh;

import java.util.ArrayList;
import java.util.List;

import toxi.geom.Vec2D;

public final class WEFace extends Face {

    public List<WingedEdge> edges = new ArrayList<WingedEdge>(3);

    public WEFace(WEVertex a, WEVertex b, WEVertex c) {
        super(a, b, c);
    }

    public WEFace(WEVertex a, WEVertex b, WEVertex c, Vec2D uvA, Vec2D uvB,
            Vec2D uvC) {
        super(a, b, c, uvA, uvB, uvC);
    }

    public void addEdge(WingedEdge e) {
        edges.add(e);
    }

    /**
     * @return the edges
     */
    public List<WingedEdge> getEdges() {
        return edges;
    }

    public final WEVertex[] getVertices(WEVertex[] verts) {
        if (verts != null) {
            verts[0] = (WEVertex) a;
            verts[1] = (WEVertex) b;
            verts[2] = (WEVertex) c;
        } else {
            verts = new WEVertex[] { (WEVertex) a, (WEVertex) b, (WEVertex) c };
        }
        return verts;
    }
}