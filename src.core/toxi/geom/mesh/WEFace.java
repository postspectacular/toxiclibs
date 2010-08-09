package toxi.geom.mesh;

import java.util.ArrayList;
import java.util.List;

import toxi.geom.Vec2D;
import toxi.geom.Vec3D;

public final class WEFace {

    public WEVertex a, b, c;
    public Vec2D uvA, uvB, uvC;
    public Vec3D normal;

    public List<WingedEdge> edges = new ArrayList<WingedEdge>(3);

    public WEFace(WEVertex a, WEVertex b, WEVertex c) {
        this.a = a;
        this.b = b;
        this.c = c;
        normal = a.sub(c).crossSelf(a.sub(b)).normalize();
        a.addFaceNormal(normal);
        b.addFaceNormal(normal);
        c.addFaceNormal(normal);
    }

    public WEFace(WEVertex a, WEVertex b, WEVertex c, Vec2D uvA, Vec2D uvB,
            Vec2D uvC) {
        this(a, b, c);
        this.uvA = uvA;
        this.uvB = uvB;
        this.uvC = uvC;
    }

    public void addEdge(WingedEdge e) {
        edges.add(e);
    }

    public void flipVertexOrder() {
        WEVertex t = a;
        a = b;
        b = t;
        normal.invert();
    }

    public Vec3D getCentroid() {
        return a.add(b).addSelf(c).scale(1f / 3);
    }

    /**
     * @return the edges
     */
    public List<WingedEdge> getEdges() {
        return edges;
    }

    public final WEVertex[] getVertices(WEVertex[] verts) {
        if (verts != null) {
            verts[0] = a;
            verts[1] = b;
            verts[2] = c;
        } else {
            verts = new WEVertex[] { a, b, c };
        }
        return verts;
    }

    public String toString() {
        return "WEFace: " + a + ", " + b + ", " + c;
    }
}