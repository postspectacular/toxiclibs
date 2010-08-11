package toxi.geom.mesh;

import java.util.ArrayList;
import java.util.List;

import toxi.geom.Vec3D;

public class TriSubdivision extends SubdivisionStrategy {

    public List<Vec3D> computeSplitPoints(WingedEdge edge) {
        List<Vec3D> mid = new ArrayList<Vec3D>(3);
        mid.add(edge.a.interpolateTo(edge.b, 0.25f));
        mid.add(edge.a.interpolateTo(edge.b, 0.5f));
        mid.add(edge.a.interpolateTo(edge.b, 0.75f));
        return mid;
    }

}
