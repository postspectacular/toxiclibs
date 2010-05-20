package toxi.geom;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper
 */
public class Polygon2D {

    public List<Vec2D> vertices = new ArrayList<Vec2D>();

    public Polygon2D() {
    }

    public Polygon2D(List<Vec2D> points) {
        for (Vec2D p : points) {
            add(p);
        }
    }

    public Polygon2D add(Vec2D p) {
        if (!vertices.contains(p)) {
            vertices.add(p);
        }
        return this;
    }

    public boolean containsPoint(Vec2D p) {
        int num = vertices.size();
        int i, j = num - 1;
        boolean oddNodes = false;
        for (i = 0; i < num; i++) {
            Vec2D vi = vertices.get(i);
            Vec2D vj = vertices.get(j);
            if (vi.y < p.y && vj.y >= p.y || vj.y < p.y && vi.y >= p.y) {
                if (vi.x + (p.y - vi.y) / (vj.y - vi.y) * (vj.x - vi.x) < p.x) {
                    oddNodes = !oddNodes;
                }
            }
            j = i;
        }
        return oddNodes;
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
    public ReadonlyVec2D getCentroid() {
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

    /**
     * Returns the number of polygon vertices.
     * 
     * @return vertex count
     */
    public int getNumPoints() {
        return vertices.size();
    }

}
