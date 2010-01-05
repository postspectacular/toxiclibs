package toxi.physics.constraints;

import toxi.geom.AABB;
import toxi.geom.Ray3D;
import toxi.geom.Vec3D;
import toxi.physics.VerletParticle;

public class BoxConstraint implements ParticleConstraint {

    protected AABB box;
    protected Ray3D intersectRay;

    public BoxConstraint(AABB box) {
        this.box = box.copy();
        this.intersectRay = new Ray3D(box, new Vec3D());
    }

    public BoxConstraint(Vec3D min, Vec3D max) {
        this(AABB.fromMinMax(min, max));
    }

    public void apply(VerletParticle p) {
        if (p.isInAABB(box)) {
            p.set(box.intersectsRay(intersectRay.setDirection(box.sub(p)), 0,
                    Float.MAX_VALUE));
        }
    }

    public AABB getBox() {
        return box.copy();
    }

    public void setBox(AABB box) {
        this.box = box.copy();
        intersectRay.set(box);
    }
}
