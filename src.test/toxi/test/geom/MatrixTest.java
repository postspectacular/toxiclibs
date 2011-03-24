package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.Matrix4x4;
import toxi.geom.ReadonlyVec3D;
import toxi.geom.Vec3D;
import toxi.math.MathUtils;

public class MatrixTest extends TestCase {

    public void testInverse() {
        Matrix4x4 m = new Matrix4x4();
        m.translateSelf(100, 100, 0);
        m.rotateX(MathUtils.HALF_PI);
        m.scaleSelf(10, 10, 10);
        System.out.println(m);
        Vec3D v = new Vec3D(0, 1, 0);
        Vec3D w = m.applyTo(v);
        m = m.getInverted();
        ReadonlyVec3D v2 = m.applyTo(w);
        System.out.println(w);
        System.out.println(v2);
        assertTrue(v2.equalsWithTolerance(v, 0.0001f));
    }

    public void testRotate() {
        Matrix4x4 m = new Matrix4x4();
        m.rotateX(MathUtils.HALF_PI);
        Vec3D v = m.applyTo(new Vec3D(0, 1, 0));
        assertTrue(new Vec3D(0, 0, 1).equalsWithTolerance(v, 0.00001f));
        m.identity();
        m.rotateY(MathUtils.HALF_PI);
        v = m.applyTo(new Vec3D(1, 0, 0));
        assertTrue(new Vec3D(0, 0, -1).equalsWithTolerance(v, 0.00001f));
        m.identity();
        m.rotateZ(MathUtils.HALF_PI);
        v = m.applyTo(new Vec3D(1, 0, 0));
        assertTrue(new Vec3D(0, 1, 0).equalsWithTolerance(v, 0.00001f));
        m.identity();
        m.rotateAroundAxis(new Vec3D(0, 1, 0), MathUtils.HALF_PI);
        v = m.applyTo(new Vec3D(1, 0, 0));
        assertTrue(new Vec3D(0, 0, 1).equalsWithTolerance(v, 0.00001f));
    }

    public void testTranslate() {
        Matrix4x4 m = new Matrix4x4();
        m.translateSelf(100, 100, 100);
        assertEquals(new Vec3D(100, 100, 100), m.applyTo(new Vec3D()));
    }
}