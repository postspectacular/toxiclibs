package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.ReadonlyVec2D;
import toxi.geom.Triangle2D;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;

public class Triangle2DTest extends TestCase {

    public void testBarycentric() {
        Vec2D a = new Vec2D(-100, 0);
        Vec2D b = new Vec2D(0, -100);
        Vec2D c = new Vec2D(100, 0);
        Triangle2D t = new Triangle2D(a, b, c);
        assertEquals(new Vec3D(1, 0, 0), t.toBarycentric(a));
        assertEquals(new Vec3D(0, 1, 0), t.toBarycentric(b));
        assertEquals(new Vec3D(0, 0, 1), t.toBarycentric(c));
        // test roundtrip
        assertEquals(a, t.fromBarycentric(t.toBarycentric(a)));
        assertEquals(b, t.fromBarycentric(t.toBarycentric(b)));
        assertEquals(c, t.fromBarycentric(t.toBarycentric(c)));
        Vec2D p = new Vec2D(0, 0);
        assertEquals(p, t.fromBarycentric(t.toBarycentric(p)));
        // test point outside
        Vec3D bp = t.toBarycentric(new Vec2D(0, -150));
        assertTrue(bp.magnitude() > 1);
    }

    public void testCentroid() {
        Vec2D a = new Vec2D(-100, 0);
        Vec2D b = new Vec2D(0, 100);
        Vec2D c = new Vec2D(100, 0);
        Triangle2D t = new Triangle2D(a, b, c);
        ReadonlyVec2D centroid = t.computeCentroid();
        assertTrue("incorrect centroid",
                centroid.equals(new Vec2D(0, 100).scaleSelf(1f / 3)));
    }

    public void testClockwise() {
        Vec2D a = new Vec2D(-100, 0);
        Vec2D b = new Vec2D(0, -100);
        Vec2D c = new Vec2D(100, 0);
        Vec2D d = new Vec2D(50, 50);
        // clockwise
        assertTrue(Triangle2D.isClockwise(a, b, c));
        assertTrue(Triangle2D.isClockwise(b, c, d));
        assertTrue(Triangle2D.isClockwise(c, d, a));
        assertTrue(Triangle2D.isClockwise(a, c, d));
        // anticlockwise
        assertFalse(Triangle2D.isClockwise(a, c, b));
        assertFalse(Triangle2D.isClockwise(d, c, b));
        assertFalse(Triangle2D.isClockwise(a, d, c));
    }

    public void testContainment() {
        Vec2D a = new Vec2D(-100, 0);
        Vec2D b = new Vec2D(0, -100);
        Vec2D c = new Vec2D(100, 0);
        Triangle2D t = new Triangle2D(a, b, c);
        assertTrue(t.containsPoint(new Vec2D(0, -50)));
        assertTrue(t.containsPoint(a));
        assertTrue(t.containsPoint(b));
        assertTrue(t.containsPoint(c));
        assertFalse(t.containsPoint(new Vec2D(0, -101)));
        // check anti-clockwise
        t.flipVertexOrder();
        assertTrue(t.containsPoint(new Vec2D(0, -50)));
        assertTrue(t.containsPoint(a));
        assertTrue(t.containsPoint(b));
        assertTrue(t.containsPoint(c));
        assertFalse(t.containsPoint(new Vec2D(0, -101)));
    }

    public void testEquilateral() {
        Triangle2D t = Triangle2D.createEquilateralFrom(new Vec2D(-100, 0),
                new Vec2D(100, 0));
        assertEquals(new Vec2D(0, -57.735027f), t.computeCentroid());
    }

    public void testIntersection() {
        Vec2D a = new Vec2D(-100, 0);
        Vec2D b = new Vec2D(0, -100);
        Vec2D c = new Vec2D(100, 0);
        Vec2D d = new Vec2D(-200, -50);
        Vec2D e = new Vec2D(0, 100);
        Vec2D f = new Vec2D(0, -30);
        Triangle2D t = new Triangle2D(a, b, c);
        Triangle2D t2 = new Triangle2D(d, e, f);
        assertTrue(t.intersectsTriangle(t2));
        f.x = 100;
        assertTrue(t.intersectsTriangle(t2));
        assertTrue(t.intersectsTriangle(new Triangle2D(a, c, e)));
        assertFalse(t.intersectsTriangle(new Triangle2D(a.add(0, 0.01f), c.add(
                0, 0.01f), e)));
        assertTrue(t.intersectsTriangle(new Triangle2D(a.add(0, 0.01f), c.add(
                0, 0.01f), f)));
    }
}
