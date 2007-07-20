package toxi.test;

import toxi.geom.*;
import toxi.math.FastMath;
import junit.framework.TestCase;

public class GeomTestSuite extends TestCase {

	public void testReflectRay() {
		SphereIntersectorReflector si=new SphereIntersectorReflector(new Vec3D(0,0,0),11);
		Ray3D r=si.reflectRay(new Ray3D(new Vec3D(0,100,0),new Vec3D(0,-1,0)));
		assertEquals(FastMath.abs(r.getDirection().y-1)<0.001, true);
	}

	public void testIsInSphere() {
		Vec3D p=new Vec3D(0,-10,0);
		Sphere s=new Sphere(new Vec3D(),10);
		assertEquals(p.isInSphere(s), true);
		p.set(0,10.1f,0);
		assertEquals(p.isInSphere(s), false);
	}
	
	public void testIsInAABB() {
		AABB box=new AABB(new Vec3D(100,0,0),new Vec3D(20,20,20));
		Vec3D p = new Vec3D(80,-15,0);
		assertEquals(p.isInAABB(box), true);
	}
	
	public void testAABBSphere() {
		AABB box=new AABB(new Vec3D(100,0,0),new Vec3D(20,20,20));
		Sphere s=new Sphere(new Vec3D(100,0,0),50);
		assertEquals(box.intersectsSphere(s),true);
	}
	
	public void testAABB2AABB() {
		AABB box=new AABB(new Vec3D(100,0,0),new Vec3D(20,20,20));
		AABB b2=new AABB(new Vec3D(100,30.1f,0),new Vec3D(10,10,10));
		assertEquals(box.intersectsBox(b2),false);
	}
}
