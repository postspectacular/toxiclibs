package toxi.physics;

import java.util.ArrayList;
import java.util.List;

import toxi.geom.Spline3D;
import toxi.geom.Vec3D;

/**
 * This class is used as a builder to dynamically construct a
 * {@link ParticleString} following a given spline path, sampled at a fixed
 * frequency/step distance. This functionality is needed especially when working
 * with various obstacles/mechanic constraints which the string should flow/wrap
 * around.
 */
public class ParticlePath extends Spline3D {

    List<VerletParticle> particles = new ArrayList<VerletParticle>();

    public ParticlePath() {
        super();
    }

    public ParticlePath(List<Vec3D> arg0) {
        super(arg0);
    }

    public List<VerletParticle> createParticles(VerletPhysics physics,
            float step, float mass) {
        particles.clear();
        int num = vertices.size();
        int i = 0;
        while (i < num - 1) {
            Vec3D a = vertices.get(i);
            Vec3D b = vertices.get(i + 1);
            Vec3D dir = b.sub(a);
            Vec3D stepDir = dir.getNormalizedTo(step);
            Vec3D curr = a.copy();
            while (curr.sub(a).dot(dir) / dir.magSquared() <= 1) {
                VerletParticle currP = new VerletParticle(curr, mass);
                particles.add(currP);
                curr.addSelf(stepDir);
            }
            i++;
        }
        return particles;
    }
}
