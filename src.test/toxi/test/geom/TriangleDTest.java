package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.ReadonlyVecD3D;
import toxi.geom.TriangleD3D;
import toxi.geom.VecD3D;

public class TriangleDTest extends TestCase {

    public void testBarycentric() {
        VecD3D a = new VecD3D(-100, -100, 0);
        VecD3D c = new VecD3D(100, 0, 0);
        VecD3D b = new VecD3D(-100, 100, 0);
        TriangleD3D t = new TriangleD3D(a, b, c);
        assertTrue(a.equalsWithTolerance(t.fromBarycentric(t.toBarycentric(a)),
                0.01f));
        assertTrue(b.equalsWithTolerance(t.fromBarycentric(t.toBarycentric(b)),
                0.01f));
        assertTrue(c.equalsWithTolerance(t.fromBarycentric(t.toBarycentric(c)),
                0.01f));
    }

    public void testCentroid() {
        VecD3D a = new VecD3D(100, 0, 0);
        VecD3D b = new VecD3D(0, 100, 0);
        VecD3D c = new VecD3D(0, 0, 100);
        TriangleD3D t = new TriangleD3D(a, b, c);
        ReadonlyVecD3D centroid = t.computeCentroid();
        assertTrue("incorrect centroid",
                centroid.equals(new VecD3D(100, 100, 100).scaleSelf(1f / 3)));
    }

    public void testClockwise() {
        VecD3D a = new VecD3D(0, 100, 0);
        VecD3D b = new VecD3D(100, 0, -50);
        VecD3D c = new VecD3D(-100, -100, 100);
        assertTrue("not clockwiseXY", TriangleD3D.isClockwiseInXY(a, b, c));
        assertTrue("not clockwiseXZ", TriangleD3D.isClockwiseInXY(a, b, c));
        assertTrue("not clockwiseYZ", TriangleD3D.isClockwiseInXY(a, b, c));
    }

    public void testContainment() {
        VecD3D a = new VecD3D(100, 0, 0);
        VecD3D b = new VecD3D(0, 100, 0);
        VecD3D c = new VecD3D(0, 0, 100);
        TriangleD3D t = new TriangleD3D(a, b, c);
        assertTrue(t.containsPoint(a));
        assertTrue(t.containsPoint(b));
        assertTrue(t.containsPoint(c));
        assertTrue(t.containsPoint(t.computeCentroid()));
        assertFalse(t.containsPoint(a.add(0.1f, 0, 0)));
        t.flipVertexOrder();
        assertTrue(t.containsPoint(t.computeCentroid()));
    }

    public void testEquilateral() {
        TriangleD3D t = TriangleD3D.createEquilateralFrom(new VecD3D(-100, 0, 0),
                new VecD3D(100, 0, 0));

    }

    public void testNormal() {
        VecD3D a = new VecD3D(0, 100, 0);
        VecD3D b = new VecD3D(100, 0, 0);
        VecD3D c = new VecD3D(-100, -100, 0);
        TriangleD3D t = new TriangleD3D(a, b, c);
        ReadonlyVecD3D n = t.computeNormal();
        assertTrue("normal wrong", n.equals(new VecD3D(0, 0, 1)));
    }
}
