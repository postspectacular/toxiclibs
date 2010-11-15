package toxi.physics.behaviors;

import toxi.geom.Vec3D;
import toxi.physics.VerletParticle;

public class ConstantForceBehavior implements ParticleBehavior {

    protected Vec3D force;
    protected Vec3D scaledForce = new Vec3D();
    protected float timeStep;

    public ConstantForceBehavior(Vec3D force) {
        this.force = force;
    }

    public void apply(VerletParticle p) {
        p.addForce(scaledForce);
    }

    public void configure(float timeStep) {
        this.timeStep = timeStep;
        setForce(force);
    }

    /**
     * @return the force
     */
    public Vec3D getForce() {
        return force;
    }

    /**
     * @param force
     *            the force to set
     */
    public void setForce(Vec3D force) {
        this.force = force;
        scaledForce = force.scale(timeStep);
    }
}
