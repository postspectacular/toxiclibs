package toxi.physics;

import java.util.ArrayList;
import java.util.List;

import toxi.geom.Vec3D;

/**
 * Utility builder/grouping/management class to connect a set of particles into
 * a physical string/thread. Custom spring types can be used by subclassing this
 * class and overwriting the
 * {@link #createSpring(VerletParticle, VerletParticle, float, float)} method.
 */
public class ParticleString {

    protected VerletPhysics physics;
    public List<VerletParticle> particles;
    public List<VerletSpring> links;

    public ParticleString(VerletPhysics physics, List<VerletParticle> plist,
            float strength) {
        this.physics = physics;
        particles = new ArrayList<VerletParticle>(plist);
        links = new ArrayList<VerletSpring>(particles.size() - 1);
        VerletParticle prev = null;
        for (VerletParticle p : particles) {
            physics.addParticle(p);
            if (prev != null) {
                VerletSpring s =
                        createSpring(prev, p, prev.distanceTo(p), strength);
                links.add(s);
                physics.addSpring(s);
            }
            prev = p;

        }
    }

    public ParticleString(VerletPhysics physics, Vec3D pos, Vec3D step,
            int num, float mass, float strength) {
        this.physics = physics;
        particles = new ArrayList<VerletParticle>(num);
        links = new ArrayList<VerletSpring>(num - 1);
        float len = step.magnitude();
        VerletParticle prev = null;
        for (int i = 0; i < num; i++) {
            VerletParticle p = new VerletParticle(pos.copy(), mass);
            particles.add(p);
            physics.particles.add(p);
            if (prev != null) {
                VerletSpring s = createSpring(prev, p, len, strength);
                links.add(s);
                physics.addSpring(s);
            }
            prev = p;
            pos.addSelf(step);
        }
    }

    /**
     * Creates a spring instance connecting 2 consecutive particles of the
     * spring. Overwrite this method to create a string using custom spring
     * types (subclassed from {@link VerletSpring}).
     * 
     * @param a
     *            1st particle
     * @param b
     *            2nd particle
     * @param len
     *            rest length
     * @param strength
     * @return spring
     */
    protected VerletSpring createSpring(VerletParticle a, VerletParticle b,
            float len, float strength) {
        return new VerletSpring(a, b, len, strength);
    }

    public VerletParticle getHead() {
        return particles.get(0);
    }

    public int getNumParticles() {
        return particles.size();
    }

    public VerletParticle getTail() {
        return particles.get(particles.size() - 1);
    }

    public void remove() {
        for (VerletSpring s : links) {
            physics.removeSpringElements(s);
        }
    }
}
