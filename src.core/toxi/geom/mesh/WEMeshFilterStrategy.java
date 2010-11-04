package toxi.geom.mesh;


public interface WEMeshFilterStrategy {

    public void filter(VertexSelector selector, int numIterations);

    /**
     * Applies the vertex filter to the given mesh
     * 
     * @param mesh
     * @param numIterations
     */
    public void filter(WETriangleMesh mesh, int numIterations);

}