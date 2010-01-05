package toxi.physics.constraints;

import toxi.physics.VerletParticle;

public class MinXConstraint implements ParticleConstraint {

    public float threshold;

    public MinXConstraint(float threshold) {
        this.threshold = threshold;
    }

    public void apply(VerletParticle p) {
        if (p.x < threshold) {
            p.x = threshold;
        }
    }

}
