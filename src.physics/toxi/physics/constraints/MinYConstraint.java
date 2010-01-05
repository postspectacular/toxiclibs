package toxi.physics.constraints;

import toxi.physics.VerletParticle;

public class MinYConstraint implements ParticleConstraint {

    public float threshold;

    public MinYConstraint(float threshold) {
        this.threshold = threshold;
    }

    public void apply(VerletParticle p) {
        if (p.y < threshold) {
            p.y = threshold;
        }
    }

}
