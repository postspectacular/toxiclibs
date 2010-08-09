package toxi.geom.mesh;

import java.util.Comparator;

import toxi.geom.Vec3D;

public interface SubdivisionStrategy {

    public Vec3D computeSplitPoint(WingedEdge edge);

    public Comparator<? super WingedEdge> getEdgeOrdering();
}
