package toxi.test.geom;

import java.util.List;

import junit.framework.TestCase;
import toxi.geom.LineD3D;
import toxi.geom.VecD3D;

public class VecD3DTest extends TestCase {

    public void testClosestAxis() {
        assertEquals(VecD3D.AxisD.X, new VecD3D(-1, 0.9f, 0.8f).getClosestAxis());
        assertEquals(null, new VecD3D(1, -1, 0).getClosestAxis());
        assertEquals(null, new VecD3D(1, 0, -1).getClosestAxis());
        assertEquals(VecD3D.AxisD.Y,
                new VecD3D(0.8f, -1, -0.99999f).getClosestAxis());
        assertEquals(null, new VecD3D(0.8f, -1, 1).getClosestAxis());
        assertEquals(VecD3D.AxisD.Z, new VecD3D(0.8f, -1, 1.1f).getClosestAxis());
        assertEquals(VecD3D.AxisD.X, new VecD3D(1, 0, 0).getClosestAxis());
        assertEquals(VecD3D.AxisD.Y, new VecD3D(0, -1, 0).getClosestAxis());
        assertEquals(VecD3D.AxisD.Z, new VecD3D(0, 0, 1).getClosestAxis());
    }

    public void testSphericalInstance() {
        VecD3D v = new VecD3D(-1, 1, 1);
        VecD3D w = v.copy();
        v.toSpherical();
        v.toCartesian();
        System.out.println(v);
        assertTrue(v.equalsWithTolerance(w, 0.0001f));
    }

    public void testSplitSegments() {
        VecD3D a = new VecD3D(0, 0, 0);
        VecD3D b = new VecD3D(100, 0, 0);
        List<VecD3D> list = LineD3D.splitIntoSegments(a, b, 8, null, true);
        assertEquals(14, list.size());
        // testing adding to existing list and skipping start point
        LineD3D.splitIntoSegments(b, a, 10, list, false);
        assertFalse(b.equals(list.get(14)));
        assertEquals(24, list.size());
    }
}
