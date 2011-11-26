package toxi.geom.mesh.subdiv;

import java.util.List;

import toxi.geom.Vec3D;

public class MidpointSubdiv implements NewSubdivStrategy {

    public List<Vec3D[]> subdivideTriangle(Vec3D a, Vec3D b, Vec3D c,
            List<Vec3D[]> resultVertices) {
        Vec3D mab = a.interpolateTo(b, 0.5f);
        Vec3D mbc = b.interpolateTo(c, 0.5f);
        Vec3D mca = c.interpolateTo(a, 0.5f);
        resultVertices.add(new Vec3D[] {
                a, mab, mca
        });
        resultVertices.add(new Vec3D[] {
                mab, b, mbc
        });
        resultVertices.add(new Vec3D[] {
                mbc, c, mca
        });
        resultVertices.add(new Vec3D[] {
                mab, mbc, mca
        });
        return resultVertices;
    }

}
