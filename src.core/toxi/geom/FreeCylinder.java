package toxi.geom;

import toxi.geom.util.TriangleMesh;
import toxi.math.MathUtils;

public class FreeCylinder extends Vec3D {

    public Vec3D dir;
    public float radius, height;

    public FreeCylinder(Vec3D p, Vec3D d, float r, float l) {
        super(p);
        dir = d;
        radius = r;
        height = l;
    }

    public TriangleMesh toMesh(int steps, float thetaOffset) {
        return toMesh("cylinder", steps, thetaOffset);
    }

    public TriangleMesh toMesh(String name, int steps, float thetaOffset) {
        Vec3D c = this.add(0.01f, 0.01f, 0.01f);
        Vec3D n = c.cross(dir.getNormalized()).normalize();
        Vec3D p = sub(dir.scale(height * 0.5f));
        Vec3D q = add(dir.scale(height * 0.5f));
        Vec3D[] top = new Vec3D[steps];
        Vec3D[] bottom = new Vec3D[steps];
        for (int i = 0; i < steps; i++) {
            float theta = i * MathUtils.TWO_PI / steps + thetaOffset;
            Vec3D o = n.getRotatedAroundAxis(dir, theta).scaleSelf(radius);
            top[i] = new Vec3D(p.x + o.x, p.y + o.y, p.z + o.z);
            bottom[i] = new Vec3D(q.x + o.x, q.y + o.y, q.z + o.z);
        }
        TriangleMesh mesh = new TriangleMesh(name);
        for (int i = 0; i < steps; i++) {
            mesh.addFace(top[i], bottom[i], top[(i + 1) % steps]);
            mesh.addFace(top[(i + 1) % steps], bottom[i], bottom[(i + 1)
                    % steps]);
            mesh.addFace(this, top[i], top[(i + 1) % steps]);
            mesh.addFace(bottom[i], q, bottom[(i + 1) % steps]);
        }
        return mesh;
    }
}