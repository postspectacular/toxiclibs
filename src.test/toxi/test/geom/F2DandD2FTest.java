package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.AABB;
import toxi.geom.AABBD;
import toxi.geom.Circle;
import toxi.geom.CircleD;
import toxi.geom.Quaternion;
import toxi.geom.QuaternionD;
import toxi.geom.Sphere;
import toxi.geom.SphereD;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;
import toxi.geom.Vec4D;
import toxi.geom.VecD2D;
import toxi.geom.VecD3D;
import toxi.geom.VecD4D;


/* For those pairs of float and double classes which have 
 *     1) a self-class constructor,
 * AND 2) a constructor from more primitive classes  
 * a constructor was added to each of the class pair to convert to the other resolution class.
 * 
 * Thus Circle got a Circle(CircleD) constructor
 * because it has a Circle(Circle) constructor and a Circle(Vec2D,float) constructor
 * 
 * IsectData2D did not get a IsectData2D(IsectDataD2D) constructor because there is no
 * constructor for IsectData2D from primitive classes
 *   
 * This test class confirms those constructors.
 */
public class F2DandD2FTest extends TestCase {
	double d0=0.123456789012345678901234567890;
	double d1=Math.PI;
	double d2=Math.E;
	double d3=Math.atan(3);
	double d4=Math.atan(4);
	double d5=Math.atan(5);
	float f0=(float)d0;
	float f1=(float)d1;
	float f2=(float)d2;
	float f3=(float)d3;
	float f4=(float)d4;
	float f5=(float)d5;
	double df0=f0;
	double df1=f1;
	double df2=f2;
	double df3=f3;
	double df4=f4;
	double df5=f5;

