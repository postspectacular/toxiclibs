package toxi.geom.mesh;

public interface MeshFilterStrategy {

    /**
     * Applies the vertex filter to the given mesh
     * 
     * @param mesh
     * @param numIterations
     */
    public void filter(WETriangleMesh mesh, int numIterations);

}