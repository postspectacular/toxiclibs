package toxi.physics.behaviors;

import toxi.geom.Vec3D;

public class GravityBehavior extends ForceBehavior {

	protected Vec3D gravity;
	protected float drag;

	public GravityBehavior(Vec3D gravity, float drag) {
		this.gravity = gravity;
		this.drag = drag;
	}

	@Override
	public void configure(float timeStep) {
		force.set(gravity.scale((1.0f - drag) * timeStep * timeStep));
	}

}
