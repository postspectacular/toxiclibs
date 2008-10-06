/**
 * 
 */
package toxi.physics;

import toxi.geom.Vec3D;

/**
 * @author toxi
 * 
 */
public class XAxisConstraint implements IParticleConstraint {

	private Vec3D constraint;

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
