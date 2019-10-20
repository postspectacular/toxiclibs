package toxi.geom;

import java.util.ArrayList;
import java.util.List;

import toxi.geom.mesh2d.DelaunayTriangulationD;
import toxi.geom.mesh2d.VoronoiD;
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
public abstract class GridTesselatorD implements PolygonTesselatorD {

    protected double res;
    private double rootSize;

    /**
     * Creates a new instance with the given grid resolution.
     * 
     * @param res
     *            snap distance for grid points
     */
    public GridTesselatorD(double res) {
        this(res, VoronoiD.DEFAULT_SIZE);
    }

    /**
     * Creates a new instance with the given grid resolution.
     * 
     * @param res
     *            snap distance for grid points
     */
    public GridTesselatorD(double res, double rootSize) {
        this.res = res;
        this.rootSize = rootSize;
    }

    protected abstract List<VecD2D> createInsidePoints(PolygonD2D poly,
            RectD bounds);

    public double getResolution() {
        return res;
    }

    public void setResolution(double res) {
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
    public List<TriangleD2D> tesselatePolygonD(PolygonD2D poly) {
        List<TriangleD2D> triangles = new ArrayList<TriangleD2D>();
        RectD bounds = poly.getBounds();
        // a Voronoi diagram relies on a Delaunay triangulation behind the
        // scenes
        VoronoiD voronoi = new VoronoiD(rootSize);
        // add perimeter points
        for (VecD2D v : poly.vertices) {
            voronoi.addPoint(v);
        }
        // add random inliers
        for (VecD2D v : createInsidePoints(poly, bounds)) {
            voronoi.addPoint(v);
        }
        // get filtered delaunay triangles:
        // ignore any triangles which share a vertex with the initial root
        // triangle or whose centroid is outside the polygon
        for (TriangleD2D t : voronoi.getTriangles()) {
            if (MathUtils.abs(t.a.x) != VoronoiD.DEFAULT_SIZE
                    && MathUtils.abs(t.a.y) != VoronoiD.DEFAULT_SIZE) {
                if (poly.containsPoint(t.computeCentroid())) {
                    triangles.add(t);
                }
            }
        }
        return triangles;
    }

}