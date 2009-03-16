package toxi.physics.constraints;

import toxi.geom.Sphere;
import toxi.geom.Vec3D;
import toxi.physics.VerletParticle;

/**
 * @author toxi 16 Mar 2009
 * 
 */
// TODO add javadocs
public class SphereConstraint implements ParticleConstraint {
	public Sphere sphere;

	public boolean isBoundary;

	SphereConstraint(Sphere sphere, boolean isBoundary) {
		this.sphere = sphere;
		this.isBoundary = isBoundary;
	}

	SphereConstraint(Vec3D origin, float radius) {
		sphere = new Sphere(origin, radius);
	}

	public void apply(VerletParticle p) {
		boolean isInside = sphere.containsPoint(p);
		if (isBoundary) {
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
