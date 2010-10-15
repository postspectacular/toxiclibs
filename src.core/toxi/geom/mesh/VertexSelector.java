package toxi.geom.mesh;

import java.util.List;

public interface VertexSelector {

    public List<Vertex> selectVertices(Mesh3D mesh, List<Vertex> selection);
}
