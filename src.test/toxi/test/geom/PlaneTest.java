package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.Plane;
import toxi.geom.Triangle3D;
import toxi.geom.Vec3D;

public class PlaneTest extends TestCase {

    public void testContainment() {
        Triangle3D t = new Triangle3D(new Vec3D(-100, 0, 0), new Vec3D(0, 0,
                -100), new Vec3D(0, 0, 100));
        Plane pl = new Plane(t.computeCentroid(), t.computeNormal());
    }

    public void testProjection() {
        Vec3D origin = new Vec3D(0, 100, 0);
        Plane plane = new Plane(origin, new Vec3D(0, 1, 0));
        Vec3D proj;
        proj = plane.getProjectedPoint(new Vec3D());
        assertEquals(origin, proj);
        proj = plane.getProjectedPoint(new Vec3D(0, 200, 0));
        assertEquals(origin, proj);
        proj = plane.getProjectedPoint(origin);
        assertEquals(origin, proj);
    }
}
