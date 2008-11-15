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
public class VerletConstrainedSpring extends VerletSpring2D {

	/**
	 * Maximum relaxation distance for either end of the spring in world units
	 */
	public float limit = Integer.MAX_VALUE;

	/**
	 * @param a
	 * @param b
	 * @param len
	 * @param str
	 */
	public VerletConstrainedSpring(VerletParticle2D a, VerletParticle2D b,
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
	public VerletConstrainedSpring(VerletParticle2D a, VerletParticle2D b,
			float len, float str, float limit) {
		super(a, b, len, str);
		this.limit = limit;
	}

	protected void update() {
		Vec2D delta = b.sub(a);
		// add minute offset to avoid div-by-zero errors
		float dist = delta.magnitude() + 0.00001f;
		float normDistStrength = (dist - restLength)
				/ (dist * (1f / a.weight + 1f / b.weight)) * strength;
		if (!a.isLocked && !isALocked) {
			a
					.addSelf(delta.scale(normDistStrength * 1 / a.weight)
							.limit(limit));
			a.applyConstraint();
		}
		if (!b.isLocked && !isBLocked) {
			b
					.subSelf(delta.scale(normDistStrength * 1 / b.weight)
							.limit(limit));
			b.applyConstraint();
		}
	}
}
