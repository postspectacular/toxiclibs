package toxi.physics2d.constraints;

import toxi.geom.Vec2D.Axis;
import toxi.physics2d.VerletParticle2D;

public class MaxConstraint implements Particle2DConstraint {

    public Axis axis;
    public float threshold;

    public MaxConstraint(Axis axis, float threshold) {
        this.axis = axis;
        this.threshold = threshold;
    }

    public void apply(VerletParticle2D p) {
        if (p.getComponent(axis) > threshold) {
            p.setComponent(axis, threshold);
        }
    }

}
