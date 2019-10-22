package toxi.geom.mesh.subdiv;

import java.util.List;

import toxi.geom.VecD3D;

public class CentroidSubdivD implements NewSubdivStrategyD {

    public List<VecD3D[]> subdivideTriangle(VecD3D a, VecD3D b, VecD3D c,
            List<VecD3D[]> resultVertices) {
        VecD3D centroid = a.add(b).addSelf(c).scaleSelf(1 / 3.0f);
        resultVertices.add(new VecD3D[] {
                a, b, centroid
        });
        resultVertices.add(new VecD3D[] {
                b, c, centroid
        });
        resultVertices.add(new VecD3D[] {
                c, a, centroid
        });
        return resultVertices;
    }

}