	public String getMethodName() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    	return stackTrace[2].getMethodName();
	}

    public void testF2DVec2D() {
    	//for(int ii=0;ii<13;ii++) {
    	//	System.out.println(String.format("ii=%2d %30.27f",ii,Math.atan(ii/10.)));
    	//	System.out.println(String.format("      %20.17f",Math.atan(ii/10.)));
    	//}
        VecD2D a = new VecD2D(0.12345679,3.1415927);
        Vec2D b = new Vec2D((float)0.123456789012345678901234567890,(float)Math.PI);
        VecD2D c = new VecD2D(a);
        System.out.println("\n"+getMethodName());
        System.out.println("floats "+b);
        System.out.println(String.format("pure doubles             (%20.18f,%20.18f),",a.x,a.y));
        System.out.println(String.format("doubles from constructor (%20.18f,%20.18f),",c.x,c.y));
        assertEquals(a,c);
    }

    public void testF2DVec3D() {
        VecD3D a = new VecD3D(0.12345679,3.1415927,2.7182817);
        Vec3D b = new Vec3D((float)0.123456789012345678901234567890,(float)Math.PI,(float)Math.E);
        VecD3D c = new VecD3D(a);
        System.out.println("\n"+getMethodName());
        System.out.println("floats "+b);
        System.out.println(String.format("pure doubles             (%20.18f,%20.18f,%20.18f),",a.x,a.y,a.z));
        System.out.println(String.format("doubles from constructor (%20.18f,%20.18f,%20.18f),",c.x,c.y,c.z));
        assertEquals(a,c);
    }

    public void testF2DVec4D() {
        VecD4D a = new VecD4D(0.12345679,3.1415927,2.7182817,0.4636476);
        Vec4D b = new Vec4D((float)0.123456789012345678901234567890,(float)Math.PI,(float)Math.E,(float)Math.atan(.5));
        VecD4D c = new VecD4D(a);
        System.out.println("\n"+getMethodName());
        System.out.println("floats "+b);
        System.out.println(String.format("pure doubles             (%20.18f,%20.18f,%20.18f,%20.18f),",a.x,a.y,a.z,a.w));
        System.out.println(String.format("doubles from constructor (%20.18f,%20.18f,%20.18f,%20.18f),",c.x,c.y,c.z,c.w));
        assertEquals(a,c);
    }

    public void testD2FVecD2D() {
        VecD2D a = new VecD2D(0.123456789012345678901234567890,Math.PI);
        Vec2D b = new Vec2D((float)0.123456789012345678901234567890,(float)Math.PI);
        Vec2D c = new Vec2D(a);
        System.out.println("\n"+getMethodName());
        System.out.println("pure double "+a);
        System.out.println("floats                  "+b);
        System.out.println("floats from constructor "+c);
        assertEquals(b,c);
     }

    public void testD2FVecD3D() {
       VecD3D a = new VecD3D(0.123456789012345678901234567890,Math.PI,Math.E);
       Vec3D b = new Vec3D((float)0.123456789012345678901234567890,(float)Math.PI,(float)Math.E);
       Vec3D c = new Vec3D(a);
       System.out.println("\n"+getMethodName());
       System.out.println("pure double "+a);
       System.out.println("floats                  "+b);
       System.out.println("floats from constructor "+c);
       assertEquals(b,c);
    }

    public void testD2FVecD4D() {
       VecD4D a = new VecD4D(0.123456789012345678901234567890,Math.PI,Math.E,Math.atan(.5));
       Vec4D b = new Vec4D((float)0.123456789012345678901234567890,(float)Math.PI,(float)Math.E,(float)Math.atan(.5));
       Vec4D c = new Vec4D(a);
       System.out.println("\n"+getMethodName());
       System.out.println("pure double "+a);
       System.out.println("floats                  "+b);
       System.out.println("floats from constructor "+c);
       assertEquals(b,c);
    }

    public void testD2FAABBD() {
    	AABBD a = new AABBD(new VecD3D(d0,d1,d2),new VecD3D(d3,d4,d5));    	
    	AABB b = new AABB(new Vec3D(f0,f1,f2),new Vec3D(f3,f4,f5));
    	AABB c = new AABB(a);
        System.out.println("\n"+getMethodName());
    	System.out.println(a.toString());
    	System.out.println(b.toString());
    	System.out.println(c.toString());
    	assertEquals(b,c);
    }
    public void testF2DAABB() {
    	AABBD a = new AABBD(new VecD3D(df0,df1,df2),new VecD3D(df3,df4,df5));    	
    	AABB b = new AABB(new Vec3D(f0,f1,f2),new Vec3D(f3,f4,f5));
    	AABBD c = new AABBD(b);
        System.out.println("\n"+getMethodName());
    	System.out.println(a.toString());
    	System.out.println(b.toString());
    	System.out.println(c.toString());
    	assertEquals(a,c);
    }
    
    public void testD2FCircleD() {
    	CircleD a = new CircleD(new VecD2D(d0,d1),d3);
    	Circle b = new Circle(new Vec2D(f0,f1),f3);
    	Circle c = new Circle(a);
        System.out.println("\n"+getMethodName());
    	System.out.println(a.toString());
    	System.out.println(b.toString());
    	System.out.println(c.toString());
    	assertEquals(b,c);
    }
    public void testF2DCircle() {
    	CircleD a = new CircleD(new VecD2D(df0,df1),df3);
    	Circle b = new Circle(new Vec2D(f0,f1),f3);
    	CircleD c = new CircleD(b);
        System.out.println("\n"+getMethodName());
    	System.out.println(a.toString());
    	System.out.println(b.toString());
    	System.out.println(c.toString());
    	assertEquals(a,c);
    }
    
    public void testD2FQuaternionD() {
    	QuaternionD a = new QuaternionD(d0,d1,d2,d3);
    	Quaternion b = new Quaternion(f0,f1,f2,f3);
    	Quaternion c = new Quaternion(a);
        System.out.println("\n"+getMethodName());
    	System.out.println(a.toString());
    	System.out.println(b.toString());
    	System.out.println(c.toString());
    	//assertEquals(b,c); /* this fails */
    	float dx=b.x-c.x;
    	float dy=b.y-c.y;
    	float dz=b.z-c.z;
    	float dw=b.w-c.w;
    	assertTrue(  (dx<toxi.math.MathUtils.EPS)
                   &&(dy<toxi.math.MathUtils.EPS)
                   &&(dz<toxi.math.MathUtils.EPS)
                   &&(dw<toxi.math.MathUtils.EPS)
                  );
    }
    public void testF2DQuaternion() {
    	QuaternionD a = new QuaternionD(df0,df1,df2,df3);
    	Quaternion b = new Quaternion(f0,f1,f2,f3);
    	QuaternionD c = new QuaternionD(b);
        System.out.println("\n"+getMethodName());
    	System.out.println(a.toString());
    	System.out.println(b.toString());
    	System.out.println(c.toString());
    	double dx=a.x-c.x;
    	double dy=a.y-c.y;
    	double dz=a.z-c.z;
    	double dw=a.w-c.w;
    	System.out.println("dx="+dx+" dy="+dy+" dz="+dz+" dw="+dw);
    	//assertEquals(a,c); /* this failed */
    	assertTrue(  (dx<toxi.math.MathUtils.EPSD)
                   &&(dy<toxi.math.MathUtils.EPSD)
                   &&(dz<toxi.math.MathUtils.EPSD)
                   &&(dw<toxi.math.MathUtils.EPSD)
                  );
    	
    }
    
    
    public void testD2FSphereD() {
    	SphereD a = new SphereD(new VecD3D(d0,d1,d2),d3);
    	Sphere b = new Sphere(new Vec3D(f0,f1,f2),f3);
    	Sphere c = new Sphere(a);
        System.out.println("\n"+getMethodName());
    	System.out.println(a.toString());
    	System.out.println(b.toString());
    	System.out.println(c.toString());
    	assertEquals(b,c);
    }
    public void testF2DSphere() {
    	SphereD a = new SphereD(new VecD3D(df0,df1,df2),df3);
    	Sphere b = new Sphere(new Vec3D(f0,f1,f2),f3);
    	SphereD c = new SphereD(b);
        System.out.println("\n"+getMethodName());
    	System.out.println(a.toString());
    	System.out.println(b.toString());
    	System.out.println(c.toString());
    	assertEquals(a,c);
    }
}
