package toxi.physics2d.constraints;

import toxi.geom.Circle;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;

public class CircularConstraint implements Particle2DConstraint {

    public Circle circle;

    public CircularConstraint(Circle circle) {
        this.circle = circle;
    }

    public CircularConstraint(Vec2D origin, float radius) {
        this.circle = new Circle(origin, radius);
    }

    public void apply(VerletParticle2D p) {
        if (circle.containsPoint(p)) {
            p.set(circle.add(p.sub(circle).normalizeTo(circle.getRadius())));
        }
    }

}
