/**
 * 
 */
package toxi.physics.constraints;

import toxi.geom.Vec3D;
import toxi.physics.VerletParticle;

/**
 * Constrains a particle's movement by only allowing free movement along the Y
 * axis of the point given in the constructor (XZ are locked).
 */

public class YAxisConstraint implements IParticleConstraint {

	/**
	 * Constraint vector whose XZ components are used to restrict particle
	 * movement
	 */
	public Vec3D constraint;

	public YAxisConstraint(Vec3D constraint) {
		this.constraint = constraint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.physics.IParticleConstraint#apply(toxi.physics.VerletParticle)
	 */
	public void apply(VerletParticle p) {
		p.x = constraint.x;
		p.z = constraint.z;
	}

}
