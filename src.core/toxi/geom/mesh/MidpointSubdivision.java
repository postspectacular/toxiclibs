package toxi.geom.mesh;

import java.util.Comparator;

import toxi.geom.Vec3D;

public class MidpointSubdivision implements SubdivisionStrategy {

    public Vec3D computeSplitPoint(WingedEdge edge) {
        return edge.getMidPoint();
    }

    public Comparator<? super WingedEdge> getEdgeOrdering() {
        return new Comparator<WingedEdge>() {

            public int compare(WingedEdge e1, WingedEdge e2) {
                return -(int) (e1.getLengthSquared() - e2.getLengthSquared());
            }

        };
    }
}
