package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.AABBD;
import toxi.geom.RayD3D;
import toxi.geom.ReadonlyVecD3D;
import toxi.geom.SphereD;
import toxi.geom.TriangleD3D;
import toxi.geom.VecD3D;

public class AABBDTest extends TestCase {

    public void testAABBD2AABBD() {
        AABBD box = new AABBD(new VecD3D(100, 0, 0), new VecD3D(20, 20, 20));
        AABBD b2 = new AABBD(new VecD3D(100, 30.1f, 0), new VecD3D(10, 10, 10));
        assertEquals(box.intersectsBox(b2), false);
    }

    public void testAABBDNormal() {
        AABBD box = new AABBD(new VecD3D(100, 100, 100), new VecD3D(100, 100, 100));
        VecD3D p = new VecD3D(100, 300, 100);
        assertEquals(VecD3D.Y_AXIS, box.getNormalForPoint(p));
        p.set(100, -300, 100);
        assertEquals(VecD3D.Y_AXIS.getInverted(), box.getNormalForPoint(p));
        p.set(300, 100, 100);
        assertEquals(VecD3D.X_AXIS, box.getNormalForPoint(p));
        p.set(-300, 100, 100);
        assertEquals(VecD3D.X_AXIS.getInverted(), box.getNormalForPoint(p));
        p.set(100, 100, 300);
        assertEquals(VecD3D.Z_AXIS, box.getNormalForPoint(p));
        p.set(100, 100, -300);
        assertEquals(VecD3D.Z_AXIS.getInverted(), box.getNormalForPoint(p));
    }

    public void testAABBDRayIntersect() {
        AABBD box = AABBD.fromMinMax(new VecD3D(), new VecD3D(100, 100, 100));
        RayD3D r = new RayD3D(new VecD3D(50, 10, 10), new VecD3D(0, 1, 0));
        System.out.println(box.intersectsRay(r, -1000, 1000));
    }

    public void testAABBDSphereD() {
        AABBD box = new AABBD(new VecD3D(100, 0, 0), new VecD3D(20, 20, 20));
        SphereD s = new SphereD(new VecD3D(100, 0, 0), 50);
        assertEquals(box.intersectsSphereD(s), true);
    }

    public void testAABBDTri() {
        AABBD box = new AABBD(new VecD3D(), new VecD3D(100, 100, 100));
        VecD3D a = new VecD3D(-90, 0, 0);
        VecD3D b = new VecD3D(-110, -200, 0);
        VecD3D c = new VecD3D(-110, 200, 0);
        TriangleD3D tri = new TriangleD3D(a, b, c);
        System.out.println(box.intersectsTriangle(tri));
    }

    public void testInclude() {
        AABBD box = AABBD.fromMinMax(new VecD3D(), new VecD3D(100, 100, 100));
        System.out.println(box);
        VecD3D p = new VecD3D(-150, -50, 110);
        box.growToContainPoint(p);
        System.out.println(box.getMin() + " " + box.getMax());
        assertTrue(box.containsPoint(p));
    }

    public void testIsec() {
        AABBD box = AABBD.fromMinMax(new VecD3D(), new VecD3D(100, 100, 100));
        AABBD box2 = AABBD.fromMinMax(new VecD3D(10, 10, 10),
                new VecD3D(80, 80, 80));
        assertTrue(box.intersectsBox(box2));
        assertTrue(box2.intersectsBox(box));
    }

    public void testIsInAABBD() {
        AABBD box = new AABBD(new VecD3D(100, 0, 0), new VecD3D(20, 20, 20));
        ReadonlyVecD3D p = new VecD3D(80, -19.99f, 0);
        assertEquals(p.isInAABBD(box), true);
        assertEquals(new VecD3D(120.01f, 19.99f, 0).isInAABBD(box), false);
    }
}
