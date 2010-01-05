package toxi.physics.constraints;

import toxi.physics.VerletParticle;

public class MinZConstraint implements ParticleConstraint {

    public float threshold;

    public MinZConstraint(float threshold) {
        this.threshold = threshold;
    }

    public void apply(VerletParticle p) {
        if (p.z < threshold) {
            p.z = threshold;
        }
    }

}
