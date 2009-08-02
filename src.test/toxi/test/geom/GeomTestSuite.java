package toxi.test.geom;

import java.util.ArrayList;

import junit.framework.TestCase;
import toxi.geom.AABB;
import toxi.geom.PointOctree;
import toxi.geom.Quaternion;
import toxi.geom.Ray3D;
import toxi.geom.Rect;
import toxi.geom.Sphere;
import toxi.geom.SphereIntersectorReflector;
import toxi.geom.Vec3D;
import toxi.math.MathUtils;

public class GeomTestSuite extends TestCase {

	public void testAABB2AABB() {
		AABB box = new AABB(new Vec3D(100, 0, 0), new Vec3D(20, 20, 20));
		AABB b2 = new AABB(new Vec3D(100, 30.1f, 0), new Vec3D(10, 10, 10));
		assertEquals(box.intersectsBox(b2), false);
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
		Vec3D isec = c.closestPointOnLine(a, b);
		assertEquals(MathUtils.abs(isec.x - c.x) < 0.5, true);
		c = new Vec3D(-50, -50, 0);
		isec = c.closestPointOnLine(a, b);
		assertEquals(isec.equals(a), true);
	}

	public void testEuler() {
		Quaternion a = new Quaternion(MathUtils.HALF_PI, Vec3D.Z_AXIS);
	}

	public void testIsInAABB() {
		AABB box = new AABB(new Vec3D(100, 0, 0), new Vec3D(20, 20, 20));
		Vec3D p = new Vec3D(80, -19.99f, 0);
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
		assertEquals(t.addPoint(new Vec3D(0, 0, 0)), true);
		assertEquals(t.addPoint(new Vec3D(0, 100, 0)), true);
		assertEquals(t.addPoint(new Vec3D(101, 0, 0)), false);
		ArrayList<Vec3D> points = t.getPointsWithinSphere(new Sphere(new Vec3D(
				50, 0, 0), 50));
		assertEquals(points.size() == 1, true);
		points = t.getPointsWithinBox(new AABB(new Vec3D(50, 50, 50),
				new Vec3D(50, 50, 50)));
		assertEquals(points.size() == 2, true);
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
		SphereIntersectorReflector si = new SphereIntersectorReflector(
				new Vec3D(0, 0, 0), 10);
		Ray3D r = si.reflectRay(new Ray3D(new Vec3D(100, 100, 0), new Vec3D(-1,
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
