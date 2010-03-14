package toxi.geom;

import toxi.geom.util.TriangleMesh;
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
    public Cone(Vec3D pos, Vec3D dir, float rNorth, float rSouth, float len) {
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
        return toMesh("cone", steps, thetaOffset);
    }

    public TriangleMesh toMesh(String name, int steps, float thetaOffset) {
        Vec3D c = this.add(0.01f, 0.01f, 0.01f);
        Vec3D n = c.cross(dir.getNormalized()).normalize();
        Vec3D p = sub(dir.scale(length * 0.5f));
        Vec3D q = add(dir.scale(length * 0.5f));
        Vec3D[] south = new Vec3D[steps];
        Vec3D[] north = new Vec3D[steps];
        for (int i = 0; i < steps; i++) {
            float theta = i * MathUtils.TWO_PI / steps + thetaOffset;
            Vec3D nr = n.getRotatedAroundAxis(dir, theta);
            Vec3D ts = nr.scale(radiusSouth);
            Vec3D tn = nr.scale(radiusNorth);
            south[i] = new Vec3D(p.x + ts.x, p.y + ts.y, p.z + ts.z);
            north[i] = new Vec3D(q.x + tn.x, q.y + tn.y, q.z + tn.z);
        }
        TriangleMesh mesh = new TriangleMesh(name);
        for (int i = 0; i < steps; i++) {
            mesh.addFace(south[i], north[i], south[(i + 1) % steps]);
            mesh.addFace(south[(i + 1) % steps], north[i], north[(i + 1)
                    % steps]);
            mesh.addFace(p, south[i], south[(i + 1) % steps]);
            mesh.addFace(north[i], q, north[(i + 1) % steps]);
        }
        return mesh;
    }
}