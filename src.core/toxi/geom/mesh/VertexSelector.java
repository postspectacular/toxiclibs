package toxi.geom.mesh;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import toxi.geom.Vec3D;

public abstract class VertexSelector {

    protected Mesh3D mesh;
    protected Set<Vertex> selection;

    public VertexSelector(Mesh3D mesh) {
        this.mesh = mesh;
    }

    public Collection<Vertex> addSelection(Collection<Vertex> sel2) {
        selection.addAll(sel2);
        return selection;
    }

    public VertexSelector clearSelection() {
        selection.clear();
        return this;
    }

    public Collection<Vertex> getSelection() {
        if (selection == null) {
            selection = new HashSet<Vertex>();
        }
        return selection;
    }

    public Collection<Vertex> invertSelection() {
        HashSet<Vertex> newSel =
                new HashSet<Vertex>(mesh.getNumVertices() - selection.size());
        for (Vertex v : mesh.getVertices()) {
            if (!selection.contains(v)) {
                newSel.add(v);
            }
        }
        selection = newSel;
        return selection;
    }

    public Collection<Vertex> selectSimilar(Collection<? extends Vec3D> sel2) {
        if (selection == null) {
            selection = new HashSet<Vertex>(sel2.size());
        }
        for (Vec3D v : sel2) {
            selection.add(mesh.getClosestVertexToPoint(v));
        }
        return selection;
    }

    public abstract Collection<Vertex> selectVertices();

    public Collection<Vertex> subtractSelection(Collection<Vertex> sel2) {
        selection.removeAll(sel2);
        return selection;
    }
}
