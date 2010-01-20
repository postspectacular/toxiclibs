package toxi.geom;

import toxi.math.MathUtils;

/**
 * This class overrides {@link Ellipse} to define a 2D circle and provides
 * several utility methods for it.
 */
public class Circle extends Ellipse {

    public Circle() {
        this(new Vec2D(), 1);
    }

    public Circle(Circle c) {
        this(c, c.radius.x);
    }

    public Circle(float radius) {
        this(0, 0, radius);
    }

    public Circle(float x, float y, float radius) {
        super(x, y, radius, radius);
    }

    public Circle(Vec2D origin, float radius) {
        super(origin, radius);
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.Ellipse#containsPoint(toxi.geom.Vec2D)
     */
    @Override
    public boolean containsPoint(Vec2D p) {
        return distanceToSquared(p) <= radius.x * radius.x;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.Ellipse#getCircumference()
     */
    @Override
    public float getCircumference() {
        return MathUtils.PI * 2 * radius.x;
    }

    public float getRadius() {
        return radius.x;
    }

    public Vec2D[] getTangentPoints(Vec2D p) {
        Vec2D m = interpolateTo(p, 0.5f);
        return intersectsCircle(new Circle(m, m.distanceTo(p)));
    }

    public Vec2D[] intersectsCircle(Circle c) {
        Vec2D[] res = null;
        Vec2D delta = c.sub(this);
        float d = delta.magnitude();
        float r1 = radius.x;
        float r2 = c.radius.x;
        if (d <= r1 + r2 && d >= Math.abs(r1 - r2)) {
            float a = (r1 * r1 - r2 * r2 + d * d) / (2.0f * d);
            d = 1 / d;
            Vec2D p = add(delta.scale(a * d));
            float h = (float) Math.sqrt(r1 * r1 - a * a);
            delta.perpendicular().scaleSelf(h * d);
            Vec2D i1 = p.add(delta);
            Vec2D i2 = p.sub(delta);
            res = new Vec2D[] { i1, i2 };
        }
        return res;
    }

    public Circle setRadius(float r) {
        super.setRadii(r, r);
        return this;
    }
}
