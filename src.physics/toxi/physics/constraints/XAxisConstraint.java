/**
 * 
 */
package toxi.physics.constraints;

import toxi.geom.Vec3D;
import toxi.physics.VerletParticle;

/**
 * Constrains a particle's movement by only allowing free movement along the X
 * axis of the point given in the constructor (YZ are locked).
 */

public class XAxisConstraint implements IParticleConstraint {

	/**
	 * Constraint vector whose YZ components are used to restrict particle
	 * movement
	 */
	public Vec3D constraint;

	public XAxisConstraint(Vec3D constraint) {
		this.constraint = constraint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.physics.IParticleConstraint#apply(toxi.physics.VerletParticle)
	 */
	public void apply(VerletParticle p) {
		p.y = constraint.y;
		p.z = constraint.z;
	}

}
