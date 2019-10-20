package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.LineD2D;
import toxi.geom.LineD2D.LineIntersection;
import toxi.geom.VecD2D;

public class LineD2DTest extends TestCase {

    public void testHashing() {
        LineD2D l1 = new LineD2D(new VecD2D(100, 420), new VecD2D(-888, 230));
        LineD2D l2 = new LineD2D(new VecD2D(-888, 230), new VecD2D(100, 420));
        assertTrue(l1.equals(l2));
        System.out.println(l1.hashCode());
        assertEquals(l1.hashCode(), l2.hashCode());
        l2.a = new VecD2D();
        assertFalse(l1.equals(l2));
        l1.b.clear();
        assertTrue(l1.equals(l2));
    }

    public void testIntersection() {
        LineD2D l = new LineD2D(new VecD2D(), new VecD2D(100, 100));
        LineD2D k = new LineD2D(new VecD2D(0, 50), new VecD2D(100, 50));
        LineIntersection isec = l.intersectLine(k);
        assertEquals(LineIntersection.Type.INTERSECTING, isec.getType());
        assertEquals(new VecD2D(50, 50), isec.getPos());
        k = l.copy();
        assertEquals(LineIntersection.Type.COINCIDENT, l.intersectLine(k)
                .getType());
        k = new LineD2D(new VecD2D(110, 110), new VecD2D(220, 220));
        assertEquals(LineIntersection.Type.COINCIDENT_NO_INTERSECT, l
                .intersectLine(k).getType());
        k = new LineD2D(new VecD2D(-100, -100), new VecD2D(100, 50));
        assertEquals(LineIntersection.Type.NON_INTERSECTING, l.intersectLine(k)
                .getType());
        k = new LineD2D(new VecD2D(200, -100), new VecD2D(400, 100));
        assertEquals(LineIntersection.Type.PARALLEL, l.intersectLine(k)
                .getType());
    }

    public void testOrientation() {
        LineD2D l = new LineD2D(new VecD2D(0, 0), new VecD2D(100, 0));
        System.out.println(l.getDirection().angleBetween(VecD2D.Y_AXIS, true));
    }

    public void testScale() {
        LineD2D l = new LineD2D(new VecD2D(200, 200), new VecD2D(100, 100));
        double len = l.getLength();
        l.scaleLength(0.9f);
        assertEquals(0.9f * len, l.getLength());
        l = new LineD2D(new VecD2D(100, 200), new VecD2D(200, 100));
        len = l.getLength();
        l.scaleLength(3f);
        assertEquals(3f * len, l.getLength());
    }
}
