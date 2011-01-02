package toxi.geom.mesh.subdiv;

import java.util.Comparator;

import toxi.geom.mesh.WingedEdge;

/**
 * Comparator used by {@link SubdivisionStrategy#getEdgeOrdering()} to define
 * the order of edges to be subdivided. This one prioritizes longer edges.
 */
public class EdgeLengthComparator implements Comparator<WingedEdge> {

    public int compare(WingedEdge e1, WingedEdge e2) {
        return (int) (e2.getLengthSquared() - e1.getLengthSquared());
    }
}
