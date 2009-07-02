package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.Vec3D;
import toxi.math.MathUtils;

public class Vec3DTest extends TestCase {

	public void testSpherical() {
		Vec3D v = new Vec3D(1, 1, 0);
		Vec3D w = v.copy();
		v.toSpherical();
		System.out.println(MathUtils.degrees(v.y) + " "
				+ MathUtils.degrees(v.z));
		v.toCartesian();
		System.out.println(v);
		assertTrue(v.equalsWithTolerance(w, 0.0001f));
	}
}
