package toxi.physics2d.behaviors;

import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;

public class ConstantForceBehavior implements ParticleBehavior2D {

    protected Vec2D force;
    protected Vec2D scaledForce = new Vec2D();
    protected float timeStep;

    public ConstantForceBehavior(Vec2D force) {
        this.force = force;
    }

    public void apply(VerletParticle2D p) {
        p.addForce(scaledForce);
    }

    public void configure(float timeStep) {
        this.timeStep = timeStep;
        setForce(force);
    }

    /**
     * @return the force
     */
    public Vec2D getForce() {
        return force;
    }

    /**
     * @param force
     *            the force to set
     */
    public void setForce(Vec2D force) {
        this.force = force;
        this.scaledForce = force.scale(timeStep);
    }

}
