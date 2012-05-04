package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.ReadonlyVec3D;
import toxi.geom.Triangle3D;
import toxi.geom.Vec3D;

public class TriangleTest extends TestCase {

    public void testBarycentric() {
        Vec3D a = new Vec3D(-100, -100, 0);
        Vec3D c = new Vec3D(100, 0, 0);
        Vec3D b = new Vec3D(-100, 100, 0);
        Triangle3D t = new Triangle3D(a, b, c);
        assertTrue(a.equalsWithTolerance(t.fromBarycentric(t.toBarycentric(a)),
                0.01f));
        assertTrue(b.equalsWithTolerance(t.fromBarycentric(t.toBarycentric(b)),
                0.01f));
        assertTrue(c.equalsWithTolerance(t.fromBarycentric(t.toBarycentric(c)),
                0.01f));
    }

    public void testCentroid() {
        Vec3D a = new Vec3D(100, 0, 0);
        Vec3D b = new Vec3D(0, 100, 0);
        Vec3D c = new Vec3D(0, 0, 100);
        Triangle3D t = new Triangle3D(a, b, c);
        ReadonlyVec3D centroid = t.computeCentroid();
        assertTrue("incorrect centroid",
                centroid.equals(new Vec3D(100, 100, 100).scaleSelf(1f / 3)));
    }

    public void testClockwise() {
        Vec3D a = new Vec3D(0, 100, 0);
        Vec3D b = new Vec3D(100, 0, -50);
        Vec3D c = new Vec3D(-100, -100, 100);
        assertTrue("not clockwiseXY", Triangle3D.isClockwiseInXY(a, b, c));
        assertTrue("not clockwiseXZ", Triangle3D.isClockwiseInXY(a, b, c));
        assertTrue("not clockwiseYZ", Triangle3D.isClockwiseInXY(a, b, c));
    }

    public void testContainment() {
        Vec3D a = new Vec3D(100, 0, 0);
        Vec3D b = new Vec3D(0, 100, 0);
        Vec3D c = new Vec3D(0, 0, 100);
        Triangle3D t = new Triangle3D(a, b, c);
        assertTrue(t.containsPoint(a));
        assertTrue(t.containsPoint(b));
        assertTrue(t.containsPoint(c));
        assertTrue(t.containsPoint(t.computeCentroid()));
        assertFalse(t.containsPoint(a.add(0.1f, 0, 0)));
        t.flipVertexOrder();
        assertTrue(t.containsPoint(t.computeCentroid()));
    }

    public void testEquilateral() {
        Triangle3D t = Triangle3D.createEquilateralFrom(new Vec3D(-100, 0, 0),
                new Vec3D(100, 0, 0));

    }

    public void testNormal() {
        Vec3D a = new Vec3D(0, 100, 0);
        Vec3D b = new Vec3D(100, 0, 0);
        Vec3D c = new Vec3D(-100, -100, 0);
        Triangle3D t = new Triangle3D(a, b, c);
        ReadonlyVec3D n = t.computeNormal();
        assertTrue("normal wrong", n.equals(new Vec3D(0, 0, 1)));
    }
}
