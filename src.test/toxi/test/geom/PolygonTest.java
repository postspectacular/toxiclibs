package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.Circle;
import toxi.geom.Polygon2D;
import toxi.geom.Vec2D;
import toxi.math.MathUtils;

public class PolygonTest extends TestCase {

    public void testAreaAndCentroid() {
        Polygon2D p = new Polygon2D();
        p.add(new Vec2D());
        p.add(new Vec2D(1, 0));
        p.add(new Vec2D(1, 1));
        p.add(new Vec2D(0, 1));
        p.add(new Vec2D());
        assertEquals(4, p.getNumPoints());
        float area = p.getArea();
        assertEquals(1f, area);
        Vec2D centroid = p.getCentroid();
        assertEquals(new Vec2D(0.5f, 0.5f), centroid);
    }

    public void testCircleArea() {
        Polygon2D p = new Polygon2D();
        float radius = 1;
        int subdiv = 36;
        for (int i = 0; i < subdiv; i++) {
            p.add(Vec2D.fromTheta(i * MathUtils.TWO_PI / subdiv)
                    .scaleSelf(radius));
        }
        float area = p.getArea();
        float area2 = new Circle(radius).getArea();
        float ratio = area / area2;
        assertTrue(ratio > 0.99 && ratio < 1);
    }
}
