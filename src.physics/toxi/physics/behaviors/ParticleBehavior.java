package toxi.physics.behaviors;

import toxi.physics.VerletParticle;

public interface ParticleBehavior {

	/**
	 * Applies the constraint to the passed in particle. The method is assumed
	 * to manipulate the given instance directly.
	 * 
	 * @param p
	 *            particle
	 */
	public void apply(VerletParticle p);

	public void configure(float timeStep);
}
