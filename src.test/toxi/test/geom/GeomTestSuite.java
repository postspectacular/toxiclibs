package toxi.test.geom;

import java.util.ArrayList;

import junit.framework.TestCase;
import toxi.geom.AABB;
import toxi.geom.Line3D;
import toxi.geom.PointOctree;
import toxi.geom.PointQuadtree;
import toxi.geom.Quaternion;
import toxi.geom.Ray3D;
import toxi.geom.ReadonlyVec3D;
import toxi.geom.Rect;
import toxi.geom.Sphere;
import toxi.geom.SphereIntersectorReflector;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;
import toxi.math.MathUtils;

public class GeomTestSuite extends TestCase {

    ReadonlyVec3D intersectsRay(AABB box, Ray3D ray, float minDist,
            float maxDist) {
        Vec3D invDir = ray.getDirection().getReciprocal();
        boolean signDirX = invDir.x < 0;
        boolean signDirY = invDir.y < 0;
        boolean signDirZ = invDir.z < 0;
        Vec3D min = box.getMin();
        Vec3D max = box.getMax();
        Vec3D bbox = signDirX ? max : min;
        float tmin = (bbox.x - ray.x) * invDir.x;
        bbox = signDirX ? min : max;
        float tmax = (bbox.x - ray.x) * invDir.x;
        bbox = signDirY ? max : min;
        float tymin = (bbox.y - ray.y) * invDir.y;
        bbox = signDirY ? min : max;
        float tymax = (bbox.y - ray.y) * invDir.y;

        if ((tmin > tymax) || (tymin > tmax)) {
            return null;
        }
        if (tymin > tmin) {
            tmin = tymin;
        }
        if (tymax < tmax) {
            tmax = tymax;
        }

        bbox = signDirZ ? max : min;
        float tzmin = (bbox.z - ray.z) * invDir.z;
        bbox = signDirZ ? min : max;
        float tzmax = (bbox.z - ray.z) * invDir.z;

        if ((tmin > tzmax) || (tzmin > tmax)) {
            return null;
        }
        if (tzmin > tmin) {
            tmin = tzmin;
        }
        if (tzmax < tmax) {
            tmax = tzmax;
        }
        if ((tmin < maxDist) && (tmax > minDist)) {
            return ray.getPointAtDistance(tmin);
        }
        return null;
    }

    public void testAABB2AABB() {
        AABB box = new AABB(new Vec3D(100, 0, 0), new Vec3D(20, 20, 20));
        AABB b2 = new AABB(new Vec3D(100, 30.1f, 0), new Vec3D(10, 10, 10));
        assertEquals(box.intersectsBox(b2), false);
    }

    public void testAABBNormal() {
        AABB box = new AABB(new Vec3D(100, 100, 100), new Vec3D(100, 100, 100));
        Vec3D p = new Vec3D(100, 300, 100);
        assertEquals(Vec3D.Y_AXIS, box.getNormalForPoint(p));
        p.set(100, -300, 100);
        assertEquals(Vec3D.Y_AXIS.getInverted(), box.getNormalForPoint(p));
        p.set(300, 100, 100);
        assertEquals(Vec3D.X_AXIS, box.getNormalForPoint(p));
        p.set(-300, 100, 100);
        assertEquals(Vec3D.X_AXIS.getInverted(), box.getNormalForPoint(p));
        p.set(100, 100, 300);
        assertEquals(Vec3D.Z_AXIS, box.getNormalForPoint(p));
        p.set(100, 100, -300);
        assertEquals(Vec3D.Z_AXIS.getInverted(), box.getNormalForPoint(p));
    }

    public void testAABBRayIntersect() {
        AABB box = AABB.fromMinMax(new Vec3D(), new Vec3D(100, 100, 100));
        Ray3D r = new Ray3D(new Vec3D(50, 10, 10), new Vec3D(0, 1, 0));
        System.out.println(intersectsRay(box, r, -1000, 1000));
    }

    public void testAABBSphere() {
        AABB box = new AABB(new Vec3D(100, 0, 0), new Vec3D(20, 20, 20));
        Sphere s = new Sphere(new Vec3D(100, 0, 0), 50);
        assertEquals(box.intersectsSphere(s), true);
    }

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

    public void testIsInAABB() {
        AABB box = new AABB(new Vec3D(100, 0, 0), new Vec3D(20, 20, 20));
        ReadonlyVec3D p = new Vec3D(80, -19.99f, 0);
        assertEquals(p.isInAABB(box), true);
        assertEquals(new Vec3D(120.01f, 19.99f, 0).isInAABB(box), false);
    }

    public void testIsInSphere() {
        Vec3D p = new Vec3D(0, -10, 0);
        Sphere s = new Sphere(new Vec3D(), 10);
        assertEquals(s.containsPoint(p), true);
        p.set(0, 10.1f, 0);
        assertEquals(s.containsPoint(p), false);
    }

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
        ArrayList<Vec3D> points =
                t.getPointsWithinSphere(new Sphere(new Vec3D(50, 0, 0), 50));
        assertEquals(points.size() == 2, true);
        points =
                t.getPointsWithinBox(new AABB(new Vec3D(50, 50, 50), new Vec3D(
                        50, 50, 50)));
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

    public void testRectMerge() {
        Rect r = new Rect(-10, 2, 3, 3);
        Rect s = new Rect(-8, 4, 5, 3);
        r.union(s);
        System.out.println(r);
        assertEquals(r.width == 7, true);
        assertEquals(r.height == 5, true);
        r = new Rect(0, 0, 3, 3);
        s = new Rect(-1, 2, 1, 1);
        r.union(s);
        System.out.println(r);
    }

    public void testReflectRay() {
        SphereIntersectorReflector si =
                new SphereIntersectorReflector(new Vec3D(0, 0, 0), 10);
        Ray3D r =
                si.reflectRay(new Ray3D(new Vec3D(100, 100, 0), new Vec3D(-1,
                        -1, 0)));
        float absDiff = r.getDirection().angleBetween(new Vec3D(1, 1, 0), true);
        System.out.println(r + " diff: " + absDiff);
        assertEquals(absDiff < 0.002, true);
    }

    public void testSlerp() {
        Quaternion a = new Quaternion(0, new Vec3D(0, 0, -1));
        Quaternion b = new Quaternion(0, new Vec3D(0, 0, 1));
        Quaternion c = a.interpolateTo(b, 0.05f);
        System.out.println(c);
    }
}
