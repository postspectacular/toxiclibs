package toxi.physics2d;


/**
 * Creates a pullback spring (default restlength=0.5) between 2 particles and
 * locks the first one given at the current position. The spring is only
 * enforced if the current length of the spring exceeds the rest length. This
 * behaviour is the opposite to the {@link VerletMinDistanceSpring2D}.
 */
class PullBackString2D extends VerletSpring2D {

    public PullBackString2D(VerletParticle2D a, VerletParticle2D b,
            float strength) {
        super(a, b, 0, strength);
        a.lock();
        setRestLength(0.5f);
    }

    protected void update(boolean applyConstraints) {
        if (b.distanceToSquared(a) > restLengthSquared) {
            super.update(applyConstraints);
        }
    }
}