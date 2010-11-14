package toxi.physics2d.behaviors;

import toxi.physics2d.VerletParticle2D;

public interface ParticleBehavior2D {

	/**
	 * Applies the constraint to the passed in particle. The method is assumed
	 * to manipulate the given instance directly.
	 * 
	 * @param p
	 *            particle
	 */
	public void apply(VerletParticle2D p);

	public void configure(float timeStep);
}
