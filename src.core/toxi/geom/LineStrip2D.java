package toxi.geom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LineStrip2D {

    public List<Vec2D> vertices;
    protected float[] arcLenIndex;

    public LineStrip2D() {
        this.vertices = new ArrayList<Vec2D>();
    }

    public LineStrip2D(Collection<? extends Vec2D> vertices) {
        this.vertices = new ArrayList<Vec2D>(vertices);
    }

    public LineStrip2D add(ReadonlyVec2D p) {
        vertices.add(p.copy());
        return this;
    }

    public LineStrip2D add(Vec2D p) {
        vertices.add(p);
        return this;
    }

    /**
     * Computes a list of points along the spline which are uniformly separated
     * by the given step distance.
     * 
     * @param step
     * @return point list
     */
    public List<Vec2D> getDecimatedVertices(float step) {
        return getDecimatedVertices(step, true);
    }

    /**
     * Computes a list of points along the spline which are close to uniformly
     * separated by the given step distance. The uniform distribution is only an
     * approximation and is based on the estimated arc length of the polyline.
     * The distance between returned points might vary in places, especially if
     * there're sharp angles between line segments.
     * 
     * @param step
     * @param doAddFinalVertex
     *            true, if the last vertex computed by
     *            {@link #computeVertices(int)} should be added regardless of
     *            its distance.
     * @return point list
     */
    public List<Vec2D> getDecimatedVertices(float step, boolean doAddFinalVertex) {
        ArrayList<Vec2D> uniform = new ArrayList<Vec2D>();
        if (vertices.size() < 3) {
            if (vertices.size() == 2) {
                new Line2D(vertices.get(0), vertices.get(1)).splitIntoSegments(
                        uniform, step, true);
                if (!doAddFinalVertex) {
                    uniform.remove(uniform.size() - 1);
                }
            } else {
                return null;
            }
        }
        float arcLen = getEstimatedArcLength();
        double delta = step / arcLen;
        int currIdx = 0;
        for (double t = 0; t < 1.0; t += delta) {
            double currT = t * arcLen;
            while (currT >= arcLenIndex[currIdx]) {
                currIdx++;
            }
            ReadonlyVec2D p = vertices.get(currIdx - 1);
            ReadonlyVec2D q = vertices.get(currIdx);
            float frac =
                    (float) ((currT - arcLenIndex[currIdx - 1]) / (arcLenIndex[currIdx] - arcLenIndex[currIdx - 1]));
            Vec2D i = p.interpolateTo(q, frac);
            uniform.add(i);
        }
        if (doAddFinalVertex) {
            uniform.add(vertices.get(vertices.size() - 1).copy());
        }
        return uniform;
    }

    public float getEstimatedArcLength() {
        if (arcLenIndex == null
                || (arcLenIndex != null && arcLenIndex.length != vertices
                        .size())) {
            arcLenIndex = new float[vertices.size()];
        }
        float arcLen = 0;
        for (int i = 1; i < arcLenIndex.length; i++) {
            ReadonlyVec2D p = vertices.get(i - 1);
            ReadonlyVec2D q = vertices.get(i);
            arcLen += p.distanceTo(q);
            arcLenIndex[i] = arcLen;
        }
        return arcLen;
    }

    public List<Line2D> getSegments() {
        final int num = vertices.size();
        List<Line2D> segments = new ArrayList<Line2D>(num - 1);
        for (int i = 1; i < num; i++) {
            segments.add(new Line2D(vertices.get(i - 1), vertices.get(i)));
        }
        return segments;
    }
}
