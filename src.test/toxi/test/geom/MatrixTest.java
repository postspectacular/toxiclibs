package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.Matrix4x4;
import toxi.geom.Vec3D;
import toxi.math.MathUtils;

public class MatrixTest extends TestCase {

	public void testIdentity() {
		Matrix4x4 m = new Matrix4x4();
		m = m.translate(100, 100, 0);
		m = m.rotateX(MathUtils.HALF_PI);
		m = m.scale(10, 10, 10);
		System.out.println(m);
		Vec3D v = new Vec3D(0, 1, 0);
		Vec3D w = m.apply(v);
		m = m.inverse();
		System.out.println(w);
		System.out.println(m.apply(w));
	}
}