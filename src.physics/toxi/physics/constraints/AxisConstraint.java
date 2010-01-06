/**
 * 
 */
package toxi.physics.constraints;

import toxi.geom.Vec3D.Axis;
import toxi.physics.VerletParticle;

/**
 * Constrains a particle's movement by locking a given axis to a fixed value.
 */
public class AxisConstraint implements ParticleConstraint {

    public float constraint;
    public Axis axis;

    /**
     * @param axis
     *            axis to lock
     * @param constraint
     *            constrain the axis to this value
     */
    public AxisConstraint(Axis axis, float constraint) {
        this.axis = axis;
        this.constraint = constraint;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.physics.IParticleConstraint#apply(toxi.physics.VerletParticle)
     */
    public void apply(VerletParticle p) {
        p.setComponent(axis, constraint);
    }

}
