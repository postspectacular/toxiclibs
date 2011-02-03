package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.Rect;

public class RectTest extends TestCase {

    public void testRectMerge() {
        Rect r = new Rect(-10, 2, 3, 3);
        Rect s = new Rect(-8, 4, 5, 3);
        r.union(s);
        System.out.println(r);
        assertEquals(r.width == 7, true);
        assertEquals(r.height == 5, true);
        r = new Rect(0, 0, 3, 3);
        s = new Rect(-1, 2, 1, 1);
        r.union(s);
        System.out.println(r);
    }

}
