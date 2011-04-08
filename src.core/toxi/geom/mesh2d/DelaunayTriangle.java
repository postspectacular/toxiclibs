package toxi.geom.mesh2d;

/*
 * Copyright (c) 2007 by L. Paul Chew.
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import toxi.util.datatypes.ArraySet;

/**
 * A DelaunayTriangle is an immutable Set of exactly three Pnts.
 * 
 * All Set operations are available. Individual vertices can be accessed via
 * iterator() and also via triangle.get(index).
 * 
 * Note that, even if two triangles have the same vertex set, they are
 * *different* triangles. Methods equals() and hashCode() are consistent with
 * this rule.
 * 
 * @author Paul Chew
 * 
 *         Created December 2007. Replaced general simplices with geometric
 *         triangle.
 * 
 */
public class DelaunayTriangle extends ArraySet<DelaunayVertex> {

    private int idNumber; // The id number
    private DelaunayVertex circumcenter = null; // The triangle's circumcenter

    private static int idGenerator = 0; // Used to create id numbers
    public static boolean moreInfo = false; // True if more info in toString

    /**
     * @param collection
     *            a Collection holding the Simplex vertices
     * @throws IllegalArgumentException
     *             if there are not three distinct vertices
     */
    public DelaunayTriangle(Collection<? extends DelaunayVertex> collection) {
        super(collection);
        idNumber = idGenerator++;
        if (this.size() != 3) {
            throw new IllegalArgumentException(
                    "DelaunayTriangle must have 3 vertices");
        }
    }

    /**
     * @param vertices
     *            the vertices of the DelaunayTriangle.
     * @throws IllegalArgumentException
     *             if there are not three distinct vertices
     */
    public DelaunayTriangle(DelaunayVertex... vertices) {
        this(Arrays.asList(vertices));
    }

    @Override
    public boolean add(DelaunayVertex vertex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        return (this == o);
    }

    /**
     * Report the facet opposite vertex.
     * 
     * @param vertex
     *            a vertex of this DelaunayTriangle
     * @return the facet opposite vertex
     * @throws IllegalArgumentException
     *             if the vertex is not in triangle
     */
    public ArraySet<DelaunayVertex> facetOpposite(DelaunayVertex vertex) {
        ArraySet<DelaunayVertex> facet = new ArraySet<DelaunayVertex>(this);
        if (!facet.remove(vertex)) {
            throw new IllegalArgumentException("Vertex not in triangle");
        }
        return facet;
    }

    /**
     * @return the triangle's circumcenter
     */
    public DelaunayVertex getCircumcenter() {
        if (circumcenter == null) {
            circumcenter = DelaunayVertex.circumcenter(this
                    .toArray(new DelaunayVertex[0]));
        }
        return circumcenter;
    }

    /**
     * Get arbitrary vertex of this triangle, but not any of the bad vertices.
     * 
     * @param badVertices
     *            one or more bad vertices
     * @return a vertex of this triangle, but not one of the bad vertices
     * @throws NoSuchElementException
     *             if no vertex found
     */
    public DelaunayVertex getVertexButNot(DelaunayVertex... badVertices) {
        Collection<DelaunayVertex> bad = Arrays.asList(badVertices);
        for (DelaunayVertex v : this) {
            if (!bad.contains(v)) {
                return v;
            }
        }
        throw new NoSuchElementException("No vertex found");
    }

    /* The following two methods ensure that a DelaunayTriangle is immutable */

    @Override
    public int hashCode() {
        return (idNumber ^ (idNumber >>> 32));
    }

    /**
     * True iff triangles are neighbors. Two triangles are neighbors if they
     * share a facet.
     * 
     * @param triangle
     *            the other DelaunayTriangle
     * @return true iff this DelaunayTriangle is a neighbor of triangle
     */
    public boolean isNeighbor(DelaunayTriangle triangle) {
        int count = 0;
        for (DelaunayVertex vertex : this) {
            if (!triangle.contains(vertex)) {
                count++;
            }
        }
        return count == 1;
    }

    /* The following two methods ensure that all triangles are different. */

    @Override
    public Iterator<DelaunayVertex> iterator() {
        return new Iterator<DelaunayVertex>() {

            private Iterator<DelaunayVertex> it = DelaunayTriangle.super
                    .iterator();

            public boolean hasNext() {
                return it.hasNext();
            }

            public DelaunayVertex next() {
                return it.next();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public String toString() {
        if (!moreInfo) {
            return "DelaunayTriangle" + idNumber;
        }
        return "DelaunayTriangle" + idNumber + super.toString();
    }

}