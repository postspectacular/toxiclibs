package toxi.geom;

/**
 * This class defines a 2D circle and provides several utility methods for it.
 */
public class Circle extends Vec2D {

    public float radius;

    public Circle() {
        this(new Vec2D(), 1);
    }

    public Circle(Circle c) {
        this(c, c.radius);
    }

    public Circle(Vec2D origin, float radius) {
        super(origin);
        this.radius = radius;
    }

    public boolean containsPoint(Vec2D p) {
        return distanceToSquared(p) <= radius * radius;
    }
}
