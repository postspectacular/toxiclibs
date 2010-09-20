package toxi.geom.mesh.subdiv;

import java.util.ArrayList;
import java.util.List;

import toxi.geom.Vec3D;
import toxi.geom.mesh.WingedEdge;

public class MidpointDisplacementSubdivision extends DisplacementSubdivision {

    public Vec3D centroid;

    public MidpointDisplacementSubdivision(Vec3D centroid, float amount) {
        super(amount);
        this.centroid = centroid;
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
