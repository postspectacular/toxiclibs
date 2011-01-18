package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.Sphere;
import toxi.geom.Vec2D;
import toxi.math.MathUtils;

public class SphereTest extends TestCase {

    public void testSurfaceDistance() {
        Vec2D p = new Vec2D(90, 60).scale(MathUtils.DEG2RAD);
        Vec2D q = new Vec2D(90, 61).scale(MathUtils.DEG2RAD);
        Sphere e = new Sphere(Sphere.EARTH_RADIUS);
        double dist = (float) e.surfaceDistanceBetween(p, q);
        assertTrue(MathUtils.abs(dist - 111.1952) < 0.1);
    }
}
