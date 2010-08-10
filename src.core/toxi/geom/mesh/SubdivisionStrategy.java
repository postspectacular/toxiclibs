package toxi.geom.mesh;

import java.util.Comparator;
import java.util.List;

import toxi.geom.Vec3D;

public interface SubdivisionStrategy {

    public List<Vec3D> computeSplitPoint(WingedEdge edge);

    public Comparator<? super WingedEdge> getEdgeOrdering();
}
