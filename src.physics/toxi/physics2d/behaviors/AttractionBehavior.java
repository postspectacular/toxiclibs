package toxi.physics2d.behaviors;

import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;

/**
 *
 */
public class AttractionBehavior implements ParticleBehavior2D {

    protected Vec2D attractor;
    protected float attrStrength;

    protected float radius, radiusSquared;
    protected float strength;
    protected float jitter;
    protected float timeStep;

    public AttractionBehavior(Vec2D attractor, float radius, float strength) {
        this(attractor, radius, strength, 0);
    }

    public AttractionBehavior(Vec2D attractor, float radius, float strength,
            float jitter) {
        this.attractor = attractor;
        this.strength = strength;
        this.jitter = jitter;
        setRadius(radius);
    }

    public void apply(VerletParticle2D p) {
        Vec2D delta = attractor.sub(p);
        float dist = delta.magSquared();
        if (dist < radiusSquared) {
            Vec2D f =
                    delta.normalizeTo((1.0f - dist / radiusSquared))
                            .jitter(jitter).scaleSelf(attrStrength);
            p.addForce(f);
        }
    }

    public void configure(float timeStep) {
        this.timeStep = timeStep;
        setStrength(strength);
    }

    /**
     * @return the attractor
     */
    public Vec2D getAttractor() {
        return attractor;
    }

    /**
     * @return the jitter
     */
    public float getJitter() {
        return jitter;
    }

    /**
     * @return the radius
     */
    public float getRadius() {
        return radius;
    }

    /**
     * @return the strength
     */
    public float getStrength() {
        return strength;
    }

    /**
     * @param attractor
     *            the attractor to set
     */
    public void setAttractor(Vec2D attractor) {
        this.attractor = attractor;
    }

    /**
     * @param jitter
     *            the jitter to set
     */
    public void setJitter(float jitter) {
        this.jitter = jitter;
    }

    private void setRadius(float r) {
        this.radius = r;
        this.radiusSquared = r * r;
    }

    /**
     * @param strength
     *            the strength to set
     */
    public void setStrength(float strength) {
        this.strength = strength;
        this.attrStrength = strength * timeStep;
    }

}
