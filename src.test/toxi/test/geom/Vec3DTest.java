package toxi.test.geom;

import java.util.List;

import junit.framework.TestCase;
import toxi.geom.Line3D;
import toxi.geom.Vec3D;

public class Vec3DTest extends TestCase {

    public void testClosestAxis() {
        assertEquals(Vec3D.Axis.X, new Vec3D(-1, 0.9f, 0.8f).getClosestAxis());
        assertEquals(null, new Vec3D(1, -1, 0).getClosestAxis());
        assertEquals(null, new Vec3D(1, 0, -1).getClosestAxis());
        assertEquals(Vec3D.Axis.Y,
                new Vec3D(0.8f, -1, -0.99999f).getClosestAxis());
        assertEquals(null, new Vec3D(0.8f, -1, 1).getClosestAxis());
        assertEquals(Vec3D.Axis.Z, new Vec3D(0.8f, -1, 1.1f).getClosestAxis());
        assertEquals(Vec3D.Axis.X, new Vec3D(1, 0, 0).getClosestAxis());
        assertEquals(Vec3D.Axis.Y, new Vec3D(0, -1, 0).getClosestAxis());
        assertEquals(Vec3D.Axis.Z, new Vec3D(0, 0, 1).getClosestAxis());
    }

    public void testSphericalInstance() {
        Vec3D v = new Vec3D(-1, 1, 1);
        Vec3D w = v.copy();
        v.toSpherical();
        v.toCartesian();
        System.out.println(v);
        assertTrue(v.equalsWithTolerance(w, 0.0001f));
    }

    public void testSplitSegments() {
        Vec3D a = new Vec3D(0, 0, 0);
        Vec3D b = new Vec3D(100, 0, 0);
        List<Vec3D> list = Line3D.splitIntoSegments(a, b, 8, null, true);
        assertEquals(14, list.size());
        // testing adding to existing list and skipping start point
        Line3D.splitIntoSegments(b, a, 10, list, false);
        assertFalse(b.equals(list.get(14)));
        assertEquals(24, list.size());
    }
}
