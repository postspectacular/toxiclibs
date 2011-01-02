package toxi.geom.mesh.subdiv;

import java.util.Comparator;

import toxi.geom.mesh.WingedEdge;

/**
 * Comparator used by {@link SubdivisionStrategy#getEdgeOrdering()} to define
 * the order of edges to be subdivided. This one prioritizes edges with the most
 * faces associated.
 */
public class FaceCountComparator implements Comparator<WingedEdge> {

    public int compare(WingedEdge e1, WingedEdge e2) {
        return (e2.faces.size() - e1.faces.size());
    }
}
