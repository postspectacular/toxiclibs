package toxi.test.geom;

import java.util.HashMap;

import junit.framework.TestCase;
import toxi.geom.Line3D;
import toxi.geom.Vec3D;
import toxi.geom.mesh.WEVertex;
import toxi.geom.mesh.WingedEdge;

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
        HashMap<Line3D, WingedEdge> map = new HashMap<Line3D, WingedEdge>();
        map.put(l1, new WingedEdge(new WEVertex(l1.a, 0),
                new WEVertex(l1.b, 1), null, 0));
        WingedEdge e = map.get(l1);
        assertEquals(l1, e);
    }
}
