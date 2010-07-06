package toxi.geom;

import java.util.ArrayList;
import java.util.List;

/**
 * A simplified, rectangle-only version of the Sutherland-Hodgeman polygon
 * clipping algorithm to clip 2D polygons.
 * 
 * More information: http://en.wikipedia.org/wiki/Sutherland-Hodgman_algorithm
 * 
 */
public class SutherlandHodgemanClipper implements PolygonClipper2D {

    protected Rect bounds;

    public SutherlandHodgemanClipper(Rect bounds) {
        this.bounds = bounds;
    }

    public Polygon2D clipPolygon(Polygon2D poly) {
        List<Vec2D> points = new ArrayList<Vec2D>(poly.vertices);
        List<Vec2D> clipped = new ArrayList<Vec2D>();
        points.add(points.get(0));
        for (int edgeID = 0; edgeID < 4; edgeID++) {
            clipped.clear();
            for (int i = 0, num = points.size() - 1; i < num; i++) {
                Vec2D p = points.get(i);
                Vec2D q = points.get(i + 1);
                if (isInsideEdge(p, edgeID)) {
                    if (isInsideEdge(q, edgeID)) {
                        clipped.add(q.copy());
                    } else {
                        clipped.add(getClippedPosOnEdge(edgeID, p, q));
                    }
                    continue;
                }
                if (isInsideEdge(q, edgeID)) {
                    clipped.add(getClippedPosOnEdge(edgeID, p, q));
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
            clipped.clear();
        }
        return new Polygon2D(points);
    }

    /**
     * @return the bounding rect
     */
    public Rect getBounds() {
        return bounds;
    }

    private final Vec2D getClippedPosOnEdge(int edgeID, Vec2D p1, Vec2D p2) {
        switch (edgeID) {
            case 0:
                return new Vec2D(p1.x + ((bounds.y - p1.y) * (p2.x - p1.x))
                        / (p2.y - p1.y), bounds.y);
            case 2:
                float by = bounds.y + bounds.height;
                return new Vec2D(p1.x + ((by - p1.y) * (p2.x - p1.x))
                        / (p2.y - p1.y), by);
            case 1:
                float bx = bounds.x + bounds.width;
                return new Vec2D(bx, p1.y + ((bx - p1.x) * (p2.y - p1.y))
                        / (p2.x - p1.x));

            case 3:
                return new Vec2D(bounds.x, p1.y
                        + ((bounds.x - p1.x) * (p2.y - p1.y)) / (p2.x - p1.x));
            default:
                return null;
        }
    }

    private final boolean isInsideEdge(Vec2D p, int edgeID) {
        switch (edgeID) {
            case 0:
                return p.y >= bounds.y;
            case 2:
                return p.y < bounds.y + bounds.height;
            case 3:
                return p.x >= bounds.x;
            case 1:
                return p.x < bounds.x + bounds.width;
            default:
                return false;
        }
    }

    /**
     * @param bounds
     *            the bounding rect to set
     */
    public void setBounds(Rect bounds) {
        this.bounds = bounds;
    }
}
