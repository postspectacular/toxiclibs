package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.AABB;
import toxi.geom.Vec3D;

public class AABBIsecTest extends TestCase {

    public void testInclude() {
        AABB box = AABB.fromMinMax(new Vec3D(), new Vec3D(100, 100, 100));
        System.out.println(box);
        Vec3D p = new Vec3D(-150, -50, 110);
        box.includePoint(p);
        System.out.println(box.getMin() + " " + box.getMax());
        assertTrue(box.containsPoint(p));
    }

    public void testIsec() {
        AABB box = AABB.fromMinMax(new Vec3D(), new Vec3D(100, 100, 100));
        AABB box2 =
                AABB.fromMinMax(new Vec3D(10, 10, 10), new Vec3D(80, 80, 80));
        assertTrue(box.intersectsBox(box2));
        assertTrue(box2.intersectsBox(box));
    }
}
