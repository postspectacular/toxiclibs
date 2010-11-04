package toxi.geom.mesh;

public class DefaultSelector extends VertexSelector {

    public DefaultSelector(Mesh3D mesh) {
        super(mesh);
    }

    @Override
    public VertexSelector selectVertices() {
        clearSelection();
        selection.addAll(mesh.getVertices());
        return this;
    }
}
