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
public class GlobalGridTesselatorD extends GridTesselatorD {

    public GlobalGridTesselatorD(double res) {
        super(res);
    }

    protected List<VecD2D> createInsidePoints(PolygonD2D poly, RectD bounds) {
        List<VecD2D> points = new ArrayList<VecD2D>();
        for (double y = bounds.y; y < bounds.getBottom(); y += res) {
            double yy = MathUtils.roundTo(y, res);
            for (double x = bounds.x; x < bounds.getRight(); x += res) {
                VecD2D p = new VecD2D(MathUtils.roundTo(x, res), yy);
                if (poly.containsPoint(p)) {
                    points.add(p);
                }
            }
        }
        return points;
    }
}
