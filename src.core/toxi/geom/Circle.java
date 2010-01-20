package toxi.geom;

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
        return super.getCircumference();
    }

    public float getRadius() {
        return radius.x;
    }

    public Circle setRadius(float r) {
        super.setRadii(r, r);
        return this;
    }
}
