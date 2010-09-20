package toxi.geom.mesh.subdiv;

import java.util.Comparator;

import toxi.geom.mesh.WingedEdge;

public class EdgeLengthComparator implements Comparator<WingedEdge> {

    public int compare(WingedEdge e1, WingedEdge e2) {
        return -(int) (e1.getLengthSquared() - e2.getLengthSquared());
    }
}
