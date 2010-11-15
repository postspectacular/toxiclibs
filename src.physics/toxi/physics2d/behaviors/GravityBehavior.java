package toxi.physics2d.behaviors;

import toxi.geom.Vec2D;

public class GravityBehavior extends ConstantForceBehavior {

    public GravityBehavior(Vec2D gravity) {
        super(gravity);
    }

    @Override
    public void configure(float timeStep) {
        this.timeStep = timeStep;
        scaledForce = force.scale(timeStep * timeStep);
    }
}
