package toxi.geom;

import java.util.ArrayList;
import java.util.List;

import toxi.math.MathUtils;

/**
 * A concrete implementation of the abstract {@link GridTesselator} using a grid
 * in global coordinate space for generating additional points within a polygon.
 * The resolution setting of this class defines the axis-aligned distance
 * between grid points. E.g. a resolution of 10 means grid points are created a
 * world space positions of multiples of 10 (i.e. 0,10,20 etc.). This resolution
 * is used independently on polygon size, so depending on the chosen resolution
 * and polygon size no additional inliers MIGHT be created at all. This behavior
 * property is useful in cases where you want to adjust the number of resulting
 * triangles dynamically, e.g. based on polygon size. Use the
 * {@link LocalGridTesselator} for an alternative behavior.
 * 
 * @see GridTesselator
 * @see LocalGridTesselator
 * @see PolygonTesselator
 */
public class GlobalGridTesselator extends GridTesselator {

    public GlobalGridTesselator(float res) {
        super(res);
    }

    protected List<Vec2D> createInsidePoints(Polygon2D poly, Rect bounds) {
        List<Vec2D> points = new ArrayList<Vec2D>();
        for (float y = bounds.y; y < bounds.getBottom(); y += res) {
            float yy = MathUtils.roundTo(y, res);
            for (float x = bounds.x; x < bounds.getRight(); x += res) {
                Vec2D p = new Vec2D(MathUtils.roundTo(x, res), yy);
                if (poly.containsPoint(p)) {
                    points.add(p);
                }
            }
        }
        return points;
    }
}
