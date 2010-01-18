/**
 * 
 */
package toxi.physics.constraints;

import toxi.geom.Vec3D;
import toxi.geom.Vec3D.Axis;
import toxi.physics.VerletParticle;

/**
 * Constrains a particle's movement by locking it to a fixed axis aligned plane.
 */
public class PlaneConstraint implements ParticleConstraint {

    public Vec3D constraint;
    public Axis axis1, axis2;

    /**
     * @param axis
     *            1st axis to lock
     * @param axis2
     *            2d axis to lock
     * @param constraint
     *            point on the desired constraint plane
     */
    public PlaneConstraint(Axis axis, Axis axis2, Vec3D constraint) {
        this.axis1 = axis;
        this.axis2 = axis2;
        this.constraint = constraint;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.physics.IParticleConstraint#apply(toxi.physics.VerletParticle)
     */
    public void apply(VerletParticle p) {
        p.setComponent(axis1, constraint.getComponent(axis1));
        p.setComponent(axis2, constraint.getComponent(axis2));
    }

}
