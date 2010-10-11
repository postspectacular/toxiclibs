package toxi.geom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Container type for convex polygons. Implements {@link Shape2D}.
 */
public class Polygon2D implements Shape2D {

    public List<Vec2D> vertices = new ArrayList<Vec2D>();

    public Polygon2D() {
    }

    public Polygon2D(List<Vec2D> points) {
        for (Vec2D p : points) {
            add(p.copy());
        }
    }

    public Polygon2D(Vec2D... points) {
        for (Vec2D p : points) {
            add(p.copy());
        }
    }

    /**
     * Adds a new vertex to the polygon (builder pattern).
     * 
     * @param p
     *            vertex point to add
     * @return itself
     */
    public Polygon2D add(Vec2D p) {
        if (!vertices.contains(p)) {
            vertices.add(p);
        }
        return this;
    }

    public boolean containsPoint(ReadonlyVec2D p) {
        int num = vertices.size();
        int i, j = num - 1;
        boolean oddNodes = false;
        float px = p.x();
        float py = p.y();
        for (i = 0; i < num; i++) {
            Vec2D vi = vertices.get(i);
            Vec2D vj = vertices.get(j);
            if (vi.y < py && vj.y >= py || vj.y < py && vi.y >= py) {
                if (vi.x + (py - vi.y) / (vj.y - vi.y) * (vj.x - vi.x) < px) {
                    oddNodes = !oddNodes;
                }
            }
            j = i;
        }
        return oddNodes;
    }

    /**
     * Flips the ordering of the polygon's vertices.
     * 
     * @return itself
     */
    public Polygon2D flipVertexOrder() {
        Collections.reverse(vertices);
        return this;
    }

    /**
     * Computes the area of the polygon, provided it isn't self intersecting.
     * Code ported from:
     * http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/
     * 
     * @return polygon area
     */
    public float getArea() {
        float area = 0;
        int numPoints = vertices.size();
        for (int i = 0; i < numPoints; i++) {
            Vec2D a = vertices.get(i);
            Vec2D b = vertices.get((i + 1) % numPoints);
            area += a.x * b.y;
            area -= a.y * b.x;
        }
        area *= 0.5f;
        return area;
    }

    /**
     * Computes the polygon's centre of mass. Code ported from:
     * http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/
     * 
     * @return centroid point
     */
    public Vec2D getCentroid() {
        Vec2D res = new Vec2D();
        int numPoints = vertices.size();
        for (int i = 0; i < numPoints; i++) {
            Vec2D a = vertices.get(i);
            Vec2D b = vertices.get((i + 1) % numPoints);
            float factor = a.x * b.y - b.x * a.y;
            res.x += (a.x + b.x) * factor;
            res.y += (a.y + b.y) * factor;
        }
        return res.scale(1f / (getArea() * 6));
    }

    public float getCircumference() {
        float circ = 0;
        for (int i = 0, num = vertices.size(); i < num; i++) {
            circ += vertices.get(i).distanceTo(vertices.get((i + 1) % num));
        }
        return circ;
    }

    /**
     * Returns the number of polygon vertices.
     * 
     * @return vertex count
     */
    public int getNumPoints() {
        return vertices.size();
    }

    /**
     * Checks if the vertices of this polygon are in clockwise ordering by
     * examining the first 3.
     * 
     * @return true, if clockwise
     */
    public boolean isClockwise() {
        if (vertices.size() > 2) {
            return Triangle2D.isClockwise(vertices.get(0), vertices.get(1),
                    vertices.get(2));
        }
        return false;
    }

    @Deprecated
    public Polygon2D reverseOrientation() {
        return flipVertexOrder();
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (Iterator<Vec2D> i = vertices.iterator(); i.hasNext();) {
            buf.append(i.next().toString());
            if (i.hasNext()) {
                buf.append(", ");
            }
        }
        return buf.toString();
    }
}
