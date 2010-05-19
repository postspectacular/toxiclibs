package toxi.physics.constraints;

import toxi.geom.AABB;
import toxi.geom.Ray3D;
import toxi.geom.Vec3D;
import toxi.physics.VerletParticle;

public class BoxConstraint implements ParticleConstraint {

    protected AABB box;
    protected Ray3D intersectRay;
    private float restitution = 1;

    public BoxConstraint(AABB box) {
        this.box = box.copy();
        this.intersectRay = new Ray3D(box, new Vec3D());
    }

    public BoxConstraint(Vec3D min, Vec3D max) {
        this(AABB.fromMinMax(min, max));
    }

    public void apply(VerletParticle p) {
        if (p.isInAABB(box)) {
            Vec3D dir = p.getVelocity();
            Vec3D prev = p.getPreviousPosition();
            if (prev.isInAABB(box)) {
                dir.invert();
            }
            intersectRay.set(prev);
            intersectRay.setDirection(dir);
            Vec3D isec = box.intersectsRay(intersectRay, 0, Float.MAX_VALUE);
            if (isec != null) {
                isec.addSelf(box.getNormalForPoint(isec).scaleSelf(0.01f));
                p.setPreviousPosition(isec);
                p.set(isec.sub(dir.scaleSelf(restitution)));
            }
        }
    }

    public AABB getBox() {
        return box.copy();
    }

    public float getRestitution() {
        return restitution;
    }

    public void setBox(AABB box) {
        this.box = box.copy();
        intersectRay.set(box);
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }
}
