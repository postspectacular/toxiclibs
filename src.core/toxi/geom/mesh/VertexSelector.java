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
        this.selection = new HashSet<Vertex>();
    }

    public VertexSelector addSelection(Collection<Vertex> sel2) {
        selection.addAll(sel2);
        return this;
    }

    public VertexSelector clearSelection() {
        selection.clear();
        return this;
    }

    public Mesh3D getMesh() {
        return mesh;
    }

    public Collection<Vertex> getSelection() {
        return selection;
    }

    public VertexSelector invertSelection() {
        HashSet<Vertex> newSel =
                new HashSet<Vertex>(mesh.getNumVertices() - selection.size());
        for (Vertex v : mesh.getVertices()) {
            if (!selection.contains(v)) {
                newSel.add(v);
            }
        }
        selection = newSel;
        return this;
    }

    public VertexSelector selectSimilar(Collection<? extends Vec3D> sel2) {
        for (Vec3D v : sel2) {
            selection.add(mesh.getClosestVertexToPoint(v));
        }
        return this;
    }

    public abstract VertexSelector selectVertices();

    public VertexSelector subtractSelection(Collection<Vertex> sel2) {
        selection.removeAll(sel2);
        return this;
    }
}
