package toxi.geom.mesh.subdiv;

import java.util.List;

import toxi.geom.VecD3D;

public interface NewSubdivStrategyD {

    public List<VecD3D[]> subdivideTriangle(VecD3D a, VecD3D b, VecD3D c,
            List<VecD3D[]> resultVertices);
}
