package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.Circle;
import toxi.geom.Polygon2D;
import toxi.geom.ReadonlyVec2D;
import toxi.geom.Vec2D;

public class PolygonTest extends TestCase {

    public void testAreaAndCentroid() {
        Polygon2D p = new Polygon2D();
        p.add(new Vec2D());
        p.add(new Vec2D(1, 0));
        p.add(new Vec2D(1, 1));
        p.add(new Vec2D(0, 1));
        p.add(new Vec2D());
        assertEquals(4, p.getNumVertices());
        float area = p.getArea();
        assertEquals(1f, area);
        ReadonlyVec2D centroid = p.getCentroid();
        assertEquals(new Vec2D(0.5f, 0.5f), centroid);
    }

    public void testCircleArea() {
        float radius = 1;
        int subdiv = 36;
        Polygon2D p = new Circle(radius).toPolygon2D(subdiv);
        float area = p.getArea();
        float area2 = new Circle(radius).getArea();
        float ratio = area / area2;
        assertTrue((1 - ratio) < 0.01);
    }

    public void testClockwise() {
        Polygon2D p = new Circle(50).toPolygon2D(8);
        assertTrue(p.isClockwise());
    }

    public void testContainment() {
        final Vec2D origin = new Vec2D(100, 100);
        Polygon2D p = new Circle(origin, 50).toPolygon2D(8);
        assertTrue(p.containsPoint(origin));
        assertTrue(p.containsPoint(p.vertices.get(0)));
        assertFalse(p.containsPoint(p.vertices.get(3).scale(1.01f)));
    }

    public void testIncreaseVertcount() {
        final Vec2D origin = new Vec2D(100, 100);
        Polygon2D p = new Circle(origin, 50).toPolygon2D(3);
        p.increaseVertexCount(6);
        assertEquals(6, p.getNumVertices());
    }

    public void testReduce() {
        Polygon2D p = new Circle(100).toPolygon2D(30);
        float len = p.vertices.get(0).distanceTo(p.vertices.get(1));
        p.reduceVertices(len * 0.99f);
        assertEquals(30, p.getNumVertices());
        p.reduceVertices(len * 1.5f);
        assertEquals(15, p.getNumVertices());
    }
}
