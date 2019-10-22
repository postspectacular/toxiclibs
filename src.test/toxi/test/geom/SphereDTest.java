package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.RayD3D;
import toxi.geom.SphereD;
import toxi.geom.SphereDIntersectorReflector;
import toxi.geom.VecD2D;
import toxi.geom.VecD3D;
import toxi.math.MathUtils;

public class SphereDTest extends TestCase {

    public void testIsInSphereD() {
        VecD3D p = new VecD3D(0, -10, 0);
        SphereD s = new SphereD(new VecD3D(), 10);
        assertEquals(s.containsPoint(p), true);
        p.set(0, 10.1f, 0);
        assertEquals(s.containsPoint(p), false);
    }

    public void testReflectRayD() {
        SphereDIntersectorReflector si = new SphereDIntersectorReflector(
                new VecD3D(0, 0, 0), 10);
        RayD3D r = si.reflectRayD(new RayD3D(new VecD3D(100, 100, 0), new VecD3D(-1,
                -1, 0)));
        double absDiff = r.getDirection().angleBetween(new VecD3D(1, 1, 0), true);
        System.out.println(r + " diff: " + absDiff);
        assertEquals(absDiff < 0.002, true);
    }

    public void testSurfaceDistance() {
        VecD2D p = new VecD2D(90, 60).scale(MathUtils.DEG2RAD);
        VecD2D q = new VecD2D(90, 61).scale(MathUtils.DEG2RAD);
        SphereD e = new SphereD(SphereD.EARTH_RADIUS);
        double dist =  e.surfaceDistanceBetween(p, q);
        assertTrue(MathUtils.abs(dist - 111.1952) < 0.1);
    }
}
