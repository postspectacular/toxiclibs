package toxi.test.geom;

import java.util.HashMap;

import junit.framework.TestCase;
import toxi.geom.Line3D;
import toxi.geom.Vec3D;
import toxi.geom.mesh.WEVertex;
import toxi.geom.mesh.WingedEdge;
import toxi.math.MathUtils;

public class Line3DTest extends TestCase {

    public void testClosestPoint() {
        Vec3D a = new Vec3D();
        Vec3D b = new Vec3D(100, 0, 0);
        Vec3D c = new Vec3D(50, 50, 0);
        Line3D line = new Line3D(a, b);
        Vec3D isec = line.closestPointTo(c);
        assertEquals(MathUtils.abs(isec.x - c.x) < 0.5, true);
        c = new Vec3D(-50, -50, 0);
        isec = line.closestPointTo(c);
        assertEquals(isec.equals(a), true);
    }

    public void testHashing() {
        Line3D l1 = new Line3D(new Vec3D(100, 420, -50), new Vec3D(-888, 230,
                2999));
        Line3D l2 = new Line3D(new Vec3D(-888, 230, 2999), new Vec3D(100, 420,
                -50));
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
