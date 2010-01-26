package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.Triangle2D;
import toxi.geom.Vec2D;

public class Triangle2DTest extends TestCase {

    public void testCentroid() {
        Vec2D a = new Vec2D(-100, 0);
        Vec2D b = new Vec2D(0, 100);
        Vec2D c = new Vec2D(100, 0);
        Triangle2D t = new Triangle2D(a, b, c);
        Vec2D centroid = t.computeCentroid();
        System.out.println(centroid);
        assertTrue("incorrect centroid", centroid.equals(new Vec2D(0, 100)
                .scaleSelf(1f / 3)));
    }

    public void testClockwise() {
        Vec2D a = new Vec2D(-100, 0);
        Vec2D c = new Vec2D(0, -100);
        Vec2D b = new Vec2D(100, 0);
        assertTrue("not clockwiseXY", Triangle2D.isClockwise(a, b, c));
    }

    public void testContainment() {
        Vec2D a = new Vec2D(-100, 0);
        Vec2D c = new Vec2D(0, -100);
        Vec2D b = new Vec2D(100, 0);
        Triangle2D t = new Triangle2D(a, b, c);
        assertEquals(true, t.containsPoint(new Vec2D(0, -50)));
        assertEquals(false, t.containsPoint(new Vec2D(0, -101)));
    }

    public void testEquilateral() {
        Triangle2D t =
                Triangle2D.createEquilateralFrom(new Vec2D(-100, 0), new Vec2D(
                        100, 0));
        assertEquals(new Vec2D(0, -57.735027f), t.computeCentroid());
    }
}
