package toxi.physics.constraints;

import toxi.geom.AABB;
import toxi.physics.VerletParticle;

public class SoftBoxConstraint implements ParticleConstraint {

    private AABB box;
    private float damping;

    public SoftBoxConstraint(AABB box, float damping) {
        this.box = box;
        this.damping = damping;
    }

    public void apply(VerletParticle p) {
        if (p.isInAABB(box)) {
            p.x *= damping;
            p.z *= damping;
        }
    }

}
