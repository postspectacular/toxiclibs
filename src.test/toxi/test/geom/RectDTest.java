package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.RectD;
import toxi.geom.VecD2D;

public class RectDTest extends TestCase {

    public void testIntersectionRectD() {
        RectD a = new RectD(100, 100, 100, 100);
        RectD b = new RectD(80, 80, 100, 100);
        RectD i = a.intersectionRectDWith(b);
        assertEquals(new RectD(100, 100, 80, 80), i);
        b = new RectD(80, 80, 20, 20);
        i = a.intersectionRectDWith(b);
        assertEquals(new VecD2D(), i.getDimensions());
        b.width = 10;
        i = a.intersectionRectDWith(b);
        assertNull(i);
        b = new RectD(180, 180, 30, 50);
        i = a.intersectionRectDWith(b);
        assertEquals(new RectD(180, 180, 20, 20), i);
    }

    public void testIsec() {
        RectD a = new RectD(100, 100, 100, 100);
        RectD b = new RectD(110, 110, 10, 10);
        assertTrue(a.intersectsRectD(b));
        assertTrue(b.intersectsRectD(a));
        b = new RectD(80, 80, 30, 200);
        assertTrue(a.intersectsRectD(b));
    }

    public void testRectDMerge() {
        RectD r = new RectD(-10, 2, 3, 3);
        RectD s = new RectD(-8, 4, 5, 3);
        r = r.unionRectDWith(s);
        assertEquals(new RectD(-10, 2, 7, 5), r);
        r = new RectD(0, 0, 3, 3);
        s = new RectD(-1, 2, 1, 1);
        r = r.unionRectDWith(s);
        assertEquals(new RectD(-1, 0, 4, 3), r);
    }
}
