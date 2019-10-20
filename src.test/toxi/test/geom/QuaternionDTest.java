package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.Matrix4x4;
import toxi.geom.QuaternionD;
import toxi.geom.ReadonlyVecD3D;
import toxi.geom.VecD3D;
import toxi.math.MathUtils;

public class QuaternionDTest extends TestCase {

    public void testCreateFromAxisAngle() {
        ReadonlyVecD3D axis = new VecD3D(100, 100, 100);
        double angle = MathUtils.PI * 1.5f;
        QuaternionD a = QuaternionD.createFromAxisAngle(axis, angle);
        assertEquals(MathUtils.sin(-MathUtils.QUARTER_PI), a.w);
        double[] reverse = a.toAxisAngle();
        VecD3D revAxis = new VecD3D(reverse[1], reverse[2], reverse[3]);
        assertTrue(axis.getNormalized().equalsWithTolerance(revAxis, 0.01f));
        assertTrue(MathUtils.abs(angle - reverse[0]) < 0.01);
    }

    public void testEuler() {
        QuaternionD q = QuaternionD.createFromEuler(MathUtils.QUARTER_PI,
                MathUtils.QUARTER_PI, 0);
        System.out.println(q);
        double[] reverse = q.toAxisAngle();
        System.out.println("toAxisAngle():");
        for (double f : reverse) {
            System.out.println(f);
        }
    }

    public void testMatrixRoundtrip() {
        for (int i = 0; i < 1000; i++) {
            QuaternionD q = QuaternionD.createFromAxisAngle(VecD3D.randomVector(),
                    MathUtils.random(MathUtils.TWO_PI)).normalize();
            Matrix4x4 m = q.toMatrix4x4();
            QuaternionD q2 = QuaternionD.createFromMatrix(m);
            VecD3D p = VecD3D.randomVector();
            VecD3D p2 = p.copy();
            q.applyTo(p);
            q2.applyTo(p2);
            // doubles are not very kind to round tripping
            // hence quite large epsilon
            assertTrue(p.equalsWithTolerance(p2, 0.0001f));
        }
    }

    public void testSlerp() {
        QuaternionD a = new QuaternionD(0, new VecD3D(0, 0, -1));
        QuaternionD b = new QuaternionD(0, new VecD3D(0, 0, 1));
        QuaternionD c = a.interpolateTo(b, 0.05f);
        System.out.println(c);
    }
}
