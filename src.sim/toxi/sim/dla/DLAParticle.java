package toxi.sim.dla;

import toxi.geom.Vec3D;

public class DLAParticle extends Vec3D {
	protected Vec3D opos;
	protected Vec3D dir;
	protected float escapeRadius;
	protected float particleSpeed;
	protected float searchSpeed;

	public DLAParticle(Vec3D p, float escapeRadius, float particleSpeed,
			float searchSpeed) {
		super(p);
		this.escapeRadius = escapeRadius;
		this.particleSpeed = particleSpeed;
		this.searchSpeed = searchSpeed;
		opos = p.copy();
		reorientate();
	}

	public void reorientate() {
		dir = Vec3D.randomVector();
	}

	public void update(Vec3D target) {
		Vec3D d = target.sub(this);
		if (d.magnitude() > escapeRadius) {
			set(opos);
			reorientate();
			d = target.sub(this);
		}
		Vec3D ndir = d.getNormalizedTo(searchSpeed);
		dir.interpolateToSelf(ndir, particleSpeed);
		addSelf(dir);
	}
}