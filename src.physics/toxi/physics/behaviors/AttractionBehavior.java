package toxi.physics.behaviors;

import toxi.geom.Vec3D;
import toxi.physics.VerletParticle;

public class AttractionBehavior implements ParticleBehavior {

	protected Vec3D attractor;
	protected float attrStrength;

	protected float radius, radiusSquared;
	protected float stength;

	public AttractionBehavior(Vec3D attractor, float radius, float stength) {
		this.attractor = attractor;
		this.stength = stength;
		setRadius(radius);
	}

	public void apply(VerletParticle p) {
		Vec3D delta = attractor.sub(p);
		float dist = delta.magSquared();
		if (dist < radiusSquared) {
			Vec3D f = delta.normalizeTo((1.0f - dist / radiusSquared)
					* attrStrength);
			p.addForce(f);
		}
	}

	public void configure(float timeStep) {
		this.attrStrength = stength * timeStep;
	}

	private void setRadius(float r) {
		this.radius = r;
		this.radiusSquared = r * r;
	}

}
