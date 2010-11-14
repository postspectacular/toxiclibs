package toxi.physics2d.behaviors;

import toxi.geom.Vec2D;

public class GravityBehavior extends ForceBehavior {

	protected Vec2D gravity;

	public GravityBehavior(Vec2D gravity) {
		this.gravity = gravity;
	}

	@Override
	public void configure(float timeStep) {
		force.set(gravity.scale(timeStep * timeStep));
	}
}
