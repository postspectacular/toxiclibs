package toxi.physics2d.behaviors;

import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;

public class ForceBehavior implements ParticleBehavior2D {

	protected final Vec2D force = new Vec2D();

	protected float timeStep;

	public void apply(VerletParticle2D p) {
		p.addForce(force);
	}

	public void configure(float timeStep) {
		this.timeStep = timeStep;
	}

}
