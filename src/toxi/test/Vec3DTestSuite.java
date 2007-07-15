package toxi.test;

import toxi.geom.Ray3D;
import toxi.geom.SphereIntersectorReflector;
import toxi.geom.Vec3D;
import junit.framework.TestCase;

public class Vec3DTestSuite extends TestCase {

	public void testReflectRay() {
		SphereIntersectorReflector si=new SphereIntersectorReflector(new Vec3D(0,0,0),11);
		Ray3D r=si.reflectRay(new Ray3D(new Vec3D(0,100,0),new Vec3D(0,-1,0)));
		System.out.println(r);
	}

}
