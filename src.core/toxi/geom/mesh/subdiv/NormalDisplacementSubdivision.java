package toxi.geom.mesh.subdiv;

import java.util.ArrayList;
import java.util.List;

import toxi.geom.Vec3D;
import toxi.geom.mesh.WingedEdge;

/**
 * This subdivision strategy splits an edge in two equal halves at its mid
 * point. The midpoint itself is being displaced, however, in the average
 * direction of face normals associated with this edge. The displacement amount
 * is configurable as fraction of the original edge length. So given that:
 * 
 * <pre>
 * M = edge midpoint
 * N = averaged normal vector
 * l = edge length
 * a = displacement amplification factor
 * 
 * M' = M + N * a * l
 * </pre>
 */
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
