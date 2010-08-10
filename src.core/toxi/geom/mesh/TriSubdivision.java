package toxi.geom.mesh;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import toxi.geom.Vec3D;

public class TriSubdivision implements SubdivisionStrategy {

    public List<Vec3D> computeSplitPoint(WingedEdge edge) {
        List<Vec3D> mid = new ArrayList<Vec3D>(3);
        mid.add(edge.a.interpolateTo(edge.b, 0.25f));
        mid.add(edge.a.interpolateTo(edge.b, 0.5f));
        mid.add(edge.a.interpolateTo(edge.b, 0.75f));
        return mid;
    }

    public Comparator<? super WingedEdge> getEdgeOrdering() {
        return new EdgeLengthComparator();
    }
}
