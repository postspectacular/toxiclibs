package toxi.geom.mesh2d;

/*
 * Copyright (c) 2005, 2007 by L. Paul Chew.
 *
 * Permission is hereby granted, without written agreement and without
 * license or royalty fees, to use, copy, modify, and distribute this
 * software and its documentation for any purpose, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import toxi.util.datatypes.UndirectedGraph;

/**
 * A 2D Delaunay DelaunayTriangulationD (DT) with incremental site insertion.
 * 
 * This is not the fastest way to build a DT, but it's a reasonable way to build
 * a DT incrementally and it makes a nice interactive display. There are several
 * O(n log n) methods, but they require that the sites are all known initially.
 * 
 * A DelaunayTriangulationD is a Set of Triangles. A DelaunayTriangulationD is
 * unmodifiable as a Set; the only way to change it is to add sites (via
 * delaunayPlace).
 * 
 * @author Paul Chew
 * 
 *         Created July 2005. Derived from an earlier, messier version.
 * 
 *         Modified November 2007. Rewrote to use AbstractSet as parent class
 *         and to use the UndirectedGraph class internally. Tried to make the DT
 *         algorithm clearer by explicitly creating a cavity. Added code needed
 *         to find a Voronoi cell.
 * 
 * @author Karsten Schmidt
 * 
 *         Ported to use toxiclibs classes (June 2010).
 */
public class DelaunayTriangulationD extends AbstractSet<DelaunayTriangleD> {

    private DelaunayTriangleD mostRecent = null;

    private UndirectedGraph<DelaunayTriangleD> triGraph;

    /**
     * All sites must fall within the initial triangle.
     * 
     * @param triangle
     *            the initial triangle
     */
    public DelaunayTriangulationD(DelaunayTriangleD triangle) {
        triGraph = new UndirectedGraph<DelaunayTriangleD>();
        triGraph.add(triangle);
        mostRecent = triangle;
    }

    /**
     * True iff triangle is a member of this triangulation. This method isn't
     * required by AbstractSet, but it improves efficiency.
     * 
     * @param triangle
     *            the object to check for membership
     */
    public boolean contains(Object triangle) {
        return triGraph.getNodes().contains(triangle);
    }

    /**
     * Place a new site into the DT. Nothing happens if the site matches an
     * existing DT vertex.
     * 
     * @param site
     *            the new DelaunayVertexD
     * @throws IllegalArgumentException
     *             if site does not lie in any triangle
     */
    public void delaunayPlace(DelaunayVertexD site) {
        // Uses straightforward scheme rather than best asymptotic time
        // Locate containing triangle
        DelaunayTriangleD triangle = locate(site);
        // Give up if no containing triangle or if site is already in DT
        if (triangle == null) {
            throw new IllegalArgumentException("No containing triangle");
        }
        if (triangle.contains(site)) {
            return;
        }
        // Determine the cavity and update the triangulation
        Set<DelaunayTriangleD> cavity = getCavity(site, triangle);
        mostRecent = update(site, cavity);
    }

    /**
     * Determine the cavity caused by site.
     * 
     * @param site
     *            the site causing the cavity
     * @param triangle
     *            the triangle containing site
     * @return set of all triangles that have site in their circumcircle
     */
    private Set<DelaunayTriangleD> getCavity(DelaunayVertexD site,
            DelaunayTriangleD triangle) {
        Set<DelaunayTriangleD> encroached = new HashSet<DelaunayTriangleD>();
        Queue<DelaunayTriangleD> toBeChecked = new LinkedList<DelaunayTriangleD>();
        Set<DelaunayTriangleD> marked = new HashSet<DelaunayTriangleD>();
        toBeChecked.add(triangle);
        marked.add(triangle);
        while (!toBeChecked.isEmpty()) {
            triangle = toBeChecked.remove();
            if (site.vsCircumcircle(triangle.toArray(new DelaunayVertexD[0])) == 1) {
                // Site outside triangle => triangle not in cavity
                continue;
            }
            encroached.add(triangle);
            // Check the neighbors
            for (DelaunayTriangleD neighbor : triGraph
                    .getConnectedNodesFor(triangle)) {
                if (marked.contains(neighbor)) {
                    continue;
                }
                marked.add(neighbor);
                toBeChecked.add(neighbor);
            }
        }
        return encroached;
    }

    @Override
    public Iterator<DelaunayTriangleD> iterator() {
        return triGraph.getNodes().iterator();
    }

