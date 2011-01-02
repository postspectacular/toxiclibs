package toxi.geom.mesh.subdiv;

import java.util.ArrayList;
import java.util.List;

import toxi.geom.Vec3D;
import toxi.geom.mesh.WingedEdge;

/**
 * This subdivision strategy splits an edge in three equal parts using two split
 * points at 33% and 66% of the edge.
 */
public class DualSubdivision extends SubdivisionStrategy {

    public List<Vec3D> computeSplitPoints(WingedEdge edge) {
        List<Vec3D> mid = new ArrayList<Vec3D>(2);
        mid.add(edge.a.interpolateTo(edge.b, 0.3333f));
        mid.add(edge.a.interpolateTo(edge.b, 0.6666f));
        return mid;
    }
}
