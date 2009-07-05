package toxi.test.geom;

import java.util.ArrayList;

import junit.framework.TestCase;
import toxi.geom.Vec3D;
import toxi.math.MathUtils;

public class Vec3DTest extends TestCase {

	public void testSphericalInstance() {
		Vec3D v = new Vec3D(-1, 1, 1);
		Vec3D w = v.copy();
		v.toSpherical();
		System.out.println(MathUtils.degrees(v.y) + " "
				+ MathUtils.degrees(v.z));
		v.toCartesian();
		System.out.println(v);
		assertTrue(v.equalsWithTolerance(w, 0.0001f));
	}

	public void testSplitSegments() {
		Vec3D a = new Vec3D(0, 0, 0);
		Vec3D b = new Vec3D(100, 0, 0);
		ArrayList<Vec3D> list = Vec3D.splitIntoSegments(a, b, 8, null, true);
		assertEquals(14, list.size());
		// testing adding to existing list and skipping start point
		Vec3D.splitIntoSegments(b, a, 10, list, false);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(i + ": " + list.get(i));
		}
		assertFalse(b.equals(list.get(14)));
		assertEquals(24, list.size());
	}
}
