package toxi.geom;

import java.util.ArrayList;
import java.util.List;

/**
 * Standard multi-segment bezier curve implementation with optional automatic
 * handle alignment between segments. Also provides curve tangent calculation
 * feature. Can be used to create closed curves. Usage of this class is very
 * similar to {@link Spline3D}.
 */
public class BezierCurve3D {

    public static Vec3D computePointInSegment(Vec3D a, Vec3D b, Vec3D c,
            Vec3D d, float t) {
        float it = 1.0f - t;
        float it2 = it * it;
        float t2 = t * t;
        return a.scale(it2 * it).addSelf(b.scale(3 * t * it2))
                .addSelf(c.scale(3 * t2 * it)).addSelf(d.scale(t2 * t));
    }

    public static Vec3D computeTangentInSegment(Vec3D a, Vec3D b, Vec3D c,
            Vec3D d, float t) {
        float t2 = t * t;
        float x = (3 * t2 * (-a.x + 3 * b.x - 3 * c.x + d.x) + 6 * t
                * (a.x - 2 * b.x + c.x) + 3 * (-a.x + b.x));
        float y = (3 * t2 * (-a.y + 3 * b.y - 3 * c.y + d.y) + 6 * t
                * (a.y - 2 * b.y + c.y) + 3 * (-a.y + b.y));
        float z = (3 * t2 * (-a.z + 3 * b.z - 3 * c.z + d.z) + 6 * t
                * (a.z - 2 * b.z + c.z) + 3 * (-a.z + b.z));
        return new Vec3D(x, y, z).normalize();
    }

    private List<Vec3D> points;

    public BezierCurve3D() {
        points = new ArrayList<Vec3D>();
    }

    public BezierCurve3D(List<Vec3D> points) {
        this.points = points;
    }

    public BezierCurve3D add(Vec3D p) {
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
            Vec3D c;
            if (id == 0 && isClosed()) {
                c = points.get(points.size() - 2);
            } else {
                c = points.get(id - 1);
            }
            Vec3D d = points.get(id);
            Vec3D e = points.get(id + 1);
            Vec3D cd = d.sub(c);
            Vec3D de = e.sub(d);
            Vec3D cd2 = cd.interpolateTo(de, 0.5f);
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
     * @return list of Vec3Ds
     */
    public LineStrip3D toLineStrip3D(int res) {
        LineStrip3D strip = new LineStrip3D();
        int i = 0;
        int maxRes = res;
        for (int num = points.size(); i < num - 3; i += 3) {
            Vec3D a = points.get(i);
            Vec3D b = points.get(i + 1);
            Vec3D c = points.get(i + 2);
            Vec3D d = points.get(i + 3);
            if (i + 3 > num - 3) {
                maxRes++;
            }
            for (int t = 0; t < maxRes; t++) {
                strip.add(computePointInSegment(a, b, c, d, (float) t / res));
            }
        }
        return strip;
    }

}
