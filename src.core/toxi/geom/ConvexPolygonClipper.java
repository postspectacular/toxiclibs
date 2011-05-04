package toxi.geom;

import java.util.ArrayList;
import java.util.List;

public class ConvexPolygonClipper implements PolygonClipper2D {

    protected Polygon2D bounds;
    protected Vec2D boundsCentroid;

    public ConvexPolygonClipper(Polygon2D bounds) {
        setBounds(bounds);
    }

    public Polygon2D clipPolygon(Polygon2D poly) {
        List<Vec2D> points = new ArrayList<Vec2D>(poly.vertices);
        List<Vec2D> clipped = new ArrayList<Vec2D>();
        points.add(points.get(0));
        for (Line2D clipEdge : bounds.getEdges()) {
            clipped.clear();
            float sign = clipEdge.classifyPoint(boundsCentroid);
            for (int i = 0, num = points.size() - 1; i < num; i++) {
                Vec2D p = points.get(i);
                Vec2D q = points.get(i + 1);
                if (clipEdge.classifyPoint(p) == sign) {
                    if (clipEdge.classifyPoint(q) == sign) {
                        clipped.add(q.copy());
                    } else {
                        clipped.add(getClippedPosOnEdge(clipEdge, p, q));
                    }
                    continue;
                }
                if (clipEdge.classifyPoint(q) == sign) {
                    clipped.add(getClippedPosOnEdge(clipEdge, p, q));
                    clipped.add(q.copy());
                }
            }
            if (clipped.size() > 0
                    && clipped.get(0) != clipped.get(clipped.size() - 1)) {
                clipped.add(clipped.get(0));
            }
            List<Vec2D> t = points;
            points = clipped;
            clipped = t;
        }
        return new Polygon2D(points).removeDuplicates(0.001f);
    }

    public Polygon2D getBounds() {
        return bounds;
    }

    protected Vec2D getClippedPosOnEdge(Line2D clipEdge, Vec2D p, Vec2D q) {
        return clipEdge.intersectLine(new Line2D(p, q)).getPos();
    }

    protected boolean isKnownVertex(List<Vec2D> list, Vec2D q) {
        for (Vec2D p : list) {
            if (p.equalsWithTolerance(q, 0.001f)) {
                return true;
            }
        }
        return false;
    }

    public void setBounds(Polygon2D bounds) {
        this.bounds = bounds;
        this.boundsCentroid = bounds.getCentroid();
    }
}
