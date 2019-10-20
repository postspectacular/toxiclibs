package toxi.geom;

import java.util.ArrayList;
import java.util.List;

/**
 * Standard multi-segment bezier curve implementation with optional automatic
 * handle alignment between segments. Also provides curve tangent calculation
 * feature. Can be used to create closed curves. Usage of this class is very
 * similar to {@link Spline3D}.
 */
public class BezierCurveD3D {

    public static VecD3D computePointInSegment(VecD3D a, VecD3D b, VecD3D c,
            VecD3D d, double t) {
        double it = 1.0f - t;
        double it2 = it * it;
        double t2 = t * t;
        return a.scale(it2 * it).addSelf(b.scale(3 * t * it2))
                .addSelf(c.scale(3 * t2 * it)).addSelf(d.scale(t2 * t));
    }

    public static VecD3D computeTangentInSegment(VecD3D a, VecD3D b, VecD3D c,
            VecD3D d, double t) {
        double t2 = t * t;
        double x = (3 * t2 * (-a.x + 3 * b.x - 3 * c.x + d.x) + 6 * t
                * (a.x - 2 * b.x + c.x) + 3 * (-a.x + b.x));
        double y = (3 * t2 * (-a.y + 3 * b.y - 3 * c.y + d.y) + 6 * t
                * (a.y - 2 * b.y + c.y) + 3 * (-a.y + b.y));
        double z = (3 * t2 * (-a.z + 3 * b.z - 3 * c.z + d.z) + 6 * t
                * (a.z - 2 * b.z + c.z) + 3 * (-a.z + b.z));
        return new VecD3D(x, y, z).normalize();
    }

    private List<VecD3D> points;

    public BezierCurveD3D() {
        points = new ArrayList<VecD3D>();
    }

    public BezierCurveD3D(List<VecD3D> points) {
        this.points = points;
    }

    public BezierCurveD3D add(VecD3D p) {
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
            VecD3D c;
            if (id == 0 && isClosed()) {
                c = points.get(points.size() - 2);
            } else {
                c = points.get(id - 1);
            }
            VecD3D d = points.get(id);
            VecD3D e = points.get(id + 1);
            VecD3D cd = d.sub(c);
            VecD3D de = e.sub(d);
            VecD3D cd2 = cd.interpolateTo(de, 0.5f);
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
     * @return list of VecD3Ds
     */
    public LineStripD3D toLineStripD3D(int res) {
        LineStripD3D strip = new LineStripD3D();
        int i = 0;
        int maxRes = res;
        for (int num = points.size(); i < num - 3; i += 3) {
            VecD3D a = points.get(i);
            VecD3D b = points.get(i + 1);
            VecD3D c = points.get(i + 2);
            VecD3D d = points.get(i + 3);
            if (i + 3 > num - 3) {
                maxRes++;
            }
            for (int t = 0; t < maxRes; t++) {
                strip.add(computePointInSegment(a, b, c, d,  t / res));
            }
        }
        return strip;
    }

}
