package toxi.test;

import junit.framework.TestCase;
import toxi.geom.Vec2D;
import toxi.math.Interpolation2D;

public class BilinearTest extends TestCase {

    public void testBilinear() {
        Vec2D p = new Vec2D();
        Vec2D q = new Vec2D(100, 100);
        float val;
        val = Interpolation2D.bilinear(new Vec2D(10, 0), p, q, 100, 0, 0, 0);
        assertEquals(90f, val);
        val = Interpolation2D.bilinear(new Vec2D(50, 0), p, q, 0, 100, 0, 0);
        assertEquals(50f, val);
        val = Interpolation2D.bilinear(new Vec2D(0, 10), p, q, 0, 0, 100, 0);
        assertEquals(10f, val);
        val = Interpolation2D.bilinear(new Vec2D(90, 100), p, q, 0, 0, 0, 100);
        assertEquals(90f, val);
    }
}
