package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.Line2D;
import toxi.geom.Line2D.LineIntersection;
import toxi.geom.Vec2D;

public class Line2DTest extends TestCase {

    public void testHashing() {
        Line2D l1 = new Line2D(new Vec2D(100, 420), new Vec2D(-888, 230));
        Line2D l2 = new Line2D(new Vec2D(-888, 230), new Vec2D(100, 420));
        assertTrue(l1.equals(l2));
        System.out.println(l1.hashCode());
        assertEquals(l1.hashCode(), l2.hashCode());
        l2.a = new Vec2D();
        assertFalse(l1.equals(l2));
        l1.b.clear();
        assertTrue(l1.equals(l2));
    }

    public void testIntersection() {
        Line2D l = new Line2D(new Vec2D(), new Vec2D(100, 100));
        Line2D k = new Line2D(new Vec2D(0, 50), new Vec2D(100, 50));
        LineIntersection isec = l.intersectLine(k);
        assertEquals(LineIntersection.Type.INTERSECTING, isec.getType());
        assertEquals(new Vec2D(50, 50), isec.getPos());
        k = l.copy();
        assertEquals(LineIntersection.Type.COINCIDENT, l.intersectLine(k)
                .getType());
        k = new Line2D(new Vec2D(110, 110), new Vec2D(220, 220));
        assertEquals(LineIntersection.Type.COINCIDENT_NO_INTERSECT, l
                .intersectLine(k).getType());
        k = new Line2D(new Vec2D(-100, -100), new Vec2D(100, 50));
        assertEquals(LineIntersection.Type.NON_INTERSECTING, l.intersectLine(k)
                .getType());
        k = new Line2D(new Vec2D(200, -100), new Vec2D(400, 100));
        assertEquals(LineIntersection.Type.PARALLEL, l.intersectLine(k)
                .getType());
    }

    public void testOrientation() {
        Line2D l = new Line2D(new Vec2D(0, 0), new Vec2D(100, 0));
        System.out.println(l.getDirection().angleBetween(Vec2D.Y_AXIS, true));
    }

    public void testScale() {
        Line2D l = new Line2D(new Vec2D(200, 200), new Vec2D(100, 100));
        float len = l.getLength();
        l.scaleLength(0.9f);
        assertEquals(0.9f * len, l.getLength());
        l = new Line2D(new Vec2D(100, 200), new Vec2D(200, 100));
        len = l.getLength();
        l.scaleLength(3f);
        assertEquals(3f * len, l.getLength());
    }
}
