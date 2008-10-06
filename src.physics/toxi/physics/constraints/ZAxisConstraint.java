/**
 * 
 */
package toxi.physics.constraints;

import toxi.geom.Vec3D;
import toxi.physics.VerletParticle;

/**
 * @author toxi
 * 
 */
public class ZAxisConstraint implements IParticleConstraint {

	private Vec3D constraint;

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
