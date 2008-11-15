/**
 * 
 */
package toxi.physics.constraints;

import toxi.physics.VerletParticle;

/**
 * Defines an interface used to constrain VerletParticles based on a custom
 * criteria (e.g. only allowed to move along an axis or shape etc.). The
 * constraint will be applied directly after each spring update.
 * 
 * @author toxi
 */
public interface ParticleConstraint {
	/**
	 * Applies the constraint to the passed in particle. The method is assumed
	 * to manipulate the given instance directly.
	 * 
	 * @param p particle
	 */
	public void apply(VerletParticle p);
}
