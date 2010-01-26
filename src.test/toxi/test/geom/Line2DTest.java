package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.Line2D;
import toxi.geom.Vec2D;
import toxi.geom.Line2D.LineIntersection;

public class Line2DTest extends TestCase {

    public void testIntersection() {
        Line2D l = new Line2D(new Vec2D(), new Vec2D(100, 100));
        Line2D k = new Line2D(new Vec2D(0, 50), new Vec2D(100, 50));
        LineIntersection isec = l.intersectLine(k);
        assertEquals(LineIntersection.Type.INTERSECTING, isec.getType());
        assertEquals(new Vec2D(50, 50), isec.getPos());
        k = l.copy();
        assertEquals(LineIntersection.Type.COINCIDENT, l.intersectLine(k)
                .getType());
        k = new Line2D(new Vec2D(-100, -100), new Vec2D(100, 50));
        assertEquals(LineIntersection.Type.NON_INTERSECTING, l.intersectLine(k)
                .getType());
    }

}
