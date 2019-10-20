package toxi.test.geom;

import java.util.List;

import junit.framework.TestCase;
import toxi.geom.AABBD;
import toxi.geom.PointOctreeD;
import toxi.geom.PointQuadtreeD;
import toxi.geom.RectD;
import toxi.geom.SphereD;
import toxi.geom.VecD2D;
import toxi.geom.VecD3D;

public class TreeDTest extends TestCase {

    public void testOctree() {
        PointOctreeD t = new PointOctreeD(new VecD3D(), 100);
        t.setMinNodeSize(0.5f);
        assertEquals(t.addPoint(new VecD3D(0, 0, 0)), true);
        assertEquals(t.addPoint(new VecD3D(1, 0, 0)), true);
        PointOctreeD leaf1 = t.getLeafForPoint(new VecD3D(0, 0, 0));
        PointOctreeD leaf2 = t.getLeafForPoint(new VecD3D(1, 0, 0));
        assertNotSame(leaf1, leaf2);
        assertEquals(t.addPoint(new VecD3D(0, 100, 0)), true);
        assertEquals(t.addPoint(new VecD3D(101, 0, 0)), false);
        List<VecD3D> points = t.getPointsWithinSphereD(new SphereD(new VecD3D(50,
                0, 0), 50));
        assertEquals(points.size() == 2, true);
        points = t.getPointsWithinBox(new AABBD(new VecD3D(50, 50, 50),
                new VecD3D(50, 50, 50)));
        assertEquals(points.size() == 3, true);
    }

    public void testQuadtree() {
        PointQuadtreeD t = new PointQuadtreeD(null, 0, 0, 100, 100);
        assertEquals(t.index(new VecD2D(0, 0)), true);
        assertEquals(t.index(new VecD2D(1, 1)), true);
        assertEquals(t.index(new VecD2D(4, 0)), true);
        PointQuadtreeD leaf1 = t.findNode(new VecD2D(0, 0));
        PointQuadtreeD leaf2 = t.findNode(new VecD2D(4, 0));
        assertNotSame(leaf1, leaf2);
        List<VecD2D> points = t.itemsWithinRectD(new RectD(0, 0, 2, 2), null);
        assertEquals(2, points.size());
    }

}
