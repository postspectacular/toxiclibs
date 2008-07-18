package toxi.test;

import java.util.ArrayList;

import toxi.geom.*;
import toxi.math.MathUtils;
import junit.framework.TestCase;

public class GeomTestSuite extends TestCase {

	public void testReflectRay() {
		SphereIntersectorReflector si=new SphereIntersectorReflector(new Vec3D(0,0,0),10);
		Ray3D r=si.reflectRay(new Ray3D(new Vec3D(0,100,0),new Vec3D(0,-1,0)));
		System.out.println(MathUtils.abs(r.getDirection().y-1));
		assertEquals(MathUtils.abs(r.getDirection().y-1)<0.002, true);
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
		Vec3D p = new Vec3D(80,-19.99f,0);
		assertEquals(p.isInAABB(box), true);
		assertEquals(new Vec3D(120.01f,19.99f,0).isInAABB(box), false);
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
	
	public void testOctree() {
		PointOctree t=new PointOctree(new Vec3D(),100);
		assertEquals(t.addPoint(new Vec3D(0,0,0)),true);
		assertEquals(t.addPoint(new Vec3D(0,100,0)),true);
		assertEquals(t.addPoint(new Vec3D(101,0,0)),false);
		ArrayList points=t.getPointsWithinSphere(new Sphere(new Vec3D(50,0,0),50));
		assertEquals(points.size()==1, true);
		points=t.getPointsWithinBox(new AABB(new Vec3D(50,50,50),new Vec3D(50,50,50)));
		assertEquals(points.size()==2, true);
	}
	
	public void testClosestPoint() {
		Vec3D a=new Vec3D();
		Vec3D b=new Vec3D(100,0,0);
		System.out.println(new Vec3D(50,50,0).closestPointOnLine(a, b));
	}
}
