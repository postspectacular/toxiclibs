/**
 * 
 */
package toxi.physics2d.constraints;

import toxi.geom.Vec2D;
import toxi.geom.Vec2D.Axis;
import toxi.physics2d.VerletParticle2D;

/**
 * Constrains a particle's movement by locking a given axis to a fixed value.
 */

public class AxisConstraint implements Particle2DConstraint {

    public float constraint;
    public Axis axis;

    /**
     * @param axis
     *            axis to lock
     * @param constraint
     *            constrain the axis to this value
     */
    public AxisConstraint(Vec2D.Axis axis, float constraint) {
        this.axis = axis;
        this.constraint = constraint;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * toxi.physics.IParticleConstraint#apply(toxi.physics.VerletParticle2D)
     */
    public void apply(VerletParticle2D p) {
        p.setComponent(axis, constraint);
    }

}
