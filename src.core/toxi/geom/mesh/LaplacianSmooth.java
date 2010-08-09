package toxi.geom.mesh;

import java.util.HashMap;
import java.util.List;

import toxi.geom.Vec3D;

public class LaplacianSmooth implements FilterStrategy {

    public void filter(WETriangleMesh mesh, int numIterations) {
        HashMap<WEVertex, Vec3D> smoothed =
                new HashMap<WEVertex, Vec3D>(mesh.vertices.size());
        for (int i = 0; i < numIterations; i++) {
            smoothed.clear();
            for (WEVertex v : mesh.vertices.values()) {
                Vec3D laplacian = new Vec3D();
                List<WEVertex> neighbours = v.getNeighbors();
                for (WEVertex n : neighbours) {
                    laplacian.addSelf(n);
                }
                laplacian.scaleSelf(1f / neighbours.size());
                smoothed.put(v, laplacian);
            }
            for (WEVertex v : smoothed.keySet()) {
                mesh.vertices.get(v).set(smoothed.get(v));
            }
            mesh.rebuildIndex();
        }
        mesh.computeFaceNormals();
        mesh.computeVertexNormals();
    }
}
