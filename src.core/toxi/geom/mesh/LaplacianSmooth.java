package toxi.geom.mesh;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import toxi.geom.Vec3D;

/**
 * Applies a laplacian smooth function to all vertices in the mesh
 * 
 */
public class LaplacianSmooth implements WEMeshFilterStrategy {

    public void filter(VertexSelector selector, int numIterations) {
        final Collection<Vertex> selection = selector.getSelection();
        if (!(selector.getMesh() instanceof WETriangleMesh)) {
            throw new IllegalArgumentException(
                    "This filter requires a WETriangleMesh");
        }
        final WETriangleMesh mesh = (WETriangleMesh) selector.getMesh();
        final HashMap<Vertex, Vec3D> filtered =
                new HashMap<Vertex, Vec3D>(selection.size());
        for (int i = 0; i < numIterations; i++) {
            filtered.clear();
            for (Vertex v : selection) {
                final Vec3D laplacian = new Vec3D();
                final List<WEVertex> neighbours = ((WEVertex) v).getNeighbors();
                for (WEVertex n : neighbours) {
                    laplacian.addSelf(n);
                }
                laplacian.scaleSelf(1f / neighbours.size());
                filtered.put(v, laplacian);
            }
            for (Vertex v : filtered.keySet()) {
                mesh.vertices.get(v).set(filtered.get(v));
            }
            mesh.rebuildIndex();
        }
        mesh.computeFaceNormals();
        mesh.computeVertexNormals();
    }

    public void filter(WETriangleMesh mesh, int numIterations) {
        filter(new DefaultSelector(mesh).selectVertices(), numIterations);
    }
}
