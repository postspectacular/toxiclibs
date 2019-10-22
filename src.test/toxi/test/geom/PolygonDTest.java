package toxi.test.geom;

import junit.framework.TestCase;
import toxi.geom.CircleD;
import toxi.geom.PolygonD2D;
import toxi.geom.ReadonlyVecD2D;
import toxi.geom.VecD2D;

public class PolygonDTest extends TestCase {

    public void testAreaAndCentroid() {
        PolygonD2D p = new PolygonD2D();
        p.add(new VecD2D());
        p.add(new VecD2D(1, 0));
        p.add(new VecD2D(1, 1));
        p.add(new VecD2D(0, 1));
        p.add(new VecD2D());
        assertEquals(4, p.getNumVertices());
        double area = p.getArea();
        assertEquals(1f, area);
        ReadonlyVecD2D centroid = p.getCentroid();
        assertEquals(new VecD2D(0.5f, 0.5f), centroid);
    }

    public void testCircleDArea() {
        double radius = 1;
        int subdiv = 36;
        PolygonD2D p = new CircleD(radius).toPolygonD2D(subdiv);
        double area = p.getArea();
        double area2 = new CircleD(radius).getArea();
        double ratio = area / area2;
        assertTrue((1 - ratio) < 0.01);
    }

    public void testClockwise() {
        PolygonD2D p = new CircleD(50).toPolygonD2D(8);
        assertTrue(p.isClockwise());
    }

    public void testContainment() {
        final VecD2D origin = new VecD2D(100, 100);
        PolygonD2D p = new CircleD(origin, 50).toPolygonD2D(8);
        assertTrue(p.containsPoint(origin));
        assertTrue(p.containsPoint(p.vertices.get(0)));
        assertFalse(p.containsPoint(p.vertices.get(3).scale(1.01f)));
    }

    public void testIncreaseVertcount() {
        final VecD2D origin = new VecD2D(100, 100);
        PolygonD2D p = new CircleD(origin, 50).toPolygonD2D(3);
        p.increaseVertexCount(6);
        assertEquals(6, p.getNumVertices());
    }

    public void testReduce() {
        PolygonD2D p = new CircleD(100).toPolygonD2D(30);
        double len = p.vertices.get(0).distanceTo(p.vertices.get(1));
        p.reduceVertices(len * 0.99f);
        assertEquals(30, p.getNumVertices());
        p.reduceVertices(len * 1.5f);
        assertEquals(15, p.getNumVertices());
    }
}
