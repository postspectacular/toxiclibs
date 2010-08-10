package toxi.geom.mesh;

import java.util.Comparator;

public class EdgeLengthComparator implements Comparator<WingedEdge> {

    public int compare(WingedEdge e1, WingedEdge e2) {
        return -(int) (e1.getLengthSquared() - e2.getLengthSquared());
    }
}
