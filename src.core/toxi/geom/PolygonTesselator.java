package toxi.geom;

import java.util.List;

public interface PolygonTesselator {

    /**
     * Tesselates the given polygon into a set of triangles.
     * 
     * @param poly
     *            polygon
     * @return list of triangles
     */
    public List<Triangle2D> tesselatePolygon(Polygon2D poly);

}