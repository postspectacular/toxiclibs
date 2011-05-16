package toxi.geom;

import java.util.ArrayList;
import java.util.List;

import toxi.math.ScaleMap;

/**
 * A concrete implementation of the abstract {@link GridTesselator} using a grid
 * in polygon-local coordinate space for generating additional points within a
 * polygon. The resolution setting of this class defines number of desired grid
 * points in X & Y direction. E.g. a resolution of 10 means up to 10x10 grid
 * points are created a within the polygon bounding rect. For smaller polygons,
 * the resulting triangles will simply be smaller too. This resolution is used
 * independently on polygon size. Use the {@link GlobalGridTesselator} for an
 * alternative behavior, resulting in more uniformly sized triangles.
 * 
 * @see GridTesselator
 * @see GlobalGridTesselator
 * @see PolygonTesselator
 */
public class LocalGridTesselator extends GridTesselator {

    public LocalGridTesselator(int res) {
        super(res);
    }

    protected List<Vec2D> createInsidePoints(Polygon2D poly, Rect bounds) {
        List<Vec2D> points = new ArrayList<Vec2D>();
        int ires = (int) res;
        ScaleMap xmap = new ScaleMap(0, ires, bounds.getLeft(),
                bounds.getRight());
        ScaleMap ymap = new ScaleMap(0, ires, bounds.getTop(),
                bounds.getBottom());
        for (int y = 0; y < ires; y++) {
            float yy = (float) ymap.getMappedValueFor(y);
            for (int x = 0; x < ires; x++) {
                Vec2D p = new Vec2D((float) xmap.getMappedValueFor(x), yy);
                if (poly.containsPoint(p)) {
                    points.add(p);
                }
            }
        }
        return points;
    }
}
