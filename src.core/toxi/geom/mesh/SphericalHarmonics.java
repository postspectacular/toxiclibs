package toxi.geom.mesh;

import toxi.geom.Vec3D;
import toxi.math.MathUtils;

/**
 * Spherical harmonics surface evaluator based on code by Paul Bourke:
 * http://local.wasp.uwa.edu.au/~pbourke/geometry/sphericalh/
 */
public class SphericalHarmonics implements SurfaceFunction {

    float[] m;

    public SphericalHarmonics(float[] m) {
        this.m = m;
    }

    // FIXME check where flipped vertex order is coming from sometimes
    public Vec3D computeVertexFor(Vec3D p, float phi, float theta) {
        float r = 0;
        r += Math.pow(MathUtils.sin(m[0] * theta), m[1]);
        r += Math.pow(MathUtils.cos(m[2] * theta), m[3]);
        r += Math.pow(MathUtils.sin(m[4] * phi), m[5]);
        r += Math.pow(MathUtils.cos(m[6] * phi), m[7]);

        float sinTheta = MathUtils.sin(theta);
        p.x = r * sinTheta * MathUtils.cos(phi);
        p.y = r * MathUtils.cos(theta);
        p.z = r * sinTheta * MathUtils.sin(phi);
        return p;
    }

    public float getPhiRange() {
        return MathUtils.TWO_PI;
    }

    public int getPhiResolutionLimit(int res) {
        return res;
    }

    public float getThetaRange() {
        return MathUtils.PI;
    }

    public int getThetaResolutionLimit(int res) {
        return res;
    }
}