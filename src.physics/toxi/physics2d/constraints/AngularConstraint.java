package toxi.physics2d.constraints;

import toxi.geom.Vec2D;
import toxi.math.MathUtils;
import toxi.physics2d.VerletParticle2D;

public class AngularConstraint implements Particle2DConstraint {

	public Vec2D rootPos;
	public float theta;

	public AngularConstraint(int theta) {
		this.theta = MathUtils.radians(theta);
	}

	public AngularConstraint(float theta) {
		this.theta = theta;
	}

	public AngularConstraint(Vec2D p, int theta) {
		rootPos = new Vec2D(p);
		this.theta = MathUtils.radians(theta);
	}

	public void apply(VerletParticle2D p) {
		Vec2D delta = p.sub(rootPos);
		float heading = MathUtils.floor(delta.heading() / theta) * theta;
		p.set(rootPos
				.add(Vec2D.fromTheta(heading).scaleSelf(delta.magnitude())));
	}

}
