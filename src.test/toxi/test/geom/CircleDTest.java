package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.CircleD;
import toxi.geom.VecD2D;

public class CircleDTest extends TestCase {

    private void showPoints(VecD2D[] points) {
        if (points != null) {
            for (VecD2D p : points) {
                System.out.println(p);
            }
        } else {
            System.out.println("<null>");
        }
    }

    public void testCircleDCircleDIntersection() {
        CircleD a = new CircleD(100);
        CircleD b = new CircleD(new VecD2D(200, 100), 200);
        VecD2D[] isec = a.intersectsCircleD(b);
        assertTrue(isec != null);
        assertTrue(isec[0].equals(new VecD2D(0, 100)));
        showPoints(isec);
        b.setRadius(100);
        isec = a.intersectsCircleD(b);
        assertTrue(isec == null);
        b.setRadius(99).set(0, 0);
        isec = a.intersectsCircleD(b);
        assertTrue(isec == null);
        b.x = 1;
        isec = a.intersectsCircleD(b);
        assertTrue(isec != null);
        showPoints(isec);
    }
}
