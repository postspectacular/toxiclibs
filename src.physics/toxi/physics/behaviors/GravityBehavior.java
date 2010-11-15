package toxi.physics.behaviors;

import toxi.geom.Vec3D;

public class GravityBehavior extends ConstantForceBehavior {

    public GravityBehavior(Vec3D gravity) {
        super(gravity);
    }

    @Override
    public void configure(float timeStep) {
        this.timeStep = timeStep;
        scaledForce = force.scale(timeStep * timeStep);
    }

}
