package toxi.geom;

import java.util.List;

public interface PolygonTesselatorD {

    /**
     * Tesselates the given polygon into a set of triangles.
     * 
     * @param poly
     *            polygon
     * @return list of triangles
     */
    public List<TriangleD2D> tesselatePolygonD(PolygonD2D poly);

}