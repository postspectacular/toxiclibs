package toxi.geom.mesh.subdiv;

import java.util.ArrayList;
import java.util.List;

import toxi.geom.Vec3D;
import toxi.geom.mesh.WingedEdge;

/**
 * This subdivision strategy splits an edge in four equal parts using three
 * split points at 25%, 50% and 75% of the edge.
 */
public class TriSubdivision extends SubdivisionStrategy {

    public List<Vec3D> computeSplitPoints(WingedEdge edge) {
        List<Vec3D> mid = new ArrayList<Vec3D>(3);
        mid.add(edge.a.interpolateTo(edge.b, 0.25f));
        mid.add(edge.a.interpolateTo(edge.b, 0.5f));
        mid.add(edge.a.interpolateTo(edge.b, 0.75f));
        return mid;
    }

}
