package toxi.geom.mesh;

import java.util.ArrayList;
import java.util.List;

import toxi.geom.Vec3D;

public class DualDisplacementSubdivision extends SubdivisionStrategy {

    private Vec3D centroid;
    private float ampA, ampB;

    public DualDisplacementSubdivision(Vec3D centroid, float ampA, float ampB) {
        this.centroid = centroid;
        this.ampA = ampA;
        this.ampB = ampB;
    }

    public List<Vec3D> computeSplitPoints(WingedEdge edge) {
        List<Vec3D> mid = new ArrayList<Vec3D>(2);
        float len = edge.getLength();
        Vec3D a = edge.a.interpolateTo(edge.b, 0.3333f);
        a.addSelf(a.sub(centroid).normalizeTo(ampA * len));
        Vec3D b = edge.a.interpolateTo(edge.b, 0.6666f);
        b.addSelf(b.sub(centroid).normalizeTo(ampB * len));
        mid.add(a);
        mid.add(b);
        return mid;
    }
}
