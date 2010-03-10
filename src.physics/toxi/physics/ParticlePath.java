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

	public ParticlePath(List<Vec3D> points) {
		super(points);
	}

	/**
	 * Creates particles along the spline at the fixed interval given. The
	 * precision of this interval will largely depend on the number of
	 * subdivision vertices created, but can be adjusted via the related
	 * parameter.
	 * 
	 * @param physics
	 *            physics instance
	 * @param subDiv
	 *            number spline segment subdivisions
	 * @param step
	 *            desired rest length between particles
	 * @param mass
	 *            desired particle mass
	 * @return list of particles
	 */
	public List<VerletParticle> createParticles(VerletPhysics physics,
			int subDiv, float step, float mass) {
		particles.clear();
		computeVertices(subDiv);
		for (Vec3D v : getDecimatedVertices(step, true)) {
			VerletParticle p = createSingleParticle(v, mass);
			particles.add(p);
			physics.addParticle(p);
		}
		return particles;
	}

	/**
	 * Extension point for creating a custom/sub-classed VerletParticle
	 * instance.
	 * 
	 * @param pos
	 * @param mass
	 * @return particle
	 */
	protected VerletParticle createSingleParticle(Vec3D pos, float mass) {
		return new VerletParticle(pos, mass);
	}
}
