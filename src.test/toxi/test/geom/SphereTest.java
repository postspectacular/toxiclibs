package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.Ray3D;
import toxi.geom.Sphere;
import toxi.geom.SphereIntersectorReflector;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;
import toxi.math.MathUtils;

public class SphereTest extends TestCase {

    public void testIsInSphere() {
        Vec3D p = new Vec3D(0, -10, 0);
        Sphere s = new Sphere(new Vec3D(), 10);
        assertEquals(s.containsPoint(p), true);
        p.set(0, 10.1f, 0);
        assertEquals(s.containsPoint(p), false);
    }

    public void testReflectRay() {
        SphereIntersectorReflector si = new SphereIntersectorReflector(
                new Vec3D(0, 0, 0), 10);
        Ray3D r = si.reflectRay(new Ray3D(new Vec3D(100, 100, 0), new Vec3D(-1,
                -1, 0)));
        float absDiff = r.getDirection().angleBetween(new Vec3D(1, 1, 0), true);
        System.out.println(r + " diff: " + absDiff);
        assertEquals(absDiff < 0.002, true);
    }

    public void testSurfaceDistance() {
        Vec2D p = new Vec2D(90, 60).scale(MathUtils.DEG2RAD);
        Vec2D q = new Vec2D(90, 61).scale(MathUtils.DEG2RAD);
        Sphere e = new Sphere(Sphere.EARTH_RADIUS);
        double dist = (float) e.surfaceDistanceBetween(p, q);
        assertTrue(MathUtils.abs(dist - 111.1952) < 0.1);
    }
}
