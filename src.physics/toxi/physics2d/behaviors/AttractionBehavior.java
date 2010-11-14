package toxi.physics2d.behaviors;

import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;

public class AttractionBehavior implements ParticleBehavior2D {

	protected Vec2D attractor;
	protected float attrStrength;

	protected float radius, radiusSquared;
	protected float stength;
	protected float jitter;

	public AttractionBehavior(Vec2D attractor, float radius, float strength) {
		this(attractor, radius, strength, 0);
	}

	public AttractionBehavior(Vec2D attractor, float radius, float strength,
			float jitter) {
		this.attractor = attractor;
		this.stength = strength;
		this.jitter = jitter;
		setRadius(radius);
	}

	public void apply(VerletParticle2D p) {
		Vec2D delta = attractor.sub(p);
		float dist = delta.magSquared();
		if (dist < radiusSquared) {
			Vec2D f = delta.normalizeTo((1.0f - dist / radiusSquared))
					.jitter(jitter).scaleSelf(attrStrength);
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
