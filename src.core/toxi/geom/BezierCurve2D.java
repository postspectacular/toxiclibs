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

    public Vec2D computePointInSegment(Vec2D a, Vec2D b, Vec2D c, Vec2D d,
            float t) {
        float invT = 1.0f - t;
        float invT2 = invT * invT;
        float invT3 = invT2 * invT;
        float t2 = t * t;
        float t3 = t2 * t;
        float x = a.x * invT3 + 3 * b.x * t * invT2 + 3 * c.x * t2 * invT + d.x
                * t3;
        float y = a.y * invT3 + 3 * b.y * t * invT2 + 3 * c.y * t2 * invT + d.y
                * t3;
        return new Vec2D(x, y);
    }

    public Vec2D computeTangentInSegment(Vec2D a, Vec2D b, Vec2D c, Vec2D d,
            float t) {
        float t2 = t * t;
        float x = (3 * t2 * (-a.x + 3 * b.x - 3 * c.x + d.x) + 6 * t
                * (a.x - 2 * b.x + c.x) + 3 * (-a.x + b.x));
        float y = (3 * t2 * (-a.y + 3 * b.y - 3 * c.y + d.y) + 6 * t
                * (a.y - 2 * b.y + c.y) + 3 * (-a.y + b.y));
        return new Vec2D(x, y).normalize();
    }

    /**
     * Computes a list of intermediate curve points for all segments. For each
     * curve segment the given number of points will be produced.
     * 
     * @param res
     *            number of points per segment
     * @return list of Vec2Ds
     */
    public List<Vec2D> computeVertices(int res) {
        List<Vec2D> vertices = new ArrayList<Vec2D>();
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
                vertices.add(computePointInSegment(a, b, c, d, (float) t / res));
            }
        }
        return vertices;
    }

    /**
     * @return true, if the curve is closed. I.e. the first and last control
     *         point coincide.
     */
    public boolean isClosed() {
        return points.get(0).equals(points.get(points.size() - 1));
    }

    public Polygon2D toPolygon2D(int res) {
        Polygon2D poly = new Polygon2D(computeVertices(res));
        if (isClosed()) {
            poly.vertices.remove(poly.vertices.get(poly.vertices.size() - 1));
        }
        return poly;
    }
}
