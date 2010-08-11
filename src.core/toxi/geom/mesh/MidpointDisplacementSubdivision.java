package toxi.geom.mesh;

import java.util.ArrayList;
import java.util.List;

import toxi.geom.Vec3D;

public class MidpointDisplacementSubdivision extends MidpointSubdivision {

    private Vec3D centroid;
    private float amp;

    public MidpointDisplacementSubdivision(Vec3D centroid, float amount) {
        this.centroid = centroid;
        this.amp = amount;
    }

    @Override
    public List<Vec3D> computeSplitPoints(WingedEdge edge) {
        Vec3D mid = edge.getMidPoint();
        // Vec3D mid = edge.a.interpolateTo(edge.b, 0.25f);
        Vec3D n = mid.sub(centroid).normalizeTo(amp * edge.getLength());
        List<Vec3D> points = new ArrayList<Vec3D>(1);
        points.add(mid.addSelf(n));
        return points;
    }

}
