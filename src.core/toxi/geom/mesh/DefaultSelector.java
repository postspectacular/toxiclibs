package toxi.geom.mesh;

import java.util.Collection;

public class DefaultSelector extends VertexSelector {

    public DefaultSelector(Mesh3D mesh) {
        super(mesh);
    }

    @Override
    public Collection<Vertex> selectVertices() {
        return mesh.getVertices();
    }

}
