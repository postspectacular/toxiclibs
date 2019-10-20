package toxi.geom.mesh.subdiv;

import java.util.List;

import toxi.geom.VecD3D;

public class MidpointSubdivD implements NewSubdivStrategyD {

    public List<VecD3D[]> subdivideTriangle(VecD3D a, VecD3D b, VecD3D c,
            List<VecD3D[]> resultVertices) {
        VecD3D mab = a.interpolateTo(b, 0.5f);
        VecD3D mbc = b.interpolateTo(c, 0.5f);
        VecD3D mca = c.interpolateTo(a, 0.5f);
        resultVertices.add(new VecD3D[] {
                a, mab, mca
        });
        resultVertices.add(new VecD3D[] {
                mab, b, mbc
        });
        resultVertices.add(new VecD3D[] {
                mbc, c, mca
        });
        resultVertices.add(new VecD3D[] {
                mab, mbc, mca
        });
        return resultVertices;
    }

}
