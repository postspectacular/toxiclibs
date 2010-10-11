package toxi.physics;

/**
 * Creates a pullback spring (default restlength=0.5) between 2 particles and
 * locks the first one given at the current position. The spring is only
 * enforced if the current length of the spring exceeds the rest length. This
 * behaviour is the opposite to the {@link VerletMinDistanceSpring}.
 */
class PullBackString extends VerletSpring {

    public PullBackString(VerletParticle a, VerletParticle b, float strength) {
        super(a, b, 0, strength);
        a.lock();
        setRestLength(0);
    }

    protected void update(boolean applyConstraints) {
        if (b.distanceToSquared(a) > 0.5f) {
            super.update(applyConstraints);
        }
    }
}