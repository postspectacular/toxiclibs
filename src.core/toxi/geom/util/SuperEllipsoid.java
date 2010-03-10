package toxi.geom.util;

import toxi.geom.Vec3D;
import toxi.math.MathUtils;

/**
 * Super ellipsoid surface evaluator based on code by Paul Bourke:
 * http://local.wasp.uwa.edu.au/~pbourke/geometry/superellipse/
 */
public class SuperEllipsoid implements SurfaceFunction {

    private float p1;
    private float p2;

    public SuperEllipsoid(float n1, float n2) {
        this.p1 = n1;
        this.p2 = n2;
    }

    public Vec3D computeVertexFor(float phi, float theta) {
        phi -= MathUtils.HALF_PI;
        float cosPhi = MathUtils.cos(phi);
        float cosTheta = MathUtils.cos(theta);
        float sinPhi = MathUtils.sin(phi);
        float sinTheta = MathUtils.sin(theta);

        float t =
                MathUtils.sign(cosPhi)
                        * (float) Math.pow(MathUtils.abs(cosPhi), p1);
        Vec3D p = new Vec3D();
        p.x =
                t * MathUtils.sign(cosTheta)
                        * (float) Math.pow(MathUtils.abs(cosTheta), p2);
        p.y =
                MathUtils.sign(sinPhi)
                        * (float) Math.pow(MathUtils.abs(sinPhi), p1);
        p.z =
                t * MathUtils.sign(sinTheta)
                        * (float) Math.pow(MathUtils.abs(sinTheta), p2);
        return p;
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
