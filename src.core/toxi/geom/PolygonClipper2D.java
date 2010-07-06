package toxi.geom;

/**
 * Defines an interface for clipping 2D polygons. Currently the only
 * implementation for this available is {@link SutherlandHodgemanClipper}.
 */
public interface PolygonClipper2D {

    /**
     * Creates a clipped version of the polygon to the boundary shape set.
     * 
     * @param poly
     *            polygon to be clipped
     * @return clipped poly
     */
    public Polygon2D clipPolygon(Polygon2D poly);

}