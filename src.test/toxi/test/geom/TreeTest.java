package toxi.test.geom;

import java.util.ArrayList;

import junit.framework.TestCase;
import toxi.geom.AABB;
import toxi.geom.PointOctree;
import toxi.geom.PointQuadtree;
import toxi.geom.Sphere;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;

public class TreeTest extends TestCase {

    public void testOctree() {
        PointOctree t = new PointOctree(new Vec3D(), 100);
        t.setMinNodeSize(0.5f);
        assertEquals(t.addPoint(new Vec3D(0, 0, 0)), true);
        assertEquals(t.addPoint(new Vec3D(1, 0, 0)), true);
        PointOctree leaf1 = t.getLeafForPoint(new Vec3D(0, 0, 0));
        PointOctree leaf2 = t.getLeafForPoint(new Vec3D(1, 0, 0));
        assertNotSame(leaf1, leaf2);
        assertEquals(t.addPoint(new Vec3D(0, 100, 0)), true);
        assertEquals(t.addPoint(new Vec3D(101, 0, 0)), false);
        ArrayList<Vec3D> points = t.getPointsWithinSphere(new Sphere(new Vec3D(
                50, 0, 0), 50));
        assertEquals(points.size() == 2, true);
        points = t.getPointsWithinBox(new AABB(new Vec3D(50, 50, 50),
                new Vec3D(50, 50, 50)));
        assertEquals(points.size() == 3, true);
    }

    public void testQuadtree() {
        PointQuadtree t = new PointQuadtree(new Vec2D(), 100);
        t.setMinNodeSize(2);
        assertEquals(t.addPoint(new Vec2D(0, 0)), true);
        assertEquals(t.addPoint(new Vec2D(1, 1)), true);
        assertEquals(t.addPoint(new Vec2D(4, 0)), true);
        PointQuadtree leaf1 = t.getLeafForPoint(new Vec2D(0, 0));
        PointQuadtree leaf2 = t.getLeafForPoint(new Vec2D(4, 0));
        assertNotSame(leaf1, leaf2);
        ArrayList<Vec2D> points = t.getPointsWithinRect(leaf1);
        assertEquals(2, points.size());
    }

}
