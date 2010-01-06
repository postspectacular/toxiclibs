package toxi.physics2d.constraints;

import toxi.geom.Ray2D;
import toxi.geom.Rect;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;

public class RectConstraint implements Particle2DConstraint {

    protected Rect rect;
    protected Ray2D intersectRay;

    public RectConstraint(Rect rect) {
        this.rect = rect.copy();
        this.intersectRay = new Ray2D(rect.getCentroid(), new Vec2D());
    }

    public RectConstraint(Vec2D min, Vec2D max) {
        this(Rect.fromMinMax(min, max));
    }

    public void apply(VerletParticle2D p) {
        if (rect.containsPoint(p)) {
            p.set(rect.intersectsRay(intersectRay.setDirection(intersectRay
                    .sub(p)), 0, Float.MAX_VALUE));
        }
    }

    public Rect getBox() {
        return rect.copy();
    }

    public void setBox(Rect rect) {
        this.rect = rect.copy();
        intersectRay.set(rect.getCentroid());
    }
}
