package toxi.geom;

import toxi.geom.mesh.TriangleMesh;
import toxi.math.MathUtils;

/**
 * A geometric definition of a cone (and cylinder as a special case) with
 * support for mesh creation/representation. The class is currently still
 * incomplete in that it doesn't provide any other features than the
 * construction of a cone shaped mesh.
 */
public class Cone extends Vec3D {

    public Vec3D dir;
    public float radiusSouth;
    public float radiusNorth;
    public float length;

    /**
     * Constructs a new cone instance.
     * 
     * @param pos
     *            centre position
     * @param dir
     *            direction
     * @param rNorth
     *            radius on the side in the forward direction
     * @param rSouth
     *            radius on the side in the opposite direction
     * @param len
     *            length of the cone
     */
    public Cone(ReadonlyVec3D pos, ReadonlyVec3D dir, float rNorth,
            float rSouth, float len) {
        super(pos);
        this.dir = dir.getNormalized();
        this.radiusNorth = rNorth;
        this.radiusSouth = rSouth;
        this.length = len;
    }

    public TriangleMesh toMesh(int steps) {
        return toMesh(steps, 0);
    }

    public TriangleMesh toMesh(int steps, float thetaOffset) {
        return toMesh("cone", steps, thetaOffset, true, true);
    }

    public TriangleMesh toMesh(String name, int steps, float thetaOffset,
            boolean topClosed, boolean bottomClosed) {
        ReadonlyVec3D c = this.add(0.01f, 0.01f, 0.01f);
        ReadonlyVec3D n = c.cross(dir.getNormalized()).normalize();
        Vec3D halfAxis = dir.scale(length * 0.5f);
        Vec3D p = sub(halfAxis);
        Vec3D q = add(halfAxis);
        Vec3D[] south = new Vec3D[steps];
        Vec3D[] north = new Vec3D[steps];
        float phi = MathUtils.TWO_PI / steps;
        for (int i = 0; i < steps; i++) {
            float theta = i * phi + thetaOffset;
            ReadonlyVec3D nr = n.getRotatedAroundAxis(dir, theta);
            south[i] = nr.scale(radiusSouth).addSelf(p);
            north[i] = nr.scale(radiusNorth).addSelf(q);
        }
        int numV = steps * 2 + 2;
        int numF =
                steps * 2 + (topClosed ? steps : 0)
                        + (bottomClosed ? steps : 0);
        TriangleMesh mesh = new TriangleMesh(name, numV, numF);
        for (int i = 0, j = 1; i < steps; i++, j++) {
            if (j == steps) {
                j = 0;
            }
            mesh.addFace(south[i], north[i], south[j]);
            mesh.addFace(south[j], north[i], north[j]);
            if (bottomClosed) {
                mesh.addFace(p, south[i], south[j]);
            }
            if (topClosed) {
                mesh.addFace(north[i], q, north[j]);
            }
        }
        return mesh;
    }
}