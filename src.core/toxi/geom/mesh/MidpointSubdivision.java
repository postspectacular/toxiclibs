package toxi.geom.mesh;

import java.util.ArrayList;
import java.util.List;

import toxi.geom.Vec3D;

public class MidpointSubdivision extends SubdivisionStrategy {

    public List<Vec3D> computeSplitPoints(WingedEdge edge) {
        List<Vec3D> mid = new ArrayList<Vec3D>(1);
        mid.add(edge.getMidPoint());
        return mid;
    }

}
