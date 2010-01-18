package toxi.physics.constraints;

import toxi.geom.Vec3D.Axis;
import toxi.physics.VerletParticle;

public class MaxConstraint implements ParticleConstraint {

    public Axis axis;
    public float threshold;

    public MaxConstraint(Axis axis, float threshold) {
        this.axis = axis;
        this.threshold = threshold;
    }

    public void apply(VerletParticle p) {
        if (p.getComponent(axis) > threshold) {
            p.setComponent(axis, threshold);
        }
    }

}
