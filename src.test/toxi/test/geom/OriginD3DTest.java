package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.OriginD3D;
import toxi.geom.VecD3D;

public class OriginD3DTest extends TestCase {

    public void testViewConstruct() {
        OriginD3D o = new OriginD3D(new VecD3D(0, -100, 0), new VecD3D(0, 1, 0));
        System.out.println(o.xAxis);
        System.out.println(o.yAxis);
        System.out.println(o.zAxis);
        System.out.println(o.xAxis.angleBetween(o.zAxis));
    }
}
