package toxi.geom.mesh;

import toxi.geom.Vec3D;

public class MidpointDisplacementSubdivision extends MidpointSubdivision {

    private Vec3D centroid;
    private float amp;

    public MidpointDisplacementSubdivision(Vec3D centroid, float amount) {
        this.centroid = centroid;
        this.amp = amount;
    }

    @Override
    public Vec3D computeSplitPoint(WingedEdge edge) {
        Vec3D mid = edge.getMidPoint();
        // Vec3D mid = edge.a.interpolateTo(edge.b, 0.25f);
        Vec3D n = mid.sub(centroid).normalizeTo(amp * edge.getLength());
        return mid.addSelf(n);
    }

}