    /**
     * Locate the triangle with point inside it or on its boundary.
     * 
     * @param point
     *            the point to locate
     * @return the triangle that holds point; null if no such triangle
     */
    public DelaunayTriangleD locate(DelaunayVertexD point) {
        DelaunayTriangleD triangle = mostRecent;
        if (!this.contains(triangle)) {
            triangle = null;
        }

        // Try a directed walk (this works fine in 2D, but can fail in 3D)
        Set<DelaunayTriangleD> visited = new HashSet<DelaunayTriangleD>();
        while (triangle != null) {
            if (visited.contains(triangle)) { // This should never happen
                System.out.println("Warning: Caught in a locate loop");
                break;
            }
            visited.add(triangle);
            // Corner opposite point
            DelaunayVertexD corner = point.isOutside(triangle
                    .toArray(new DelaunayVertexD[0]));
            if (corner == null) {
                return triangle;
            }
            triangle = this.neighborOpposite(corner, triangle);
        }
        // No luck; try brute force
        System.out.println("Warning: Checking all triangles for " + point);
        for (DelaunayTriangleD tri : this) {
            if (point.isOutside(tri.toArray(new DelaunayVertexD[0])) == null) {
                return tri;
            }
        }
        // No such triangle
        System.out.println("Warning: No triangle holds " + point);
        return null;
    }

    /**
     * Report neighbor opposite the given vertex of triangle.
     * 
     * @param site
     *            a vertex of triangle
     * @param triangle
     *            we want the neighbor of this triangle
     * @return the neighbor opposite site in triangle; null if none
     * @throws IllegalArgumentException
     *             if site is not in this triangle
     */
    public DelaunayTriangleD neighborOpposite(DelaunayVertexD site,
            DelaunayTriangleD triangle) {
        if (!triangle.contains(site)) {
            throw new IllegalArgumentException("Bad vertex; not in triangle");
        }
        for (DelaunayTriangleD neighbor : triGraph
                .getConnectedNodesFor(triangle)) {
            if (!neighbor.contains(site)) {
                return neighbor;
            }
        }
        return null;
    }

    /**
     * Return the set of triangles adjacent to triangle.
     * 
     * @param triangle
     *            the triangle to check
     * @return the neighbors of triangle
     */
    public Set<DelaunayTriangleD> neighbors(DelaunayTriangleD triangle) {
        return triGraph.getConnectedNodesFor(triangle);
    }

    @Override
    public int size() {
        return triGraph.getNodes().size();
    }

    /**
     * Report triangles surrounding site in order (cw or ccw).
     * 
     * @param site
     *            we want the surrounding triangles for this site
     * @param triangle
     *            a "starting" triangle that has site as a vertex
     * @return all triangles surrounding site in order (cw or ccw)
     * @throws IllegalArgumentException
     *             if site is not in triangle
     */
    public List<DelaunayTriangleD> surroundingTriangles(DelaunayVertexD site,
            DelaunayTriangleD triangle) {
        if (!triangle.contains(site)) {
            throw new IllegalArgumentException("Site not in triangle");
        }
        List<DelaunayTriangleD> list = new ArrayList<DelaunayTriangleD>();
        DelaunayTriangleD start = triangle;
        DelaunayVertexD guide = triangle.getVertexButNot(site); // Affects cw or
        // ccw
        while (true) {
            list.add(triangle);
            DelaunayTriangleD previous = triangle;
            triangle = this.neighborOpposite(guide, triangle); // Next triangle
            guide = previous.getVertexButNot(site, guide); // Update guide
            if (triangle == start) {
                break;
            }
        }
        return list;
    }

    @Override
    public String toString() {
        return "DelaunayTriangulationD with " + size() + " triangles";
    }

    /**
     * Update the triangulation by removing the cavity triangles and then
     * filling the cavity with new triangles.
     * 
     * @param site
     *            the site that created the cavity
     * @param cavity
     *            the triangles with site in their circumcircle
     * @return one of the new triangles
     */
    private DelaunayTriangleD update(DelaunayVertexD site,
            Set<DelaunayTriangleD> cavity) {
        Set<Set<DelaunayVertexD>> boundary = new HashSet<Set<DelaunayVertexD>>();
        Set<DelaunayTriangleD> theTriangles = new HashSet<DelaunayTriangleD>();

        // Find boundary facets and adjacent triangles
        for (DelaunayTriangleD triangle : cavity) {
            theTriangles.addAll(neighbors(triangle));
            for (DelaunayVertexD vertex : triangle) {
                Set<DelaunayVertexD> facet = triangle.facetOpposite(vertex);
                if (boundary.contains(facet)) {
                    boundary.remove(facet);
                } else {
                    boundary.add(facet);
                }
            }
        }
        theTriangles.removeAll(cavity); // Adj triangles only

        // Remove the cavity triangles from the triangulation
        for (DelaunayTriangleD triangle : cavity) {
            triGraph.remove(triangle);
        }

        // Build each new triangle and add it to the triangulation
        Set<DelaunayTriangleD> newTriangles = new HashSet<DelaunayTriangleD>();
        for (Set<DelaunayVertexD> vertices : boundary) {
            vertices.add(site);
            DelaunayTriangleD tri = new DelaunayTriangleD(vertices);
            triGraph.add(tri);
            newTriangles.add(tri);
        }

        // Update the graph links for each new triangle
        theTriangles.addAll(newTriangles); // Adj triangle + new triangles
        for (DelaunayTriangleD triangle : newTriangles) {
            for (DelaunayTriangleD other : theTriangles) {
                if (triangle.isNeighbor(other)) {
                    triGraph.connect(triangle, other);
                }
            }
        }

        // Return one of the new triangles
        return newTriangles.iterator().next();
    }
}