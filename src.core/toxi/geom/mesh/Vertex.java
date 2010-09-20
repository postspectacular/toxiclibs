package toxi.geom.mesh;

import toxi.geom.Vec3D;

public class Vertex extends Vec3D {

    public final Vec3D normal = new Vec3D();

    public final int id;

    public Vertex(Vec3D v, int id) {
        super(v);
        this.id = id;
    }

    final void addFaceNormal(Vec3D n) {
        normal.addSelf(n);
    }

    final void clearNormal() {
        normal.clear();
    }

    final void computeNormal() {
        normal.normalize();
    }

    public String toString() {
        return id + ": p: " + super.toString() + " n:" + normal.toString();
    }
}