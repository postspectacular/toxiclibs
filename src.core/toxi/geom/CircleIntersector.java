package toxi.geom;

/**
 * This class handles Circle-Ray2D intersections by implementing the
 * {@link Intersector2D} interface.
 * 
 */
public class CircleIntersector implements Intersector2D {

    private IsectData2D isec;
    private Circle circle;

    public CircleIntersector(Circle circle) {
        this.circle = circle;
    }

    public IsectData2D getIntersectionData() {
        return isec;
    }

    public boolean intersectsRay(Ray2D ray) {
        isec.clear();
        Vec2D q = circle.sub(ray);
        float distSquared = q.magSquared();
        float v = q.dot(ray.getDirection());
        float r = circle.getRadius();
        float d = r * r - (distSquared - v * v);
        if (d >= 0.0) {
            isec.isIntersection = true;
            isec.dist = v - (float) Math.sqrt(d);
            isec.pos = ray.getPointAtDistance(isec.dist);
            isec.normal = isec.pos.sub(circle).normalize();
        }
        return isec.isIntersection;
    }
}
