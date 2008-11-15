/**
 * 
 */
package toxi.physics.constraints;

import toxi.geom.Vec3D;
import toxi.physics.VerletParticle;

/**
 * Constrains a particle's movement by only allowing free movement along the Z
 * axis of the point given in the constructor (XY are locked).
 */
public class ZAxisConstraint implements ParticleConstraint {

	/**
	 * Constraint vector whose XY components are used to restrict particle
	 * movement
	 */
	public Vec3D constraint;

	public ZAxisConstraint(Vec3D constraint) {
		this.constraint = constraint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.physics.IParticleConstraint#apply(toxi.physics.VerletParticle)
	 */
	public void apply(VerletParticle p) {
		p.x = constraint.x;
		p.y = constraint.y;
	}

}
