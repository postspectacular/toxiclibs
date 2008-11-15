/**
 * 
 */
package toxi.physics2d.constraints;

import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;

/**
 * Constrains a particle's movement by only allowing free movement along the Y
 * axis of the point given in the constructor (XZ are locked).
 */

public class YAxisConstraint implements Particle2DConstraint {

	/**
	 * Constraint vector whose XZ components are used to restrict particle
	 * movement
	 */
	public Vec2D constraint;

	public YAxisConstraint(Vec2D constraint) {
		this.constraint = constraint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.physics.IParticleConstraint#apply(toxi.physics.VerletParticle)
	 */
	public void apply(VerletParticle2D p) {
		p.x = constraint.x;
	}

}
