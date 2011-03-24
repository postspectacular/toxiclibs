package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.AABB;
import toxi.geom.Ray3D;
import toxi.geom.ReadonlyVec3D;
import toxi.geom.Sphere;
import toxi.geom.Triangle3D;
import toxi.geom.Vec3D;

public class AABBTest extends TestCase {

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
        System.out.println(box.intersectsRay(r, -1000, 1000));
    }

    public void testAABBSphere() {
        AABB box = new AABB(new Vec3D(100, 0, 0), new Vec3D(20, 20, 20));
        Sphere s = new Sphere(new Vec3D(100, 0, 0), 50);
        assertEquals(box.intersectsSphere(s), true);
    }

    public void testAABBTri() {
        AABB box = new AABB(new Vec3D(), new Vec3D(100, 100, 100));
        Vec3D a = new Vec3D(-90, 0, 0);
        Vec3D b = new Vec3D(-110, -200, 0);
        Vec3D c = new Vec3D(-110, 200, 0);
        Triangle3D tri = new Triangle3D(a, b, c);
        System.out.println(box.intersectsTriangle(tri));
    }

    public void testInclude() {
        AABB box = AABB.fromMinMax(new Vec3D(), new Vec3D(100, 100, 100));
        System.out.println(box);
        Vec3D p = new Vec3D(-150, -50, 110);
        box.growToContainPoint(p);
        System.out.println(box.getMin() + " " + box.getMax());
        assertTrue(box.containsPoint(p));
    }

    public void testIsec() {
        AABB box = AABB.fromMinMax(new Vec3D(), new Vec3D(100, 100, 100));
        AABB box2 = AABB.fromMinMax(new Vec3D(10, 10, 10),
                new Vec3D(80, 80, 80));
        assertTrue(box.intersectsBox(box2));
        assertTrue(box2.intersectsBox(box));
    }

    public void testIsInAABB() {
        AABB box = new AABB(new Vec3D(100, 0, 0), new Vec3D(20, 20, 20));
        ReadonlyVec3D p = new Vec3D(80, -19.99f, 0);
        assertEquals(p.isInAABB(box), true);
        assertEquals(new Vec3D(120.01f, 19.99f, 0).isInAABB(box), false);
    }
}
