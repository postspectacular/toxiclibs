package toxi.math;

import toxi.geom.Vec2D;

/**
 * Implementations of 2D interpolation functions (currently only bilinear).
 */
public class Interpolation2D {

    /**
     * @param x
     *            x coord of point to filter
     * @param y
     *            y coord of point to filter
     * @param x1
     *            x coord of top-left corner
     * @param y1
     *            y coord of top-left corner
     * @param x2
     *            x coord of bottom-right corner
     * @param y2
     *            y coord of bottom-right corner
     * @param tl
     *            top-left value
     * @param tr
     *            top-right value
     * @param bl
     *            bottom-left value
     * @param br
     *            bottom-right value
     * @return interpolated value
     */
    public static float bilinear(double x, double y, double x1, double y1,
            double x2, double y2, float tl, float tr, float bl, float br) {
        double denom = 1.0 / ((x2 - x1) * (y2 - y1));
        double dx1 = (x - x1) * denom;
        double dx2 = (x2 - x) * denom;
        double dy1 = y - y1;
        double dy2 = y2 - y;
        return (float) (tl * dx2 * dy2 + tr * dx1 * dy2 + bl * dx2 * dy1 + br
                * dx1 * dy1);
    }

    /**
     * @param p
     *            point to filter
     * @param p1
     *            top-left corner
     * @param p2
     *            bottom-right corner
     * @param tl
     *            top-left value
     * @param tr
     *            top-right value
     * @param bl
     *            bottom-left value
     * @param br
     *            bottom-right value
     * @return interpolated value
     */
    public static float bilinear(Vec2D p, Vec2D p1, Vec2D p2, float tl,
            float tr, float bl, float br) {
        return bilinear(p1.x, p1.y, p2.x, p2.y, p.x, p.y, tl, tr, bl, br);
    }

}
