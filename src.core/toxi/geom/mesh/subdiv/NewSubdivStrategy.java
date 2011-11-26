package toxi.geom.mesh.subdiv;

import java.util.List;

import toxi.geom.Vec3D;

public interface NewSubdivStrategy {

    public List<Vec3D[]> subdivideTriangle(Vec3D a, Vec3D b, Vec3D c,
            List<Vec3D[]> resultVertices);
}
