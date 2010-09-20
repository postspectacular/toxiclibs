package toxi.geom.mesh;

import toxi.geom.Vec2D;
import toxi.geom.Vec3D;

public class Face {

    public Vertex a, b, c;
    public Vec2D uvA, uvB, uvC;
    public Vec3D normal;

    public Face(Vertex a, Vertex b, Vertex c) {
        this.a = a;
        this.b = b;
        this.c = c;
        normal = a.sub(c).crossSelf(a.sub(b)).normalize();
        a.addFaceNormal(normal);
        b.addFaceNormal(normal);
        c.addFaceNormal(normal);
    }

    public Face(Vertex a, Vertex b, Vertex c, Vec2D uvA, Vec2D uvB, Vec2D uvC) {
        this(a, b, c);
        this.uvA = uvA;
        this.uvB = uvB;
        this.uvC = uvC;
    }

    public void computeNormal() {
        normal = a.sub(c).crossSelf(a.sub(b)).normalize();
    }

    public void flipVertexOrder() {
        Vertex t = a;
        a = b;
        b = t;
        normal.invert();
    }

    public Vec3D getCentroid() {
        return a.add(b).addSelf(c).scale(1f / 3);
    }

    public final Vertex[] getVertices(Vertex[] verts) {
        if (verts != null) {
            verts[0] = a;
            verts[1] = b;
            verts[2] = c;
        } else {
            verts = new Vertex[] { a, b, c };
        }
        return verts;
    }

    public String toString() {
        return getClass().getName() + " " + a + ", " + b + ", " + c;
    }
}