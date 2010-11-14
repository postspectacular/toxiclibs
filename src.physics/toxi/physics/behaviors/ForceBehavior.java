package toxi.physics.behaviors;

import toxi.geom.Vec3D;
import toxi.physics.VerletParticle;

public class ForceBehavior implements ParticleBehavior {

	protected final Vec3D force = new Vec3D();

	protected float timeStep;

	public void apply(VerletParticle p) {
		p.addForce(force);
	}

	public void configure(float timeStep) {
		this.timeStep = timeStep;
	}

}
