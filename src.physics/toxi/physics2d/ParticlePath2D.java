package toxi.physics2d;

import java.util.ArrayList;
import java.util.List;

import toxi.geom.Spline2D;
import toxi.geom.Vec2D;

/**
 * This class is used as a builder to dynamically construct a
 * {@link ParticleString2D} following a given spline path, sampled at a fixed
 * frequency/step distance. This functionality is needed especially when working
 * with various obstacles/mechanic constraints which the string should flow/wrap
 * around.
 */
public class ParticlePath2D extends Spline2D {

    List<VerletParticle2D> particles = new ArrayList<VerletParticle2D>();

    public ParticlePath2D() {
        super();
    }

    public ParticlePath2D(List<Vec2D> points) {
        super(points);
    }

    public List<VerletParticle2D> createParticles(VerletPhysics2D physics,
            int subDiv, float step, float mass) {
        particles.clear();
        computeVertices(subDiv);
        int num = vertices.size();
        int i = 0;
        while (i < num - 1) {
            Vec2D a = vertices.get(i);
            Vec2D b = vertices.get(i + 1);
            Vec2D dir = b.sub(a);
            Vec2D stepDir = dir.getNormalizedTo(step);
            Vec2D curr = a.copy();
            while (curr.sub(a).dot(dir) / dir.magSquared() <= 1) {
                VerletParticle2D currP = new VerletParticle2D(curr, mass);
                particles.add(currP);
                curr.addSelf(stepDir);
            }
            i++;
        }
        return particles;
    }
}
