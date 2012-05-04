package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.Rect;
import toxi.geom.Vec2D;

public class RectTest extends TestCase {

    public void testIntersectionRect() {
        Rect a = new Rect(100, 100, 100, 100);
        Rect b = new Rect(80, 80, 100, 100);
        Rect i = a.intersectionRectWith(b);
        assertEquals(new Rect(100, 100, 80, 80), i);
        b = new Rect(80, 80, 20, 20);
        i = a.intersectionRectWith(b);
        assertEquals(new Vec2D(), i.getDimensions());
        b.width = 10;
        i = a.intersectionRectWith(b);
        assertNull(i);
        b = new Rect(180, 180, 30, 50);
        i = a.intersectionRectWith(b);
        assertEquals(new Rect(180, 180, 20, 20), i);
    }

    public void testIsec() {
        Rect a = new Rect(100, 100, 100, 100);
        Rect b = new Rect(110, 110, 10, 10);
        assertTrue(a.intersectsRect(b));
        assertTrue(b.intersectsRect(a));
        b = new Rect(80, 80, 30, 200);
        assertTrue(a.intersectsRect(b));
    }

    public void testRectMerge() {
        Rect r = new Rect(-10, 2, 3, 3);
        Rect s = new Rect(-8, 4, 5, 3);
        r = r.unionRectWith(s);
        assertEquals(new Rect(-10, 2, 7, 5), r);
        r = new Rect(0, 0, 3, 3);
        s = new Rect(-1, 2, 1, 1);
        r = r.unionRectWith(s);
        assertEquals(new Rect(-1, 0, 4, 3), r);
    }
}
