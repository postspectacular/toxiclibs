package toxi.geom.mesh.subdiv;

import java.util.ArrayList;
import java.util.List;

import toxi.geom.Vec3D;
import toxi.geom.mesh.WingedEdge;

public class NormalDisplacementSubdivision extends DisplacementSubdivision {

    public NormalDisplacementSubdivision(float amp) {
        super(amp);
    }

    @Override
    public List<Vec3D> computeSplitPoints(WingedEdge edge) {
        Vec3D mid = edge.getMidPoint();
        Vec3D n = edge.faces.get(0).normal;
        if (edge.faces.size() > 1) {
            n.addSelf(edge.faces.get(1).normal);
        }
        n.normalizeTo(amp * edge.getLength());
        List<Vec3D> points = new ArrayList<Vec3D>(3);
        points.add(mid.addSelf(n));
        return points;
    }

}
