package toxi.test.geom;

import java.util.HashMap;

import junit.framework.TestCase;
import toxi.geom.LineD3D;
import toxi.geom.VecD3D;
import toxi.geom.mesh.WEVertexD;
import toxi.geom.mesh.WingedEdgeD;
import toxi.math.MathUtils;

public class LineD3DTest extends TestCase {

    public void testClosestPoint() {
        VecD3D a = new VecD3D();
        VecD3D b = new VecD3D(100, 0, 0);
        VecD3D c = new VecD3D(50, 50, 0);
        LineD3D line = new LineD3D(a, b);
        VecD3D isec = line.closestPointTo(c);
        assertEquals(MathUtils.abs(isec.x - c.x) < 0.5, true);
        c = new VecD3D(-50, -50, 0);
        isec = line.closestPointTo(c);
        assertEquals(isec.equals(a), true);
    }

    public void testHashing() {
        LineD3D l1 = new LineD3D(new VecD3D(100, 420, -50), new VecD3D(-888, 230,
                2999));
        LineD3D l2 = new LineD3D(new VecD3D(-888, 230, 2999), new VecD3D(100, 420,
                -50));
        assertTrue(l1.equals(l2));
        assertEquals(l1.hashCode(), l2.hashCode());
        l2.a = new VecD3D();
        assertFalse(l1.equals(l2));
        l1.b.clear();
        assertTrue(l1.equals(l2));
        HashMap<LineD3D, WingedEdgeD> map = new HashMap<LineD3D, WingedEdgeD>();
        map.put(l1, new WingedEdgeD(new WEVertexD(l1.a, 0),
                new WEVertexD(l1.b, 1), null, 0));
        WingedEdgeD e = map.get(l1);
        assertEquals(l1, e);
    }
}
