package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.Quaternion;
import toxi.geom.ReadonlyVec3D;
import toxi.geom.Vec3D;
import toxi.math.MathUtils;

public class QuaternionTest extends TestCase {

    public void testCreateFromAxisAngle() {
        ReadonlyVec3D axis = new Vec3D(100, 100, 100);
        float angle = MathUtils.PI * 1.5f;
        Quaternion a = Quaternion.createFromAxisAngle(axis, angle);
        assertEquals(MathUtils.sin(-MathUtils.QUARTER_PI), a.w);
        float[] reverse = a.toAxisAngle();
        Vec3D revAxis = new Vec3D(reverse[1], reverse[2], reverse[3]);
        assertTrue(axis.getNormalized().equalsWithTolerance(revAxis, 0.01f));
        assertTrue(MathUtils.abs(angle - reverse[0]) < 0.01);
    }

    public void testEuler() {
        Quaternion q = Quaternion.createFromEuler(MathUtils.QUARTER_PI,
                MathUtils.QUARTER_PI, 0);
        System.out.println(q);
        float[] reverse = q.toAxisAngle();
        System.out.println("toAxisAngle():");
        for (float f : reverse) {
            System.out.println(f);
        }
    }

    public void testSlerp() {
        Quaternion a = new Quaternion(0, new Vec3D(0, 0, -1));
        Quaternion b = new Quaternion(0, new Vec3D(0, 0, 1));
        Quaternion c = a.interpolateTo(b, 0.05f);
        System.out.println(c);
    }
}
