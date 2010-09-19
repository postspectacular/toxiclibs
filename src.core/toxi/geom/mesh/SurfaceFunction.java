package toxi.geom.mesh;

import toxi.geom.Vec3D;

/**
 * This interface defines a functor for evaluating the coordinates of a surface
 * mesh used by {@link SurfaceMeshBuilder}.
 * 
 * It is assumed the implementation creates vertices within the unit sphere
 * (normalized).
 */
public interface SurfaceFunction {

    public Vec3D computeVertexFor(Vec3D p, float phi, float theta);

    public float getPhiRange();

    public int getPhiResolutionLimit(int res);

    public float getThetaRange();

    public int getThetaResolutionLimit(int res);
}
