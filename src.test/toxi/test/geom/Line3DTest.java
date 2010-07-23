package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.Line3D;
import toxi.geom.Vec3D;

public class Line3DTest extends TestCase {

    public void testHashing() {
        Line3D l1 =
                new Line3D(new Vec3D(100, 420, -50), new Vec3D(-888, 230, 2999));
        Line3D l2 =
                new Line3D(new Vec3D(-888, 230, 2999), new Vec3D(100, 420, -50));
        assertTrue(l1.equals(l2));
        assertEquals(l1.hashCode(), l2.hashCode());
        l2.a = new Vec3D();
        assertFalse(l1.equals(l2));
        l1.b.clear();
        assertTrue(l1.equals(l2));
    }
}
