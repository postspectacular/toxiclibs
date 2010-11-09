package toxi.geom.mesh;

import java.util.Collection;

public interface WEMeshFilterStrategy {

    public void filter(WETriangleMesh mesh, Collection<Vertex> selection,
            int numIterations);

    /**
     * Applies the vertex filter to the given mesh
     * 
     * @param mesh
     * @param numIterations
     */
    public void filter(WETriangleMesh mesh, int numIterations);

}