// this class is a bug fix for the version included in the current version of
// verletphysics-0008. It will be included in the next release...

// this class handles collision detection and resolution between an individual
// particle and an axis-aligned bounding box (AABB). The intersection is done
// casting a ray from from the particle in the current direction of movement
class BoxConstraint implements ParticleConstraint {

    protected AABB box;
    protected Ray3D intersectRay;
    protected float restitution = 1;

    BoxConstraint(AABB box) {
        this.box = box.copy();
        this.intersectRay = new Ray3D(box, new Vec3D());
    }

    void apply(VerletParticle p) {
        if (p.isInAABB(box)) {
            Vec3D dir = p.getVelocity();
            Vec3D prev = p.getPreviousPosition();
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

   AABB getBox() {
        return box.copy();
    }

    float getRestitution() {
        return restitution;
    }

    void setBox(AABB box) {
        this.box = box.copy();
        intersectRay.set(box);
    }

    void setRestitution(float restitution) {
        this.restitution = restitution;
    }
}

