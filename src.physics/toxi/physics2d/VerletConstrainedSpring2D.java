/**
 * 
 */
package toxi.physics2d;

import toxi.geom.Vec2D;

/**
 * Implements a spring whose maximum relaxation distance at every time step can
 * be limited to achieve better (if physically incorrect) stability of the whole
 * spring system.
 * 
 * @author toxi
 * 
 */
public class VerletConstrainedSpring2D extends VerletSpring2D {

	/**
	 * Maximum relaxation distance for either end of the spring in world units
	 * (by default unlimited until set by user)
	 */
	public float limit = Float.MAX_VALUE;

	/**
	 * @param a
	 * @param b
	 * @param len
	 * @param str
	 */
	public VerletConstrainedSpring2D(VerletParticle2D a, VerletParticle2D b,
			float len, float str) {
		super(a, b, len, str);
	}

	/**
	 * @param a
	 * @param b
	 * @param len
	 * @param str
	 * @param limit
	 */
	public VerletConstrainedSpring2D(VerletParticle2D a, VerletParticle2D b,
			float len, float str, float limit) {
		super(a, b, len, str);
		this.limit = limit;
	}

	protected void update(boolean applyConstraints) {
		Vec2D delta = b.sub(a);
		// add minute offset to avoid div-by-zero errors
		float dist = delta.magnitude() + EPS;
		float invWeightA = 1f / a.weight;
		float invWeightB = 1f / b.weight;
		float normDistStrength = (dist - restLength)
				/ (dist * (invWeightA + invWeightB)) * strength;
		if (!a.isLocked && !isALocked) {
			a.addSelf(delta.scale(normDistStrength * invWeightA).limit(limit));
			if (applyConstraints) {
				a.applyConstraints();
			}
		}
		if (!b.isLocked && !isBLocked) {
			b.subSelf(delta.scale(normDistStrength * invWeightB).limit(limit));
			if (applyConstraints) {
				b.applyConstraints();
			}
		}
	}
}
