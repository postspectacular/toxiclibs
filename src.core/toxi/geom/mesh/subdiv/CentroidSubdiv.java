package toxi.geom.mesh.subdiv;

import java.util.List;

import toxi.geom.Vec3D;

public class CentroidSubdiv implements NewSubdivStrategy {

    public List<Vec3D[]> subdivideTriangle(Vec3D a, Vec3D b, Vec3D c,
            List<Vec3D[]> resultVertices) {
        Vec3D centroid = a.add(b).addSelf(c).scaleSelf(1 / 3.0f);
        resultVertices.add(new Vec3D[] {
                a, b, centroid
        });
        resultVertices.add(new Vec3D[] {
                b, c, centroid
        });
        resultVertices.add(new Vec3D[] {
                c, a, centroid
        });
        return resultVertices;
    }

}
