package toxi.geom;

import java.util.ArrayList;
import java.util.List;

/**
 * Standard multi-segment bezier curve implementation with optional automatic
 * handle alignment between segments. Can be used to create closed curves which
 * can then be converted into {@link Polygon2D} instances. Also provides curve
 * tangent calculation feature. Usage of this class is very similar to
 * {@link Spline2D}.
 */
public class BezierCurve2D {

    public static Vec2D computePointInSegment(Vec2D a, Vec2D b, Vec2D c,
            Vec2D d, float t) {
        float it = 1.0f - t;
        float it2 = it * it;
        float t2 = t * t;
        return a.scale(it2 * it).addSelf(b.scale(3 * t * it2))
                .addSelf(c.scale(3 * t2 * it)).addSelf(d.scale(t2 * t));
    }

    public static Vec2D computeTangentInSegment(Vec2D a, Vec2D b, Vec2D c,
            Vec2D d, float t) {
        float t2 = t * t;
        float x = (3 * t2 * (-a.x + 3 * b.x - 3 * c.x + d.x) + 6 * t
                * (a.x - 2 * b.x + c.x) + 3 * (-a.x + b.x));
        float y = (3 * t2 * (-a.y + 3 * b.y - 3 * c.y + d.y) + 6 * t
                * (a.y - 2 * b.y + c.y) + 3 * (-a.y + b.y));
        return new Vec2D(x, y).normalize();
    }

    private List<Vec2D> points;

    public BezierCurve2D() {
        points = new ArrayList<Vec2D>();
    }

    public BezierCurve2D(List<Vec2D> points) {
        this.points = points;
    }

    public BezierCurve2D add(Vec2D p) {
        points.add(p);
        return this;
    }

    public void alignAllHandles() {
        for (int i = 0, num = points.size() - 1; i < num; i += 3) {
            alignHandlesForPoint(i);
        }
    }

    public void alignHandlesForPoint(int id) {
        if (id < points.size() - 1) {
            Vec2D c;
            if (id == 0 && isClosed()) {
                c = points.get(points.size() - 2);
            } else {
                c = points.get(id - 1);
            }
            Vec2D d = points.get(id);
            Vec2D e = points.get(id + 1);
            Vec2D cd = d.sub(c);
            Vec2D de = e.sub(d);
            Vec2D cd2 = cd.interpolateTo(de, 0.5f);
            c.set(d.sub(cd2));
            e.set(d.add(de.interpolateToSelf(cd, 0.5f)));
        } else {
            throw new IllegalArgumentException("invalid point index");
        }
    }

    /**
     * @return true, if the curve is closed. I.e. the first and last control
     *         point coincide.
     */
    public boolean isClosed() {
        return points.get(0).equals(points.get(points.size() - 1));
    }

    /**
     * Computes a list of intermediate curve points for all segments. For each
     * curve segment the given number of points will be produced.
     * 
     * @param res
     *            number of points per segment
     * @return list of Vec2Ds
     */
    public LineStrip2D toLineStrip2D(int res) {
        LineStrip2D strip = new LineStrip2D();
        int i = 0;
        int maxRes = res;
        for (int num = points.size(); i < num - 3; i += 3) {
            Vec2D a = points.get(i);
            Vec2D b = points.get(i + 1);
            Vec2D c = points.get(i + 2);
            Vec2D d = points.get(i + 3);
            if (i + 3 > num - 3) {
                maxRes++;
            }
            for (int t = 0; t < maxRes; t++) {
                strip.add(computePointInSegment(a, b, c, d, (float) t / res));
            }
        }
        return strip;
    }

    public Polygon2D toPolygon2D(int res) {
        Polygon2D poly = new Polygon2D(toLineStrip2D(res).getVertices());
        if (isClosed()) {
            poly.vertices.remove(poly.vertices.get(poly.vertices.size() - 1));
        }
        return poly;
    }
}
