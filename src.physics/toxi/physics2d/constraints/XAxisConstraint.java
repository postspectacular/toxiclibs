/**
 * 
 */
package toxi.physics2d.constraints;

import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;

/**
 * Constrains a particle's movement by only allowing free movement along the X
 * axis of the point given in the constructor (YZ are locked).
 */

public class XAxisConstraint implements Particle2DConstraint {

	/**
	 * Constraint vector whose YZ components are used to restrict particle
	 * movement
	 */
	public Vec2D constraint;

	public XAxisConstraint(Vec2D constraint) {
		this.constraint = constraint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxi.physics.IParticleConstraint#apply(toxi.physics.VerletParticle)
	 */
	public void apply(VerletParticle2D p) {
		p.y = constraint.y;
	}

}
