package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.Origin3D;
import toxi.geom.Vec3D;

public class Origin3DTest extends TestCase {

    public void testViewConstruct() {
        Origin3D o = new Origin3D(new Vec3D(0, -100, 0), new Vec3D(0, 1, 0));
        System.out.println(o.xAxis);
        System.out.println(o.yAxis);
        System.out.println(o.zAxis);
        System.out.println(o.xAxis.angleBetween(o.zAxis));
    }
}
