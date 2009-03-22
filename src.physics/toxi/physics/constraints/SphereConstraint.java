package toxi.physics.constraints;

import toxi.geom.Sphere;
import toxi.geom.Vec3D;
import toxi.physics.VerletParticle;

/**
 * This class implements a spherical constraint for 3D
 * {@linkplain VerletParticle}s. The constraint can be configured in two ways: A
 * bounding sphere not allowing particles to escape or alternatively does not
 * allow particles to enter the space occupied by the sphere.
 * 
 * @author toxi 16 Mar 2009
 */
public class SphereConstraint implements ParticleConstraint {
	public Sphere sphere;

	public boolean isBoundingSphere;

	public final static boolean INSIDE = true;
	public final static boolean OUTSIDE = false;

	/**
	 * Creates a new instance using the sphere definition and constraint mode
	 * given.
	 * 
	 * @param sphere
	 *            sphere instance
	 * @param isBoundary
	 *            constraint mode. Use {@linkplain #INSIDE} or
	 *            {@linkplain #OUTSIDE} to specify constraint behaviour.
	 */
	public SphereConstraint(Sphere sphere, boolean isBoundary) {
		this.sphere = sphere;
		this.isBoundingSphere = isBoundary;
	}

	/**
	 * Creates a new instance using the sphere definition and constraint mode
	 * given.
	 * 
	 * @param origin
	 *            sphere origin
	 * @param radius
	 *            sphere radius
	 * @param isBoundary
	 *            constraint mode. Use {@linkplain #INSIDE} or
	 *            {@linkplain #OUTSIDE} to specify constraint behaviour.
	 */
	public SphereConstraint(Vec3D origin, float radius, boolean isBoundary) {
		sphere = new Sphere(origin, radius);
		this.isBoundingSphere = isBoundary;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * toxi.physics.constraints.ParticleConstraint#apply(toxi.physics.VerletParticle
	 * )
	 */
	public void apply(VerletParticle p) {
		boolean isInside = sphere.containsPoint(p);
		if (isBoundingSphere) {
			if (!isInside) {
				p.normalize().scaleSelf(sphere.radius);
			}
		}
		else {
			if (isInside) {
				p.normalize().scaleSelf(sphere.radius);
			}
		}
	}

}
