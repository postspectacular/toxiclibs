package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.PlaneD;
import toxi.geom.TriangleD3D;
import toxi.geom.VecD3D;

public class PlaneDTest extends TestCase {

    public void testContainment() {
        TriangleD3D t = new TriangleD3D(new VecD3D(-100, 0, 0), new VecD3D(0, 0,
                -100), new VecD3D(0, 0, 100));
        PlaneD pl = new PlaneD(t.computeCentroid(), t.computeNormal());
    }

    public void testProjection() {
        VecD3D origin = new VecD3D(0, 100, 0);
        PlaneD plane = new PlaneD(origin, new VecD3D(0, 1, 0));
        VecD3D proj;
        proj = plane.getProjectedPoint(new VecD3D());
        assertEquals(origin, proj);
        proj = plane.getProjectedPoint(new VecD3D(0, 200, 0));
        assertEquals(origin, proj);
        proj = plane.getProjectedPoint(origin);
        assertEquals(origin, proj);
    }
}
