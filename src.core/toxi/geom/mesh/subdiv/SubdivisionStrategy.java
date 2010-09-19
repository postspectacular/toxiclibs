package toxi.geom.mesh.subdiv;

import java.util.Comparator;
import java.util.List;

import toxi.geom.Vec3D;
import toxi.geom.mesh.WingedEdge;

public abstract class SubdivisionStrategy {

    public static final Comparator<? super WingedEdge> DEFAULT_ORDERING =
            new EdgeLengthComparator();

    protected Comparator<? super WingedEdge> order = DEFAULT_ORDERING;

    /**
     * Computes a number of points on (or near) the given edge which are used
     * for splitting the edge in smaller segments.
     * 
     * @param edge
     *            edge to split
     * @return
     */
    public abstract List<Vec3D> computeSplitPoints(WingedEdge edge);

    /**
     * Returns the {@link Comparator} used to sort a mesh's edge list based on a
     * certain criteria. By default the {@link EdgeLengthComparator} is used.
     * 
     * @return
     */
    public Comparator<? super WingedEdge> getEdgeOrdering() {
        return order;
    }

    /**
     * Sets the given edge list {@link Comparator} for a strategy
     * implementation.
     * 
     * @param order
     */
    public void setEdgeOrdering(Comparator<? super WingedEdge> order) {
        this.order = order;
    }
}
