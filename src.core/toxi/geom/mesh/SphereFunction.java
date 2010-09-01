package toxi.geom.mesh;

import toxi.geom.Sphere;
import toxi.geom.Vec3D;
import toxi.math.MathUtils;

/**
 * This implementation of a {@link SurfaceFunction} samples a given
 * {@link Sphere} instance when called by the {@link SurfaceMeshBuilder}.
 */
public class SphereFunction implements SurfaceFunction {

    public Sphere sphere;

    /**
     * Creates a new instance using a sphere of the given radius, located at the
     * world origin.
     * 
     * @param radius
     */
    public SphereFunction(float radius) {
        this(new Sphere(new Vec3D(), radius));
    }

    /**
     * Creates a new instance using the given sphere
     * 
     * @param s
     *            sphere
     */
    public SphereFunction(Sphere s) {
        this.sphere = s;
    }

    public Vec3D computeVertexFor(float phi, float theta) {
        return new Vec3D(sphere.radius, phi, theta).toCartesian().addSelf(
                sphere);
    }

    public float getPhiRange() {
        return MathUtils.TWO_PI;
    }

    public int getPhiResolutionLimit(int res) {
        return res / 2;
    }

    public float getThetaRange() {
        return MathUtils.TWO_PI;
    }

    public int getThetaResolutionLimit(int res) {
        return res;
    }
}
