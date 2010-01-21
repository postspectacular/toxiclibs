package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.Circle;
import toxi.geom.Vec2D;

public class CircleTest extends TestCase {

    private void showPoints(Vec2D[] points) {
        if (points != null) {
            for (Vec2D p : points) {
                System.out.println(p);
            }
        } else {
            System.out.println("<null>");
        }
    }

    public void testCircleCircleIntersection() {
        Circle a = new Circle(100);
        Circle b = new Circle(new Vec2D(200, 100), 200);
        Vec2D[] isec = a.intersectsCircle(b);
        assertTrue(isec != null);
        assertTrue(isec[0].equals(new Vec2D(0, 100)));
        showPoints(isec);
        b.setRadius(100);
        isec = a.intersectsCircle(b);
        assertTrue(isec == null);
        b.setRadius(99).set(0, 0);
        isec = a.intersectsCircle(b);
        assertTrue(isec == null);
        b.x = 1;
        isec = a.intersectsCircle(b);
        assertTrue(isec != null);
        showPoints(isec);
    }
}
