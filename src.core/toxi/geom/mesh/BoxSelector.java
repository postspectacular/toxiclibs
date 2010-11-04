package toxi.geom.mesh;

import toxi.geom.AABB;

/**
 * A {@link VertexSelector} implementation for selecting vertices within a given
 * {@link AABB}.
 */
public class BoxSelector extends VertexSelector {

    public final AABB box;

    public BoxSelector(Mesh3D mesh, AABB box) {
        super(mesh);
        this.box = box;
    }

    @Override
    public VertexSelector selectVertices() {
        clearSelection();
        for (Vertex v : mesh.getVertices()) {
            if (box.containsPoint(v)) {
                selection.add(v);
            }
        }
        return this;
    }

}
