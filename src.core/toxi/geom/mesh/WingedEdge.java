package toxi.geom.mesh;

import java.util.ArrayList;
import java.util.List;

import toxi.geom.Line3D;

public class WingedEdge extends Line3D {

    public List<WEFace> faces = new ArrayList<WEFace>(2);
    public final int id;

    public WingedEdge(WEVertex a, WEVertex b, WEFace f, int id) {
        super(a, b);
        this.id = id;
        addFace(f);
    }

    public WingedEdge addFace(WEFace f) {
        faces.add(f);
        return this;
    }

    /**
     * @return the faces
     */
    public List<WEFace> getFaces() {
        return faces;
    }

    public WEVertex getOtherEndFor(WEVertex v) {
        if (a == v) {
            return (WEVertex) b;
        }
        if (b == v) {
            return (WEVertex) a;
        }
        return null;
    }

    public void remove() {
        for (WEFace f : faces) {
            f.edges.remove(this);
        }
        ((WEVertex) a).edges.remove(this);
        ((WEVertex) b).edges.remove(this);
    }

    public String toString() {
        return "id: " + id + " " + super.toString() + " f: " + faces.size();
    }
}
