package toxi.geom;

import java.util.ArrayList;
import java.util.List;

import toxi.geom.mesh2d.DelaunayTriangulation;
import toxi.geom.mesh2d.Voronoi;
import toxi.math.MathUtils;

/**
 * This is an implementation of the {@link PolygonTesselator} interface and
 * abstract parent class for tesselating 2D polygons using a grid of additional
 * points created within the polygon. These inlier points are connected to the
 * original polygon vertices using a {@link DelaunayTriangulation}. The quality
 * and final amount of triangles used can be adjusted via the number of
 * additional grid points. This class currently has two concrete
 * implementations: {@link GlobalGridTesselator} and {@link LocalGridTesselator}
 * .
 */
public abstract class GridTesselator implements PolygonTesselator {

    protected float res;
    private float rootSize;

    /**
     * Creates a new instance with the given grid resolution.
     * 
     * @param res
     *            snap distance for grid points
     */
    public GridTesselator(float res) {
        this(res, Voronoi.DEFAULT_SIZE);
    }

    /**
     * Creates a new instance with the given grid resolution.
     * 
     * @param res
     *            snap distance for grid points
     */
    public GridTesselator(float res, float rootSize) {
        this.res = res;
        this.rootSize = rootSize;
    }

    protected abstract List<Vec2D> createInsidePoints(Polygon2D poly,
            Rect bounds);

    public float getResolution() {
        return res;
    }

    public void setResolution(float res) {
        this.res = res;
    }

    /**
     * Tesselates/decomposes the given polygon into a list of 2D triangles using
     * the currently set grid resolution.
     * 
     * @param poly
     *            polygon to be tesselated
     * @return list of triangles
     */
    public List<Triangle2D> tesselatePolygon(Polygon2D poly) {
        List<Triangle2D> triangles = new ArrayList<Triangle2D>();
        Rect bounds = poly.getBounds();
        // a Voronoi diagram relies on a Delaunay triangulation behind the
        // scenes
        Voronoi voronoi = new Voronoi(rootSize);
        // add perimeter points
        for (Vec2D v : poly.vertices) {
            voronoi.addPoint(v);
        }
        // add random inliers
        for (Vec2D v : createInsidePoints(poly, bounds)) {
            voronoi.addPoint(v);
        }
        // get filtered delaunay triangles:
        // ignore any triangles which share a vertex with the initial root
        // triangle or whose centroid is outside the polygon
        for (Triangle2D t : voronoi.getTriangles()) {
            if (MathUtils.abs(t.a.x) != Voronoi.DEFAULT_SIZE
                    && MathUtils.abs(t.a.y) != Voronoi.DEFAULT_SIZE) {
                if (poly.containsPoint(t.computeCentroid())) {
                    triangles.add(t);
                }
            }
        }
        return triangles;
    }

}