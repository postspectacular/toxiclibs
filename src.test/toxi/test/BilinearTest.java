package toxi.test;

import junit.framework.TestCase;
import toxi.geom.Vec2D;
import toxi.math.Interpolation2D;

public class BilinearTest extends TestCase {

    public void testBilinear() {
        Vec2D p = new Vec2D();
        Vec2D q = new Vec2D(100, 100);
        float val;
        val = Interpolation2D.bilinear(new Vec2D(10, 0), p, q, 100, 200, 200,
                100);
        assertEquals(110f, val);
        val = Interpolation2D.bilinear(new Vec2D(50, 0), p, q, 100, 200, 200,
                100);
        assertEquals(150f, val);
        val = Interpolation2D.bilinear(new Vec2D(90, 10), p, q, 100, 200, 200,
                100);
        assertEquals(182f, val);
        val = Interpolation2D.bilinear(new Vec2D(90, 100), p, q, 100, 200, 200,
                100);
        assertEquals(110f, val);
        val = Interpolation2D.bilinear(10, 10, 0, 0, 100, 100, 100, 200, 200,
                100);
        assertEquals(118f, val);
    }
}
